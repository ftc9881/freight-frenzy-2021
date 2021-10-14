package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONObject;

import java.util.Map;

public interface ComponentIF {
    void configure(JSONObject jsonObject) throws ConfigurationException;

    void addTelemetryData(Telemetry telemetry);

    void getPropertyValues(Map<String, Object> values);
}
