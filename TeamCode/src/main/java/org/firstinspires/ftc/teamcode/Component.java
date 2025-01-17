package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public abstract class Component implements ComponentIF {
    public final OpMode _opMode;
    protected boolean _log = false;
    protected boolean _telemetry = false;

    protected String _name;

    public Component(OpMode opMode, String name) {
        _opMode = opMode;
        _name = name;
    }

    @Override
    public void configure(JSONObject jsonObject) throws ConfigurationException {
        try {
            if (jsonObject.has("log")) {
                _log = jsonObject.getBoolean("log");
            }
            if (jsonObject.has("telemetry")) {
                _telemetry = jsonObject.getBoolean("telemetry");
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    @Override
    public void addTelemetryData(Telemetry telemetry) {
    }

    public void getPropertyValues(Map<String, Object> values) {
    }
}
