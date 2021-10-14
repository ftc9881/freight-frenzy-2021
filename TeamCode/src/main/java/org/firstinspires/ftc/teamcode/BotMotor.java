package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class BotMotor extends Component {
    private static final String CLASS_NAME = "BotMotor";

    DcMotor _dcMotor = null;

    private String _name;
    private boolean _velocity = false;
    private double _maxVelocity = 36000;  // 360 degrees * 6000 RPM / (60 S/M)
    private int _clicksPerMeter = 1500;

    private DcMotor.RunMode _mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;

    private int _basePosition = 0;

    // TODO: Support configuring PID
    // TODO: Add lookup table to translate input power to motor power
    // TODO: Add parametric power conversion (i.e. exponent)

    BotMotor(String name, OpMode opMode) {
        super(opMode);

        _name = name;
    }

    @Override
    public void configure(JSONObject jsonObject) throws ConfigurationException {
        String motorName = null;

        try {
            motorName = jsonObject.getString("name");

            try {
                _dcMotor = _opMode.hardwareMap.get(DcMotor.class, motorName);
                _dcMotor.setDirection(DcMotorSimple.Direction.FORWARD);
                _dcMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                _dcMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                _dcMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }
            catch(IllegalArgumentException e) {
                throw new ConfigurationException("No motor with the name: " + motorName, e);
            }

            // Direction

            if( jsonObject.has("direction")) {
                String directionName = jsonObject.getString("direction");

                DcMotorSimple.Direction direction = DcMotorSimple.Direction.valueOf(directionName);

                _dcMotor.setDirection(direction);

                RobotLog.dd(CLASS_NAME, "configure()::direction: %s", direction);
            }

            // Mode

            if( jsonObject.has("mode")) {
                String modeName = jsonObject.getString("mode");

                _mode = DcMotor.RunMode.valueOf(modeName);

                _dcMotor.setMode(_mode);

                RobotLog.dd(CLASS_NAME, "configure()::mode: %s", _mode);
            }

            // Zero power behavior

            if( jsonObject.has("zeroPowerBehavior")) {
                String zeroPowerName = jsonObject.getString("zeroPowerBehavior");

                DcMotor.ZeroPowerBehavior zeroPowerBehavior = DcMotor.ZeroPowerBehavior.valueOf(zeroPowerName);

                _dcMotor.setZeroPowerBehavior(zeroPowerBehavior);

                RobotLog.dd(CLASS_NAME, "configure()::zeroPowerBehavior: %s", zeroPowerBehavior);
            }

            // Velocity

            if( jsonObject.has("velocity")) {
                _velocity = jsonObject.getBoolean("velocity");
                RobotLog.dd(CLASS_NAME, "configure()::_velocity: %s", _velocity);
            }

            if( jsonObject.has("maxVelocity")) {
                _maxVelocity = jsonObject.getDouble("maxVelocity");
                RobotLog.dd(CLASS_NAME, "configure()::_maxVelocity: %s", _maxVelocity);
            }

            if( jsonObject.has("clicksPerMeter")) {
                _clicksPerMeter = jsonObject.getInt("clicksPerMeter");
                RobotLog.dd(CLASS_NAME, "configure()::_clicksPerMeter: %s", _clicksPerMeter);
            }

        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    /**
     * Sets the speed, which is a value between 0 and 1.   If velocity is to be used, then the
     * spreed is converted to velocity using the maxVelocity value, otherwise it is just power.
     * @param speed
     */
    public void setSpeed(double speed) {
        if(_velocity) {
            double effVelocity = speed * _maxVelocity;
            ((DcMotorEx)_dcMotor).setVelocity(effVelocity, AngleUnit.DEGREES);
        }
        else {
            _dcMotor.setPower(speed);
        }
    }

    public void stop() {
        setSpeed(0);
    }

    public double getSpeed() {
        if(_velocity) {
            return ((DcMotorEx)_dcMotor).getVelocity(AngleUnit.DEGREES) * _maxVelocity;
        }
        else {
            return _dcMotor.getPower();
        }
    }

    public int getCurrentPosition() {
        return _dcMotor.getCurrentPosition() - _basePosition;
    }

    public void resetPosition() {
        RobotLog.dd(CLASS_NAME, "resetPosition()");

        _basePosition = _dcMotor.getCurrentPosition();
    }

    public double getDistance() {
        return ((double)getCurrentPosition()) / _clicksPerMeter;
    }

    public double getVelocity() {
        return ((DcMotorEx)_dcMotor).getVelocity();
    }

    public void addTelemetryData(Telemetry telemetry) {
        String effName = _name;

        if(effName == null) {
            effName = "motor";
        }

        telemetry.addData(effName, "speed: %.2f pos: %d vel: %.2f", getSpeed(), getCurrentPosition(), getVelocity());
    }

    public void getPropertyValues(Map<String, Object> values) {
        super.getPropertyValues(values);

        String prefix = "";

        if(_name != null) {
            prefix = _name + ":";
        }

        values.put(prefix + "position", getCurrentPosition());
        values.put(prefix + "velocity", getVelocity());
        values.put(prefix + "distance", getDistance());
    }
}
