package client.agent;

import client.Client;
import client.agent.observer.SectionListener;
import client.gui.ClientGUI;
import client.gui.SectionGUI;
import server.map.sections.GrowthField;
import server.map.sections.Portal;

import java.util.ArrayList;

public class AgentInterface {

    /**
     * Each SectionGUI must have set an STATE
     * Only one State can be active at once
     *
     * Classic fields distribute and gain units at a normal speed
     *
     * Locked fields do not get any friendly units, however they distribute them
     *
     * Null fields are unreachable fields, the won't participate in any calculation by the Server
     */
    public enum State{
        CLASSIC, LOCKED, NULL
    }

    /**
     * reference to the GUI in which this agents actions are shown
     */
    public static ClientGUI clientGUI = null;

    /**
     * Due to the fact, that the client can only send eight actions per second,
     * they need to be buffered. The Client will then send the actions in the order
     * they were added to the List
     */
    public static ArrayList<Integer> actionQueueGr = new ArrayList<>();

    /**
     * Method which returns the clientID of this client
     * @return the clientID of the Client adapted to this AgentInterface
     */
    public static int getClientID() {
        return Client.clientId;
    }

    /**
     * Method returns the amount of SectionGUI in each row
     * The amount of rows always corresponds to the amount of columns
     * @return mapsize
     */
    public static int getMapsize() {
        return Client.mapsize;
    }

    /**
     * Method finds the SectionGUI with the given ID
     * @param id of the SectionGUI
     * @return SectionGUI with the given ID
     */
    public static SectionGUI getSectionByID(int id) {
        return clientGUI.getSectionById(id);
    }

    /**
     * Method returns the neighbours of this SectionGUI
     * @see #getNeighboursByID(int)
     */
    public static SectionGUI[] getNeighbours(SectionGUI sectionGUI) {
        return getNeighboursByID(sectionGUI.getID());
    }

    /**
     * Method returns the neighbours of this SectionGUI by its ID
     * SectionGUI[] is filled with SectionGUIs in the following order: top-right-bot-left
     * If the SectionGUI is placed near a border and therefore misses at least one neighbour,
     * a SectionGUI with the id:-1 gets returned instead
     * {@link client.gui.SectionGUI#id}
     * @param id of the SectionGUI
     * @return SectionGUI[] filled with all the neighbours of this SectionGUI
     */
    public static SectionGUI[] getNeighboursByID(int id) {
        SectionGUI g = getSectionByID(id);
        if (g != null)
            return g.clientGUI.getNeighborsById(id);
        return null;
    }

    /**
     * Method is used to get the State of a specific SectionGUI
     * {@link State}
     * {@link client.gui.SectionGUI#id}
     * @param section SectionGUI
     * @return  State of the SectionGUI
     */
    public static State getState(SectionGUI section) {
        if (section.isNull)
            return State.NULL;
        else if(section.isLocked())
            return State.LOCKED;
        else return State.CLASSIC;
    }
    /**
     * Method is used to get the State of a specific SectionGUI by its ID
     * {@link client.gui.SectionGUI#id}
     * @see #getState(SectionGUI)
     */
    public static State getState(int id) {
        return getState(clientGUI.getSectionById(id));
    }

    /**
     * Method return int[] filled with all SectionIDs, which have the given State
     * IDs are ordered chronological
     * {@link State}
     * {@link client.gui.SectionGUI}
     * @param state State
     * @return SectionGUI[] filled with Sections
     */
    public static SectionGUI[] getSectionsByState(State state) {
        ArrayList<SectionGUI> ret = new ArrayList<>();
        for (SectionGUI sectionGUI : clientGUI.getSections()) {
            if (getState(sectionGUI)==state) ret.add(sectionGUI);
        }
        return ret.toArray(new SectionGUI[ret.size()]);
    }

    /**
     * Method is used to get the Owner of a specific SectionGUI
     * {@link client.gui.SectionGUI#owner}
     * @param section SectionGUI
     * @return Owner of the SectionGUI
     */
    public static int getOwner(SectionGUI section) {
        return section.owner;
    }

    /**
     * Method is used to get the Owner of a specific SectionGUI by its ID
     * {@link client.gui.SectionGUI#id}
     * @see #getOwner(SectionGUI)
     */
    public static int getOwner(int id) {
        return getOwner(getSectionByID(id));
    }

