package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class Device extends Component implements DeviceIF {
    public Device(OpMode opMode, String name) {
        super(opMode, name);
    }

    @Override
    public boolean isValidBehavior(String behavior) {
        return false;
    }

    @Override
    public void processAction(ActionIF action, double value) {
    }
}
