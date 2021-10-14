package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.json.JSONObject;

public class DriveTrainMecanum extends DriveTrainQuad {
    public DriveTrainMecanum(OpMode opMode) {
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

        double sinAngle = Math.sin(moveAngle + Math.PI / 4);
        double cosAngle = Math.cos(moveAngle + Math.PI / 4);

        double leftFrontPower = moveSpeed * sinAngle + turnSpeed;
        double rightFrontPower = moveSpeed * cosAngle - turnSpeed;
        double leftRearPower = moveSpeed * cosAngle + turnSpeed;
        double rightRearPower = moveSpeed * sinAngle - turnSpeed;

        double maxPower = Math.max(Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower)), Math.max(Math.abs(leftRearPower), Math.abs(rightRearPower)));

        if(maxPower > 1) {
            leftFrontPower /= maxPower;
            rightFrontPower /= maxPower;
            leftRearPower /= maxPower;
            rightRearPower /= maxPower;
        }

        //  Send calculated power to wheels

        setPower(leftFrontPower, rightFrontPower, leftRearPower, rightRearPower);
    }

}
