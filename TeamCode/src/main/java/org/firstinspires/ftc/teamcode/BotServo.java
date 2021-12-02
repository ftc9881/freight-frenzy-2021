package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class BotServo extends Component {
    private static final String CLASS_NAME = "BotServo";

    Servo _servo = null;

    BotServo(OpMode opMode, String name) {
        super(opMode, name);
    }

    @Override
    public void configure(JSONObject jsonObject) throws ConfigurationException {
        String servoName = null;

        try {
            servoName = jsonObject.getString("name");

            try {
                _servo = _opMode.hardwareMap.get(Servo.class, servoName);
            }
            catch(IllegalArgumentException e) {
                throw new ConfigurationException("No servo with the name: " + servoName, e);
            }

            // Direction

            if( jsonObject.has("direction")) {
                String directionName = jsonObject.getString("direction");

                Servo.Direction direction = Servo.Direction.valueOf(directionName);

                _servo.setDirection(direction);

                RobotLog.dd(CLASS_NAME, "configure()::direction: %s", direction);
            }

            // TODO: Add scaleRange() ?

        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    public double setPosition(double position) {
        RobotLog.dd(CLASS_NAME, "position: %.2f", position);

        _servo.setPosition(position);

        return(position);
    }

    public double getPosition() {
        return _servo.getPosition();
    }

    public void addTelemetryData(Telemetry telemetry) {
        String effName = _name;

        if(effName == null) {
            effName = "motor";
        }

        telemetry.addData(effName, "pos: %f", getPosition());
    }

    public void getPropertyValues(Map<String, Object> values) {
        super.getPropertyValues(values);

        String prefix = "";

        if(_name != null) {
            prefix = _name + ":";
        }

        values.put(prefix + "position", getPosition());
    }
}
