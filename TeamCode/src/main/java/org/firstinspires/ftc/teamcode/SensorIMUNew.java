package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SensorIMUNew extends Sensor implements SensorIMU_IF {
    private static final String CLASS_NAME = "SensorIMU";

    BNO055IMU _imu;

    private Orientation _angles;
    private Orientation _lastAngles;
    private double _globalHeading;
    private double _currentHeading;

    enum Axis {
        X,
        Y,
        Z
    }

    private Axis _headingAxis = Axis.X;
    private Axis _rightAxis = Axis.Y;

    private boolean _reverse = false;

    public SensorIMUNew(OpMode opMode, String name) {
        super(opMode, name);
    }

    @Override
    public void configure(JSONObject jsonObject, Map<String, DeviceIF> devices) throws JSONException, ConfigurationException {
        super.configure(jsonObject, devices);

        String imuName;

        try {
            imuName = jsonObject.getString("name");

            RobotLog.dd(CLASS_NAME, "imuName: %s", imuName);

            try {
                _imu = _opMode.hardwareMap.get(BNO055IMU.class, imuName);

                BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

                parameters.mode                = BNO055IMU.SensorMode.IMU;
                parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
                parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
                parameters.loggingEnabled      = false;

                _imu.initialize(parameters);
            }
            catch(IllegalArgumentException e) {
                throw new ConfigurationException("No IMU with the name: " + imuName, e);
            }

            if (jsonObject.has("headingAxis")) {
                _headingAxis = Axis.valueOf(jsonObject.getString("headingAxis"));
            }
            if (jsonObject.has("rightAxis")) {
                _rightAxis = Axis.valueOf(jsonObject.getString("rightAxis"));
            }
            if (jsonObject.has("reverse")) {
                _reverse = jsonObject.getBoolean("reverse");
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    public void update() {
        super.update();

        _angles = _imu.getAngularOrientation();

        if(_lastAngles == null) {
            _globalHeading = 0;
            _currentHeading = 0;
        }
        else {
            // Update global yaw

            double angle = 0;
            double lastAngle = 0;

            /*
                I do this because of gymbal-lock related problems when interpreting the heading
                axis of the robot when the IMU isn't flat, with Z pointing up.   This code
                constructs a heading vector, transforms it, and then gets the angle between them
                on a plane parallel the plane of the robot's rotation.

                This may not work for the non-X case since that is the only one tested.
             */

            VectorF normal = null;
            int forwardDim = 0;
            int rightDim = 0;

            switch (_headingAxis) {
                case X:
                    normal = new VectorF(1, 0, 0, 1);
                    forwardDim = 0;
                    break;
                case Y:
                    normal = new VectorF(0, 1, 0, 1);
                    forwardDim = 1;
                    break;
                case Z:
                    normal = new VectorF(0, 0, 1, 1);
                    forwardDim = 2;
                    break;
            }

            switch (_rightAxis) {
                case X:
                    rightDim = 0;
                    break;
                case Y:
                    rightDim = 1;
                    break;
                case Z:
                    rightDim = 2;
                    break;
            }

            VectorF xformed = _angles.getRotationMatrix().transform(normal);
            VectorF lastXformed = _lastAngles.getRotationMatrix().transform(normal);

            angle = 180 * Math.atan2(xformed.get(forwardDim), xformed.get(rightDim)) / Math.PI;
            lastAngle = 180 * Math.atan2(lastXformed.get(forwardDim), lastXformed.get(rightDim)) / Math.PI;

            double deltaAngle = angle - lastAngle;

            if(_reverse) {
                deltaAngle *= -1;
            }

            if (deltaAngle < -180) {
                deltaAngle += 360;
            } else if (deltaAngle > 180) {
                deltaAngle -= 360;
            }

            RobotLog.dd(CLASS_NAME, "update()::angle: %.2f lastAngle: %.2f deltaAngle: %.2f", angle, lastAngle, deltaAngle);

            _globalHeading += deltaAngle;
            _currentHeading += deltaAngle;

            RobotLog.dd(CLASS_NAME, "update()::globalHeading: %.2f currentHeading: %.2f", _globalHeading, _currentHeading);
        }

        _lastAngles = _angles;
    }

    double getRoll() {
        if(_angles != null) {
            return _angles.secondAngle;
        }

        return Double.NaN;
    }

    double getPitch() {
        if(_angles != null) {
           return _angles.firstAngle;
        }

        return Double.NaN;
    }

    double getYaw() {
        if(_angles != null) {
            return _angles.thirdAngle;
        }

        return Double.NaN;
    }

    public double getGlobalHeading() {
        return _globalHeading;
    }

    public double getCurrentHeading() {
        return _currentHeading;
    }

    public void resetCurrentHeading() {
        _currentHeading = 0;
    }

    @Override
    public void addTelemetryData(Telemetry telemetry) {
        super.addTelemetryData(telemetry);

        if(_telemetry) {
            telemetry.addData("IMU",
                    "R: %.2f P: %.2f Y: %.2f G Hdhg: %.2f C Hdng: %.2f",
                    getRoll(), getPitch(), getYaw(), getGlobalHeading(), getCurrentHeading()
            );
        }
    }

    @Override
    public void getPropertyValues(Map<String, Object> values) {
        super.getPropertyValues(values);

        values.put("roll", getRoll());
        values.put("pitch", getPitch());
        values.put("yaw", getYaw());
        values.put("globalHeading", getGlobalHeading());
        values.put("currentHeading", getCurrentHeading());
    }
}
