package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.json.JSONObject;

public class DriveTrainTank extends DriveTrainDual {
    public DriveTrainTank(OpMode opMode) {
        super(opMode);
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

        // TODO: Implement this

        double leftPower = 0;
        double rightPower = 0;

        //  Send calculated power to wheels

        setPower(leftPower, rightPower);
    }

}
