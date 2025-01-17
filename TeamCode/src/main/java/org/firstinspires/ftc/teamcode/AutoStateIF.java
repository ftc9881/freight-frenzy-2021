package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.json.JSONObject;

import java.util.Map;

public interface AutoStateIF {
    void setOpMode(OpMode opmode);

    void configure(JSONObject jsonObject,
                   DriveTrainIF driveTrain,
                   Map<String, DeviceIF> devices,
                   Map<String, SensorIF> sensors)
            throws ConfigurationException;

    void init(RobotBase robotBase);

    String doState(RobotBase robotBase, Map<String, Object> propertyValues) throws InterruptedException;

    void terminate(RobotBase robotBase);
}
