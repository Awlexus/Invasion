package server.resources.mapParser.maps;

import server.map.BattlefieldThread;
import server.map.sections.GrowthField;
import server.map.sections.Portal;
import server.map.sections.Section;
import server.resources.mapParser.Map;
import server.resources.mapParser.MapParser;

public class BeyBlade implements Map {

	private int mapSize = 15;
	private int playerCount = 4;
	private BattlefieldThread map;

	@Override
	public BattlefieldThread getBattlefield() {
		map = new BattlefieldThread(mapSize, playerCount, MapParser.BEY_BLADE);
		setSpawn();
		setAttributes();
		setNull();
		return map;
	}

	private void setSpawn() {
		// Player 1;
		map.getSectionByPos(0).setOwner(1);
		map.getSectionByPos(0).setUnits(50);
		map.transformSectionTo(0, new GrowthField(map, map.getSectionByPos(0)));

		// Player 2;
		map.getSectionByPos(14).setOwner(2);
		map.getSectionByPos(14).setUnits(50);
		map.transformSectionTo(14, new GrowthField(map, map.getSectionByPos(14)));

		// Player 3;
		map.getSectionByPos(210).setOwner(3);
		map.getSectionByPos(210).setUnits(50);
		map.transformSectionTo(210, new GrowthField(map, map.getSectionByPos(210)));

		// Player 4;
		map.getSectionByPos(224).setOwner(4);
		map.getSectionByPos(224).setUnits(50);
		map.transformSectionTo(224, new GrowthField(map, map.getSectionByPos(224)));
	}

	private void setAttributes() {

		map.transformSectionTo(22, new GrowthField(map, map.getSectionByPos(22)));

		map.transformSectionTo(106, new GrowthField(map, map.getSectionByPos(106)));

		map.transformSectionTo(112, new GrowthField(map, map.getSectionByPos(112)));

		map.transformSectionTo(118, new GrowthField(map, map.getSectionByPos(118)));

		map.transformSectionTo(202, new GrowthField(map, map.getSectionByPos(202)));

		map.transformSectionTo(6, new Portal(map, map.getSectionByPos(6)));
		map.transformSectionTo(8, new Portal(map, map.getSectionByPos(8)));
		map.transformSectionTo(90, new Portal(map, map.getSectionByPos(90)));
		map.transformSectionTo(104, new Portal(map, map.getSectionByPos(104)));
		map.transformSectionTo(120, new Portal(map, map.getSectionByPos(120)));
		map.transformSectionTo(134, new Portal(map, map.getSectionByPos(134)));
		map.transformSectionTo(216, new Portal(map, map.getSectionByPos(216)));
		map.transformSectionTo(218, new Portal(map, map.getSectionByPos(218)));
		((Portal) map.getSectionByPos(6)).setBoundedPortal(((Portal) map.getSectionByPos(216)));
		((Portal) map.getSectionByPos(8)).setBoundedPortal(((Portal) map.getSectionByPos(218)));
		((Portal) map.getSectionByPos(90)).setBoundedPortal(((Portal) map.getSectionByPos(104)));
		((Portal) map.getSectionByPos(104)).setBoundedPortal(((Portal) map.getSectionByPos(90)));
		((Portal) map.getSectionByPos(120)).setBoundedPortal(((Portal) map.getSectionByPos(134)));
		((Portal) map.getSectionByPos(134)).setBoundedPortal(((Portal) map.getSectionByPos(120)));
		((Portal) map.getSectionByPos(216)).setBoundedPortal(((Portal) map.getSectionByPos(6)));
		((Portal) map.getSectionByPos(218)).setBoundedPortal(((Portal) map.getSectionByPos(8)));
	}

	private void setNull() {
		int posNullFields[] = new int[] { 7, 16, 17, 21, 23, 28, 33, 34, 43, 50, 51, 57, 67, 68, 72, 86, 91, 94, 96, 98,
				101, 103, 105, 109, 115, 119, 121, 123, 126, 128, 130, 133, 138, 152, 156, 157, 167, 173, 174, 181,
				190,191,196,201,203,207,208,217 };
		for (int posNullField : posNullFields) {
			map.getSectionByPos(posNullField).setState(Section.State.NULL);
		}
	}

}