    /**
     * Method return an int[] filled with all SectionIDs, which have the given owner
     * IDs are ordered chronologically
     * {@link client.gui.SectionGUI#owner}
     * {@link client.gui.SectionGUI}
     * @param owner ownerID
     * @return SectionGUI[] filled with Sections
     */
    public static SectionGUI[] getSectionsByOwner(int owner) {
        ArrayList<SectionGUI> ret = new ArrayList<>();
        for (SectionGUI sectionGUI : clientGUI.getSections()) {
            if (sectionGUI.owner == owner) ret.add(sectionGUI);
        }
        return ret.toArray(new SectionGUI[ret.size()]);
    }

    /**
     * Method returns the attributeType of the given SectionGUI
     * If no attribute is set on this SectionGUI, -1 will be returned
     * GrowthAttribute:1
     * PortalAttribute:2
     * {@link client.gui.SectionGUI#growthAttribute}
     * @param section SectionGUI
     * @return attributeType
     */
    public static int getAttribute(SectionGUI section) {
        // TODO: 04.04.2017 could also be portalAttribute
        return section.isGrowthAttribute()? 1:-1;
    }

    /**
     * Method returns the attributeType of the given SectionGUI by its ID
     * {@link client.gui.SectionGUI#id}
     * @see #getAttribute(SectionGUI)
     */
    public static int getAttribute(int id) {
        return getAttribute(getSectionByID(id));
    }

    /**
     * Method returns an int[] filled with all SectionIDs, which have set the given attribute
     * IDs are ordered chronological
     * {@link client.gui.SectionGUI#growthAttribute}
     * {@link client.gui.SectionGUI#id}
     * @param attribute attributeType
     * @return SectionGUI[] of Sections
     */
    public static SectionGUI[] getSectionsByAttribute(int attribute) {
        switch (attribute) {
            case GrowthField.GROWTH_FIELD_ATTRIBUTE:
            case Portal.PORTAL_FIELD_ATTRIBUTE:
                break;
            default:
                throw new IllegalArgumentException("Invalid Attribute");
        }
        ArrayList<SectionGUI> ret = new ArrayList<>();
        for (SectionGUI sectionGUI : clientGUI.getSections()) {
            if (getAttribute(sectionGUI)==attribute) ret.add(sectionGUI);
        }
        return ret.toArray(new SectionGUI[ret.size()]);
    }

    /**
     * Method is used to get the UnitCount of a specific SectionGUI
     * @param section SectionGUI
     * {@link client.gui.SectionGUI#unitValue}
     * @return UnitCount of the SectionGUI
     */
    public static int getUnitCount(SectionGUI section) {
        return section.unitValue;
    }

    /**
     * Method is used to get the UnitCount of a specific SectionGUI by its ID
     * {@link client.gui.SectionGUI#id}
     * @see #getUnitCount(SectionGUI)
     */
    public static int getUnitCount(int id) {
        return getUnitCount(getSectionByID(id));
    }

    /**
     * Method counts the total UnitCount of any player on this map
     * @param owner playerID
     * @return total UnitCount of the player
     */
    public static int getTotalUnitCount(int owner) {
        int ret = 0;
        for (SectionGUI sectionGUI : clientGUI.getSections()) {
            if (sectionGUI.owner == owner) ret+=getUnitCount(sectionGUI);
        }
        return ret;
    }

    /**
     * {@link client.gui.SectionGUI#id}
     * {@link State}
     * Performing an action onto an Section means to change its State
     * Only Classic and Locked Sections can be switched
     */
    public static void addAction(int id) {
        if (Client.actionQueue.size() >= 200) throw new RuntimeException("ActionQueueBufferOverloadException");
        Client.actionQueue.add(id);
    }

    /**
     * Method which adds an action to the queue
     * @see #addAction(int)
     * @param section SectionGUI, whose State needs to be changed
     */
    public static void addAction(SectionGUI section) {
        addAction(section.getID());
    }

    /**
     * {@link client.gui.SectionGUI#id}
     * @see #addAction(int)
     */
    public static void addAction(int[] ids) {
        for (int id : ids) {
            addAction(id);
        }
    }

    /**
     * Method which can add multiple actions at once
     * @see #addAction(int)
     * @param sections SectionGUIs, whose State needs to be changed
     */
    public static void addAction(SectionGUI[] sections) {
        for (SectionGUI section : sections) {
            addAction(section.getID());
        }
    }

    /**
     * {@link client.gui.SectionGUI#id}
     * @see #addSectionListener(SectionListener, SectionGUI)
     */
    public static void addSectionListener(SectionListener listener, int id) {
        addSectionListener(listener, getSectionByID(id));
    }

    /**
     * Method is used to add a Listener to a Section
     * @param listener Listener to be added
     * @param section SectionGUI
     */
    public static void addSectionListener(SectionListener listener, SectionGUI section) {
        section.addListener(listener);
    }
}
