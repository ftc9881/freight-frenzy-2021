package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class DeviceMotor extends Device implements DeviceIF {
    private static final String CLASS_NAME = "DeviceMotor";

    private BotMotor _botMotor = null;

    private double _speed = 0;

    private double _maxSpeed = 1;

    private double _minSpeed = -1;

    private double _speedScale = 1;

    private double _speedExponent = 1;

    enum Behavior {
        FORWARD,
        REVERSE,
        STOP
    }

    enum Parameter {
        MAX_SPEED,
        MIN_SPEED,
        SPEED_SCALE,
        SPEED_EXPONENT
    }

    public DeviceMotor(OpMode opMode, String name) {
        super(opMode, name);

        _botMotor = new BotMotor(opMode, name);
    }

    @Override
    public void configure(JSONObject jsonObject) throws ConfigurationException {
        super.configure(jsonObject);

        _botMotor.configure(jsonObject);

        try {
            if( jsonObject.has("maxSpeed")) {
                _maxSpeed = jsonObject.getDouble("maxSpeed");
                RobotLog.dd(CLASS_NAME, "configure()::_maxSpeed: %s", _maxSpeed);
            }

            if( jsonObject.has("minSpeed")) {
                _minSpeed = jsonObject.getDouble("minSpeed");
                RobotLog.dd(CLASS_NAME, "configure()::_minSpeed: %s", _minSpeed);
            }

            if( jsonObject.has("speedScale")) {
                _speedScale = jsonObject.getDouble("speedScale");
                RobotLog.dd(CLASS_NAME, "configure()::_speedScale: %s", _speedScale);
            }

            if( jsonObject.has("speedExponent")) {
                _speedExponent = jsonObject.getDouble("speedExponent");
                RobotLog.dd(CLASS_NAME, "configure()::_speedExponent: %s", _speedExponent);
            }

            if( jsonObject.has("maxPosition")) {
                int maxPosition = jsonObject.getInt("maxPosition");
                RobotLog.dd(CLASS_NAME, "configure()::maxPosition: %s", maxPosition);
                _botMotor.setMaxPosition(maxPosition);
            }

            if( jsonObject.has("minPosition")) {
                int minPosition = jsonObject.getInt("minPosition");
                RobotLog.dd(CLASS_NAME, "configure()::minPosition: %s", minPosition);
                _botMotor.setMinPosition(minPosition);
            }

        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }

    }

    void setSpeed(double speed) {
        _speed = speed;
        _botMotor.setSpeed(transformSpeed(speed));
    }

    public double getSpeed() {
        return _speed;
    }

    public int getCurrentPosition() {
        return _botMotor.getCurrentPosition();
    }

    public void resetPosition() {
        _botMotor.resetPosition();
    }

    public double getDistance() {
        return _botMotor.getDistance();
    }

    public double getVelocity() {
        return _botMotor.getVelocity();
    }

    public void stop() {
        _botMotor.stop();
    }

    @Override
    public void addTelemetryData(Telemetry telemetry) {
        super.addTelemetryData(telemetry);

        if(_telemetry) {
            _botMotor.addTelemetryData(telemetry);
            telemetry.addData(_name, "speedScale: %.2f speedExponent: %.2f", _speedScale, _speedExponent);

        }
    }

    @Override
    public void getPropertyValues(Map<String, Object> values) {
        super.getPropertyValues(values);

        _botMotor.getPropertyValues(values);
    }

    double transformSpeed(double speed) {
        return Math.max(_minSpeed, Math.min(_maxSpeed, Math.signum(speed) * Math.pow(Math.abs(speed) * _speedScale, _speedExponent)));
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
    public void behave(ActionIF action, String behaviorName, Map<String, Object> properties) {
        RobotLog.dd(this.getClass().getSimpleName(), "Process action: %s %s %s", _name, behaviorName, properties);

        Behavior behavior = Behavior.valueOf(behaviorName);

        double value = Double.valueOf(properties.get("value").toString());

        switch(behavior) {
            case FORWARD:
                setSpeed(value);
                break;
            case REVERSE:
                setSpeed(-value);
                break;
            case STOP:
                _botMotor.setSpeed(0);
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

        switch(parameter) {
            case MAX_SPEED:
                _maxSpeed = Double.valueOf(value).doubleValue();
                break;
            case MIN_SPEED:
                _minSpeed = Double.valueOf(value).doubleValue();
                break;
            case SPEED_SCALE:
                _speedScale = Double.valueOf(value).doubleValue();
                break;
            case SPEED_EXPONENT:
                _speedExponent = Double.valueOf(value).doubleValue();
                break;
        }
    }

    public void init() {
        resetPosition();
        _botMotor.init();
    }

    public void update() {
        _botMotor.update();
    }

    public void terminate() {
        _botMotor.terminate();
    }
}
