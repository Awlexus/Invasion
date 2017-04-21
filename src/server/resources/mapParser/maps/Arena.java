package server.resources.mapParser.maps;

import server.map.BattlefieldThread;
import server.resources.mapParser.MapParser;
import server.map.sections.GrowthField;
import server.map.sections.Section;
import server.resources.mapParser.Map;


public class Arena implements Map {

    private int mapSize = 11;
    private int playerCount = 2;
    private BattlefieldThread map;


    public BattlefieldThread getBattlefield() {
        map = new BattlefieldThread(mapSize, playerCount, MapParser.ARENA);
        setSpawn();
        setAttributes();
        setNull();
        return map;
    }

    private void setSpawn() {
        // Player 1; left
        map.getSectionByPos(56).setOwner(1);
        map.getSectionByPos(56).setUnits(50);
        map.transformSectionTo(56, new GrowthField(map, map.getSectionByPos(56)));

        // Player 2; right
        map.getSectionByPos(64).setOwner(2);
        map.getSectionByPos(64).setUnits(50);
        map.transformSectionTo(64, new GrowthField(map, map.getSectionByPos(64)));
    }

    private void setAttributes() {
        map.transformSectionTo(5, new GrowthField(map, map.getSectionByPos(5)));

        map.transformSectionTo(13, new GrowthField(map, map.getSectionByPos(13)));
        map.transformSectionTo(19, new GrowthField(map, map.getSectionByPos(19)));

        map.transformSectionTo(58, new GrowthField(map, map.getSectionByPos(58)));
        map.transformSectionTo(62, new GrowthField(map, map.getSectionByPos(62)));

        map.transformSectionTo(92, new GrowthField(map, map.getSectionByPos(92)));
        map.transformSectionTo(94, new GrowthField(map, map.getSectionByPos(94)));

        map.transformSectionTo(110, new GrowthField(map, map.getSectionByPos(110)));
        map.transformSectionTo(120, new GrowthField(map, map.getSectionByPos(120)));
    }

    private void setNull() {
        int posNullFields[] = new int[] {12, 16, 20, 24, 30, 57, 63, 78, 86, 91, 95, 111, 119};
        for (int posNullField : posNullFields) {
            map.getSectionByPos(posNullField).setState(Section.State.NULL);
        }
    }
}
