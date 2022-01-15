package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AutoStateCustom implements AutoStateIF {
    private static final String CLASS_NAME = "AutoStateCustom";

    protected OpMode _opMode;

    AutoStateIF _autoState = null;

    public AutoStateCustom(OpMode opMode) {
        _opMode = opMode;
    }

    @Override
    public void setOpMode(OpMode opMode) {
        _opMode = opMode;
    }

    public void configure(JSONObject jsonObject,
                          DriveTrainIF driveTrain,
                          Map<String, DeviceIF> devices,
                          Map<String, SensorIF> sensors)
            throws ConfigurationException {
        try {
            if(jsonObject.has("className")) {
                String className = jsonObject.getString("className");

                Class classy = ClassLoader.getSystemClassLoader().loadClass(className);
                _autoState = (AutoStateIF)classy.newInstance();
                _autoState.setOpMode(_opMode);
            } else {
                RobotLog.dd(CLASS_NAME, "No className");
                throw new ConfigurationException("No className specified");
            }

        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationException(e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new ConfigurationException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }

        _autoState.configure(jsonObject, driveTrain, devices, sensors);
    }

    public void init(RobotBase robotBase) {

    }

    public void terminate(RobotBase robotBase) {

    }

    public String doState(RobotBase robotBase, Map<String, Object> propertyValues) throws InterruptedException {
        return _autoState.doState(robotBase, propertyValues);
    }

}