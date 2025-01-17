package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class AutoStateFactory implements AutoStateFactoryIF {
    @Override
    public AutoStateIF autoStateInstance(AutoStateType autoStateType, OpMode opMode,
                                         SteeringIF moveSteering,
                                         SteeringIF turnSteering) {
        switch (autoStateType) {
            case MOVE:
                return new AutoStateMove(opMode, moveSteering);
            case TURN:
                return new AutoStateTurn(opMode, turnSteering);
            case MOTOR:
                return new AutoStateMotor(opMode);
            case VUFORIA_OBJECT:
                return new AutoStateVuforiaObject(opMode);
            case PAUSE:
                return new AutoStatePause(opMode);
            case TERMINATE:
                return new AutoStateTerminate(opMode);
            case CUSTOM:
                return new AutoStateCustom(opMode);
            default:
                return null;
        }
    }
}
