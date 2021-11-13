package org.firstinspires.ftc.teamcode;

import org.json.JSONObject;

import java.util.Map;

public interface SteeringIF {
    void configure(JSONObject jsonObject, Map<String, SensorIF> sensors) throws ConfigurationException;

    Movement steer(Movement movement);

    Movement turn(double targetAngle, boolean global, double maxError, double minSpeed, double maxSpeed);

    void init();

    void update();
}
