package com.cmr.busify;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.cmr.busify.databinding.FragmentConductorBinding;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ConductorFragment extends Fragment {
	private FragmentConductorBinding binding;

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
		IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
		integrator.setPrompt("Scan a QR code");
		integrator.setBeepEnabled(true);
		integrator.setOrientationLocked(true);
		integrator.initiateScan();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if (result != null) {
			if (result.getContents() != null) {
				// Handle the scanned result
				String scannedData = result.getContents();

				// Do something with the scanned data
				// For example, you can pass it to another activity
				// Intent intent = new Intent(getActivity(), GetTicketActivity.class);
				// intent.putExtra("scannedData", scannedData);
				// startActivity(intent);
				Toast.makeText(requireContext(), scannedData, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(requireContext(), "No QR found!", Toast.LENGTH_SHORT).show();
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}
