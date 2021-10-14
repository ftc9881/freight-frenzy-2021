package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SensorColor extends Sensor implements SensorIF {
    private static final String CLASS_NAME = "SensorColor";

    NormalizedColorSensor _colorSensor;
    float _gain = 2;

    NormalizedRGBA _colors;
    final float[] _hsvValues = new float[3];

    public SensorColor(OpMode opMode) {
        super(opMode);
    }

    @Override
    public void configure(JSONObject jsonObject, Map<String, DeviceIF> devices) throws JSONException, ConfigurationException {
        super.configure(jsonObject, devices);

        String sensorName;

        try {
            sensorName = jsonObject.getString("name");

            try {
                _colorSensor = _opMode.hardwareMap.get(NormalizedColorSensor.class, sensorName);
            }
            catch(IllegalArgumentException e) {
                throw new ConfigurationException("No color sensor with the name: " + sensorName, e);
            }

            if (jsonObject.has("gain")) {
                _gain = (float)jsonObject.getDouble("gain");
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    public void update() {
        super.update();

        _colorSensor.setGain(_gain);

        // Get the normalized colors from the sensor
        _colors = _colorSensor.getNormalizedColors();

        // Update the hsvValues array by passing it to Color.colorToHSV()
        Color.colorToHSV(_colors.toColor(), _hsvValues);
    }

    @Override
    public void addTelemetryData(Telemetry telemetry) {
        super.addTelemetryData(telemetry);

        if(_telemetry) {
            telemetry.addData("Color",
                    "H: %.2f S: %.2f V: %.2f",
                    _hsvValues[0], _hsvValues[1], _hsvValues[2]
                    );
        }
    }

    @Override
    public void getPropertyValues(Map<String, Object> values) {
        super.getPropertyValues(values);

        values.put("hue", _hsvValues[0]);
        values.put("saturation", _hsvValues[1]);
        values.put("value", _hsvValues[2]);
    }
}
