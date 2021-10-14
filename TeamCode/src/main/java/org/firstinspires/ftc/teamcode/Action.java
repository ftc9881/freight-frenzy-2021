package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.RobotLog;

public class Action implements ActionIF {
    private final DeviceIF _device;
    private final String _behavior;

    public Action(DeviceIF device, String behavior) {
        _device = device;
        _behavior = behavior;
    }

    @Override
    public String getBehavior() {
        return _behavior;
    }

    @Override
    public void process(double value) {
        RobotLog.dd(this.getClass().getSimpleName(), "Process action");

        _device.processAction(this, value);
    }
}
