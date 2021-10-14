package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Movement {
    public double _moveSpeed = 0;
    public double _moveAngle = 0;
    public double _turnSpeed = 0;

    public Movement() {
    }

    public Movement(double moveSpeed, double moveAngle, double turnSpeed) {
        _moveSpeed = moveSpeed;
        _moveAngle = moveAngle;
        _turnSpeed = turnSpeed;
    }

    public Movement(Movement movement) {
        this(movement._moveSpeed, movement._moveAngle, movement._turnSpeed);
    }

    public static Movement combine(Movement movementOne, Movement movementTwo) {
        if(movementOne == null) {
            return movementTwo;
        }
        else if(movementTwo == null) {
            return movementOne;
        }
        else {
            return new Movement(
                    (movementOne._moveSpeed + movementTwo._moveSpeed) / 2,
                    (movementOne._moveAngle + movementTwo._moveAngle) / 2,
                    (movementOne._turnSpeed + movementTwo._turnSpeed) / 2
            );
        }
    }

    public void configure(JSONObject jsonObject) throws ConfigurationException {
        try {
            if (jsonObject.has("moveSpeed")) {
                _moveSpeed = jsonObject.getDouble("moveSpeed");
            }
            if (jsonObject.has("moveAngle")) {
                _moveAngle = Math.PI * jsonObject.getDouble("moveAngle") / 180;
            }
            if (jsonObject.has("turnSpeed")) {
                _turnSpeed = jsonObject.getDouble("turnSpeed");
            }
        } catch (JSONException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    public void updateTelemetry(Telemetry telemetry) {
        telemetry.addData("movement", "speed: %.2f angle: %.2f turn: %.2f",
                _moveSpeed,
                180 * _moveAngle / Math.PI,
                _turnSpeed);
    }

    public void getPropertyValues(Map<String, Object> values) {
        values.put("moveSpeed", _moveSpeed);
        values.put("moveAngle", _moveAngle);
        values.put("turnSpeed", _turnSpeed);
    }

}
