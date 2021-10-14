package org.firstinspires.ftc.teamcode;

import org.json.JSONException;

public class ConfigurationException extends Exception {
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(Throwable cause) {
        super(cause);
    }
}
