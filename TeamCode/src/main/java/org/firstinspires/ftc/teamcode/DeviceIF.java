package org.firstinspires.ftc.teamcode;

public interface DeviceIF extends ComponentIF {
    boolean isValidBehavior(String behavior);

    void processAction(ActionIF action, double value);
}
