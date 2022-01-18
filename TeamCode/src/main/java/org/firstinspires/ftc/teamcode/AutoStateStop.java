package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AutoStateStop extends AutoStateMotion implements AutoStateIF {
    private static final String CLASS_NAME = "AutoStateStop";

    public AutoStateStop(OpMode opMode, SteeringIF steering) {
        super(opMode, steering);
    }

    public boolean doAction(RobotBase robotBase) throws InterruptedException  {
        boolean active = super.doAction(robotBase);

        if(active) {
            RobotLog.dd(CLASS_NAME, "doAction()");

            _driveTrain.stop();

            active = false;
        }

        return active;
    }
}