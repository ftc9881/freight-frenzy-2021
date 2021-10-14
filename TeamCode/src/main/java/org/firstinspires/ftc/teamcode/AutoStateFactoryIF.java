package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public interface AutoStateFactoryIF {
    enum AutoStateType {
        MOVE,
        TURN,
        MOTOR,
        WAIT,
        TERMINATE
    }

    AutoStateIF autoStateInstance(AutoStateType autoStateType, OpMode opMode);
}
