package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.RobotLog;

import org.json.JSONException;
import org.json.JSONObject;

public class StuPID {
    private static final String CLASS_NAME = "StuPID";

    double _kP = 0;
    double _kI = 0;
    double _kD = 0;

    double _minI = Double.NEGATIVE_INFINITY;
    double _maxI = Double.POSITIVE_INFINITY;

    double _i = 0;
    double _errorPrev = 0;

    public void configure(JSONObject jsonObject) throws ConfigurationException {
        try {
            if (jsonObject.has("kP")) {
                _kP = jsonObject.getDouble("kP");
            }
            if (jsonObject.has("kI")) {
                _kI = jsonObject.getDouble("kI");
            }
            if (jsonObject.has("kD")) {
                _kD = jsonObject.getDouble("kD");
            }
            if (jsonObject.has("minI")) {
                _minI = jsonObject.getDouble("minI");
            }
            if (jsonObject.has("maxI")) {
                _maxI = jsonObject.getDouble("maxI");
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    void reset() {
        _i = 0;
        _errorPrev = Double.NaN;
    }

    double getControlVariable(double processVariable, double setPoint, double deltaT) {
        double error = setPoint - processVariable;

        double p = _kP * error;

        _i += _kI * error * deltaT;

        _i = Math.min(Math.max(_i, _minI), _maxI);

        double d;

        if(!Double.isNaN(_errorPrev)) {
            d = _kD * (error - _errorPrev) / deltaT;
        }
        else {
            d = 0;
        }

        _errorPrev = error;

        double controlVariable = p + _i + d;

        RobotLog.dd(CLASS_NAME, "p: %.2f i: %.2f d: %.2f cv: %.2f", p, _i, d, controlVariable);

        return controlVariable;
    }
}
