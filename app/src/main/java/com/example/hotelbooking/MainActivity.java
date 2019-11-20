package com.example.hotelbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity{

    private Spinner spinnerLocation, spinnerRoomType;
    private EditText etNumberOfAdults, etNumberOfChildren, etNumberOfRooms;
    private TextView tvCheckInDate, tvCheckOutDate, tvLocation, tvRoomType, tvRoomTotal, tvVAT, tvServiceCharge, tvTotal, tvNumberOfDays, tvGuests;

    private Boolean boolCheckIn, boolCheckOut;
    private Button btnCalculate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        etNumberOfAdults = findViewById(R.id.etNumberOfAdults);
        etNumberOfChildren = findViewById(R.id.etNumberOfChildren);
        etNumberOfRooms = findViewById(R.id.etNumberOfRooms);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        spinnerRoomType = findViewById(R.id.spinnerRoomType);
        tvCheckInDate = findViewById(R.id.tvCheckInDate);
        tvCheckOutDate = findViewById(R.id.tvCheckOutDate);
        tvRoomTotal = findViewById(R.id.tvRoomTotal);
        tvNumberOfDays = findViewById(R.id.tvNumberOfDays);
        tvGuests = findViewById(R.id.tvGuests);
        tvVAT = findViewById(R.id.tvVAT);
        tvServiceCharge = findViewById(R.id.tvServiceCharge);
        tvTotal = findViewById(R.id.tvTotal);

        String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        tvCheckInDate.setText(date);
        tvCheckOutDate.setText(date);


        tvLocation = findViewById(R.id.tvLocation);
        tvRoomType = findViewById(R.id.tvRoomType);

        btnCalculate = findViewById(R.id.btnCalculate);

        tvCheckInDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolCheckIn = true;
                loadDatePicker();
            }
        });

        tvCheckOutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolCheckOut = true;
                loadDatePicker();
            }
        });

        String locations[] = {"Please select Location", "Bhaktapur", "Chitwan", "Kathmandu"};
        ArrayAdapter locationAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                locations
        );

        String roomTypes[] = {"Please select Room Type", "Deluxe", "AC", "Platinum"};
        ArrayAdapter roomTypeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                roomTypes
        );

        spinnerLocation.setAdapter(locationAdapter);
        spinnerRoomType.setAdapter(roomTypeAdapter);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(etNumberOfAdults.getText()) || (Integer.parseInt(etNumberOfAdults.getText().toString())) < 1){
                    etNumberOfAdults.setError("Enter Valid Number");
                    return;
                }
                else if (Integer.parseInt(etNumberOfChildren.getText().toString()) < 0){
                    etNumberOfChildren.setError("Enter Valid Number");
                    return;
                }
                else if (TextUtils.isEmpty(etNumberOfRooms.getText()) || (Integer.parseInt(etNumberOfRooms.getText().toString())) < 1){
                    etNumberOfRooms.setError("Enter Valid Number");
                    return;
                }

                if (spinnerLocation.getSelectedItem().toString() == "Please select Location"){
                    Toast.makeText(MainActivity.this, "Please select Location", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (spinnerRoomType.getSelectedItem().toString() == "Please select Room Type"){
                    Toast.makeText(MainActivity.this, "Please select Room Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                float rate = 0, total = 0, VAT = 0, serviceCharge = 0;
                String roomType = spinnerRoomType.getSelectedItem().toString();
                int days = 0;
                String checkInDate = tvCheckInDate.getText().toString();
                String checkOutDate = tvCheckOutDate.getText().toString();

                switch (spinnerRoomType.getSelectedItem().toString()){
                    case "Deluxe":
                        rate = 2000;
                        break;
                    case "AC":
                        rate = 3000;
                        break;
                    case "Platinum":
                        rate = 4000;
                        break;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd");
                try {
                    Date checkIn = sdf.parse(checkInDate);
                    Date checkOut = sdf.parse(checkOutDate);
                    long diff = checkOut.getTime() - checkIn.getTime();
                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    days = (int) diffDays;

                    total = rate * days * Float.parseFloat(etNumberOfRooms.getText().toString());
                    VAT = (total*13)/100;
                    serviceCharge = (total*10)/100;

                    tvLocation.setText("Location : " + spinnerLocation.getSelectedItem().toString());
                    tvRoomType.setText("Room Type : " + spinnerRoomType.getSelectedItem().toString());
                    tvNumberOfDays.setText("Number of days staying : " + days);
                    if (Integer.parseInt(etNumberOfChildren.getText().toString()) == 0){
                        tvGuests.setText("Guests : " + etNumberOfAdults.getText() + " Adults");
                    }
                    else {
                        tvGuests.setText("Guests : " + etNumberOfAdults.getText() + " Adults, " + etNumberOfChildren.getText() + " Children");
                    }
                    tvRoomTotal.setText("Total Cost of Room : Rs " + total);
                    tvVAT.setText("VAT : Rs " + VAT);
                    tvServiceCharge.setText("Service Charge : Rs " + serviceCharge);
                    tvTotal.setText("Total : Rs " + (total + VAT + serviceCharge));
                } catch (ParseException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadDatePicker(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                if (boolCheckIn == true) {
                    tvCheckInDate.setText(year + "/" + month + "/" + dayOfMonth);
                    boolCheckIn = false;
                }
                else if (boolCheckOut == true){
                    tvCheckOutDate.setText(year + "/" + month + "/" + dayOfMonth);
                    boolCheckOut = false;
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }
}
