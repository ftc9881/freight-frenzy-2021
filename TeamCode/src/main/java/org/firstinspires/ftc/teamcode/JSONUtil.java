package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JSONUtil {
    private static final String CLASS_NAME = "JSONUtil";

    public static JSONObject getJsonObject(String jsonFileName) throws IOException, JSONException {
        int tries = 0;

        StringBuilder responseStrBuilder = null;

        while(tries < 30) {
            BufferedReader streamReader = new BufferedReader(new FileReader(jsonFileName));
            responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            if(responseStrBuilder.length() == 0) {
                RobotLog.dd(CLASS_NAME, "Empty file: %s", jsonFileName);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ++tries;
            } else {
                tries = Integer.MAX_VALUE;
            }
        }

        JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());

        //returns the json object
        return jsonObject;
    }
}
