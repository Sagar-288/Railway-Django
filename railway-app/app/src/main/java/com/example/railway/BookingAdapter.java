package com.example.railway;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookingAdapter extends ArrayAdapter<Booking> {

    public BookingAdapter(Activity context, ArrayList<Booking> bookings){
        super((Context) context, 0, bookings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.bookitem, parent, false);
        }

        Booking currentBooking = getItem(position);

        TextView nametrainTextView = (TextView) listItemView.findViewById(R.id.train_name1);
        nametrainTextView.setText(currentBooking.trainName);

        TextView numberTextView = (TextView) listItemView.findViewById(R.id.train_number1);
        numberTextView.setText(String.valueOf(currentBooking.trainNo));

        TextView acTextView = (TextView) listItemView.findViewById(R.id.ac);
        acTextView.setText(currentBooking.acSeats + "/" + currentBooking.acFare);


        TextView sitterTextView = (TextView) listItemView.findViewById(R.id.sitter);
        sitterTextView.setText(currentBooking.sittingSeats + "/" + currentBooking.sittingFare);


        TextView sleepTextView = (TextView) listItemView.findViewById(R.id.sleeper);
        sleepTextView.setText(currentBooking.sleeperSeats + "/" + currentBooking.sleeperFare);


        return listItemView;
    }
}

