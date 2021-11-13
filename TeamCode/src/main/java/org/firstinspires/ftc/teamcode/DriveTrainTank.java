package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONObject;

public class DriveTrainTank extends DriveTrainDual {
    private static final String CLASS_NAME = "DriveTrainTank";

    public DriveTrainTank(OpMode opMode, String name) {
        super(opMode, name);
    }

    @Override
    public void configure(JSONObject jsonObject) throws ConfigurationException {
        super.configure(jsonObject);
    }

    @Override
    public void updateMovement(Movement movement) {
        double moveSpeed = movement._moveSpeed;
        double moveAngle = movement._moveAngle;
        double turnSpeed = movement._turnSpeed;

        double leftPower = moveSpeed + turnSpeed;
        double rightPower = moveSpeed - turnSpeed;

        if(leftPower > 1) leftPower = 1;
        else if(leftPower <-1) leftPower = -1;

        if(rightPower > 1) rightPower = 1;
        else if(rightPower <-1) rightPower = -1;

        RobotLog.dd(CLASS_NAME, "updateMovement()::leftPower: %.2f rightPower: %.2f", leftPower, rightPower);

        //  Send calculated power to wheels

        setPower(leftPower, rightPower);
    }

}
