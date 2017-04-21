package server.map;

import server.map.sections.Section;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class PlayerThread implements Runnable {

    Socket client;
    private BattlefieldThread battlefield;
    private Thread readerThread = new Thread(new Reader());

    /**
     * A PlayerThreads ID represents its spot in the BattlefieldThread
     */
    int playerID = -1;

    /**
     * A PlayerThread can be determined as a zombie. It becomes a zombie
     * if the connection is lost, although the game isn't over yet. A zombie thereby
     * reserves this players spot in a battlefield but no longer takes any actions.
     * A zombie gets resurrected if the client corresponding to this PlayerThread reconnects
     */
    volatile boolean zombie = false;

    public PlayerThread(Socket client, BattlefieldThread battlefield) {
        this.client = client;
        this.battlefield = battlefield;
    }

    @Override
    public void run() {
        if (zombie) return;
        try {
            DataOutputStream out = new DataOutputStream(client.getOutputStream());

            byte[] fullData = mergeArrays(battlefield.getData());

            out.writeInt(fullData.length);
            out.write(fullData);
        } catch (IOException ignored) {
            zombie = true;
            battlefield.checkForLife();
        }
    }

    void startReader() {
        readerThread.start();
    }

    private class Reader implements Runnable {
        @Override
        public void run() {
            if (zombie) return;
            try {
                while (true) {
                    InputStream in = client.getInputStream();
                    while (true) {
                        byte[] bytes = new byte[3];
                        //noinspection ResultOfMethodCallIgnored
                        in.read(bytes);
                        if (bytes[0] > 0)
                            applyInput(bytes[0], bytes[1], bytes[2]);
                        else break;
                    }
                    PlayerThread.this.run();
                }
            } catch (IOException ignored) {
                zombie = true;
                battlefield.checkForLife();
            }
        }
    }

    /**
     * Method which applies the Input given to the BattlefieldThread
     * if either player or action equals -1 method returns
     * @param player clientId who gives the action
     * @param action action which was performed, 1 to lock or unlock the Section
     * @param pos sectionId on which the action will be applied
     */
    private void applyInput(int player, int action, int pos) {
        if (player == -1 || action == -1)
            return;

        // bytes to int
        if (pos < 0)
            pos+=256;
        Section section = battlefield.getSectionByPos(pos);
        switch (action) {
            case 1: // lock or unlock section
                if (player == section.getOwner())
                    if (section.getState() == Section.State.LOCKED) {
                        section.setState(Section.State.CLASSIC);
                    } else {
                        section.setState(Section.State.LOCKED);
                    }
                break;
        }
    }

    protected static byte[] mergeArrays(byte[]... arrays) {
        ArrayList<Byte> retList = new ArrayList<>();
        for (byte[] array : arrays) {
            for (byte anArray : array) {
                retList.add(anArray);
            }
        }
        byte[] ret = new byte[retList.size()];
        for (int i = 0; i < retList.size(); i++) {
            ret[i]=retList.get(i);
        }
        return ret;
    }
}

/*
@Override
    public void run() {
        if (zombie) return;
        try {
            ByteArrayOutputStream out = ((ByteArrayOutputStream) client.getOutputStream());

            // bytes[mapSize][owner, state, colorBits, unitValue]
            byte[][] bytes = battlefield.getData();
            // attributes
            byte[] att = battlefield.getAttributesData();
            byte[] fullData = mergeArrays();
            for (byte[] partBytes : bytes) {
                out.write(partBytes);
            }

            out.write(att.length/128);
            out.write(att.length%128);
            out.write(att);

            System.out.println(bytes[0].length+bytes[1].length+bytes[2].length+bytes[3].length+att.length);
        } catch (IOException ignored) {
            zombie = true;
            battlefield.checkForLife();
        }
    }
 */