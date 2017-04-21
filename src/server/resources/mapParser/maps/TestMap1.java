package server.resources.mapParser.maps;

import server.resources.mapParser.Map;
import server.map.BattlefieldThread;
import server.map.sections.GrowthField;
import server.resources.mapParser.Map;
import server.resources.mapParser.Map;
import server.resources.mapParser.MapParser;
import server.map.sections.Section;

public class TestMap1 implements Map {

    private int mapSize = 15;
    private int playerCount = 4;
    private BattlefieldThread map;

    public BattlefieldThread getBattlefield() {
        map = new BattlefieldThread(mapSize, playerCount, MapParser.TEST_MAP2);
        setSpawn();
        setAttributes();
        setNull();
        return map;
    }
    private void setSpawn() {
        map.getSectionByPos(96).setOwner(1);
        map.getSectionByPos(96).setUnits(50);
        map.transformSectionTo(96, new GrowthField(map, map.getSectionByPos(96)));
        map.getSectionByPos(98).setOwner(2);
        map.getSectionByPos(98).setUnits(50);
        map.transformSectionTo(98, new GrowthField(map, map.getSectionByPos(98)));
        map.getSectionByPos(126).setOwner(3);
        map.getSectionByPos(126).setUnits(50);
        map.transformSectionTo(126, new GrowthField(map, map.getSectionByPos(126)));
        map.getSectionByPos(128).setOwner(4);
        map.getSectionByPos(128).setUnits(50);
        map.transformSectionTo(128, new GrowthField(map, map.getSectionByPos(128)));

    }

    private void setAttributes() {
        map.transformSectionTo(51, new GrowthField(map, map.getSectionByPos(51)));
        map.transformSectionTo(101, new GrowthField(map, map.getSectionByPos(101)));
        map.transformSectionTo(123, new GrowthField(map, map.getSectionByPos(123)));
        map.transformSectionTo(173, new GrowthField(map, map.getSectionByPos(173)));

    }
    private void setNull() {
        int posNullFields[] = new int[] {16 , 18 , 19 , 20 , 21 , 22 , 23 , 24 , 25 , 26 , 28 , 46 , 52 , 58 , 61 , 65 , 66 , 73 , 76 , 80 , 83 , 84 , 85 , 88 , 91 , 95 , 97 , 100 , 103 , 106 , 108 , 111 , 112 , 113 , 116 , 118 , 121 , 124 , 127 , 129 , 133 , 136 , 139 , 140 , 141 , 144 , 148 , 151 , 158 , 159 , 163 , 166 , 172 , 178 , 196 , 198 , 199 , 200 , 201 , 202 , 203 , 204 , 205 , 206 , 208};
        for(int posNullField : posNullFields) {
            map.getSectionByPos(posNullField).setState(Section.State.NULL);
        }
    }
}