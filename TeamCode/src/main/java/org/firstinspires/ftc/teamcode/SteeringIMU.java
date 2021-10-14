package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SteeringIMU extends Steering implements SteeringIF {
    static final String CLASS_NAME = "SteeringIMU";

    SensorIMU _sensorIMU;
    StuPID _pid = new StuPID();

    @Override
    public void configure(JSONObject jsonObject, Map<String, SensorIF> sensors) throws ConfigurationException {
        super.configure(jsonObject, sensors);

        try {
            if (jsonObject.has("imuSensor")) {
                _sensorIMU = (SensorIMU)sensors.get(jsonObject.getString("imuSensor"));
            }
            if (jsonObject.has("pid")) {
                _pid.configure(jsonObject.getJSONObject("pid"));
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    public void init() {
        _sensorIMU.resetCurrentHeading();
        _pid.reset();
    }

    public Movement steer(Movement movement) {
        _sensorIMU.update();
        double heading = _sensorIMU.getCurrentHeading();

        double adjust = _pid.getControlVariable(heading, 0, 1);

        RobotLog.dd(CLASS_NAME, "heading: %.2f adjust: %.2f", heading, adjust);

        Movement effMovement = new Movement(movement);
        effMovement._turnSpeed += adjust;

        return effMovement;
    }

    @Override
    public Movement turn(double speed, double target, boolean global) {
        _sensorIMU.update();
        double heading = _sensorIMU.getCurrentHeading();


        return null;
    }

}
