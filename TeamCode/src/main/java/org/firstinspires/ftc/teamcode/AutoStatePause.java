package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AutoStatePause extends AutoState implements AutoStateIF {
    private static final String CLASS_NAME = "AutoStatePause";

    public AutoStatePause(OpMode opMode) {
        super(opMode);
    }

    public boolean doAction() throws InterruptedException {
        boolean active = super.doAction();

        return active;
    }
}