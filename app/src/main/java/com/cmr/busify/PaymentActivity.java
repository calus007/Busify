package com.cmr.busify;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.cmr.busify.databinding.ActivityPaymentBinding;

import java.util.Objects;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity {

	private LinearLayout topLinearLayout;
	private TextView layoutHeader, fairTextView, fromTo;
	private CardView cardView1, cardView2, cardView3;
	private View payBtn;
	private static final long SHIPPING_COST_CENTS = 500L;
	private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 42;
	private ActivityPaymentBinding binding; // Add this line

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityPaymentBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		topLinearLayout = binding.topLinearLayout1;
		layoutHeader = binding.layoutHeader;
		cardView1 = binding.cardView1;
		cardView2 = binding.cardView2;
		cardView3 = binding.cardView3;

		setupAnimations();

		Intent intent = getIntent();
		if (intent != null) {
			String startingLocation = intent.getStringExtra("startingLocation");
			String destination = intent.getStringExtra("destination");

			fairTextView = binding.fairTextView;

			String ticketCost = calculateTicketData(startingLocation, destination);
			fairTextView.setText(ticketCost);

			payBtn = binding.payBtn;
			fromTo = binding.fromTo;
			fromTo.setText(startingLocation + "  to  " + destination);

			payBtn.setOnClickListener(view -> {
				Intent intent1 = new Intent(PaymentActivity.this, GetTicketActivity.class);
				intent1.putExtra("start", startingLocation);
				intent1.putExtra("end", destination);
				intent1.putExtra("fair", ticketCost);
				// Start the GetTicketActivity
				startActivity(intent1);
			});
		} else {
			Toast.makeText(PaymentActivity.this, "Error getting your selected locations!", Toast.LENGTH_SHORT).show();
			Intent intent2 = new Intent(PaymentActivity.this, HomeActivity.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent2);
			finish();
		}
	}

	private String calculateTicketData(String startLocation, String destinationLocation) {
		if (Objects.equals(startLocation, "Suchitra") && Objects.equals(destinationLocation, "CMR College") || Objects.equals(startLocation, "CMR College") && Objects.equals(destinationLocation, "Suchitra"))
			return "Rs.30.00";
		else if (Objects.equals(startLocation, "Medchal") && Objects.equals(destinationLocation, "Jubilee Bus Station") || Objects.equals(startLocation, "Jubilee Bus Station") && Objects.equals(destinationLocation, "Medchal"))
			return "Rs.40.00";
		else if (Objects.equals(startLocation, "Secunderabad") && Objects.equals(destinationLocation, "Koti") || Objects.equals(startLocation, "Koti") && Objects.equals(destinationLocation, "Secunderabad"))
			return "Rs.35.00";
		else if (Objects.equals(startLocation, "Kompally") && Objects.equals(destinationLocation, "Medchal") || Objects.equals(startLocation, "Medchal") && Objects.equals(destinationLocation, "Kompally"))
			return "Rs.30.00";
		else if (Objects.equals(startLocation, "Secunderabad") && Objects.equals(destinationLocation, "Patancheruvu") || Objects.equals(startLocation, "Patancheruvu") && Objects.equals(destinationLocation, "Secunderabad"))
			return "Rs.45.00";
		else if (Objects.equals(startLocation, "Secunderabad") && Objects.equals(destinationLocation, "LB Nagar") || Objects.equals(startLocation, "LB Nagar") && Objects.equals(destinationLocation, "Secunderabad"))
			return "Rs.50.00";
		else if (Objects.equals(startLocation, "Secunderabad") && Objects.equals(destinationLocation, "Hitech City") || Objects.equals(startLocation, "Hitech City") && Objects.equals(destinationLocation, "Secunderabad"))
			return "Rs.40.00";
		else if (Objects.equals(startLocation, "Dulapally") && Objects.equals(destinationLocation, "Maisammaguda") || Objects.equals(startLocation, "Maisammaguda") && Objects.equals(destinationLocation, "Dulapally"))
			return "Rs.30.00";
		else if (Objects.equals(startLocation, "Dulapally") && Objects.equals(destinationLocation, "Gandimaisamma") || Objects.equals(startLocation, "Gandimaisamma") && Objects.equals(destinationLocation, "Dulapally"))
			return "Rs.35.00";
		else if (Objects.equals(startLocation, "Bowenpally") && Objects.equals(destinationLocation, "Miyapur") || Objects.equals(startLocation, "Miyapur") && Objects.equals(destinationLocation, "Bowenpally"))
			return "Rs.35.00";
		else {
			Random random = new Random();
			int randomNumber = random.nextInt(6) + 4;
			int randomMultipleOf5 = randomNumber * 5;
			return "Rs." + randomMultipleOf5 + ".00";
		}
	}

	private void setupAnimations() {
		Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		Animation bottomDown = AnimationUtils.loadAnimation(this, R.anim.bottom_down);

		topLinearLayout.setAnimation(bottomDown);
		layoutHeader.setAnimation(bottomDown);

		Handler handler = new Handler();
		handler.postDelayed(() -> {
			cardView1.setAnimation(fadeIn);
			cardView2.setAnimation(fadeIn);
			cardView3.setAnimation(fadeIn);
		}, 1000);
	}
}