package client.gui;


import client.Client;
import client.agent.AgentInterface;
import client.agent.observer.SectionEvent;
import client.agent.observer.SectionListener;
import client.resources.ImageLibrary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

public class SectionGUI extends JPanel implements MouseListener{

    // each SectionGUI has an id depending on its position on the screen
    // from 0 (top,left) to mapsize²-1 (bot,right)
    private int id;

    // if this is true, then the field basically doesn't exist
    // this value will only be set once, nullFields have a black background rather than an imageIcon
    // its imageIcon won't be recalculated ever
    public boolean isNull = false;

    // if a field is locked, it will no longer get units by friendly neighbors
    // the lockedState also determines the imageIcon of this SectionGUI
    private boolean locked = false;

    // each SectionGUI belongs to an player or the computer (owner = 0)
    // the owner also determines the imageIcon of this SectionGUI
    public int owner = 0;

    // unitValue tells more or less how many units are in an area
    // however this isn't the exact value
    // the true value is between (unitValue-1)*20 and unitValue*20
    // ex: for a unitValue of 2 the exact Value seen by the server_old is between 20-40
    public int unitValue = 0;

    // colorBits are used to calculate the Image of an SectionGUI
    // each bit of colorBits stands for an information
    // the 8 bits are divided by top,right,bot.left
    // each pair tells if more units are leaving or entering the field,
    // and how big the difference is
    private int colorBits = -1;

    // if this boolean is false, there is no need to recalculate the Image of
    // this SectionGUI as it definitely won't change
    private boolean reCalcNeeded = true;

    // if this boolean is false, there is no need to force the neighbors of
    // this SectionGUI to recalculate as this fields changes have no impact on them
    private boolean neighborsReCalcNeeded = false;

    // id of the last pathIcon that has been assigned to this SectionGUI
    // used in order to avoid unnecessary steps
    private int[] lastPathId = new int[4];

    // last attributes of this SectionGUI
    // used in order to avoid unnecessary steps
    private boolean[] lastAttributes = new boolean[] {false};

    // tells if this SectionGUI has already been recalculated
    // this value will be used in order to avoid unnecessary steps
    boolean reCalced = false;

    // reference to the clientGUI
    // will be used to get the neighbors of this SectionGUI
    public ClientGUI clientGUI;

    // Observer added to a SectionGUI will invoke in the following situations:
    // owner changed
    // unitValue changed
    // State changed
    private ArrayList<SectionListener> observer = new ArrayList<>();

    /*
      Attributes
     */

    public static final int GROWTH_ATTRIBUTE = 1;
    public static final int PORTAL_ATTRIBUTE = 2;

    private boolean growthAttribute = false;

    private boolean portalAttribute = false;



    /**
     * SectionGUI provides different methods in order to set important values
     * for the calculation of the imageIcon of this SectionGUI
     * @param id of this SectionGUI
     * @param gui reference to it's clientGUI
     */
    SectionGUI(int id, ClientGUI gui) {
        super();
        this.id = id;
        this.clientGUI = gui;
        // Layout allows this SectionGUI to overlap added components
        this.setLayout(new OverlayLayout(this));
        addMouseListener(this);
        reCalcNeeded = true;
    }

    public int getID() {
        return this.id;
    }

    /**
     * sets the isNullState to this SectionGUI
     * nullFields don't have an imageIcon, they will be painted black
     * this State won't change during the game, it will only be set once
     */
    void setIsNull(boolean isNull) {
        this.isNull = isNull;
        if (isNull)
            setBackground(new Color(0));
    }

    /**
     * @return the current lockedState
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * sets the new lockedState to this SectionGUI
     * if the lockedState changes it will recalculate the imageIcon of this SectionGUI
     * @param locked new lockedState
     */
    void setLocked(boolean locked) {
        if (this.locked == locked)
            return;
        this.locked = locked;
        reCalcNeeded = true;
        neighborsReCalcNeeded = true;

        AgentInterface.State oldState = this.locked? AgentInterface.State.CLASSIC: AgentInterface.State.LOCKED;
        SectionEvent e = new SectionEvent(this, owner, unitValue, oldState);
        for (SectionListener sectionListener : observer) {
            sectionListener.stateChanged(e);
        }
    }

    /**
     * @return id of this SectionGUI
     */
    int getId() {
        return id;
    }

    /**
     * set the new owner to this SectionGUI
     * if the owner changes it will recalculate the imageIcon of this SectionGUI
     * @param owner new owner
     */
    void setOwner(int owner) {
        if (this.owner == owner)
            return;
        int oldOwner = this.owner;

        this.owner = owner;
        reCalcNeeded = true;
        neighborsReCalcNeeded = true;

        AgentInterface.State state = this.locked? AgentInterface.State.LOCKED: AgentInterface.State.CLASSIC;
        SectionEvent e = new SectionEvent(this, oldOwner, unitValue, state);
        for (SectionListener sectionListener : observer) {
            sectionListener.ownerChanged(e);
        }
    }

