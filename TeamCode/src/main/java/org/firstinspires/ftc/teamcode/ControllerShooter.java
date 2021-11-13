package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONObject;

import java.util.Map;

public class ControllerShooter extends Controller implements ControllerIF {
    static final String CLASS_NAME = "ControllerShooter";

    public ControllerShooter(OpMode opMode, String name) {
        super(opMode, name);
    }

    @Override
    public void configure(JSONObject jsonObject) throws ConfigurationException {
        super.configure(jsonObject);
    }

    @Override
    public void updateMovement() {
    }

    @Override
    public Movement getMovement() {
        return null;
    }
}
