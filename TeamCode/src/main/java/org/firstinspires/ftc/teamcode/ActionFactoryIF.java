package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public interface ActionFactoryIF {
    enum ActionType {
        BEHAVIOR,
        PARAMETER
    }

    ActionIF actionInstance(ActionType actionType) throws ConfigurationException;
}
