package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONObject;

import java.util.Map;

public class DeviceMotor extends Device implements DeviceIF {
    private BotMotor _botMotor = null;

    public DeviceMotor(OpMode opMode) {
        super(opMode);

        _botMotor = new BotMotor(null, opMode);
    }

    @Override
    public void configure(JSONObject jsonObject) throws ConfigurationException {
        super.configure(jsonObject);

        _botMotor.configure(jsonObject);
    }

    void setSpeed(double speed) {
        _botMotor.setSpeed(speed);
    }
    public double getSpeed() {
        return _botMotor.getSpeed();
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

    private enum Behavior {
        FORWARD,
        REVERSE,
        STOP
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
            _botMotor.addTelemetryData(telemetry);
        }
    }

    @Override
    public void getPropertyValues(Map<String, Object> values) {
        super.getPropertyValues(values);

        _botMotor.getPropertyValues(values);
    }

    @Override
    public void processAction(ActionIF action, double value) {
        RobotLog.dd(this.getClass().getSimpleName(), "Process action: %s %s", action.getBehavior(), value);

        Behavior behavior = Behavior.valueOf(action.getBehavior());

        switch(behavior) {
            case FORWARD:
                _botMotor.setSpeed(value);
                break;
            case REVERSE:
                _botMotor.setSpeed(-value);
                break;
            case STOP:
                _botMotor.setSpeed(0);
                break;
        }
    }
}
