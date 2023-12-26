package com.cmr.busify;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.cmr.busify.databinding.FragmentConductorBinding;

public class ConductorFragment extends Fragment {
	private FragmentConductorBinding binding;
	private static final int CAMERA_REQUEST_CODE = 123;

	// ActivityResultLauncher for camera
	private final ActivityResultLauncher<Intent> cameraLauncher =
			registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
					result -> {
						if (result.getResultCode() == RESULT_OK) {
							// The image is captured and available in the 'data' bundle
							Bundle extras = result.getData().getExtras();
							if (extras != null) {
								// Use the bitmap directly or convert it to a URI as needed
								Bitmap imageBitmap = (Bitmap) extras.get("data");

								// Use the imageBitmap as needed (e.g., display in ImageView or save to storage)
							}
						}
					});

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		binding = FragmentConductorBinding.inflate(inflater, container, false);
		View view = binding.getRoot();

		setupAnimations();

		CardView scanQrBtn = binding.cardView3;
		scanQrBtn.setOnClickListener(v -> openCamera());

		return view;
	}

	private void setupAnimations() {
		Animation fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
		Animation bottomDown = AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_down);

		binding.topLinearLayout2.setAnimation(bottomDown);
		binding.layoutHeader.setAnimation(bottomDown);

		Handler handler = new Handler();
		handler.postDelayed(() -> {
			binding.cardView1.setAnimation(fadeIn);
			binding.cardView2.setAnimation(fadeIn);
			binding.cardView3.setAnimation(fadeIn);
		}, 1000);
	}

	private void openCamera() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
			cameraLauncher.launch(cameraIntent);
		}
	}
}
