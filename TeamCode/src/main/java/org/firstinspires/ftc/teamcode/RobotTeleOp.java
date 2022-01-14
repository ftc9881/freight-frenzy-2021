package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class RobotTeleOp extends RobotBase {
    protected ControllerIF _controllerOne;
    protected ControllerIF _controllerTwo;

    private void setControllerOne(ControllerIF controller) {
        _controllerOne = controller;
    }

    private void setControllerTwo(ControllerIF controller) {
        _controllerTwo = controller;
    }

    private ControllerIF constructController(JSONObject jsonObject, Map<String, DeviceIF> devices, String name) throws JSONException, ConfigurationException {
        String controllerTypeName = jsonObject.getString("type");

        ComponentFactoryIF.ControllerType controllerType = ComponentFactoryIF.ControllerType.valueOf(controllerTypeName);

        RobotLog.dd(CLASS_NAME, "controllerType: %s", controllerType);

        ControllerIF controller = _componentFactory.controllerInstance(controllerType, this, name);

        controller.configure(jsonObject, devices);

        return controller;
    }

    public void configure(JSONObject jsonObject) throws ConfigurationException {
        super.configure(jsonObject);

        try {

            // Configure controllers

            if(jsonObject.has("controllerOne")) {
                ControllerIF controllerOne = constructController(jsonObject.getJSONObject("controllerOne"), _devices, "controllerOne");
                setControllerOne(controllerOne);
            }

            if(jsonObject.has("controllerTwo")) {
                ControllerIF controllerTwo = constructController(jsonObject.getJSONObject("controllerTwo"), _devices, "controllerTwo");
                setControllerTwo(controllerTwo);
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    void updatePropertyValues(Map<String,Object> values) {
        super.updatePropertyValues(values);

        if(_controllerOne != null) {
            _controllerOne.getPropertyValues(values);
        }

        if(_controllerTwo != null) {
            _controllerTwo.getPropertyValues(values);
        }
    }

    public void addTelemetryData(Telemetry telemetry) {
        super.addTelemetryData(telemetry);

        if(_controllerOne != null) {
            _controllerOne.addTelemetryData(telemetry);
        }

        if(_controllerTwo != null) {
            _controllerTwo.addTelemetryData(telemetry);
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        // Wait for the game to start (driver presses PLAY)

        waitForStart();

        ElapsedTime _runtime = new ElapsedTime();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            Map<String, Object> logValues = new HashMap<>();

            Movement movementOne = null;
            Movement movementTwo = null;

            if(_controllerOne != null) {
                _controllerOne.updateGamepad(gamepad1);
                _controllerOne.addTelemetryData(telemetry);
                _controllerOne.updateMovement();
                movementOne = _controllerOne.getMovement();
            }

            if(_controllerTwo != null) {
                _controllerTwo.updateGamepad(gamepad2);
                _controllerTwo.addTelemetryData(telemetry);
                _controllerTwo.updateMovement();
                movementTwo = _controllerTwo.getMovement();
            }

            Movement movementBoth = Movement.combine(movementOne, movementTwo);

            _driveTrain.updateMovement(movementBoth);

            updateComponents();

            telemetry.clearAll();

            addTelemetryData(telemetry);

            logValues();

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + _runtime.toString());

            telemetry.update();
        }

        terminateComponents();
    }
}
