package com.example.railway;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class JourneyAdapter extends ArrayAdapter<Journey> {

    public JourneyAdapter(Activity context, ArrayList<Journey> journeys){
        super(context, 0, journeys);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item, parent, false);
        }

        Journey currentJourney = getItem(position);

        TextView nametrainTextView = (TextView) listItemView.findViewById(R.id.train_name);
        nametrainTextView.setText(currentJourney.trainName);

        TextView numberTextView = (TextView) listItemView.findViewById(R.id.train_number);
        numberTextView.setText(String.valueOf(currentJourney.trainNo));

        TextView genderTextView = (TextView) listItemView.findViewById(R.id.gender);
        genderTextView.setText(String.valueOf(currentJourney.gender));

        TextView ageTextView = (TextView) listItemView.findViewById(R.id.age);
        ageTextView.setText(String.valueOf(currentJourney.passAge));

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.train_date);
        dateTextView.setText(String.valueOf(currentJourney.trainDate));

        TextView typeTextView = (TextView) listItemView.findViewById(R.id.seattype);
        typeTextView.setText(String.valueOf(currentJourney.sittingClass));

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.name);
        nameTextView.setText(String.valueOf(currentJourney.passName));

        TextView pnrTextView = (TextView) listItemView.findViewById(R.id.pnrNo);
        pnrTextView.setText(String.valueOf(currentJourney.pnrNo));


        return listItemView;
    }
}
