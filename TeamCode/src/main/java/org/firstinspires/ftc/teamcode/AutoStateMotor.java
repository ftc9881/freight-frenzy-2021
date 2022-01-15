package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AutoStateMotor extends AutoState implements AutoStateIF {
    private static final String CLASS_NAME = "AutoStateMotor";

    double _speed = 1;
    int _maxPosition = Integer.MAX_VALUE;
    DeviceMotor _deviceMotor;

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
            if (jsonObject.has("maxPosition")) {
                _maxPosition = jsonObject.getInt("maxPosition");
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    public void begin() {
        super.begin();

        RobotLog.dd(CLASS_NAME, "init()");

        _deviceMotor.init();
    }

    public boolean doAction() throws InterruptedException {
        boolean active = super.doAction();

        if(active) {
            RobotLog.dd(CLASS_NAME, "doAction()");

            _deviceMotor.setSpeed(_speed);

            int position = _deviceMotor.getCurrentPosition();

            active = Math.abs(position) <= _maxPosition;
        }

        return active;
    }

    public void end() {
        super.end();

        _deviceMotor.stop();
    }

}
