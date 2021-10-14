package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AutoStateTurn extends AutoState implements AutoStateIF {
    private static final String CLASS_NAME = "AutoStateTurn";

    double _angle = 0;
    double _maxError = 1;
    boolean _global = false;
    double _maxTurnSpeed = .25;

    SensorIMU _sensorIMU;
    StuPID _pid = new StuPID();

    DriveTrainIF _driveTrain;

    public AutoStateTurn(OpMode opMode) {
        super(opMode);
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
            if (jsonObject.has("global")) {
                _global = jsonObject.getBoolean("global");
                RobotLog.dd(CLASS_NAME, "configure()::global %.2f", _global);
            }
            if (jsonObject.has("imuSensor")) {
                _sensorIMU = (SensorIMU)sensors.get(jsonObject.getString("imuSensor"));
            }
            else {
                throw new ConfigurationException("Missing imuSensor");
            }
            if (jsonObject.has("pid")) {
                _pid.configure(jsonObject.getJSONObject("pid"));
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    public void init() {
        super.init();

        RobotLog.dd(CLASS_NAME, "init");

        _sensorIMU.resetCurrentHeading();
    }

    @Override
    public boolean doAction() {
        boolean active = super.doAction();

        if(active) {
            RobotLog.dd(CLASS_NAME, "doAction()");

            _sensorIMU.update();

            double currAngle;

            if (_global) {
                currAngle = _sensorIMU.getGlobalHeading();
            } else {
                currAngle = _sensorIMU.getCurrentHeading();
            }

            double error = currAngle - _angle;

            RobotLog.dd(CLASS_NAME, "curr: %.2f error: %.2f", currAngle, error);

            if (Math.abs(error) > _maxError) {
                double controlVariable = _pid.getControlVariable(currAngle, _angle, 1);

                if (controlVariable > _maxTurnSpeed) {
                    controlVariable = _maxTurnSpeed;
                } else if (controlVariable < -_maxTurnSpeed) {
                    controlVariable = -_maxTurnSpeed;
                }

                RobotLog.dd(CLASS_NAME, "control: %.2f", controlVariable);

                Movement movement = new Movement(0, 0, controlVariable);

                _driveTrain.updateMovement(movement);

                active = true;
            } else {
                active = false;
            }
        }

        return active;
    }

    public void end() {
        super.end();

        _driveTrain.stop();
    }

}
