package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class BehaviorAction implements ActionIF {
    private DeviceIF _device = null;
    private String _behavior = null;

    public BehaviorAction() {
    }

    public BehaviorAction(DeviceIF device, String behavior) {
        _device = device;
        _behavior = behavior;
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
            _behavior = jsonObject.getString("behavior");
        } catch (JSONException e) {
            throw new ConfigurationException("Missing behavior", e);
        }

    }

    public String getBehavior() {
        return _behavior;
    }

    @Override
    public void process(Map<String, Object> properties) {
        RobotLog.dd(this.getClass().getSimpleName(), "Process action");

        _device.behave(this, _behavior, properties);
    }
}
