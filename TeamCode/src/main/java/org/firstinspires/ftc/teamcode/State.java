package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class State {
    boolean _telemetry = false;

    String _name = null;

    public State(String name) {
        _name = name;
    }

    void configureAllActions(JSONObject actions, Map<String, DeviceIF> devices) throws ConfigurationException {
    }

    List<ActionIF> configureActions(JSONObject jsonObject, Map<String, DeviceIF> devices) throws ConfigurationException {
        RobotLog.dd(this.getClass().getSimpleName(), "Configure actions");

        /*
            TODO: Implement multiple actions
         */

        List<ActionIF> actions = new ArrayList<>();

        String deviceName;

        try {
            deviceName = jsonObject.getString("deviceName");

            RobotLog.dd(this.getClass().getSimpleName(), "deviceName: " + deviceName);
        } catch (JSONException e) {
            throw new ConfigurationException("Missing deviceName parameter", e);
        }

        DeviceIF device = devices.get(deviceName);

        if(device == null) {
            throw new ConfigurationException("Invalid deviceName: " + deviceName, null);
        }

        String behavior;

        try {
            behavior = jsonObject.getString("behavior");
        } catch (JSONException e) {
            throw new ConfigurationException("Missing behavior parameter", e);
        }

        RobotLog.dd(this.getClass().getSimpleName(), "behavior: " + behavior);

        if(!device.isValidBehavior(behavior)) {
            throw new ConfigurationException("Invalid behavior for " + deviceName + ": " + behavior, null);
        }

        Action action = new Action(device, behavior);

        actions.add(action);

        String deviceName2 = null;

        try {
            deviceName2 = jsonObject.getString("deviceName2");

            RobotLog.dd(this.getClass().getSimpleName(), "deviceName2: " + deviceName);
        } catch (JSONException e) {
        }

        if(deviceName2 != null) {
            DeviceIF device2 = devices.get(deviceName2);

            if (device2 == null) {
                throw new ConfigurationException("Invalid deviceName2: " + deviceName2, null);
            }

            String behavior2;

            try {
                behavior2 = jsonObject.getString("behavior2");
            } catch (JSONException e) {
                throw new ConfigurationException("Missing behavior2 parameter", e);
            }

            RobotLog.dd(this.getClass().getSimpleName(), "behavior2: " + behavior2);

            if (!device.isValidBehavior(behavior2)) {
                throw new ConfigurationException("Invalid behavior2 for " + deviceName2 + ": " + behavior2, null);
            }

            Action action2 = new Action(device2, behavior2);

            actions.add(action2);
        }

        return(actions);
    }

    void configure(JSONObject jsonObject, Map<String, DeviceIF> devices) throws ConfigurationException {
        RobotLog.dd(this.getClass().getSimpleName(), "Configure State");

        try {
            if(jsonObject.has("telemetry")) {
                _telemetry = jsonObject.getBoolean("telemetry");
                RobotLog.dd(this.getClass().getSimpleName(), "_telemetry: %s", _telemetry);
            }

            if(jsonObject.has("actions")) {
                configureAllActions(jsonObject.getJSONObject("actions"), devices);
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e);
        }
    }

    public boolean isTelemetry() {
        return _telemetry;
    }

    public void updateTelemetry(Telemetry telemetry) {
    }
}
