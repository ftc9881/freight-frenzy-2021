package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AutoStateTurn extends AutoStateMotion {
    private static final String CLASS_NAME = "AutoStateTurn";

    double _angle = 0;
    double _maxError = 1;
    boolean _global = false;
    double _maxTurnSpeed = .25;
    double _minTurnSpeed = .15;

    public AutoStateTurn(OpMode opMode, SteeringIF steering) {
        super(opMode, steering);
    }

    public void configure(JSONObject jsonObject,
                          DriveTrainIF driveTrain,
                          Map<String, DeviceIF> devices,
                          Map<String, SensorIF> sensors) throws ConfigurationException {
        super.configure(jsonObject, driveTrain, devices, sensors);

        _driveTrain = driveTrain;

        try {
            if (jsonObject.has("angle")) {
                _angle = jsonObject.getDouble("angle");
                RobotLog.dd(CLASS_NAME, "configure()::angle: %.2f", _angle);
            }
            if (jsonObject.has("maxError")) {
                _maxError = jsonObject.getDouble("maxError");
                RobotLog.dd(CLASS_NAME, "configure()::maxError: %.2f", _maxError);
            }
            if (jsonObject.has("maxTurnSpeed")) {
                _maxTurnSpeed = jsonObject.getDouble("maxTurnSpeed");
                RobotLog.dd(CLASS_NAME, "configure():: %.2f", _maxTurnSpeed);
            }
            if (jsonObject.has("minTurnSpeed")) {
                _minTurnSpeed = jsonObject.getDouble("minTurnSpeed");
                RobotLog.dd(CLASS_NAME, "configure():: %.2f", _minTurnSpeed);
            }
            if (jsonObject.has("global")) {
                _global = jsonObject.getBoolean("global");
                RobotLog.dd(CLASS_NAME, "configure()::global %.2f", _global);
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    @Override
    public boolean doAction(RobotBase robotBase) throws InterruptedException  {
        boolean active = super.doAction(robotBase);

        if(active) {
            RobotLog.dd(CLASS_NAME, "doAction()");

            _steering.update();

            Movement movement = _steering.turn(_angle, _global, _maxError, _minTurnSpeed, _maxTurnSpeed);

            if (movement == null) {
                return false;
            } else {
                _driveTrain.updateMovement(movement);
            }
        }

        return active;
    }
}
