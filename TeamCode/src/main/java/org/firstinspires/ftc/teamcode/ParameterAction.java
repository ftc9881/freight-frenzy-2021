package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ParameterAction implements ActionIF {
    private DeviceIF _device = null;
    private String _parameter = null;
    private String _value = null;

    public ParameterAction() {
    }

    public ParameterAction(DeviceIF device) {
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

        try {
            _parameter = jsonObject.getString("parameter");
        } catch (JSONException e) {
            throw new ConfigurationException("Missing parameter", e);
        }


        try {
            _value = jsonObject.getString("value");
        } catch (JSONException e) {
            throw new ConfigurationException("Missing value", e);
        }
    }

    @Override
    public void process(Map<String, Object> properties) {
        RobotLog.dd(this.getClass().getSimpleName(), "Process action");
        _device.setParameter(_parameter, _value);
    }
}
