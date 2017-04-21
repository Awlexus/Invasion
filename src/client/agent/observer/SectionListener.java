package client.agent.observer;

import java.util.EventListener;

public interface SectionListener extends EventListener{

    /**
     * invoked when the owner of a Section changes
     */
    void ownerChanged(SectionEvent e);

    /**
     * invoked when the unitValue of a Section changes
     */
    void unitValueChanged(SectionEvent e);

    /**
     * invoked when the State of a Section changes
     */
    void stateChanged(SectionEvent e);
}
