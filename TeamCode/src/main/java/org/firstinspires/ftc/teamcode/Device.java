package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class Device extends Component implements DeviceIF {
    public Device(OpMode opMode) {
        super(opMode);
    }

    @Override
    public boolean isValidBehavior(String behavior) {
        return false;
    }

    @Override
    public void processAction(ActionIF action, double value) {
    }
}
