package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class DeviceServo extends Device implements DeviceIF {
    private static final String CLASS_NAME = "DeviceServo";

    private BotServo _botServo = null;

    double _minPosition = 0;
    double _maxPosition = 1;

    double _positionIncrement = .1;

    private enum Behavior {
        MAXIMUM,
        MINIMUM,
        FORWARD,
        REVERSE
    }

    private enum Parameter {
        MIN_POSITION,
        MAX_POSITION,
        POSITION_INCREMENT
    }

    public DeviceServo(OpMode opMode, String name) {
        super(opMode, name);

        _botServo = new BotServo(opMode, name);
    }

    @Override
    public void configure(JSONObject jsonObject) throws ConfigurationException {
        super.configure(jsonObject);

        _botServo.configure(jsonObject);

        try {
            if( jsonObject.has("maxPosition")) {
                _maxPosition = jsonObject.getDouble("maxPosition");
                RobotLog.dd(CLASS_NAME, "configure()::_maxPosition: %s", _maxPosition);
            }
            if( jsonObject.has("minPosition")) {
                _minPosition = jsonObject.getDouble("minPosition");
                RobotLog.dd(CLASS_NAME, "configure()::_minPosition: %s", _minPosition);
            }
            if( jsonObject.has("positionIncrement")) {
                _positionIncrement = jsonObject.getDouble("positionIncrement");
                RobotLog.dd(CLASS_NAME, "configure()::_positionIncrement: %s", _positionIncrement);
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isValidBehavior(String behavior) {
        try {
            Behavior.valueOf(behavior);
            return true;
        } catch (IllegalArgumentException ex) {
            return super.isValidBehavior(behavior);
        }
    }

    @Override
    public void addTelemetryData(Telemetry telemetry) {
        super.addTelemetryData(telemetry);

        if(_telemetry) {
            _botServo.addTelemetryData(telemetry);
        }
    }

    @Override
    public void getPropertyValues(Map<String, Object> values) {
        super.getPropertyValues(values);

        _botServo.getPropertyValues(values);
    }

    @Override
    public void behave(ActionIF action, String behaviorName, Map<String, Object> properties) {
        RobotLog.dd(this.getClass().getSimpleName(), "Process action: %s %s %s", _name, behaviorName, properties);

        Behavior behavior = Behavior.valueOf(behaviorName);

        double value = Double.valueOf(properties.get("value").toString());

        switch(behavior) {
            case MAXIMUM:
                _botServo.setPosition(_maxPosition);
                break;
            case MINIMUM:
                _botServo.setPosition(_minPosition);
                break;
            case FORWARD:
                _botServo.setPosition(Math.max(_minPosition, Math.min(_maxPosition, _botServo.getPosition() + _positionIncrement)));
                break;
            case REVERSE:
                _botServo.setPosition(Math.max(_minPosition, Math.min(_maxPosition, _botServo.getPosition() - _positionIncrement)));
                break;
        }
    }

    @Override
    public boolean isValidParameter(String parameter) {
        try {
            Parameter.valueOf(parameter);
            return true;
        } catch (IllegalArgumentException ex) {
            return super.isValidParameter(parameter);
        }
    }

    @Override
    public void setParameter(String parameterName, String value) {
        RobotLog.dd(this.getClass().getSimpleName(), "Set parameter: %s %s %s", _name, parameterName, value);

        Parameter parameter = Parameter.valueOf(parameterName);

        // Have the ability to configure multiple parameters

        double doubleValue = Double.valueOf(value).doubleValue();

        switch(parameter) {
            case MAX_POSITION:
                _maxPosition = doubleValue;
                break;
            case MIN_POSITION:
                _minPosition = doubleValue;
                break;
            case POSITION_INCREMENT:
                _positionIncrement = doubleValue;
                break;
        }
    }


    public void init() {
    }

    public void update() {
    }

    public void terminate() {
    }

}
