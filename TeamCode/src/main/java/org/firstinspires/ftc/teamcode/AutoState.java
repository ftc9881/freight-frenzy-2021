package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class AutoState implements AutoStateIF {
    private static final String CLASS_NAME = "AutoState";

    protected OpMode _opMode;

    Map<String, Transition> _transitions = new HashMap<>();

    long _maxMilliseconds = Long.MAX_VALUE;
    long _initTime = 0;

    public AutoState(OpMode opMode) {
        _opMode = opMode;
    }

    @Override
    public void setOpMode(OpMode opMode) {
        _opMode = opMode;
    }

    private void configureTransitions(JSONObject jsonObject,
                                      DriveTrainIF driveTrain,
                                      Map<String, DeviceIF> devices,
                                      Map<String, SensorIF> sensors)
            throws JSONException, ConfigurationException {
        JSONArray transitionNames = jsonObject.names();

        for(int i = 0; i < transitionNames.length(); ++i) {
            String transitionName = transitionNames.getString(i);

            JSONObject transitionConfig = jsonObject.getJSONObject(transitionName);

            Transition transition = new Transition();

            transition.configure(transitionConfig, driveTrain, devices, sensors);

            _transitions.put(transitionName, transition);
        }
    }

    public void configure(JSONObject jsonObject,
                          DriveTrainIF driveTrain,
                          Map<String, DeviceIF> devices,
                          Map<String, SensorIF> sensors)
            throws ConfigurationException {
        try {
            if(jsonObject.has("transitions")) {
                configureTransitions(jsonObject.getJSONObject("transitions"), driveTrain, devices, sensors);
            } else {
                RobotLog.dd(CLASS_NAME, "No transitions specified");
            }
            if (jsonObject.has("maxMilliseconds")) {
                _maxMilliseconds = jsonObject.getLong("maxMilliseconds");
            }

        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    String evaluateTransitions(Map<String, Object> propertyValues) {
        for(String transitionName : _transitions.keySet()) {
            RobotLog.dd(CLASS_NAME, "evaluateTransitions()::transitionName: %s", transitionName);

            Transition transition = _transitions.get(transitionName);

            boolean active = transition.isActive(propertyValues);

            RobotLog.dd(CLASS_NAME, "evaluateTransitions()::active: %s", active);

            if(active) {
                return transition.getNewState();
            }
        }

        return null;
    }

    @Override
    public String doState(RobotBase robotBase, Map<String, Object> propertyValues) throws InterruptedException {
        init();

        boolean active = true;

        while(robotBase.opModeIsActive() && active) {
            robotBase.logValues();

            RobotLog.dd(CLASS_NAME, "doState()::doAction()::Pre");

            active = doAction();

            RobotLog.dd(CLASS_NAME, "doState()::active: %s", active);

            robotBase.updateComponents();
        }

        end();

        Map<String, Object> effPropertyValues = new HashMap<>(propertyValues);
        robotBase.updatePropertyValues(effPropertyValues);

        String newState = evaluateTransitions(effPropertyValues);

        RobotLog.dd(CLASS_NAME, "doState()::newState: %s", active);

        return(newState);
    }

    public void init() {
        _initTime = System.currentTimeMillis();
    }

    protected boolean doAction() throws InterruptedException {
        long elapsedTime = System.currentTimeMillis() - _initTime;

        return elapsedTime < _maxMilliseconds;
    }

    public void end() {

    }
}
