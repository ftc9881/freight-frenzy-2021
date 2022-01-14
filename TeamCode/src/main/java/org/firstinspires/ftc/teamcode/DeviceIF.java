package org.firstinspires.ftc.teamcode;

import java.util.Map;

public interface DeviceIF extends ComponentIF {
    boolean isValidBehavior(String behavior);

    void behave(ActionIF action, String behavior, Map<String, Object> properties);

    boolean isValidParameter(String parameter);

    void setParameter(String parameterName, String value);
}
