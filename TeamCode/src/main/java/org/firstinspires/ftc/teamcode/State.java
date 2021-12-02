package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.firstinspires.ftc.teamcode.BooleanState.Activity.valueOf;

public abstract class State {
    boolean _telemetry = false;

    String _name = null;

    private ActionFactoryIF _actionFactory = new ActionFactory();

    public State(String name) {
        _name = name;
    }

    protected abstract void configureAction(JSONObject jsonObject, Map<String, DeviceIF> devices) throws ConfigurationException;

    protected ActionIF createAction(JSONObject jsonObject, Map<String, DeviceIF> devices) throws ConfigurationException {
        String typeName = null;

        try {
            typeName = jsonObject.getString("type");
        } catch (JSONException e) {
            throw new ConfigurationException("Missing type parameter", e);
        }

        ActionFactoryIF.ActionType actionType = ActionFactoryIF.ActionType.valueOf(typeName);

        ActionIF action = _actionFactory.actionInstance(actionType);

        action.configure(jsonObject, devices);

        return action;
    }

    void configureAllActions(JSONArray actions, Map<String, DeviceIF> devices) throws ConfigurationException {
        for(int i = 0;i < actions.length();++i) {
            try {
                JSONObject jsonObject = actions.getJSONObject(i);
                configureAction(jsonObject, devices);
            } catch (JSONException e) {
                throw new ConfigurationException("Action not a JSON object", e);
            }
        }
    }

    void configure(JSONObject jsonObject, Map<String, DeviceIF> devices) throws ConfigurationException {
        RobotLog.dd(this.getClass().getSimpleName(), "Configure State");

        try {
            if(jsonObject.has("telemetry")) {
                _telemetry = jsonObject.getBoolean("telemetry");
                RobotLog.dd(this.getClass().getSimpleName(), "_telemetry: %s", _telemetry);
            }

            if(jsonObject.has("actions")) {
                configureAllActions(jsonObject.getJSONArray("actions"), devices);
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
