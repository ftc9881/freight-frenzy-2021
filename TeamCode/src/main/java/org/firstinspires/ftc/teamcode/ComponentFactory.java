package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class ComponentFactory implements ComponentFactoryIF {
    @Override
    public DriveTrainIF driveTrainInstance(DriveTrainType driveTrainType, OpMode opMode) {
        switch(driveTrainType) {
            case MECANUM:
                return new DriveTrainMecanum(opMode);
            case TANK:
                return new DriveTrainTank(opMode);
            default:
                return null;
        }
    }

    @Override
    public DeviceIF deviceInstance(DeviceType deviceType, OpMode opMode) {
        switch(deviceType) {
            case MOTOR:
                return new DeviceMotor(opMode);
            default:
                return null;
        }
    }

    @Override
    public SensorIF sensorInstance(SensorType sensorType, RobotBase opMode) {
        switch(sensorType) {
            case IMU:
                return new SensorIMU(opMode);
            case COLOR:
                return new SensorColor(opMode);
            default:
                return null;
        }
    }

    @Override
    public ControllerIF controllerInstance(ControllerType controllerType, OpMode opMode) {
        switch(controllerType) {
            case TANK:
                return new ControllerTank(opMode);
            case STEER:
                return new ControllerSteer(opMode);
            default:
                return null;
        }
    }


}
