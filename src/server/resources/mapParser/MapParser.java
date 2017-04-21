package server.resources.mapParser;

import server.map.BattlefieldThread;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MapParser {

    public static final String SIDESHOW = "Sideshow";
    public static final String ARENA = "Arena";
    public static final String DAOIS_STATION = "DaoisStation";
    public static final String TEST_MAP = "TestMap";
    public static final String PORTAL_TEST_MAP = "PortalTestMap";
    public static final String BEY_BLADE = "BeyBlade";
    public static final String TEST_MAP2 = "TestMap1";

    public static BattlefieldThread getMap(String className) {
        switch (className) {
            case SIDESHOW:
            case ARENA:
            case DAOIS_STATION:
            case TEST_MAP:
            case PORTAL_TEST_MAP:
            case BEY_BLADE:
            case TEST_MAP2:
                break;
            default:
                throw new IllegalArgumentException("NoSuchMapException");
        }
        Map map = findMap(className);
        return map.getBattlefield();
    }

    private static Map findMap(String className) {
        Map map = null;
        try {
            Class<?> clazz = Class.forName("server.resources.mapParser.maps."+className);
            Constructor<?> cont = clazz.getConstructor();
            Object object = cont.newInstance();
            map = ((Map) object);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException ignored) {}
        return map;
    }
}
