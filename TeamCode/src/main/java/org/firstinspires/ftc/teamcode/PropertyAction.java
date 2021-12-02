package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class PropertyAction implements ActionIF {
    private DeviceIF _device = null;

    public PropertyAction() {
    }

    public PropertyAction(DeviceIF device) {
        _device = device;
    }

    @Override
    public void configure(JSONObject jsonObject, Map<String, DeviceIF> devices) throws ConfigurationException {
        try {
            String deviceName = jsonObject.getString("deviceName");

            _device = devices.get(deviceName);

            if(_device == null) {
                throw new ConfigurationException("Invalid device name: " + deviceName);
            }
        } catch (JSONException e) {
            throw new ConfigurationException("Missing device", e);
        }
    }

    @Override
    public void process(Map<String, Object> properties) {
        RobotLog.dd(this.getClass().getSimpleName(), "Process action");
    }
}
