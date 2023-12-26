package com.cmr.busify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cmr.busify.databinding.ActivityGetTicketBinding;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class GetTicketActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		com.cmr.busify.databinding.ActivityGetTicketBinding binding = ActivityGetTicketBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		TextView ticketNo = binding.ticketNo;
		TextView dAT = binding.dAT;
		TextView fromAndTo = binding.fromAndTo;
		TextView busNo = binding.busNo;
		TextView validity = binding.validity;
		TextView costOfTicket = binding.costOfTicket;
		ImageButton sampleQr = binding.sampleQr;

		// Retrieve data from the intent
		Intent intent = getIntent();
		if (intent != null) {
			String startingLocation = intent.getStringExtra("start");
			String destination = intent.getStringExtra("end");
			String fair = intent.getStringExtra("fair");

			ticketNo.setText("Ticket No.:" + String.valueOf(generateUniqueNumber()));
			dAT.setText(currentDateAndTime());
			fromAndTo.setText(startingLocation + "   to   " + destination);
			busNo.setText(busNumber(startingLocation, destination));
			validity.setText("Valid till Date: " + validityDate());
			costOfTicket.setText(fair);
		}

		sampleQr.setOnClickListener(v -> Toast.makeText(GetTicketActivity.this, "Please make payment to get QR", Toast.LENGTH_SHORT).show());
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

	private String busNumber(String startLocation, String destinationLocation) {
		if (Objects.equals(startLocation, "Suchitra") && Objects.equals(destinationLocation, "CMR College") || Objects.equals(startLocation, "CMR College") && Objects.equals(destinationLocation, "Suchitra"))
			return "229";
		else if (Objects.equals(startLocation, "Medchal") && Objects.equals(destinationLocation, "Jubilee Bus Station") || Objects.equals(startLocation, "Jubilee Bus Station") && Objects.equals(destinationLocation, "Medchal"))
			return "25S";
		else if (Objects.equals(startLocation, "Secunderabad") && Objects.equals(destinationLocation, "Koti") || Objects.equals(startLocation, "Koti") && Objects.equals(destinationLocation, "Secunderabad"))
			return "1, 8A";
		else if (Objects.equals(startLocation, "Kompally") && Objects.equals(destinationLocation, "Medchal") || Objects.equals(startLocation, "Medchal") && Objects.equals(destinationLocation, "Kompally"))
			return "229";
		else if (Objects.equals(startLocation, "Secunderabad") && Objects.equals(destinationLocation, "Patancheruvu") || Objects.equals(startLocation, "Patancheruvu") && Objects.equals(destinationLocation, "Secunderabad"))
			return "219";
		else if (Objects.equals(startLocation, "Secunderabad") && Objects.equals(destinationLocation, "LB Nagar") || Objects.equals(startLocation, "LB Nagar") && Objects.equals(destinationLocation, "Secunderabad"))
			return "90L";
		else if (Objects.equals(startLocation, "Secunderabad") && Objects.equals(destinationLocation, "Hitech City") || Objects.equals(startLocation, "Hitech City") && Objects.equals(destinationLocation, "Secunderabad"))
			return "47L";
		else if (Objects.equals(startLocation, "Dulapally") && Objects.equals(destinationLocation, "Maisammaguda") || Objects.equals(startLocation, "Maisammaguda") && Objects.equals(destinationLocation, "Dulapally"))
			return "227";
		else if (Objects.equals(startLocation, "Dulapally") && Objects.equals(destinationLocation, "Gandimaisamma") || Objects.equals(startLocation, "Gandimaisamma") && Objects.equals(destinationLocation, "Dulapally"))
			return "273";
		else if (Objects.equals(startLocation, "Bowenpally") && Objects.equals(destinationLocation, "Miyapur") || Objects.equals(startLocation, "Miyapur") && Objects.equals(destinationLocation, "Bowenpally"))
			return "219";
		else {
			Random random = new Random();
			int randomNumber = random.nextInt(71);
			int result = randomNumber + 215;
			return String.valueOf(result);
		}
	}

	private String currentDateAndTime() {
		// Get the current date and time
		LocalDateTime currentDateTime = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			currentDateTime = LocalDateTime.now();
		}
		// Format the date and time using a specific pattern
		DateTimeFormatter formatter = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss");
		}
		String formattedDateTime = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			formattedDateTime = currentDateTime.format(formatter);
		}
		// Print the current date and time
		return formattedDateTime;
	}

	private String validityDate() {
		// Get today's date
		LocalDate today = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			today = LocalDate.now();
		}

		// Calculate tomorrow's date
		LocalDate tomorrow = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			tomorrow = today.plusDays(1);
		}

		// Format the date using a specific pattern
		DateTimeFormatter formatter = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		}
		String formattedTomorrow = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			formattedTomorrow = tomorrow.format(formatter);
		}

		// Print tomorrow's date
		return formattedTomorrow + "    00:00 AM";
	}

	private int generateUniqueNumber() {
		Set<Integer> generatedNumbers = new HashSet<>();
		Random random = new Random();

		while (true) {
			int randomNumber = generateRandomNumber(random);
			if (!generatedNumbers.contains(randomNumber)) {
				generatedNumbers.add(randomNumber);
				return randomNumber;
			}
		}
	}

	private int generateRandomNumber(Random random) {
		int min = (int) Math.pow(10, 5 - 1);
		int max = (int) Math.pow(10, 5) - 1;
		return random.nextInt(max - min + 1) + min;
	}
}