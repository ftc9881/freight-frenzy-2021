package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class RobotBase extends LinearOpMode {
    static final String CLASS_NAME = "RobotBase";

    public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Robot";

    public ComponentFactoryIF _componentFactory = new ComponentFactory();

    protected DriveTrainIF _driveTrain;

    protected Map<String, DeviceIF> _devices = new HashMap<>();

    protected Map<String, SensorIF> _sensors = new HashMap<>();

    protected static final String DRIVE_TRAIN_NAME = "[drive_train]";

    boolean _log = false;

    private DriveTrainIF constructDriveTrain(JSONObject jsonObject) throws JSONException, ConfigurationException {
        String driveTrainTypeName = jsonObject.getString("type");

        ComponentFactoryIF.DriveTrainType driveTrainType = ComponentFactoryIF.DriveTrainType.valueOf(driveTrainTypeName);

        RobotLog.dd(CLASS_NAME, "driveTrainType: %s", driveTrainType);

        DriveTrainIF driveTrain = _componentFactory.driveTrainInstance(driveTrainType, this, null);

        driveTrain.configure(jsonObject);

        return driveTrain;
    }

    private void configureDevices(JSONObject jsonObject) throws JSONException, ConfigurationException {
        JSONArray deviceNames = jsonObject.names();

        for(int i = 0; i < deviceNames.length(); ++i) {
            String deviceName = deviceNames.getString(i);

            JSONObject deviceConfig = jsonObject.getJSONObject(deviceName);

            String deviceTypeName = deviceConfig.getString("type");

            ComponentFactoryIF.DeviceType deviceType = ComponentFactoryIF.DeviceType.valueOf(deviceTypeName);

            RobotLog.dd(CLASS_NAME, "deviceType: %s", deviceType);

            DeviceIF device = _componentFactory.deviceInstance(deviceType, this, deviceName);

            device.configure(deviceConfig);

            _devices.put(deviceName, device);
        }
    }

    private void configureSensors(JSONObject jsonObject, Map<String, DeviceIF> devices) throws JSONException, ConfigurationException {
        JSONArray sensorNames = jsonObject.names();

        for(int i = 0; i < sensorNames.length(); ++i) {
            String sensorName = sensorNames.getString(i);

            JSONObject deviceConfig = jsonObject.getJSONObject(sensorName);

            String sensorTypeName = deviceConfig.getString("type");

            ComponentFactoryIF.SensorType sensorType = ComponentFactoryIF.SensorType.valueOf(sensorTypeName);

            RobotLog.dd(CLASS_NAME, "sensorType: %s", sensorType);

            SensorIF sensor = _componentFactory.sensorInstance(sensorType, this, sensorName);

            sensor.configure(deviceConfig, devices);

            _sensors.put(sensorName, sensor);
        }
    }

    /**
     * Configure robot given the configFile JSON
     * @param configFile JSON file containing robot configuration
     * @param baseConfigPath Path of configuration file.  If null then use global config path.
     */
    public void readConfiguration(String configFile, String baseConfigPath) throws ConfigurationException {
        if (baseConfigPath == null) {
            baseConfigPath = BASE_PATH;
        }

        String configFileFull = baseConfigPath + "/" + configFile;

        RobotLog.dd(CLASS_NAME, "configFileFull: %s", configFileFull);

        try {
            JSONObject jsonObject = JSONUtil.getJsonObject(configFileFull);

            configure(jsonObject);
        } catch (JSONException | IOException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    public void configure(JSONObject jsonObject) throws ConfigurationException {
        try {
            // Configure drive train

            if(jsonObject.has("driveTrain")) {
                DriveTrainIF driveTrain = constructDriveTrain(jsonObject.getJSONObject("driveTrain"));
                setDriveTrain(driveTrain);
            }
            else {
                throw new ConfigurationException("Missing driveTrain");
            }

            // Configure Devices

            if(jsonObject.has("devices")) {
                configureDevices(jsonObject.getJSONObject("devices"));
            }

            // Configure sensors

            if(jsonObject.has("sensors")) {
                configureSensors(jsonObject.getJSONObject("sensors"), _devices);
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    private void setDriveTrain(DriveTrainIF driveTrain) {
        _driveTrain = driveTrain;
        _devices.put(DRIVE_TRAIN_NAME, driveTrain);
    }

    abstract String getRobotConfigFilename();

    void updatePropertyValues(Map<String,Object> values) {
        _driveTrain.getPropertyValues(values);

        for(SensorIF sensor : _sensors.values()) {
            sensor.getPropertyValues(values);
        }

        for(DeviceIF device : _devices.values()) {
            device.getPropertyValues(values);
        }
    }

    public void logValues() {
        if(_log) {
            Map<String, Object> propertyValues = new HashMap<>();
            updatePropertyValues(propertyValues);
        }
    }

    public void addTelemetryData(Telemetry telemetry) {
        _driveTrain.addTelemetryData(telemetry);

        for(SensorIF sensor : _sensors.values()) {
            sensor.update();
            sensor.addTelemetryData(telemetry);
        }

        for(DeviceIF device : _devices.values()) {
            device.addTelemetryData(telemetry);
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        try {
            telemetry.addData("Status", "Read robot configuration");
            telemetry.update();

            readConfiguration(getRobotConfigFilename(), null);

            telemetry.addData("Status", "Robot configuration loaded");
            telemetry.update();
        } catch (ConfigurationException e) {
            e.printStackTrace();
            telemetry.addData("ConfigurationException",  e.getMessage());
            telemetry.update();
        }

        initComponents();
    }

    public void initComponents() {
        _driveTrain.init();

        for(ComponentIF component : _devices.values()) {
            component.init();
        }

        for(ComponentIF component : _sensors.values()) {
            component.init();
        }
    }

    public void updateComponents() {
        _driveTrain.update();

        for(ComponentIF component : _devices.values()) {
            component.update();
        }

        for(ComponentIF component : _sensors.values()) {
            component.update();
        }
    }

    public void terminateComponents() {
        _driveTrain.terminate();

        for(ComponentIF component : _devices.values()) {
            component.terminate();
        }

        for(ComponentIF component : _sensors.values()) {
            component.terminate();
        }
    }

}
