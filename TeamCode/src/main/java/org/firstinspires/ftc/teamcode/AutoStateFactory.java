package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class AutoStateFactory implements AutoStateFactoryIF {
    @Override
    public AutoStateIF autoStateInstance(AutoStateType autoStateType, OpMode opMode) {
        switch (autoStateType) {
            case MOVE:
                return new AutoStateMove(opMode);
            case TURN:
                return new AutoStateTurn(opMode);
            case MOTOR:
                return new AutoStateMotor(opMode);
            case WAIT:
                return new AutoStateWait(opMode);
            case TERMINATE:
                return new AutoStateTerminate(opMode);
            default:
                return null;
        }
    }
}
