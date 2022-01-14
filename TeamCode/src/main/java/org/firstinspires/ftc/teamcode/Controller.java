package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public abstract class Controller extends Component implements ControllerIF {
    DoubleState _leftX = new DoubleState("leftX", 0);
    DoubleState _leftY = new DoubleState("leftY", 0);
    DoubleState _rightX = new DoubleState("rightX", 0);
    DoubleState _rightY = new DoubleState("rightY", 0);

    BooleanState _rightStickButton = new BooleanState("rightStickButton", false);
    BooleanState _leftStickButton = new BooleanState("leftStickButton", false);

    BooleanState _dpadLeft = new BooleanState("dpadLeft", false);
    BooleanState _dpadRight = new BooleanState("dpadRight", false);
    BooleanState _dpadUp = new BooleanState("dpadUp", false);
    BooleanState _dpadDown = new BooleanState("dpadDown", false);

    BooleanState _a = new BooleanState("a", false);
    BooleanState _b = new BooleanState("b",false);
    BooleanState _x = new BooleanState("x", false);
    BooleanState _y = new BooleanState("y", false);

    DoubleState _rightTrigger = new DoubleState("rightTrigger", 0);
    DoubleState _leftTrigger = new DoubleState("leftTrigger", 0);

    BooleanState _rightButton = new BooleanState("rightBotton", false);
    BooleanState _leftButton = new BooleanState("leftButton", false);

    public Controller(OpMode opMode, String name) {
        super(opMode, name);
    }

    private void configureControls(JSONObject jsonObject, Map<String, DeviceIF> devices) throws JSONException, ConfigurationException {
        JSONArray controlNames = jsonObject.names();

        for(int i = 0; i < controlNames.length(); ++i) {
            String controlTypeName = controlNames.getString(i);

            RobotLog.dd(this.getClass().getSimpleName(), "controlTypeName: %s", controlTypeName);

            ControllerIF.ControlType controlType;

            try {
                controlType = ControlType.valueOf(controlTypeName);
            } catch (IllegalArgumentException ex) {
                throw new ConfigurationException("Invalid controlTypeName: " + controlTypeName, ex);
            }

            RobotLog.dd(this.getClass().getSimpleName(), "controlType: %s", controlType);

            JSONObject controlConfig = jsonObject.getJSONObject(controlTypeName);

            switch(controlType) {
            case LEFT_X:
                _leftX.configure(controlConfig, devices);
                break;
            case LEFT_Y:
                _leftY.configure(controlConfig, devices);
                break;
            case RIGHT_X:
                _rightX.configure(controlConfig, devices);
                break;
            case RIGHT_Y:
                _rightY.configure(controlConfig, devices);
                break;
            case RIGHT_STICK_BUTTON:
                _rightStickButton.configure(controlConfig, devices);
                break;
            case LEFT_STICK_BUTTON:
                _leftStickButton.configure(controlConfig, devices);
                break;
            case DPAD_LEFT:
                _dpadLeft.configure(controlConfig, devices);
                break;
            case DPAD_RIGHT:
                _dpadRight.configure(controlConfig, devices);
                break;
            case DPAD_UP:
                _dpadUp.configure(controlConfig, devices);
                break;
            case DPAD_DOWN:
                _dpadDown.configure(controlConfig, devices);
                break;
            case A:
                _a.configure(controlConfig, devices);
                break;
            case B:
                _b.configure(controlConfig, devices);
                break;
            case X:
                _x.configure(controlConfig, devices);
                break;
            case Y:
                _y.configure(controlConfig, devices);
                break;
            case RIGHT_TRIGGER:
                _rightTrigger.configure(controlConfig, devices);
                break;
            case LEFT_TRIGGER:
                _leftTrigger.configure(controlConfig, devices);
                break;
            case RIGHT_BUTTON:
                _rightButton.configure(controlConfig, devices);
                break;
            case LEFT_BUTTON:
                _leftButton.configure(controlConfig, devices);
                break;
            }
        }
    }

    public void configure(JSONObject jsonObject, Map<String, DeviceIF> devices) throws ConfigurationException {
        super.configure(jsonObject);

        try {
            if(jsonObject.has("controls")) {
                configureControls(jsonObject.getJSONObject("controls"), devices);
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    public void updateGamepad(Gamepad gamepad) {
        _leftX.updateValue(gamepad.left_stick_x);
        _leftY.updateValue(gamepad.left_stick_y);
        _rightX.updateValue(gamepad.right_stick_x);
        _rightY.updateValue(gamepad.right_stick_y);

        _rightStickButton.updateValue(gamepad.right_stick_button);
        _leftStickButton.updateValue(gamepad.left_stick_button);

        _dpadLeft.updateValue(gamepad.dpad_left);
        _dpadRight.updateValue(gamepad.dpad_right);
        _dpadUp.updateValue(gamepad.dpad_up);
        _dpadDown.updateValue(gamepad.dpad_down);

        _a.updateValue(gamepad.a);
        _b.updateValue(gamepad.b);
        _x.updateValue(gamepad.x);
        _y.updateValue(gamepad.y);

        _rightTrigger.updateValue(gamepad.right_trigger);
        _leftTrigger.updateValue(gamepad.left_trigger);

        _rightButton.updateValue(gamepad.right_bumper);
        _leftButton.updateValue(gamepad.left_bumper);
    }

    @Override
    public void addTelemetryData(Telemetry telemetry) {
        super.addTelemetryData(telemetry);

        if(_telemetry) {
            _leftX.updateTelemetry(telemetry);
            _leftY.updateTelemetry(telemetry);
            _rightX.updateTelemetry(telemetry);
            _rightY.updateTelemetry(telemetry);

            _rightStickButton.updateTelemetry(telemetry);
            _leftStickButton.updateTelemetry(telemetry);

            _dpadLeft.updateTelemetry(telemetry);
            _dpadRight.updateTelemetry(telemetry);
            _dpadUp.updateTelemetry(telemetry);
            _dpadDown.updateTelemetry(telemetry);

            _a.updateTelemetry(telemetry);
            _b.updateTelemetry(telemetry);
            _x.updateTelemetry(telemetry);
            _y.updateTelemetry(telemetry);

            _rightTrigger.updateTelemetry(telemetry);
            _leftTrigger.updateTelemetry(telemetry);

            _rightButton.updateTelemetry(telemetry);
            _leftButton.updateTelemetry(telemetry);
        }
    }

    @Override
    public void getPropertyValues(Map<String, Object> values) {
        super.getPropertyValues(values);

        _leftX.getPropertyValues(values);
        _leftY.getPropertyValues(values);
        _rightX.getPropertyValues(values);
        _rightY.getPropertyValues(values);

        _rightStickButton.getPropertyValues(values);
        _leftStickButton.getPropertyValues(values);

        _dpadLeft.getPropertyValues(values);
        _dpadRight.getPropertyValues(values);
        _dpadUp.getPropertyValues(values);
        _dpadDown.getPropertyValues(values);

        _a.getPropertyValues(values);
        _b.getPropertyValues(values);
        _x.getPropertyValues(values);
        _y.getPropertyValues(values);

        _rightTrigger.getPropertyValues(values);
        _leftTrigger.getPropertyValues(values);

        _rightButton.getPropertyValues(values);
        _leftButton.getPropertyValues(values);
    }

    public void init() {
    }

    public void update() {
    }

    public void terminate() {
    }
}
