package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public interface AutoStateFactoryIF {
    enum AutoStateType {
        MOVE,
        TURN,
        MOTOR,
        VUFORIA_OBJECT,
        PAUSE,
        TERMINATE,
        CUSTOM;
    }

    AutoStateIF autoStateInstance(AutoStateType autoStateType,
                                  OpMode opMode,
                                  SteeringIF moveSteering,
                                  SteeringIF turnSteering
    );
}