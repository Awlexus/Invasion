package server.map.sections;

import server.map.BattlefieldThread;
import server.resources.values.Values;


public class GrowthField extends Section {

    public static final int GROWTH_FIELD_ATTRIBUTE = 1;

    public GrowthField (BattlefieldThread map, Section s) {
        super(map);
        super.applySection(s);
    }

    public byte[] getAttributes() {
        byte[] ret;
        byte[] org = super.getAttributes();

        ret = new byte[org.length+1];

        System.arraycopy(org, 0, ret, 0, org.length);
        ret[ret.length-1] = GROWTH_FIELD_ATTRIBUTE;

        return ret;
    }
    public void grow() {
        if (owner != 0)
            unitCount+= Values.growAmount*Values.growthFieldBuff;
    }
}
