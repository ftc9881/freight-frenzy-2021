package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AutoStateMotor extends AutoState implements AutoStateIF {
    private static final String CLASS_NAME = "AutoStateMotor";

    DeviceMotor _deviceMotor;

    double _speed = 1;
    int _targetPosition = Integer.MIN_VALUE;  // Treat MIN_VALUE as missing
    int _maxError = 100;

    StuPID _pid = new StuPID(.001, 0, 0);

    public AutoStateMotor(OpMode opMode) {
        super(opMode);
    }

    public void configure(JSONObject jsonObject,
                          DriveTrainIF driveTrain,
                          Map<String, DeviceIF> devices,
                          Map<String, SensorIF> sensors) throws ConfigurationException {
        super.configure(jsonObject, driveTrain, devices, sensors);

        try {
            if (jsonObject.has("motor")) {
                String motorName = jsonObject.getString("motor");

                _deviceMotor =  (DeviceMotor)devices.get(motorName);

                if(_deviceMotor == null) {
                    throw new ConfigurationException("Motor not defined: " + motorName);
                }
            }
            else {
                throw new ConfigurationException("Motor name not specified");
            }
            if (jsonObject.has("speed")) {
                _speed = jsonObject.getDouble("speed");
            }
            if (jsonObject.has("targetPosition")) {
                _targetPosition = jsonObject.getInt("targetPosition");
            }
            if (jsonObject.has("maxError")) {
                _maxError = jsonObject.getInt("maxError");
            }
            if (jsonObject.has("pid")) {
                _pid.configure(jsonObject.getJSONObject("pid"));
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    public void begin(RobotBase robotBase) {
        super.begin(robotBase);

        RobotLog.dd(CLASS_NAME, "init()");

        _deviceMotor.init();
    }

    public boolean doAction(RobotBase robotBase) throws InterruptedException {
        boolean active = super.doAction(robotBase);

        if(active) {
            RobotLog.dd(CLASS_NAME, "doAction()");

            if(_targetPosition != Integer.MIN_VALUE) {
                int position = _deviceMotor.getCurrentPosition();

                double controlVariable = _pid.getControlVariable(position, _targetPosition, 1);

                controlVariable = Math.max(-1, Math.min(1, controlVariable));

                double effSpeed = _speed * controlVariable;

                _deviceMotor.setSpeed(effSpeed);

                active = Math.abs(position - _targetPosition) > _maxError;
            } else {
                _deviceMotor.setSpeed(_speed);
            }
        }

        return active;
    }

    public void end(RobotBase robotBase) {
        super.end(robotBase);

        _deviceMotor.stop();
    }

}
