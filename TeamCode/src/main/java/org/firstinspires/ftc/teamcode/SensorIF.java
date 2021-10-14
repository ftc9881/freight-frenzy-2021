package org.firstinspires.ftc.teamcode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public interface SensorIF extends ComponentIF {
    void configure(JSONObject jsonObject, Map<String, DeviceIF> devices) throws JSONException, ConfigurationException;

    void update();
}
