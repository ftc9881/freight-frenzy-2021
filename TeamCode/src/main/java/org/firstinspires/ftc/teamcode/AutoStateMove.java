package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AutoStateMove extends AutoState implements AutoStateIF {
    private static final String CLASS_NAME = "AutoStateMove";

    Movement _movement = new Movement();
    double _distance = 0;
    SteeringIF _steering;
    DriveTrainIF _driveTrain;

    public AutoStateMove(OpMode opMode) {
        super(opMode);
    }

    private void configureSteering(JSONObject jsonObject, Map<String, SensorIF> sensors) throws ConfigurationException {
        try {
            String steeringTypeName = jsonObject.getString("type");

            Steering.SteeringType steeringType = Steering.SteeringType.valueOf(steeringTypeName);

            _steering = Steering.constructSteering(steeringType);

            _steering.configure(jsonObject, sensors);

        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
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
            if (jsonObject.has("steering")) {
                configureSteering(jsonObject.getJSONObject("steering"), sensors);
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    public void init() {
        super.init();

        RobotLog.dd(CLASS_NAME, "init()");

        if (_steering != null) {
            _steering.init();
        }

        _driveTrain.resetPositions();
    }

    public boolean doAction() {
        boolean active = super.doAction();

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

    public void end() {
        super.end();

        _driveTrain.stop();
    }
}