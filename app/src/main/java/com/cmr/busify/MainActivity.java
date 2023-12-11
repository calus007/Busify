package com.cmr.busify;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.cmr.busify.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

/** @noinspection deprecation*/
public class MainActivity extends AppCompatActivity {

	private FirebaseAuth firebaseAuth;
	private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
	private String verificationId;
	private EditText phoneNumberEditText;
	private EditText otpEditText;
	private static final int RC_SIGN_IN = 9001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		com.cmr.busify.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		setupAnimations();
		initializeFirebase();

		phoneNumberEditText = binding.phoneNumberEditText;
		otpEditText = binding.otpEditText;
		Button sendOtpButton = binding.sendOtpButton;
		ConstraintLayout verifyOtpButton = binding.verifyOtpButton;
		CardView cardView3 = findViewById(R.id.cardView3);
		cardView3.setOnClickListener(v -> signInWithGoogle());

		sendOtpButton.setOnClickListener(v -> {
			String phoneNumber = "+91" + phoneNumberEditText.getText().toString().trim();
			sendOtp(phoneNumber);
		});

		verifyOtpButton.setOnClickListener(v -> {
			String otp = otpEditText.getText().toString().trim();
			verifyOtp(otp);
		});

		callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
			@Override
			public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
				signInWithPhoneAuthCredential(phoneAuthCredential);
			}

			@Override
			public void onVerificationFailed(@NonNull FirebaseException e) {
				handleVerificationFailed(e);
			}

			@Override
			public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
				verificationId = s;
				// Enable UI to enter the OTP if needed
			}
		};
	}

	private void setupAnimations() {
		Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		Animation bottomDown = AnimationUtils.loadAnimation(this, R.anim.bottom_down);

		findViewById(R.id.topLinearLayout2).setAnimation(bottomDown);
		findViewById(R.id.appName).setAnimation(bottomDown);

		Handler handler = new Handler();
		handler.postDelayed(() -> {
			findViewById(R.id.cardView1).setAnimation(fadeIn);
			findViewById(R.id.cardView2).setAnimation(fadeIn);
			findViewById(R.id.cardView3).setAnimation(fadeIn);
			findViewById(R.id.textView2).setAnimation(fadeIn);
			findViewById(R.id.verifyOtpButton).setAnimation(fadeIn);
			findViewById(R.id.btnVerifyLayoutArrow).setAnimation(fadeIn);
		}, 1000);
	}

	private void signInWithGoogle() {
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();

		GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

		Intent signInIntent = mGoogleSignInClient.getSignInIntent();
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_SIGN_IN) {
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				// Google Sign In was successful, authenticate with Firebase
				GoogleSignInAccount account = task.getResult(ApiException.class);
				firebaseAuthWithGoogle(account.getIdToken());
			} catch (ApiException e) {
				// Google Sign In failed, update UI appropriately
				Toast.makeText(this, "Google Sign In failed", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void firebaseAuthWithGoogle(String idToken) {
		AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
		firebaseAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, task -> {
					if (task.isSuccessful()) {
						// Sign in success
						handleSignInSuccess();
					} else {
						// If sign in fails, display a message to the user.
						Toast.makeText(this, "Google Authentication failed", Toast.LENGTH_SHORT).show();
					}
				});
	}

	private void initializeFirebase() {
		try {
			FirebaseApp.initializeApp(this);
			firebaseAuth = FirebaseAuth.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Firebase initialization failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	private void sendOtp(String phoneNumber) {
		PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
				.setPhoneNumber(phoneNumber)
				.setTimeout(60L, TimeUnit.SECONDS)
				.setActivity(this)
				.setCallbacks(callbacks)
				.build();
		PhoneAuthProvider.verifyPhoneNumber(options);
	}

	private void verifyOtp(String otp) {
		PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
		signInWithPhoneAuthCredential(credential);
	}

	private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
		firebaseAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, task -> {
					if (task.isSuccessful()) {
						// Authentication succeeded
						handleSignInSuccess();
					} else {
						// Authentication failed
						handleSignInFailure(task.getException());
					}
				});
	}

	private void handleVerificationFailed(FirebaseException exception) {
		Toast.makeText(this, "Verification failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
	}

	private void handleSignInSuccess() {
		Toast.makeText(this, "Sign in successful!", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}

	private void handleSignInFailure(Exception exception) {
		if (exception instanceof FirebaseAuthInvalidCredentialsException) {
			Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Sign in failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
}
