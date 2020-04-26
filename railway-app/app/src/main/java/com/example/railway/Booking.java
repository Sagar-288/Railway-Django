package com.example.railway;

import org.json.JSONException;
import org.json.JSONObject;

public class Booking {;
    public String trainNo, trainName, acSeats, sittingSeats, sleeperSeats, acFare, sleeperFare, sittingFare;

    public Booking(JSONObject obj) throws JSONException {
        trainName = obj.getString("trainName");
        trainNo = obj.getString("trainNo");
        acSeats = obj.getString("availAC");
        sittingSeats = obj.getString("availSitting");
        sleeperSeats = obj.getString("availSleeper");
        acFare = obj.getString("acfare");
        sittingFare = obj.getString("sittingfare");
        sleeperFare = obj.getString("sleeperfare");
    }
    public JSONObject returnjson() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("trainName", trainName);
        obj.put("trainNo", trainNo);
        obj.put("acSeats", acSeats);
        obj.put("sittingSeats", sittingSeats);
        obj.put("acFare",acFare);
        obj.put("sleeperSeats",sleeperSeats);
        obj.put("sleeperFare", sleeperFare);
        obj.put("sittingFare", sittingFare);

        return obj;
    }
}
