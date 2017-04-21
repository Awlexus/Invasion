package server.resources.mapParser.maps;


import server.map.BattlefieldThread;
import server.resources.mapParser.MapParser;
import server.map.sections.GrowthField;
import server.map.sections.Section;
import server.resources.mapParser.Map;

public class DaoisStation implements Map {
	
  private int mapSize = 16;
  private int playerCount = 2;
  private BattlefieldThread map;

  public BattlefieldThread getBattlefield() {
      map = new BattlefieldThread(mapSize, playerCount, MapParser.DAOIS_STATION);
      setSpawn();
      setAttributes();
      setNull();
      return map;
    }

    private void setSpawn() {
      // Player 1; left
      map.getSectionByPos(144).setOwner(1);
      map.getSectionByPos(144).setUnits(50);
      map.transformSectionTo(144, new GrowthField(map, map.getSectionByPos(144)));

      // Player 2; right
      map.getSectionByPos(111).setOwner(2);
      map.getSectionByPos(111).setUnits(50);
      map.transformSectionTo(111, new GrowthField(map, map.getSectionByPos(111)));
    }
    
    private void setAttributes() {
      map.transformSectionTo(5, new GrowthField(map, map.getSectionByPos(5)));

      map.transformSectionTo(16, new GrowthField(map, map.getSectionByPos(16)));

      map.transformSectionTo(53, new GrowthField(map, map.getSectionByPos(53)));
      map.transformSectionTo(60, new GrowthField(map, map.getSectionByPos(60)));

      map.transformSectionTo(195, new GrowthField(map, map.getSectionByPos(195)));
      map.transformSectionTo(202, new GrowthField(map, map.getSectionByPos(202)));

      map.transformSectionTo(239, new GrowthField(map, map.getSectionByPos(239)));

      map.transformSectionTo(250, new GrowthField(map, map.getSectionByPos(250)));
    }
    
    private void setNull() {
      int posNullFields[] = new int[]{4, 8, 22, 36, 44, 50, 57, 59, 68, 76, 85,
              86, 103, 109, 117, 121, 134, 138, 146, 152, 169, 170, 179, 187, 196, 198, 205,
              211, 219, 233, 247, 251};

      for (int posNullField : posNullFields) {
        map.getSectionByPos(posNullField).setState(Section.State.NULL);
      }
    }
}
