package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoubleState extends State {
    double _value = 0;

    enum Activity {
        CHANGE
    }

    List<ActionIF> _changeActions = new ArrayList<>();

    public DoubleState(String name, double value) {
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

        Activity activity = Activity.valueOf(activityName);

        ActionIF action = createAction(jsonObject, devices);

        switch(activity) {
            case CHANGE:
                _changeActions.add(action);
                break;
        }
    }

    public double getValue() {
        return _value;
    }

    void updateValue(double value) {
        if(_value != value) {
            _value = value;

            Map<String, Object> properties = new HashMap<String, Object>();

            properties.put("value", _value);

            RobotLog.dd(this.getClass().getSimpleName(), "%s::Process change actions: %f", _name, _value);

            for(ActionIF action : _changeActions) {
                action.process(properties);
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
