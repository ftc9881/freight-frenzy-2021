package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class ComponentFactory implements ComponentFactoryIF {
    @Override
    public DriveTrainIF driveTrainInstance(DriveTrainType driveTrainType, OpMode opMode, String name) {
        switch(driveTrainType) {
            case MECANUM:
                return new DriveTrainMecanum(opMode, name);
            case TANK:
                return new DriveTrainTank(opMode, name);
            default:
                return null;
        }
    }

    @Override
    public DeviceIF deviceInstance(DeviceType deviceType, OpMode opMode, String name) {
        switch(deviceType) {
            case MOTOR:
                return new DeviceMotor(opMode, name);
            default:
                return null;
        }
    }

    @Override
    public SensorIF sensorInstance(SensorType sensorType, RobotBase opMode, String name) {
        switch(sensorType) {
            case IMU:
                return new SensorIMU(opMode, name);
            case COLOR:
                return new SensorColor(opMode, name);
            default:
                return null;
        }
    }

    @Override
    public ControllerIF controllerInstance(ControllerType controllerType, OpMode opMode, String name) {
        switch(controllerType) {
            case SHOOTER:
                return new ControllerShooter(opMode, name);
            case TANK_DUAL:
                return new ControllerTankDual(opMode, name);
            case TANK_STEER:
                return new ControllerTankSteer(opMode, name);
            case MECHANUM_STEER:
                return new ControllerMechanumSteer(opMode, name);
            default:
                return null;
        }
    }


}
