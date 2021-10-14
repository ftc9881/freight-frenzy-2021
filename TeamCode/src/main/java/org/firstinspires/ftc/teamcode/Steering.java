package org.firstinspires.ftc.teamcode;

import org.json.JSONObject;

import java.util.Map;

public abstract class Steering implements SteeringIF {
    public enum SteeringType {
        IMU
    }

    public static SteeringIF constructSteering(SteeringType steeringType) {
        switch(steeringType) {
            case IMU:
                return new SteeringIMU();
        }
        return null;
    }

    public void configure(JSONObject jsonObject, Map<String, SensorIF> sensors) throws ConfigurationException {

    }
}
