package server.map.sections;

import server.map.BattlefieldThread;


public class Portal extends Section {

    public static final int PORTAL_FIELD_ATTRIBUTE = 2;

    private Portal boundedPortal = null;

    public Portal(BattlefieldThread map, Section s) {
        super(map);
        super.applySection(s);
    }

    /**
     * sets the bounded Portal
     * @param p bounded Portal
     */
    public void setBoundedPortal(Portal p) {
        this.boundedPortal = p;
    }

    /**
     * returns the Attribute of this Portal by calling the super method
     * additionally it adds @final int PORTAL_FIELD_ATTRIBUTE
     * @return the attributes of this Portal
     */
    public byte[] getAttributes() {
        byte[] ret;
        byte[] org = super.getAttributes();

        ret = new byte[org.length+1];

        System.arraycopy(org, 0, ret, 0, org.length);
        ret[ret.length-1] = PORTAL_FIELD_ATTRIBUTE;

        return ret;
    }

    /**
     * a Portal's State can not be changed, for now
     */
    public void setState (State state) {
        this.state = State.CLASSIC;
    }

    /**
     * a Portal's unitCount can't grow
     */
    public void grow() {}

    /**
     * distributes the units by calling super method
     * additionally gives half of the remaining units to the bounded Portal
     */
    public void distribute() {
        // normal distribution to all neighbors
        super.distribute();

        // distribution to the boundedPortal
        // half of the remaining units will be send to the other Portal
        boundedPortal.addBufferedUnitCount(owner, unitCount/2);
        // subtract the unit send to the other Portal
        this.unitCount/=2;
    }
}
