package server.resources.mapParser.maps;

import server.map.BattlefieldThread;
import server.map.sections.Portal;
import server.resources.mapParser.MapParser;
import server.map.sections.GrowthField;
import server.resources.mapParser.Map;

public class TestMap implements Map {
    private int mapSize = 15;
    private int playerCount = 3;
    private BattlefieldThread map;
    private byte[] nullFields;

    public BattlefieldThread getBattlefield() {
        map = new BattlefieldThread(mapSize, playerCount, MapParser.TEST_MAP);
        setSpawn();
        setAttributes();
        setNull();
        setOther();
        return map;
    }


    private void setSpawn() {
        map.getSectionByPos(48).setOwner(1);
        map.getSectionByPos(48).setUnits(500);
        map.transformSectionTo(48, new GrowthField(map, map.getSectionByPos(48)));

        map.getSectionByPos(112).setOwner(2);
        map.getSectionByPos(112).setUnits(500);
        map.transformSectionTo(112, new GrowthField(map, map.getSectionByPos(112)));

        map.getSectionByPos(176).setOwner(3);
        map.getSectionByPos(176).setUnits(500);
        map.transformSectionTo(176, new GrowthField(map, map.getSectionByPos(176)));
    }

    private void setAttributes() {
        map.transformSectionTo(16, new Portal(map, map.getSectionByPos(16)));
        map.transformSectionTo(28, new GrowthField(map, map.getSectionByPos(28)));
        map.transformSectionTo(196, new GrowthField(map, map.getSectionByPos(196)));
        map.transformSectionTo(208, new Portal(map, map.getSectionByPos(208)));
    }

    private void setNull() {

    }

    private void setOther() {
    }
}
