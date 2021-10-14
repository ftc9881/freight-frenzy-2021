package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class AutoStateWait extends AutoState implements AutoStateIF {
    public AutoStateWait(OpMode opMode) {
        super(opMode);
    }

    @Override
    protected boolean doAction() {
        return false;
    }
}
