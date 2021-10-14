package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONObject;

import java.util.Map;

public class ControllerSteer extends Controller implements ControllerIF {
    static final String CLASS_NAME = "ControllerSteer";
    private Movement _movement = new Movement(0, 0, 0);

    public ControllerSteer(OpMode opMode) {
        super(opMode);
    }

    @Override
    public void configure(JSONObject jsonObject, Map<String, DeviceIF> devices) throws ConfigurationException {
        super.configure(jsonObject, devices);
    }

    public void updateMovement() {
        double moveSpeed = Math.sqrt(Math.pow(_leftX.getValue(), 2) + Math.pow(_leftY.getValue(), 2));
        double moveAngle = Math.atan2(-_leftY.getValue(), -_leftX.getValue()) - (Math.PI / 2);

        // TODO: This should be angular speed, but this works for now

        double turnSpeed = _rightX.getValue();

        RobotLog.dd(CLASS_NAME, "moveSpeed: %s", moveSpeed);
        RobotLog.dd(CLASS_NAME, "moveAngle: %s", moveAngle);
        RobotLog.dd(CLASS_NAME, "turnSpeed: %s", turnSpeed);

        _movement = new Movement(moveSpeed, moveAngle, turnSpeed);
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
