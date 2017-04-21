package client;


import agent.Agent;
import client.gui.ClientGUI;
import server.Server;
import server.resources.mapParser.MapParser;
import sun.misc.IOUtils;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executor;

public class Client {

    // IP-Address and Port to the Server
    private static final String HOST = "localhost";
    private static final int PORT = 65535;

    // Map
    private static final String _MAP = MapParser.BEY_BLADE;


    // Reference to the ClientGUI, which will show the game
    private static ClientGUI CLIENT_GUI;

    // Client-Object for the standard communication
    private static Socket CLIENT;
    // The Agent is the AI playing behind this Client
    private static Agent AGENT;
    // Executor that is constantly calling the run() method of the Agent
    private static Executor AGENT_EXEC;

    // boolean keeps track of whether or not the first transmission
    // between the server and client succeeded
    private static volatile boolean FIRST_TRANSMISSION;

    // length/width of the map
    public static int mapsize;

    // each Client of the same game has a clientID
    public static int clientId;

    // the actionQueue stores all actions the client (or the agent) performed
    // between the latest transmission and now. Cleared after data is send
    public static final ArrayList<Integer> actionQueue = new ArrayList<>();

    public static void main(String[] args) {
        try {
            CLIENT = new Socket(HOST, PORT);

            // initialises the client, throws IOE
            initGame();

            // initialises the Agent
            //AGENT = new EstAgent();
            //AGENT_EXEC = Executors.newFixedThreadPool(1);
            //AGENT.init();

            while (true) {
                // method reads all the data and applies it to the GUI
                readData();
            }
        } catch (IOException e) {
            handleException(e);
        } finally {
            try {CLIENT.close();} catch (IOException e) {handleException(e);}
        }
    }

    /**
     * Method which is called at the beginning of the client's communication
     *
     * Communicates with Server.ClientChecker
     *
     * Reads initData; mapsize, this client's id, and current state of the sections
     * @throws IOException if communication fails
     */
    private static void initGame() throws IOException {
        InputStream in = CLIENT.getInputStream();
        PrintStream out = new PrintStream(CLIENT.getOutputStream());

        // write requestType
        out.println(Server.CONST_PLAY);

        // write requested map
        out.println(_MAP);

        // read gameData

        // mapsize
        mapsize = in.read();
        // clientId
        clientId = in.read();
        // amount of bytes that will be read, state
        int stateCount = in.read();

        // byte to int
        if (stateCount <= 0)
            stateCount = 256+stateCount;
        // read stateData
        byte[] stateData = IOUtils.readFully(in, stateCount, true);


        // init CLIENT_GUI
        CLIENT_GUI = new ClientGUI(mapsize, stateData);
        CLIENT_GUI.setTitle(ManagementFactory.getRuntimeMXBean().getName()+"  Map: "+_MAP+"  ID:"+clientId);

        // tells server that client is ready
        out.write(1);
    }

    /**
     * writes OUTPUT to server
     * owner, action, sectionId
     * {-1,-1,-1} is send to mark the end of the conversation
     */
    public static void writeData(){
        try {
            OutputStream out = CLIENT.getOutputStream();
            synchronized (actionQueue) {
                for (Integer integer : actionQueue) {
                    out.write(new byte[]{(byte) clientId, 1, (byte) (int) integer});
                }
                actionQueue.clear();
                out.write(new byte[]{-1, -1, -1});
            }
        } catch (IOException e) {try{CLIENT.close();}catch (IOException ignored){}}
    }

    /**
     * reads inputData; owner, state, units of each section
     * applies them to the gui
     * @throws IOException if communication fails
     */
    private static void readData() throws IOException {
        DataInputStream in = new DataInputStream(CLIENT.getInputStream());
        int length = in.readInt();

        byte[] fullData = new byte[length];

        if (in.read(fullData) != length) System.out.println("ERR_WrongInputLength");

        // owner, state, colorBits
        byte[] mapData = new byte[mapsize*mapsize*3];
        System.arraycopy(fullData, 0, mapData, 0, mapData.length);

        // unitValue
        byte[] unitData = new byte[mapsize*mapsize];
        System.arraycopy(fullData, mapsize*mapsize*3, unitData, 0, unitData.length);

        // attributeData
        byte[] attributeData = new byte[length-unitData.length-mapData.length];
        System.arraycopy(fullData, length-attributeData.length, attributeData, 0, attributeData.length);

        if (mapData.length+unitData.length+attributeData.length != length) System.out.println("ERR_WrongDivision");
        CLIENT_GUI.applyDataToSections(mapData, unitData, attributeData);

        //if (!FIRST_TRANSMISSION) {AGENT.setup(); FIRST_TRANSMISSION=true;}
        //if (AGENT.decrementTick()==0) AGENT_EXEC.execute(AGENT);
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

/*
Scanner scanner = new Scanner(CLIENT.getInputStream());
        ArrayList<Byte> fullData = new ArrayList<>();
        System.out.println("in");
         while (scanner.hasNextByte()) {
            fullData.add(scanner.nextByte());
        }
        System.out.println("out");
        // owner, state, colorBits
        Byte[] mapData = new Byte[mapsize*mapsize*3];
        fullData.subList(0,mapsize*mapsize*3-1).toArray(mapData);

        // unitValue
        Byte[] unitData = new Byte[mapsize*mapsize];
        fullData.subList(mapsize*mapsize*3, mapsize*mapsize*3+mapsize*mapsize-1).toArray(unitData);

        List<Byte> attributeList = fullData.subList(mapsize*mapsize*3+mapsize*mapsize, fullData.size()-1);
        Byte[] attributes = new Byte[attributeList.size()];
        attributeList.toArray(attributes);

        byte[] mapDataB = new byte[mapData.length];
        for (int i = 0; i < mapData.length; i++) {
            mapDataB[i] = mapData[i];
        }
        byte[] unitDataB = new byte[unitData.length];
        for (int i = 0; i < unitData.length; i++) {
            mapDataB[i] = unitData[i];
        }
        byte[] attributesB = new byte[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            mapDataB[i] = attributes[i];
        }
        CLIENT_GUI.applyDataToSections(mapDataB, unitDataB, attributesB);
 */
