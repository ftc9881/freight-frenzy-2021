package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class RobotAuto extends RobotBase {
    private static final String CLASS_NAME = "RobotAuto";

    public AutoStateFactoryIF _autoStateFactory = new AutoStateFactory();

    Map<String, AutoStateIF> _autoStates = new HashMap<>();

    String _firstState = null;
    String _startState = null;

    /**
     * The default SteeringIF to use when moving
     */
    SteeringIF _moveSteering;

    /**
     * The default SteeringIF to use when turning
     */
    SteeringIF _turnSteering;

    private SteeringIF createSteering(JSONObject jsonObject, Map<String, SensorIF> sensors) throws ConfigurationException {
        try {
            String steeringTypeName = jsonObject.getString("type");

            Steering.SteeringType steeringType = Steering.SteeringType.valueOf(steeringTypeName);

            SteeringIF steering = Steering.constructSteering(steeringType);

            steering.configure(jsonObject, sensors);

            return steering;
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    public void configureDefaults(JSONObject jsonObject,
                                Map<String, DeviceIF> devices,
                                Map<String, SensorIF> sensors)
            throws ConfigurationException {
        try {
            if (jsonObject.has("moveSteering")) {
                _moveSteering = createSteering(jsonObject.getJSONObject("moveSteering"), sensors);
            }

            if (jsonObject.has("turnSteering")) {
                _turnSteering = createSteering(jsonObject.getJSONObject("turnSteering"), sensors);
            }

        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    public void configureStates(JSONObject jsonObject,
                                DriveTrainIF driveTrain,
                                Map<String, DeviceIF> devices,
                                Map<String, SensorIF> sensors,
                                SteeringIF moveSteering,
                                SteeringIF turnSteering)
            throws ConfigurationException {
        try {
            JSONArray autoStateNames = jsonObject.names();

            _firstState = autoStateNames.getString(0);

            for(int i = 0; i < autoStateNames.length(); ++i) {
                String autoStateName = autoStateNames.getString(i);

                JSONObject autoStateConfig = jsonObject.getJSONObject(autoStateName);

                String autoStateTypeName = autoStateConfig.getString("type");

                AutoStateFactoryIF.AutoStateType autoStateType = AutoStateFactoryIF.AutoStateType.valueOf(autoStateTypeName);

                RobotLog.dd(CLASS_NAME, "autoStateType: %s", autoStateType);

                AutoStateIF autoState = _autoStateFactory.autoStateInstance(autoStateType,
                        this,
                        moveSteering,
                        turnSteering
                );

                autoState.configure(autoStateConfig, driveTrain, devices, sensors);

                _autoStates.put(autoStateName, autoState);
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    public void configureAutoPlan(JSONObject jsonObject,
                                  DriveTrainIF driveTrain,
                                  Map<String, DeviceIF> devices,
                                  Map<String, SensorIF> sensors)
            throws ConfigurationException {
        try {
            // Configure controllers
            if(jsonObject.has("defaults")) {
                configureDefaults(jsonObject.getJSONObject("defaults"), devices, sensors);
            }

            if(jsonObject.has("startState")) {
                _startState = jsonObject.getString("startState");
            }

            if(jsonObject.has("states")) {
                configureStates(jsonObject.getJSONObject("states"), driveTrain, devices, sensors,
                        _moveSteering,
                        _turnSteering
                );
            }

        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    public void readAutoPlan(String autoPlanFile, String baseConfigPath,
                             DriveTrainIF driveTrain,
                             Map<String, DeviceIF> devices,
                             Map<String, SensorIF> sensors) throws ConfigurationException {
        if (baseConfigPath == null) {
            baseConfigPath = BASE_PATH;
        }

        String autoPlanFileFull = baseConfigPath + "/" + autoPlanFile;

        RobotLog.dd(CLASS_NAME, "autoPlanFileFull: %s", autoPlanFileFull);

        try {
            JSONObject jsonObject = JSONUtil.getJsonObject(autoPlanFileFull);

            configureAutoPlan(jsonObject, driveTrain, devices, sensors);
        } catch (JSONException | IOException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    abstract String getAutoPlanFilename();

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        try {
            telemetry.addData("Status", "Read auto plan");
            telemetry.update();

            readAutoPlan(getAutoPlanFilename(), null, _driveTrain, _devices, _sensors);

            telemetry.addData("Status", "Auto plan loaded");
            telemetry.update();
        } catch (ConfigurationException e) {
            e.printStackTrace();
            telemetry.addData("ConfigurationException",  e.getMessage());
            telemetry.update();
        }

        _driveTrain.resetPositions();

        // Wait for the game to start (driver presses PLAY)

        waitForStart();

        // Run auto plan

        String currentState = _startState;

        if(currentState == null) {
            currentState = _firstState;
        }

        while(opModeIsActive() && currentState != null) {
            RobotLog.dd(CLASS_NAME, "runOpMode()::currentState: %s", currentState);

            telemetry.addData("State", currentState);
            telemetry.update();

            AutoStateIF autoState = _autoStates.get(currentState);

            if(autoState == null) {
                telemetry.addData("Error", "Undefined state: " + currentState);
                telemetry.update();
                break;
            }
            else {
                Map<String, Object> effPropertyValues = new HashMap<>();
                synchronized(this) {
                    currentState = autoState.doState(this, effPropertyValues);
                }
            }
        }

        RobotLog.dd(CLASS_NAME, "runOpMode()::done");
    }
}
