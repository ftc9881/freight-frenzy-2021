package org.firstinspires.ftc.teamcode;

public interface DriveTrainIF extends ComponentIF {
    void updateMovement(Movement movement);

    double getDistance(boolean abs);

    int getPosition(boolean abs);

    void resetPositions();

    void stop();
}
