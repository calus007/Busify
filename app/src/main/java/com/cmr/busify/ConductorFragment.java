package com.cmr.busify;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
				String[] scannedDetails = scannedData.split(",");
				String userId = scannedDetails[0];
				String ticketNo = scannedDetails[1];
				// Intent intent = new Intent(getActivity(), GetTicketActivity.class);
				// intent.putExtra("scannedData", scannedData);
				// startActivity(intent);
				String msg = "User Id: " + userId + "\nTicket No: " + ticketNo;
				showAlertDialog(msg);
			} else {
				Toast.makeText(requireContext(), "No QR found!", Toast.LENGTH_SHORT).show();
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void showAlertDialog(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

		builder.setTitle("Alert Title")
				.setMessage(msg)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User clicked OK button
						dialog.dismiss();
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User clicked Cancel button
						dialog.dismiss();
					}
				});

		// Create and show the AlertDialog
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
}
