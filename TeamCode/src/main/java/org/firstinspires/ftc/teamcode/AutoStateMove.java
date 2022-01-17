package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AutoStateMove extends AutoStateMotion implements AutoStateIF {
    private static final String CLASS_NAME = "AutoStateMove";

    Movement _movement = new Movement();
    double _distance = 0;

    public AutoStateMove(OpMode opMode, SteeringIF steering) {
        super(opMode, steering);
    }

    public void configure(JSONObject jsonObject,
                          DriveTrainIF driveTrain,
                          Map<String, DeviceIF> devices,
                          Map<String, SensorIF> sensors) throws ConfigurationException {
        super.configure(jsonObject, driveTrain, devices, sensors);

        _driveTrain = driveTrain;

        try {
            if (jsonObject.has("movement")) {
                _movement.configure(jsonObject.getJSONObject("movement"));
            }
            if (jsonObject.has("distance")) {
                _distance = jsonObject.getDouble("distance");
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    public boolean doAction(RobotBase robotBase) throws InterruptedException {
        boolean active = super.doAction(robotBase);

        if(active) {
            RobotLog.dd(CLASS_NAME, "doAction()");

            Movement effMovement;

            if (_steering == null) {
                effMovement = _movement;
            } else {
                effMovement = _steering.steer(_movement);
            }

            RobotLog.dd(CLASS_NAME, "doAction()::moveSpeed: %.2f moveAngle: %.2f turnSpeed: %.2f", effMovement._moveSpeed, effMovement._moveAngle, effMovement._turnSpeed);

            _driveTrain.updateMovement(effMovement);

            double distance = _driveTrain.getDistance(true);

            RobotLog.dd(CLASS_NAME, "doAction()::distance: %.2f", distance);

            active = distance <= _distance;
        }

        return active;
    }
}