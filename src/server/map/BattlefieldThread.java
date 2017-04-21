package server.map;

import server.Server;
import server.map.sections.Section;
import server.resources.values.Values;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BattlefieldThread implements Runnable {

    // List of all PlayerThreads that ware sharing data with this BattlefieldThread
    private final Hashtable<Integer, PlayerThread> players = new Hashtable<>();
    // Amount of PlayerThreads that can play on this Battlefield
    private int maxPlayer;
    // Length/Width of the Battlefield
    private int size;
    // name is used to differentiate between BattlefieldTypes
    private String mapName;

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(maxPlayer);

    private Section[][] sections;

    public BattlefieldThread(int size, int maxPlayer, String mapName) {
        this.size = size;
        this.maxPlayer = maxPlayer;
        this.mapName = mapName;
        sections = new Section[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sections[i][j] = new Section(this);
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Section sTop = null;
                Section sRight= null;
                Section sBot = null;
                Section sLeft = null;
                if (i != 0) {
                    sTop = sections[i-1][j];}
                if (j != size-1) {
                    sRight = sections[i][j+1];}
                if (i != size-1) {
                    sBot = sections[i+1][j];}
                if (j != 0) {
                    sLeft = sections[i][j-1];}
                sections[i][j].setNeighbours(sTop,sRight,sBot,sLeft);
            }
        }
    }

    public String getMapName() {
        return this.mapName;
    }

    public boolean isFull() {
        return (players.size()>=maxPlayer);
    }

    public synchronized void addPlayer(PlayerThread player) {
        if (!isFull()) {
            // clientID
            int openPos = 1;
            // search free position
            while (players.containsKey(openPos)) openPos++;

            try {
                OutputStream out = player.client.getOutputStream();
                InputStream in = player.client.getInputStream();
                // mapSize
                out.write(size);
                // clientID
                out.write(openPos);

                // stateData
                byte[] stateData = getStateData();

                // amount of bytes that will be send, state
                out.write(stateData.length);

                // state of the Section
                out.write(stateData);

                // confirmation
                if (in.read() == 1)
                    this.players.put(openPos, player);
                player.playerID = openPos;
                executor.scheduleAtFixedRate(player, 0,
                        Values.playerCommunicationPeriod, TimeUnit.MILLISECONDS);
                player.startReader();
            } catch (IOException ignored) {
                System.out.println("PlayerRejected_ERR");
            }
        }
    }

    public Section getSectionByPos(int id) {
        return sections[id/size][id%size];
    }

    public int getMaxPlayer() {
        return this.maxPlayer;
    }


    @Override
    public void run() {
        checkForLife();
        for (int i = 0; i < Values.speedFactor; i++) {
            growUnits();
            distribute();
            applyBufferedUnits();
        }
    }

    /**
     * Method checks if any Players are still alive
     * If no more players are alive all executors
     * including the one running this Battlefield will be terminated
     */
    void checkForLife() {
        if (this.players.size()==0) return;
        Enumeration<PlayerThread> enumeration = this.players.elements();
        while (enumeration.hasMoreElements()){
            if (!enumeration.nextElement().zombie) return;
        }
        executor.shutdown();
        Server.killExecutor(this);
    }

    /**
     * Method which collects information about the current Battlefield and
     * returns them with a byte[] array
     * byte[] contains information about either owner,state,colorBits,units
     * The size of each byte[] depends on the server.map size, max(256)
     * @return byte[][] with all graphical data
     */
    byte[] getData() {
        return PlayerThread.mergeArrays(getOwnerData(), getStateData(), getColorsData(), getUnitsData(), getAttributesData());

    }

    private byte[] getOwnerData() {
        byte[] ret = new byte[size*size];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = (byte)getSectionByPos(i).getOwner();
        }
        return ret;
    }

    private byte[] getStateData() {
        byte[] ret = new byte[size*size];
        for (int i = 0; i < ret.length; i++) {
            switch (getSectionByPos(i).getState()) {
                case NULL:
                    ret[i] = 0;
                    break;
                case CLASSIC:
                    ret[i] = 1;
                    break;
                case LOCKED:
                    ret[i] = 2;
                    break;
                case HOLE:
                    ret[i] = 3;
                    break;
            }
        }
        return ret;
    }

    private byte[] getColorsData() {
        byte[] ret = new byte[size*size];
        for (int i = 0; i < ret.length; i++) {
            double[] unitOutput = getSectionByPos(i).lastOutputs;
            Section[] neighbors = getSectionByPos(i).getNeighbours();

            // TOP[Out = 0, In = 1][Slow = 0, Fast = 1] RIGHT[][] BOT[][] LEFT[][]
            byte bits = 0;

            // top
            if (neighbors[0] != null)
                bits += (unitOutput[0] >= neighbors[0].lastOutputs[2])? 0:128;

            // right
            if (neighbors[1] != null)
                bits += (unitOutput[1] >= neighbors[1].lastOutputs[3])? 0:32;

            // bot
            if (neighbors[2] != null)
                bits += (unitOutput[2] >= neighbors[2].lastOutputs[0])? 0:8;

            // left
            if (neighbors[3] != null)
                bits += (unitOutput[3] >= neighbors[3].lastOutputs[1])? 0:2;

            ret[i] = bits;
        }
        return ret;
    }

    private byte[] getUnitsData() {
        byte[] ret = new byte[size*size];
        for (int i = 0; i < ret.length; i++) {
            int units = (int)getSectionByPos(i).getUnitCount();
            if (units>256) units = 255;
            ret[i] = (byte)units;
            if (units < 20) ret[i]=0;
            else if (units < 60) ret[i]=1;
            else if (units < 100) ret[i]=2;
            else if (units < 150) ret[i]=3;
            else ret[i]=4;
        }
        return ret;
    }

    byte[] getAttributesData() {
        byte[] ret;
        List<Byte> dataList = new ArrayList<>();

        for (int i = 0; i < Math.pow(size, 2); i++) {
            Section s = getSectionByPos(i);
            byte[] sectionData = s.getAttributes();
            dataList.add(((byte) sectionData.length));
            for (byte aSectionData : sectionData) {
                dataList.add(aSectionData);
            }
        }

        ret = new byte[dataList.size()];

        int i = 0;
        for (byte b :
                dataList) {
            ret[i] = b;
            i++;
        }
        return ret;
    }

    private void growUnits() {
        for (Section[] lineSections: sections) {
            for (Section section: lineSections) {
                section.grow();
            }
        }
    }


    private void distribute() {
        for (Section[] lineSections: sections) {
            for (Section section: lineSections) {
                section.distribute();
            }
        }
    }

    private void applyBufferedUnits() {
        for (Section[] lineSections: sections) {
            for (Section section: lineSections) {
                section.applyBufferedUnitCount();
            }
        }
    }

    public void transformSectionTo(int id, Section newSection) {
        sections[id/size][id%size] = newSection;
    }

    public Section[][] getSections() {
        return sections;
    }
}
