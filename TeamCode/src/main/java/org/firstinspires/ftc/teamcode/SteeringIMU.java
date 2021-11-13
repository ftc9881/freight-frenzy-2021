package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SteeringIMU extends Steering implements SteeringIF {
    static final String CLASS_NAME = "SteeringIMU";

    SensorIMU_IF _sensorIMU;
    StuPID _pid = new StuPID();

    @Override
    public void configure(JSONObject jsonObject, Map<String, SensorIF> sensors) throws ConfigurationException {
        super.configure(jsonObject, sensors);

        try {
            if (jsonObject.has("imuSensor")) {
                _sensorIMU = (SensorIMU_IF)sensors.get(jsonObject.getString("imuSensor"));
            }
            if (jsonObject.has("pid")) {
                _pid.configure(jsonObject.getJSONObject("pid"));
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    public void init() {
        RobotLog.dd(CLASS_NAME, "init()");

        _sensorIMU.resetCurrentHeading();
        _pid.reset();
    }

    public void update() {
        RobotLog.dd(CLASS_NAME, "update()");

        _sensorIMU.update();
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
    public Movement turn(double targetAngle, boolean global, double maxError, double minSpeed, double maxSpeed) {

        double currAngle;

        if (global) {
            currAngle = _sensorIMU.getGlobalHeading();
        } else {
            currAngle = _sensorIMU.getCurrentHeading();
        }

        double error = currAngle - targetAngle;

        RobotLog.dd(CLASS_NAME, "curr: %.2f target: %.2f error: %.2f", currAngle, targetAngle, error);

        if (Math.abs(error) > maxError) {
            double controlVariable = _pid.getControlVariable(currAngle, targetAngle, 1);

            if(Math.abs(controlVariable) > maxSpeed) {
                controlVariable = maxSpeed * Math.signum(controlVariable);
            }

            if(Math.abs(controlVariable) < minSpeed) {
                controlVariable = minSpeed * Math.signum(controlVariable);
            }

            RobotLog.dd(CLASS_NAME, "control: %.2f", controlVariable);

            Movement movement = new Movement(0, 0, controlVariable);

            return movement;
        } else {
            return null;
        }
    }

}
