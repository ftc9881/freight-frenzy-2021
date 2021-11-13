package org.firstinspires.ftc.teamcode;

public interface SensorIMU_IF extends SensorIF {
    double getGlobalHeading();

    double getCurrentHeading();

    void resetCurrentHeading();
}
