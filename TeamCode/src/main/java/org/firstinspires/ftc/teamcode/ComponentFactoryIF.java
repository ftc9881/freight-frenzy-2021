package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public interface ComponentFactoryIF {

    enum DriveTrainType {
        MECANUM,
        TANK
    }

    enum DeviceType {
        MOTOR,
        SERVO
    }

    enum SensorType {
        IMU,
        COLOR
    }

    enum ControllerType {
        SHOOTER,
        TANK_DUAL,
        TANK_STEER,
        MECHANUM_STEER
    }

    DriveTrainIF driveTrainInstance(DriveTrainType driveTrainType, OpMode opMode, String name);

    DeviceIF deviceInstance(DeviceType deviceType, OpMode opMode, String name);

    SensorIF sensorInstance(SensorType sensorType, RobotBase robotBase, String name);

    ControllerIF controllerInstance(ControllerType controllerType, OpMode opMode, String name);
}
