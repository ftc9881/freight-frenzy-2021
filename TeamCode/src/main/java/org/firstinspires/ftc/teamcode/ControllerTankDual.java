package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONObject;

import java.util.Map;

public class ControllerTankDual extends Controller implements ControllerIF {
    static final String CLASS_NAME = "ControllerTankDual";

    public ControllerTankDual(OpMode opMode, String name) {
        super(opMode, name);
    }

    private Movement _movement = new Movement(0, 0, 0);

    @Override
    public void configure(JSONObject jsonObject, Map<String, DeviceIF> devices) throws ConfigurationException {
        super.configure(jsonObject, devices);
    }

    @Override
    public void updateMovement() {
        double moveSpeed = (_leftX.getValue() + _leftY.getValue()) / 2;
        // TODO: This should be angular speed, but this works for now

        double turnSpeed = _leftX.getValue() - _leftY.getValue();

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
