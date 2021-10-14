package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.json.JSONObject;

import java.util.Map;

public interface ControllerIF extends ComponentIF {
    void update(Gamepad gamepad);

    void updateMovement();

    Movement getMovement();

    enum ControlType {
        LEFT_X,
        LEFT_Y,
        RIGHT_X,
        RIGHT_Y,
        RIGHT_STICK_BUTTON,
        LEFT_STICK_BUTTON,
        DPAD_LEFT,
        DPAD_RIGHT,
        DPAD_UP,
        DPAD_DOWN,
        A,
        B,
        X,
        Y,
        RIGHT_TRIGGER,
        LEFT_TRIGGER,
        RIGHT_BUTTON,
        LEFT_BUTTON
    }

    void configure(JSONObject jsonObject, Map<String, DeviceIF> devices) throws ConfigurationException;
}
