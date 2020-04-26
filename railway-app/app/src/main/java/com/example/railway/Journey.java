package com.example.railway;

import org.json.JSONException;
import org.json.JSONObject;

public class Journey {
    public String trainName, trainNo, trainDate, pnrNo, sittingClass, passName, passAge, gender;

    public Journey(JSONObject obj) throws JSONException {
//    trainName = vtrainName;
//    trainNo = vtrainNo;

        trainName = obj.getString("trainName");
        trainNo = obj.getString("trainNo");
        trainDate = obj.getString("traindate");
        pnrNo = obj.getString("pnrNo");
        sittingClass = obj.getString("seatType");
        passName = obj.getString("name");
        passAge = obj.getString("age");
        gender = obj.getString("gender");
    }

}
