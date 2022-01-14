package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SensorIMU extends Sensor implements SensorIMU_IF {
    private static final String CLASS_NAME = "SensorIMU";

    BNO055IMU _imu;

    private Orientation _angles;
    private Orientation _lastAngles;
    private double _globalHeading;
    private double _currentHeading;

    AxesOrder _axesOrder = AxesOrder.XYZ;

    enum Axis {
        X,
        Y,
        Z
    }

    private Axis _xAxis = Axis.X;
    private Axis _yAxis = Axis.Y;
    private Axis _zAxis = Axis.Z;

    private boolean _reverse = false;

    public SensorIMU(OpMode opMode, String name) {
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

            if (jsonObject.has("axesOrder")) {
                _axesOrder = AxesOrder.valueOf(jsonObject.getString("axesOrder"));
            }

            if (jsonObject.has("xAxis")) {
                _xAxis = Axis.valueOf(jsonObject.getString("xAxis"));
            }
            if (jsonObject.has("yAxis")) {
                _yAxis = Axis.valueOf(jsonObject.getString("yAxis"));
            }
            if (jsonObject.has("zAxis")) {
                _zAxis = Axis.valueOf(jsonObject.getString("zAxis"));
            }
            if (jsonObject.has("reverse")) {
                _reverse = jsonObject.getBoolean("reverse");
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    public void init() {
    }

    public void update() {
        super.update();

        _angles = _imu.getAngularOrientation(AxesReference.INTRINSIC, _axesOrder, AngleUnit.DEGREES);

        RobotLog.dd(CLASS_NAME,
                "update()::angles: first: %.2f second: %.2f third: %.2f",
                _angles.firstAngle,
                _angles.secondAngle,
                _angles.thirdAngle
        );

        if(_lastAngles == null) {
            _globalHeading = 0;
            _currentHeading = 0;
        }
        else {
            // Update global yaw

            double angle = 0;
            double lastAngle = 0;

            switch (_zAxis) {
                case X:
                    angle = _angles.firstAngle;
                    lastAngle = _lastAngles.firstAngle;
                    break;
                case Y:
                    angle = _angles.secondAngle;
                    lastAngle = _lastAngles.secondAngle;
                    break;
                case Z:
                    angle = _angles.thirdAngle;
                    lastAngle = _lastAngles.thirdAngle;
                    break;
            }

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

    public void terminate() {
    }

    double getRoll() {
        if(_angles != null) {
            switch (_yAxis) {
                case X:
                    return _angles.firstAngle;
                case Y:
                    return _angles.secondAngle;
                case Z:
                    return _angles.thirdAngle;
            }
        }

        return Double.NaN;
    }

    double getPitch() {
        if(_angles != null) {
            switch (_xAxis) {
                case X:
                    return _angles.firstAngle;
                case Y:
                    return _angles.secondAngle;
                case Z:
                    return _angles.thirdAngle;
            }
        }

        return Double.NaN;
    }

    double getYaw() {
        if(_angles != null) {
            switch (_zAxis) {
                case X:
                    return _angles.firstAngle;
                case Y:
                    return _angles.secondAngle;
                case Z:
                    return _angles.thirdAngle;
            }
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
