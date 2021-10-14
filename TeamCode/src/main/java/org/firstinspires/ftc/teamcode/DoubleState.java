package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DoubleState extends State {
    double _value = 0;

    List<ActionIF> _changeActions = new ArrayList<>();

    public DoubleState(String name, double value) {
        super(name);
        _value = value;
    }

    void configureAllActions(JSONObject jsonObject, Map<String, DeviceIF> devices) throws ConfigurationException {
        try {
            if(jsonObject.has("change")) {
                RobotLog.dd(this.getClass().getSimpleName(), "Configure change actions");

                _changeActions = configureActions(jsonObject.getJSONObject("change"), devices);
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e);
        }
    }

    public double getValue() {
        return _value;
    }

    void updateValue(double value) {
        if(_value != value) {
            _value = value;

            RobotLog.dd(this.getClass().getSimpleName(), "%s::Process change actions: %f", _name, _value);

            for(ActionIF action : _changeActions) {
                action.process(_value);
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
