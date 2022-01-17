package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AutoStateMotion extends AutoState {
    private static final String CLASS_NAME = "AutoStateMotion";

    SteeringIF _steering;
    DriveTrainIF _driveTrain;

    public AutoStateMotion(OpMode opMode, SteeringIF steering) {
        super(opMode);

        _steering = steering;
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
            if (jsonObject.has("steering")) {
                configureSteering(jsonObject.getJSONObject("steering"), sensors);
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }


    public void begin(RobotBase robotBase) {
        super.begin(robotBase);

        RobotLog.dd(CLASS_NAME, "init()");

        if (_steering != null) {
            _steering.init();
        }

        _driveTrain.resetPositions();
    }


    public void end(RobotBase robotBase) {
        super.end(robotBase);

        _driveTrain.stop();
    }

}
