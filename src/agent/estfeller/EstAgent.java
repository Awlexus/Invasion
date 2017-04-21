package agent.estfeller;

import agent.Agent;
import client.agent.AgentInterface;
import client.agent.observer.SectionEvent;
import client.agent.observer.SectionListener;
import client.gui.SectionGUI;


public class EstAgent extends Agent{
    @Override
    public void run() {
    }

    @Override
    public void init() {
        System.out.println("Agent started...");
    }

    @Override
    public void setup() {
        SectionGUI[] s = AgentInterface.getSectionsByOwner(AgentInterface.getClientID());
        AgentInterface.getNeighbours(s[0])[0].addListener(new SectionListener() {
            @Override
            public void ownerChanged(SectionEvent e) {
                System.out.println("Owner: "+e.getOldOwner()+" to "+e.getOwner());
            }

            @Override
            public void unitValueChanged(SectionEvent e) {
                System.out.println("Unit: "+e.getOldUnitValue()+" to "+e.getUnitValue());
            }

            @Override
            public void stateChanged(SectionEvent e) {
                System.out.println("State: "+e.getOldState()+" to "+e.getState());
            }
        });
    }
}