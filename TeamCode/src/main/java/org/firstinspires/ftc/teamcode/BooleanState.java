package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.firstinspires.ftc.teamcode.BooleanState.Activity.*;

public class BooleanState extends State {
    boolean _value = false;

    enum Activity {
        PRESS,
        RELEASE
    }

    List<ActionIF> _pressActions = new ArrayList<>();
    List<ActionIF> _releaseActions = new ArrayList<>();

    public BooleanState(String name, boolean value) {
        super(name);
        _value = value;
    }

    protected void configureAction(JSONObject jsonObject, Map<String, DeviceIF> devices) throws ConfigurationException {
        String activityName = null;

        try {
            activityName = jsonObject.getString("activity");
        } catch (JSONException e) {
            throw new ConfigurationException("Missing activity", e);
        }

        Activity activity = valueOf(activityName);

        ActionIF action = createAction(jsonObject, devices);

        switch(activity) {
            case PRESS:
                _pressActions.add(action);
                break;
            case RELEASE:
                _releaseActions.add(action);
                break;
        }
    }

    void updateValue(boolean value) {
        if (_value != value) {
            _value = value;

            Map<String, Object> properties = new HashMap<String, Object>();

            properties.put("value", Double.valueOf(_value ? 1 : 0));

            if (_value) {
                RobotLog.dd(this.getClass().getSimpleName(), "%s::Process press actions: %s", _name, _value);

                for (ActionIF action : _pressActions) {
                    action.process(properties);
                }
            } else {
                RobotLog.dd(this.getClass().getSimpleName(), "%s::Process release actions: %s", _name, _value);

                for (ActionIF action : _releaseActions) {
                    action.process(properties);
                }
            }
        }
    }

    public void updateTelemetry(Telemetry telemetry) {
        if(_telemetry) {
            telemetry.addData(_name, String.valueOf(_value));
        }
    }

    public void getPropertyValues(Map<String, Object>logValues) {
        logValues.put(_name, _value);
    }
}