    /**
     * sets the unitValue of this SectionGUI
     * @param unitValue to be set
     */
    void setUnitData(int unitValue) {
        if (this.unitValue == unitValue)
            return;
        int oldUnitValue = this.unitValue;

        this.unitValue = unitValue;
        reCalcNeeded = true;

        AgentInterface.State state = this.locked? AgentInterface.State.LOCKED: AgentInterface.State.CLASSIC;
        SectionEvent e = new SectionEvent(this, owner, oldUnitValue, state);
        for (SectionListener sectionListener : observer) {
            sectionListener.unitValueChanged(e);
        }
    }

    /**
     * sets the new colorBits
     *
     * colorBits is an integer seen as an bit[]
     * each bit stands for an information needed in order to calculate
     * the right (color-/speed-)image of this SectionGUI
     *
     * 4 pairs, each pair stores data for top, right, bot or left
     *
     * 2 bits, the first bit stands for the direction of the higher unitCount moving
     * between to SectionGUIs, the second for it's value and therefor the speed of the units
     *
     * @param colorBits to be set
     */
    void setColorBits(int colorBits) {
        if (colorBits < 0)
            colorBits+=256;
        if (colorBits == this.colorBits)
            return;
        this.colorBits = colorBits;
        reCalcNeeded = true;
        neighborsReCalcNeeded = true;
    }

    /**
     * sets the last pathId of this SectionGUI
     * @param lastPathId to be set
     */
    public void setLastPathId(int[] lastPathId) {
        this.lastPathId = lastPathId;
    }

    /**
     * returns the lastPathId of this SectionGUI
     * method called by ImageLibrary in order to compare it with
     * the current pathId and thereby avoid unnecessary steps
     * @return lastPathId
     */
    public int[] getLastPathId() {
        return lastPathId;
    }

    /**
     * methods sets the current attributes of this SectionGUI
     * data is needed to display the right image
     * @param data attribute which is active
     */
    void setAttributeData(int data) {
        switch (data) {
            // growthAttribute
            case 1:
                growthAttribute = true;
                break;
            // portalAttribute
            case 2:
                portalAttribute = true;
                break;
        }

        if (!Arrays.equals(lastAttributes, new boolean[] {growthAttribute, portalAttribute}))
            reCalcNeeded = true;
    }

    public boolean isGrowthAttribute() {
        return growthAttribute;
    }

    public boolean isPortalAttribute() {
        return portalAttribute;
    }

    /**
     * resets all Attributes to its original value, most likely false
     */
    void resetAttributes() {
        growthAttribute = false;
        portalAttribute = false;
    }

    /**
     * informs this section that a neighbors image changed, therefore this Section will
     * recalculate it's imageIcon as well if it hasn't already been recalculated
     */
    private void neighborChanged() {
        if (!reCalced)
            reCalcImage();
    }

    /**
     * Method which will be called once per turn
     * it forces this SectionGUI's imageIcon to be recalculated if needed
     */
    void updateImage() {
        if (isNull)
            return;
        if (reCalcNeeded) {
            reCalcImage();
            if (neighborsReCalcNeeded)
                for (SectionGUI neighbor : clientGUI.getNeighborsById(this.id)) {
                    neighbor.neighborChanged();
                }
        }
        reCalcNeeded = false;
        neighborsReCalcNeeded = false;
    }

    /**
     * method recalculates the image of this section
     * if the image was already calculated this turn it won't redo so
     *
     * method gets an ImageIcon[] from ImageLibrary and applies them
     * to different JLabels in order to add them to this SectionGUI
     */
    void reCalcImage() {
        // nullFields have no imageIcon
        if (this.isNull)
            return;

        // if the image was already calculated this turn, it won't redo so
        if (reCalced)
            return;

        boolean[] attributes = new boolean[]{growthAttribute, portalAttribute};
        ImageIcon[] newIcons = ImageLibrary.getImage(this.id, this.locked, this.owner, this.colorBits, attributes);
        lastAttributes = attributes;


        if (newIcons != null) {
            this.removeAll();
            for (ImageIcon icon : newIcons) {
                if (icon != null)
                    add(new JLabel(icon));
            }
            revalidate();
        }

        // informs that this image has been calculated
        reCalced = true;
    }

    public void addListener(SectionListener listener) {
        observer.add(listener);
    }

    public void removeListener(SectionListener listener) {
        observer.remove(listener);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * if the mouseClick was released within this SectionGUI
     * a signal will be send to the client and from there to the server_old
     * the signal is different for each possible type of action by the user
     *
     * signal = new byte[3]
     *
     * the first byte stands for the player performing the action, it will be added from the client
     * the second byte stands for the type of action performed
     *      1 is the lock or unlock action
     *      more will be added later
     * the third byte stand for the SectionGUI's id on which the action was performed
     *
     * @param e Event
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        Client.actionQueue.add(id);
        Client.writeData();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
