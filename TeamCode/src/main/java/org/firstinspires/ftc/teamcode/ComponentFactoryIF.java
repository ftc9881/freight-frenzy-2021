package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public interface ComponentFactoryIF {

    enum DriveTrainType {
        MECANUM,
        TANK
    }

    enum DeviceType {
        MOTOR
    }

    enum SensorType {
        IMU,
        COLOR
    }

    enum ControllerType {
        TANK,
        STEER
    }

    DriveTrainIF driveTrainInstance(DriveTrainType driveTrainType, OpMode opMode);

    DeviceIF deviceInstance(DeviceType deviceType, OpMode opMode);

    SensorIF sensorInstance(SensorType sensorType, RobotBase robotBase);

    ControllerIF controllerInstance(ControllerType controllerType, OpMode opMode);
}
