package client.agent.observer;

import client.agent.AgentInterface;
import client.gui.SectionGUI;

import java.util.EventObject;

public class SectionEvent extends EventObject{

    /** {@link client.gui.SectionGUI#owner} */
    private int oldOwner;

    /** {@link client.gui.SectionGUI#unitValue} */
    private int oldUnitValue;

    /** {@link client.agent.AgentInterface.State} */
    private AgentInterface.State oldState;

    public SectionEvent(SectionGUI source, int oldOwner, int oldUnitValue, AgentInterface.State oldState) {
        super(source);
        this.oldOwner = oldOwner;
        this.oldUnitValue = oldUnitValue;
        this.oldState = oldState;
    }

    public int getOldOwner() {
        return oldOwner;
    }

    public int getOldUnitValue() {
        return oldUnitValue;
    }

    public AgentInterface.State getOldState() {
        return oldState;
    }

    public int getOwner() {
        return ((SectionGUI) source).owner;
    }

    public int getUnitValue() {
        return ((SectionGUI) source).unitValue;
    }

    public AgentInterface.State getState() {
        if (((SectionGUI) source).isNull)
            return AgentInterface.State.NULL;
        else if(((SectionGUI) source).isLocked())
            return AgentInterface.State.LOCKED;
        else return AgentInterface.State.CLASSIC;
    }
}