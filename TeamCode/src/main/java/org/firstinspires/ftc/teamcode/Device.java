package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.Map;

public abstract class Device extends Component implements DeviceIF {
    public Device(OpMode opMode, String name) {
        super(opMode, name);
    }

    @Override
    public boolean isValidBehavior(String behavior) {
        return false;
    }

    @Override
    public void behave(ActionIF action, String behavior, Map<String, Object> properties) {
    }

    public boolean isValidParameter(String parameter) {
        return false;
    }

    @Override
    public void setParameter(String parameterName, String value) {
    }
}
