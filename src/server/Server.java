package server;

import server.map.BattlefieldThread;
import server.map.PlayerThread;
import server.resources.mapParser.MapParser;
import server.resources.values.Values;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {

    private static final int PORT = 65535;

    public static final String CONST_LOGIN = "type_login";
    public static final String CONST_PLAY = "type_play";

    private static final Hashtable<BattlefieldThread, ScheduledExecutorService> battlefieldSet = new Hashtable<>();

    public static void main(String[] args) {
        // Process-ID
        System.out.println(ManagementFactory.getRuntimeMXBean().getName());

        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT);
            while (true) {
                Socket client;
                try {
                    client = server.accept();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(client.getInputStream()));
                    // reads the type of the request (Either Login- or PlayRequest)
                    String type = in.readLine();

                    if (type.equals(CONST_LOGIN)) {
                        // start LoginThread
                    } else if (type.equals(CONST_PLAY)) {
                        // reads what map the users wants to play on
                        String mapName = in.readLine();

                        synchronized (battlefieldSet) {
                            // searches for a free spot in all already created free Battlefields of this type
                            checkOpenGame:
                            {
                                // searches for a spot in all already created Battlefields
                                for (BattlefieldThread battlefield : battlefieldSet.keySet()) {
                                    if (battlefield.getMapName().equals(mapName) &&
                                            !battlefield.isFull()) { // spot found
                                        // adds the PlayerThread to the Battlefield
                                        battlefield.addPlayer(new PlayerThread(client, battlefield));
                                        // checks if the Battlefield is now full, and therefore ready to start
                                        if (battlefield.isFull())
                                            // submits the Battlefield to an ExecutorService
                                            (battlefieldSet.get(battlefield)).
                                                    scheduleAtFixedRate(battlefield, Values.battlefieldCalculateDelay,
                                                            Values.battlefieldCalculatePeriod, TimeUnit.MILLISECONDS);

                                        break checkOpenGame;
                                    }
                                }

                                // if no spot was found, a new Battlefield is created and added to the List

                                BattlefieldThread newMap = MapParser.getMap(mapName);
                                // creates Executor which will later run this BattlefieldThread
                                battlefieldSet.put(newMap, Executors.newSingleThreadScheduledExecutor());
                                // adds the PlayerThread to the Battlefield
                                newMap.addPlayer(new PlayerThread(client, newMap));
                            }
                        }
                    }
                } catch (IOException e) {
                    handleException(e);
                } finally {
                    System.out.println("Client added. Running games: "+battlefieldSet.size());
                }
            }
        } catch (IOException e) {
            handleException(e);
        } finally {
            System.out.println("ServerDown_ERR");
            try {if (server != null) server.close();} catch (Exception ignored) {}
        }
    }

    public static void killExecutor(BattlefieldThread battlefield) {
        synchronized (battlefieldSet) {
            battlefieldSet.get(battlefield).shutdown();
            battlefieldSet.remove(battlefield);
        }
        System.out.println("A Game has been completed. Running games: "+battlefieldSet.size());
    }

    /**
     * prints information about an exception
     * @param e exception
     */
    private static void handleException(Exception e) {
        System.out.println("Exception: ");
        e.getStackTrace();
        System.out.println(e.getClass());
        System.out.println(e.getMessage());
    }

}
