package agent;

/**
 * Provides the interface for the Client
 *
 * init() is called once the Client is set up, this means it is the last
 * statement of its constructor. At this point a ClientGUI-Object is already set up,
 * and filled with SectionGUI-Objects. However the SectionGUI-Objects do not contain any
 * values (owner..) yet.
 *
 * setup() is called after the first transmission between the Server and the Client,
 * therefore the SectionGUI-Objects now contain all of their values.
 *
 * This Runnable-Object is executed by a ThreadPoolExecutor with a fixed amount of 1
 * @see java.util.concurrent.Executors#newFixedThreadPool(int)
 * Therefore run() is olny executed once simultaneously, additional calls are ignored
 * {@link #period}
 */
public abstract class Agent implements Runnable {

    /**
     * Determines how often run() is called on this Agent
     * period:2 means that run() is called once per each 2nd transmission
     * 1 transmissions is fulfilled each second
     */
    private int period = 2;

    /**
     * Used to keep track on when to call run()
     */
    private int ticksToGo = period;

    /**
     * Method is called once the Client is set up
     */
    public abstract void init();

    /**
     * Method is called once the first transmission succeeded
     */
    public abstract void setup();

    public final int decrementTick() {
        if (ticksToGo <= 0) ticksToGo=period;
        ticksToGo--;
        return ticksToGo;
    }
}
