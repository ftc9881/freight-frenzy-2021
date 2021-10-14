package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public abstract class DriveTrainQuad extends Component implements DriveTrainIF {
    private static final String CLASS_NAME = "DriveTrainQuad";

    private BotMotor _leftFrontDrive = null;
    private BotMotor _rightFrontDrive = null;
    private BotMotor _leftBackDrive = null;
    private BotMotor _rightBackDrive = null;

    private boolean _telemetryPower = false;
    private boolean _telemetryPosition = true;

    public DriveTrainQuad(OpMode opMode) {
        super(opMode);

        _leftFrontDrive = new BotMotor("leftFront", opMode);
        _rightFrontDrive = new BotMotor("rightFront", opMode);
        _leftBackDrive = new BotMotor("leftBack", opMode);
        _rightBackDrive = new BotMotor("rightBack", opMode);
    }

    @Override
    public void configure(JSONObject jsonObject) throws ConfigurationException {
        super.configure(jsonObject);

        try {
            JSONObject motorsConfig = jsonObject.getJSONObject("motors");

            if (motorsConfig.has("frontLeft")) {
                _leftFrontDrive.configure(motorsConfig.getJSONObject("frontLeft"));
            } else {
                throw new ConfigurationException("Missing frontLeft configuration", null);
            }

            if (motorsConfig.has("frontRight")) {
                _rightFrontDrive.configure(motorsConfig.getJSONObject("frontRight"));
            } else {
                throw new ConfigurationException("Missing frontRight configuration", null);
            }

            if (motorsConfig.has("backLeft")) {
                _leftBackDrive.configure(motorsConfig.getJSONObject("backLeft"));
            } else {
                throw new ConfigurationException("Missing backLeft configuration", null);
            }

            if (motorsConfig.has("backRight")) {
                _rightBackDrive.configure(motorsConfig.getJSONObject("backRight"));
            } else {
                throw new ConfigurationException("Missing backRight configuration", null);
            }

            if (motorsConfig.has("telemetryPower")) {
                _telemetryPower = motorsConfig.getBoolean("telemetryPower");
            }

            if (motorsConfig.has("telemetryPosition")) {
                _telemetryPosition = motorsConfig.getBoolean("telemetryPosition");
            }

        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    void setPower(double leftFrontPower, double rightFrontPower, double leftRearPower, double rightRearPower) {
        _leftFrontDrive.setSpeed(leftFrontPower);
        _rightFrontDrive.setSpeed(rightFrontPower);
        _leftBackDrive.setSpeed(leftRearPower);
        _rightBackDrive.setSpeed(rightRearPower);
    }

    public void stop() {
        _leftFrontDrive.stop();
        _rightFrontDrive.stop();
        _leftBackDrive.stop();
        _rightBackDrive.stop();
    }

    @Override
    public abstract void updateMovement(Movement movementBoth);

    public void resetPositions() {
        _leftFrontDrive.resetPosition();
        _rightFrontDrive.resetPosition();
        _leftBackDrive.resetPosition();
        _rightBackDrive.resetPosition();
    }

    double [] getDistances() {
        logDistances();

        return new double [] {
                _leftFrontDrive.getDistance(),
                _rightFrontDrive.getDistance(),
                _leftBackDrive.getDistance(),
                _rightBackDrive.getDistance()
        };
    }

    public double getDistance(boolean abs) {
        double[] distances = getDistances();

        if(abs) {
            RobotUtil.arrayAbs(distances);
        }

        return RobotUtil.arrayMean(distances);
    }

    int [] getPositions() {
        return new  int [] {
                _leftFrontDrive.getCurrentPosition(),
                _rightFrontDrive.getCurrentPosition(),
                _leftBackDrive.getCurrentPosition(),
                _rightBackDrive.getCurrentPosition()
        };
    }

    public int getPosition(boolean abs) {
        int[] positions = getPositions();

        if(abs) {
            RobotUtil.arrayAbs(positions);
        }

        return RobotUtil.arrayMean(positions);
    }

    public void logDistances() {
        RobotLog.dd(CLASS_NAME, "logDistances()::lf: %.2f rf: %.2f lb: %.2f rb: %.2f ",
            _leftFrontDrive.getDistance(),
                    _rightFrontDrive.getDistance(),
                    _leftBackDrive.getDistance(),
                    _rightBackDrive.getDistance()
        );
    }

    public void logPositions() {
        RobotLog.dd(CLASS_NAME, "logDistances()::lf: %.2f rf: %.2f lb: %.2f rb: %.2f ",
                _leftFrontDrive.getCurrentPosition(),
                _rightFrontDrive.getCurrentPosition(),
                _leftBackDrive.getCurrentPosition(),
                _rightBackDrive.getCurrentPosition()
        );
    }

    @Override
    public void addTelemetryData(Telemetry telemetry) {
        super.addTelemetryData(telemetry);

        if(_telemetry) {
            _leftFrontDrive.addTelemetryData(telemetry);
            _rightFrontDrive.addTelemetryData(telemetry);
            _leftBackDrive.addTelemetryData(telemetry);
            _rightBackDrive.addTelemetryData(telemetry);
        }
    }

    public void getPropertyValues(Map<String, Object> values) {
        super.getPropertyValues(values);

        _leftFrontDrive.getPropertyValues(values);
        _rightFrontDrive.getPropertyValues(values);
        _leftBackDrive.getPropertyValues(values);
        _rightBackDrive.getPropertyValues(values);

        int [] positions = getPositions();

        int maxPosition = RobotUtil.arrayMax(positions);
        int minPosition = RobotUtil.arrayMin(positions);
        int meanPosition = RobotUtil.arrayMean(positions);

        values.put("maxPosition", maxPosition);
        values.put("minPosition", minPosition);
        values.put("meanPosition", meanPosition);

        double [] distances = getDistances();

        double maxDistance = RobotUtil.arrayMax(distances);
        double minDistance = RobotUtil.arrayMin(distances);
        double meanDistance = RobotUtil.arrayMean(distances);

        values.put("maxDistance", maxDistance);
        values.put("minDistance", minDistance);
        values.put("meanDistance", meanDistance);
    }
}
