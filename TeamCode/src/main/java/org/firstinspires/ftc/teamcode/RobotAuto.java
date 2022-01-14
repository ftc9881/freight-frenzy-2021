package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class RobotAuto extends RobotBase {
    private static final String CLASS_NAME = "RobotAuto";

    private ArrayList<String> _autoPlanNames = new ArrayList<String>();
    private HashMap<String, AutonomousPlan> _autoPlans = new HashMap<String, AutonomousPlan>();

    private void configureAutonomousPlans(JSONObject jsonObject,
                                          DriveTrainIF driveTrain,
                                          Map<String, DeviceIF> devices,
                                          Map<String, SensorIF> sensors) throws JSONException, ConfigurationException {
        RobotLog.dd(CLASS_NAME, "Configure AutonomousPlans");

        JSONArray autoModeNames = jsonObject.names();

        for(int i = 0; i < autoModeNames.length(); ++i) {
            String autoModeName = autoModeNames.getString(i);

            JSONObject autoModeJSON = jsonObject.getJSONObject(autoModeName);

            String autoName = autoModeJSON.getString("name");
            String autoFileName = autoModeJSON.getString("fileName");

            RobotLog.dd(CLASS_NAME, "autoName: %s", autoName);
            RobotLog.dd(CLASS_NAME, "autoFileName: %s", autoFileName);

            AutonomousPlan autoPlan = new AutonomousPlan();

            autoPlan.readAutoPlan(
                    autoFileName,
                    BASE_PATH,
                    this,
                    driveTrain,
                    devices,
                    sensors);

            _autoPlans.put(autoModeName, autoPlan);
            _autoPlanNames.add(autoModeName);
        }
    }

    public void configure(JSONObject jsonObject) throws ConfigurationException {
        super.configure(jsonObject);

        try {
            if(jsonObject.has("autonomousModes")) {
                configureAutonomousPlans(
                        jsonObject.getJSONObject("autonomousModes"),
                        _driveTrain,
                        _devices,
                        _sensors
                );
            }

        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        int indexSelected = 0;

        boolean upStateOld = false;
        boolean downStateOld = false;

        telemetry.addLine("Select Autonomous Plan");
        telemetry.update();

        while(true) {
            for(int i = 0;i < _autoPlanNames.size();++i) {
                telemetry.addData(_autoPlanNames.get(i), i == indexSelected ? '*' : ' ');
            }
            telemetry.update();

            if(gamepad1.a) {
                break;
            }

            boolean upStateNew = gamepad1.dpad_up;
            boolean downStateNew = gamepad1.dpad_down;

            if(downStateNew && !downStateOld) {
                indexSelected = indexSelected + 1;
                if(indexSelected == _autoPlans.size()) {
                    indexSelected = 0;
                }
            }

            if(upStateNew &&  !upStateOld) {
                indexSelected = indexSelected - 1;
                if(indexSelected < 0) {
                    indexSelected = _autoPlans.size() - 1;
                }
            }

            upStateOld = upStateNew;
            downStateOld = downStateNew;

            sleep(10);
        }

        String autoPlanName = _autoPlanNames.get(indexSelected);

        telemetry.addLine(String.format("Plan Selected: %s", autoPlanName));
        telemetry.update();

        _driveTrain.resetPositions();

        AutonomousPlan autoPlan = _autoPlans.get(autoPlanName);

        // Wait for the game to start (driver presses PLAY)

        waitForStart();

        autoPlan.runPlan(this);

        terminateComponents();
    }
}
