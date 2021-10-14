package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Transition {
    private static final String CLASS_NAME = "Transition";

    ComponentIF _component;
    String _propertyName;
    double _min = Double.NEGATIVE_INFINITY;
    double _max = Double.POSITIVE_INFINITY;
    String _newState;

    private ComponentIF findComponent(String name, DriveTrainIF driveTrain, Map<String, DeviceIF> devices, Map<String, SensorIF> sensors) {
        if(name.equalsIgnoreCase("driveTrain")) {
            return driveTrain;
        }
        else {
            ComponentIF component = devices.get(name);

            if(component == null) {
                component = sensors.get(name);
            }

            return component;
        }
    }

    public void configureCondition(JSONObject jsonObject,
                          DriveTrainIF driveTrain,
                          Map<String, DeviceIF> devices,
                          Map<String, SensorIF> sensors)
            throws ConfigurationException {

        try {
            if(jsonObject.has("name")) {
                RobotLog.dd(CLASS_NAME, "name: %s", jsonObject.getString("name"));

                _component = findComponent(jsonObject.getString("name"), driveTrain, devices, sensors);

                if(_component == null) {
                    throw new ConfigurationException("Invalid component name: " + jsonObject.getString("name"));
                }
            } else {
                RobotLog.dd(CLASS_NAME, "Missing component name");
            }

            if(jsonObject.has("propertyName")) {
                _propertyName = jsonObject.getString("propertyName");
            } else {
                throw new ConfigurationException("Transition missing propertyName");
            }

            if(jsonObject.has("min")) {
                _min = jsonObject.getDouble("min");
            }

            if(jsonObject.has("max")) {
                _max = jsonObject.getDouble("max");
            }

        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }

    }

    public void configure(JSONObject jsonObject,
                          DriveTrainIF driveTrain,
                          Map<String, DeviceIF> devices,
                          Map<String, SensorIF> sensors)
        throws ConfigurationException {

        RobotLog.dd(CLASS_NAME, "configure");

        try {
            if(jsonObject.has("condition")) {
                configureCondition(jsonObject.getJSONObject("condition"), driveTrain, devices, sensors);
            } else {
                RobotLog.dd(CLASS_NAME, "No condition given");
            }

            if(jsonObject.has("newState")) {
                _newState = jsonObject.getString("newState");
            } else {
                RobotLog.dd(CLASS_NAME, "No newState");
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    String getNewState() {
        return _newState;
    }

    public boolean isActive(Map<String, Object> propertyValues) {
        Map<String, Object> effPropertyValues = new HashMap<>(propertyValues);

        if(_component != null) {
            _component.getPropertyValues(effPropertyValues);
            RobotLog.dd(CLASS_NAME, effPropertyValues.keySet().toString());
        } else {
            RobotLog.dd(CLASS_NAME, "null _component");
        }

        Object objValue = effPropertyValues.get(_propertyName);

        RobotLog.dd(CLASS_NAME, "objValue: %s", objValue);

        if(objValue != null) {
            double value = ((Number) objValue).doubleValue();

            return value < _min || value > _max;
        }
        else {
            // Is no property then transition to the next state
            // TODO: Make this do something more reasonable

            return true;
        }
    }
}
