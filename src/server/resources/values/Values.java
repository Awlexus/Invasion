package server.resources.values;


public class Values {

    // Server
    public static final int speedFactor = 64*8; // Standard 64

    // Battlefield & Section
    public static final double growAmount = 0.002;
    public static final int growthFieldBuff = 50;

    // Section
    public static final double startUnitCount = 5;
    public static final int startOwner = 0;

    // Section.Distribution
    public static final int wantedCount = 15;
    public static final double differenceFactor = 1.4;
    public static final double totalFactor = 1.3;

    // Threads
    public static final int playerCommunicationPeriod = 1000;
    public static final int battlefieldCalculateDelay = 5000;
    public static final int battlefieldCalculatePeriod = 1000;
}
