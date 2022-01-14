package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public abstract class DriveTrainDual extends Device implements DriveTrainIF {
    private static final String CLASS_NAME = "DriveTrainDual";

    private DeviceMotor _leftDrive = null;
    private DeviceMotor _rightDrive = null;

    private boolean _telemetryPower = false;
    private boolean _telemetryPosition = true;

    public DriveTrainDual(OpMode opMode, String name) {
        super(opMode, name);

        _leftDrive = new DeviceMotor(opMode, "left");
        _rightDrive = new DeviceMotor(opMode, "right");
    }

    @Override
    public void configure(JSONObject jsonObject) throws ConfigurationException {
        super.configure(jsonObject);

        try {
            JSONObject motorsConfig = jsonObject.getJSONObject("motors");

            if (motorsConfig.has("left")) {
                _leftDrive.configure(motorsConfig.getJSONObject("left"));
            } else {
                throw new ConfigurationException("Missing frontLeft configuration", null);
            }

            if (motorsConfig.has("right")) {
                _rightDrive.configure(motorsConfig.getJSONObject("right"));
            } else {
                throw new ConfigurationException("Missing frontRight configuration", null);
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

    void setPower(double leftPower, double rightPower) {
        _leftDrive.setSpeed(leftPower);
        _rightDrive.setSpeed(rightPower);
    }

    public void stop() {
        _leftDrive.stop();
        _rightDrive.stop();
    }

    @Override
    public abstract void updateMovement(Movement movementBoth);

    public void resetPositions() {
        _leftDrive.resetPosition();
        _rightDrive.resetPosition();
    }

    double [] getDistances() {
        logDistances();

        return new double [] {
                _leftDrive.getDistance(),
                _rightDrive.getDistance()
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
                _leftDrive.getCurrentPosition(),
                _rightDrive.getCurrentPosition()
        };
    }

    public int getPosition(boolean abs) {
        int[] positions = getPositions();

        if(abs) {
            RobotUtil.arrayAbs(positions);
        }

        return RobotUtil.arrayMean(positions);
    }

    @Override
    public void behave(ActionIF action, String behavior, Map<String, Object> properties) {
        _leftDrive.behave(action, behavior, properties);
        _rightDrive.behave(action, behavior, properties);
    }

    @Override
    public boolean isValidParameter(String parameter) {
        try {
            DeviceMotor.Parameter.valueOf(parameter);
            return true;
        } catch (IllegalArgumentException ex) {
            return super.isValidParameter(parameter);
        }
    }

    @Override
    public void setParameter(String parameterName, String value) {
        RobotLog.dd(this.getClass().getSimpleName(), "Set parameter: %s %s %s", _name, parameterName, value);

        _leftDrive.setParameter(parameterName, value);
        _rightDrive.setParameter(parameterName, value);
    }


    public void logDistances() {
        RobotLog.dd(CLASS_NAME, "logDistances()::l: %.2f r: %.2f",
                _leftDrive.getDistance(),
                _rightDrive.getDistance()
        );
    }

    public void logPositions() {
        RobotLog.dd(CLASS_NAME, "logPositions()::l: %.2f r: %.2f",
                _leftDrive.getCurrentPosition(),
                _rightDrive.getCurrentPosition()
        );
    }

    @Override
    public void addTelemetryData(Telemetry telemetry) {
        super.addTelemetryData(telemetry);

        if(_telemetry) {
            _leftDrive.addTelemetryData(telemetry);
            _rightDrive.addTelemetryData(telemetry);
        }
    }

    public void getPropertyValues(Map<String, Object> values) {
        super.getPropertyValues(values);

        _leftDrive.getPropertyValues(values);
        _rightDrive.getPropertyValues(values);

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

    public void init() {
        resetPositions();
    }

    public void update() {
    }

    public void terminate() {
    }
}
