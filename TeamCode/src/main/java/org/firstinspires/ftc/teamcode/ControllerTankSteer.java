package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ControllerTankSteer extends Controller implements ControllerIF {
    static final String CLASS_NAME = "ControllerTankSteer";

    public ControllerTankSteer(OpMode opMode, String name) {
        super(opMode, name);
    }
    private Movement _movement = new Movement(0, 0, 0);

    double _turnScale = 1;

    double _turnExponent = 2.5;

    @Override
    public void configure(JSONObject jsonObject, Map<String, DeviceIF> devices) throws ConfigurationException {
        super.configure(jsonObject, devices);

        try {
            if( jsonObject.has("turnScale")) {
                _turnScale = jsonObject.getDouble("turnScale");
                RobotLog.dd(CLASS_NAME, "configure()::_turnScale: %s", _turnScale);
            }

            if( jsonObject.has("turnExponent")) {
                _turnExponent = jsonObject.getDouble("turnExponent");
                RobotLog.dd(CLASS_NAME, "configure()::_turnExponent: %s", _turnExponent);
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }

    }

    @Override
    public void updateMovement() {
        double moveSpeed = -_leftY.getValue();

        // TODO: This should be angular speed, but this works for now

        double turnValue = _rightX.getValue();

        double turnSpeed = _turnScale * Math.pow(Math.abs(turnValue), _turnExponent) * Math.signum(turnValue);

        RobotLog.dd(CLASS_NAME, "moveSpeed: %s", moveSpeed);
        RobotLog.dd(CLASS_NAME, "turnSpeed: %s", turnSpeed);

        _movement = new Movement(moveSpeed, 0, turnSpeed);
    }

    @Override
    public Movement getMovement() {
        return _movement;
    }

    @Override
    public void addTelemetryData(Telemetry telemetry) {
        super.addTelemetryData(telemetry);

        if(_telemetry) {
            _movement.updateTelemetry(telemetry);
        }
    }

    public void getPropertyValues(Map<String, Object> values) {
        super.getPropertyValues(values);

        _movement.getPropertyValues(values);
    }
}
