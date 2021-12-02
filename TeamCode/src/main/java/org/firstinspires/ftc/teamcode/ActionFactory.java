package org.firstinspires.ftc.teamcode;

public class ActionFactory implements ActionFactoryIF {
    public ActionIF actionInstance(ActionType actionType) throws ConfigurationException {
        switch(actionType) {
            case BEHAVIOR:
                return new BehaviorAction();
            case PROPERTY:
                return new PropertyAction();
            default:
                throw new ConfigurationException("Invalid ActionType");
        }
    }
}
