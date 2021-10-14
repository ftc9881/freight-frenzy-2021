package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Sensor extends Component implements SensorIF {
    // TODO: Support multi-threaded sensors

    public Sensor(OpMode opMode) {
        super(opMode);
    }

    @Override
    public void configure(JSONObject jsonObject, Map<String, DeviceIF> devices) throws JSONException, ConfigurationException {
        super.configure(jsonObject);

    }

    @Override
    public void update() {

    }

}
