package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class AutoStateTerminate extends AutoState implements AutoStateIF {
    public AutoStateTerminate(OpMode opMode) {
        super(opMode);
    }

    @Override
    protected boolean doAction() {
        return false;
    }
}
