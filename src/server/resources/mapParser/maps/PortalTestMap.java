package server.resources.mapParser.maps;

import server.map.BattlefieldThread;
import server.map.sections.GrowthField;
import server.map.sections.Portal;
import server.map.sections.Section;
import server.resources.mapParser.Map;
import server.resources.mapParser.MapParser;

public class PortalTestMap implements Map{

    private int mapSize = 3;
    private int playerCount = 2;
    private BattlefieldThread map;

    @Override
    public BattlefieldThread getBattlefield() {
        map = new BattlefieldThread(mapSize, playerCount, MapParser.PORTAL_TEST_MAP);
        setSpawn();
        setAttributes();
        setNull();
        return map;
    }

    private void setSpawn() {
        // Player 1; left
        map.getSectionByPos(0).setOwner(1);
        map.getSectionByPos(0).setUnits(50);
        map.transformSectionTo(0, new GrowthField(map, map.getSectionByPos(0)));
    }

    private void setAttributes() {
        map.transformSectionTo(2, new Portal(map, map.getSectionByPos(2)));
        map.transformSectionTo(6, new Portal(map, map.getSectionByPos(6)));
        ((Portal) map.getSectionByPos(2)).setBoundedPortal(((Portal) map.getSectionByPos(6)));
        ((Portal) map.getSectionByPos(6)).setBoundedPortal(((Portal) map.getSectionByPos(2)));
    }

    private void setNull() {
        int posNullFields[] = new int[] {3,4,5};
        for (int posNullField : posNullFields) {
            map.getSectionByPos(posNullField).setState(Section.State.NULL);
        }
    }
}
