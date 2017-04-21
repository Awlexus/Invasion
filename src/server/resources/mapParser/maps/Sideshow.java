package server.resources.mapParser.maps;

import server.map.BattlefieldThread;
import server.resources.mapParser.MapParser;
import server.map.sections.GrowthField;
import server.map.sections.Section;
import server.resources.mapParser.Map;



public class Sideshow implements Map {

    private int mapSize = 16;
    private int playerCount = 4;
    private BattlefieldThread map;

    public BattlefieldThread getBattlefield() {
        map = new BattlefieldThread(mapSize, playerCount, MapParser.SIDESHOW);
        setSpawn();
        setAttributes();
        setNull();
        return map;
    }


    private void setSpawn() {
        // Player 1; top
        map.getSectionByPos(25).setOwner(1);
        map.getSectionByPos(25).setUnits(50);
        map.transformSectionTo(25, new GrowthField(map, map.getSectionByPos(25)));

        // Player 2; right
        map.getSectionByPos(110).setOwner(2);
        map.getSectionByPos(110).setUnits(50);
        map.transformSectionTo(110, new GrowthField(map, map.getSectionByPos(110)));

        // Player 3; left
        map.getSectionByPos(145).setOwner(3);
        map.getSectionByPos(145).setUnits(50);
        map.transformSectionTo(145, new GrowthField(map, map.getSectionByPos(145)));

        // Player 4; bot
        map.getSectionByPos(230).setOwner(4);
        map.getSectionByPos(230).setUnits(50);
        map.transformSectionTo(230, new GrowthField(map, map.getSectionByPos(230)));
    }

    private void setAttributes() {

        map.transformSectionTo(0, new GrowthField(map, map.getSectionByPos(0)));
        map.transformSectionTo(15, new GrowthField(map, map.getSectionByPos(15)));

        map.transformSectionTo(120, new GrowthField(map, map.getSectionByPos(120)));

        map.transformSectionTo(135, new GrowthField(map, map.getSectionByPos(135)));

        map.transformSectionTo(240, new GrowthField(map, map.getSectionByPos(240)));
        map.transformSectionTo(255, new GrowthField(map, map.getSectionByPos(255)));
    }

    private void setNull() {
        int posNullFields[] = new int[]{3, 11,  19, 27, 30, 45, 48, 49, 51,
                60, 78, 79, 90, 102, 105, 150, 153, 165, 176, 177,
                195, 204, 206, 207, 210, 225, 228, 236, 244, 252};

        for (int posNullField : posNullFields) {
            map.getSectionByPos(posNullField).setState(Section.State.NULL);
        }
    }

}
