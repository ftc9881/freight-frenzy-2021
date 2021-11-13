/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="SimpleMechanum", group="Linear Opmode")
@Disabled
public class SimpleMechanum extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightBackDrive = null;

    private DcMotor intakeMotor = null;
    private DcMotor outtakeMotor = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "left_front_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        leftBackDrive  = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");

        intakeMotor = hardwareMap.get(DcMotor.class, "intake_motor");
        outtakeMotor = hardwareMap.get(DcMotor.class, "outtake_motor");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.REVERSE);

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intakeMotor.setDirection(DcMotor.Direction.REVERSE);
        outtakeMotor.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        boolean tankDrive = false;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double leftX = gamepad1.left_stick_x;
            double leftY = gamepad1.left_stick_y;
            double rightX = -gamepad1.right_stick_x;
            double rightY = gamepad1.right_stick_y;

            double rightTrigger = gamepad1.right_trigger;
            double leftTrigger = gamepad1.left_trigger;

            boolean rightButton = gamepad1.right_bumper;
            boolean leftButton = gamepad1.left_bumper;

            if(rightButton) {
                intakeMotor.setPower(1);
            }
            else if(leftButton) {
                intakeMotor.setPower(-1);
            } else {
                intakeMotor.setPower(0);
            }

            if(rightTrigger > 0) {
                outtakeMotor.setPower(-rightTrigger);
            } else if(leftTrigger > 0) {
                outtakeMotor.setPower(leftTrigger);
            } else {
                outtakeMotor.setPower(0);
            }

            double leftFrontPower;
            double rightFrontPower;
            double leftRearPower;
            double rightRearPower;

            if(tankDrive) {
                double Yf = (leftY + rightY) / 2;
                double Yt = (leftY - rightY) / 2;
                double strafeX = -(leftX + rightX) / 2;

                // Constants to adjust relative power contributions of each component

                double Kf = 1;
                double Kt = 1;
                double Ks = 1;

                leftFrontPower = Kf * Yf + Kt * Yt + Ks * strafeX;
                rightFrontPower = Kf * Yf - Kt * Yt - Ks * strafeX;
                leftRearPower = Kf * Yf + Kt * Yt - Ks * strafeX;
                rightRearPower = Kf * Yf - Kt * Yt + Ks * strafeX;
            } else {
                double speed;
                speed = Math.sqrt(Math.pow(leftX, 2) + Math.pow(leftY, 2));

                double angle;
                angle = Math.atan2(-leftY, -leftX);

                double changingSpeed;
                changingSpeed = -rightX;

                leftFrontPower = speed * (Math.sin(angle - (Math.PI / 4))) + changingSpeed;
                rightFrontPower = speed * (Math.cos(angle - (Math.PI / 4))) - changingSpeed;
                leftRearPower = speed * (Math.cos(angle - (Math.PI / 4))) + changingSpeed;
                rightRearPower = speed * (Math.sin(angle - (Math.PI / 4))) - changingSpeed;
            }

            double maxPower = Math.max(Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower)), Math.max(Math.abs(leftRearPower), Math.abs(rightRearPower)));

            if(maxPower > 1) {
                leftFrontPower /= maxPower;
                rightFrontPower /= maxPower;
                leftRearPower /= maxPower;
                rightRearPower /= maxPower;
            }

            //  Send calculated power to wheels

            leftFrontDrive.setPower(leftFrontPower);
            rightFrontDrive.setPower(rightFrontPower);
            leftBackDrive.setPower(leftRearPower);
            rightBackDrive.setPower(rightRearPower);

            int leftFrontDriveEncoder = leftFrontDrive.getCurrentPosition();
            int rightFrontDriveEncoder = rightFrontDrive.getCurrentPosition();
            int leftBackDriveEncoder = leftBackDrive.getCurrentPosition();
            int rightBackDriveEncoder = rightBackDrive.getCurrentPosition();

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
//            telemetry.addData("Front Motors", "left (%.2f), right (%.2f)", leftFrontPower, rightFrontPower);
//            telemetry.addData("Back Motors", "left (%.2f), right (%.2f)", leftRearPower, rightRearPower);
            telemetry.addData("Front Positions", "left (%d), right (%d)", leftFrontDriveEncoder, rightFrontDriveEncoder);
            telemetry.addData("Back Positions", "left (%d), right (%d)", leftBackDriveEncoder, rightBackDriveEncoder);
            telemetry.update();
        }
    }
}
