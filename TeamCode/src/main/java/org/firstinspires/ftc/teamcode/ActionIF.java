package org.firstinspires.ftc.teamcode;

import org.json.JSONObject;

import java.util.Map;

public interface ActionIF {
    void configure(JSONObject jsonObject, Map<String, DeviceIF> devices) throws ConfigurationException;

    void process(Map<String, Object> properties);
}
