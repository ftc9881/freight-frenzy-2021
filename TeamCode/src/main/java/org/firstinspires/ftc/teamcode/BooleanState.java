package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BooleanState extends State {
    boolean _value = false;

    List<ActionIF> _pressActions = new ArrayList<>();
    List<ActionIF> _releaseActions = new ArrayList<>();

    public BooleanState(String name, boolean value) {
        super(name);
        _value = value;
    }

    void configureAllActions(JSONObject jsonObject, Map<String, DeviceIF> devices) throws ConfigurationException {
        try {
            if(jsonObject.has("press")) {
                RobotLog.dd(this.getClass().getSimpleName(), "Configure press actions");

                _pressActions = configureActions(jsonObject.getJSONObject("press"), devices);
            }

            if(jsonObject.has("release")) {
                RobotLog.dd(this.getClass().getSimpleName(), "Configure release actions");

                _releaseActions = configureActions(jsonObject.getJSONObject("release"), devices);
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e);
        }
    }

    void updateValue(boolean value) {
        if (_value != value) {
            _value = value;

            if (_value) {
                RobotLog.dd(this.getClass().getSimpleName(), "%s::Process press actions: %s", _name, _value);

                for (ActionIF action : _pressActions) {
                    action.process(1);
                }
            } else {
                RobotLog.dd(this.getClass().getSimpleName(), "%s::Process release actions: %s", _name, _value);

                for (ActionIF action : _releaseActions) {
                    action.process(0);
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
