package server.map.sections;

import server.map.BattlefieldThread;
import server.resources.values.Values;

import java.util.*;

public class Section{

    /**
     * Each section must have set an STATE
     * Only one State can be active at once
     *
     * Classic fields distribute and gain units at a normal speed
     *
     * Locked fields do not get any friendly units, however they distribute them
     *
     * Hole fields do not distribute any units, however they eat twice as many
     *
     * Null fields are unreachable fields, the won't participate in any calculation by the Server
     */
    public enum State{
        CLASSIC, LOCKED, HOLE, NULL
    }

    // amount of units in this Section
    double unitCount = Values.startUnitCount;

    // owner of this Section
    // initial owner is set in Values
    int owner = Values.startOwner;

    // State of the Section
    protected State state = State.CLASSIC;

    // reference to the neighbors of this Section
    private Section[] neighbours;

    // Array storing the amount of units send to this Section and who sent them
    private double bufferedUnitCount[];

    // reference to the map
    protected BattlefieldThread map;

    // top, right, bot, left
    public double[] lastOutputs = new double[4];


    public Section(BattlefieldThread map) {
        this.map = map;
        bufferedUnitCount = new double[map.getMaxPlayer()+1];
    }

    public void setNeighbours(Section sTop, Section sRight, Section sBot, Section sLeft) {
        this.neighbours = new Section[] {sTop, sRight, sBot, sLeft};
    }

    public void setUnits(double units) {
        if (units > 65535)
            units = 65536;
        this.unitCount = units;
    }

    public double getUnits() {
        return this.unitCount;
    }

    public double getUnitCount() {
        return this.unitCount;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getOwner() {
        return this.owner;
    }

    public void setState(State state) {
        if (this.state == State.NULL)
            return;
        this.state = state;
    }

    public State getState() {
        return this.state;
    }

    public Section[] getNeighbours() {
        return this.neighbours;
    }

    public byte[] getAttributes() {
        byte[] ret;

        List<Byte> dataList = new ArrayList<>();

        ret = new byte[dataList.size()];

        int i = 0;
        for (Byte b : dataList) {
            ret[i] = b;
            i++;
        }

        return ret;
    }

    public void grow() {
        if (owner != 0) {
            unitCount+=Values.growAmount;
        }
    }

    public void distribute() {
        // owner 0 is no player and therefore not allowed to distribute any units
        if (owner == 0)
            return;

        // amount of units that are to be distributed
        double disAmount = calcDisAmount();

        // array which tells the distributionPoints for each neighbour
        // distributionPoints decide how many units are distributed to this neighbour
        int[] distributionPoints = new int[4];

        // tells how many units are distributed to a Section for each point
        double perPointDistribution;

        // array which tells if this section is allowed to distribute units to this neighbour
        // top right bot left -neighbour
        boolean[] allowed = new boolean[4];


        // fills @array allowed by calling method allowed(Section)
        for (int i = 0; i < neighbours.length; i++) {
            allowed[i] = allowed(neighbours[i]);
        }

        // fills @array distributionPoints by calling method getDistributionPoints(State)
        for (int i = 0; i < neighbours.length; i++) {
            if (allowed[i]) {
                distributionPoints[i] = neighbours[i].getDistributionPoints(this.state);
            } else
                distributionPoints[i] = 0;
        }
        // if no units are to be distributed method returns
        if (distributionPoints[0]+distributionPoints[1]+distributionPoints[2]+distributionPoints[3] == 0)
            return;

        // sets perPointDistribution by dividing max units to distribute by the total value of @array distributionPoints
        perPointDistribution = disAmount/(distributionPoints[0]+distributionPoints[1]+distributionPoints[2]+distributionPoints[3]);

        // distributes the units to the neighbours ans assigns values to @array lastOutputs
        for (int i = 0; i < neighbours.length; i++) {
            if (neighbours[i] != null)
                neighbours[i].addBufferedUnitCount(this.owner, perPointDistribution*distributionPoints[i]);
            lastOutputs[i]= perPointDistribution*distributionPoints[i];
        }

        // subtracts the distributed units from unitCount of this section
        this.unitCount-=disAmount;

    }

    public void applyBufferedUnitCount() {
        bufferedUnitCount[owner]+=unitCount;
        double negBufferUnits = 0;

        double multiplier = getMultiplier();
        for (int i = 0; i < bufferedUnitCount.length; i++) {
            if (i != owner) {
                bufferedUnitCount[i] = bufferedUnitCount[i]*multiplier;
                negBufferUnits += bufferedUnitCount[i];
            }
        }

        if (bufferedUnitCount[owner] - negBufferUnits >= 0) {
            setUnits(bufferedUnitCount[owner] - negBufferUnits);
        } else {
            // determines the player with most units
            int maxUnitPlayer = attVsAtt();

            // sorts array
            Arrays.sort(bufferedUnitCount);

            // apply new data to this Section
            setUnits(bufferedUnitCount[bufferedUnitCount.length-1]-bufferedUnitCount[bufferedUnitCount.length-2]);
            setOwner(maxUnitPlayer);
            state = State.CLASSIC;
        }
        bufferedUnitCount = new double[map.getMaxPlayer()+1];
    }

    void addBufferedUnitCount(int player, double count) {
        bufferedUnitCount[player] += count;
    }

    private int attVsAtt() {
        int maxUnitPlayer = 0;
        for (int i = 0; i < bufferedUnitCount.length; i++) {
            if (bufferedUnitCount[i] > bufferedUnitCount[maxUnitPlayer])
                maxUnitPlayer = i;
        }
        for (int i = 0; i < bufferedUnitCount.length; i++) {
            if (i != maxUnitPlayer && (bufferedUnitCount[maxUnitPlayer]-bufferedUnitCount[i]< 0.00000001))
                maxUnitPlayer = 0;
        }
        return maxUnitPlayer;
    }

    private double calcDisAmount() {
        // dA = uC*gA/wC*(uC/wC)^differenceFactor*totalFactor
        return this.unitCount* Values.growAmount/ Values.wantedCount
                *Math.pow(this.unitCount/ Values.wantedCount, Values.differenceFactor)
                *Values.totalFactor;
    }

    private boolean allowed(Section section) {
        if (section == null)
            return false;
        switch (state) {
            case NULL: // noCaseScenario
                return false;

            case CLASSIC:
                switch (section.getState()) {
                    case NULL:
                        return false;
                    case CLASSIC:
                        return true;
                    case LOCKED:
                        return section.unitCount < Values.wantedCount || !(section.getOwner() == this.owner);
                    case HOLE:
                        return true;
                }
                break;

            case LOCKED:
                switch (section.getState()) {
                    case NULL:
                        return false;
                    case CLASSIC:
                        return (section.getOwner() == this.owner);
                    case LOCKED:
                        return false;
                    case HOLE:
                        return false;
                }
                break;
            
            case HOLE:
                return false;
        }
        return false; // noCaseScenario
    }

    private int getDistributionPoints(State state) {
        switch (this.state) {
            case NULL: // noCaseScenario
                return 0;

            case CLASSIC:
                switch (state) {
                    case NULL: // noCaseScenario
                        return 0;
                    case CLASSIC:
                        return 100;
                    case LOCKED:
                        return 506;
                    case HOLE: // noCaseScenario
                        return 0;
                }
                break;

            case LOCKED:
                switch (state) {
                    case NULL: // noCaseScenario
                        return 0;
                    case CLASSIC:
                        return 100;
                    case LOCKED: // noCaseScenario
                        return 0;
                    case HOLE: // noCaseScenario
                        return 0;
                }
                break;

            case HOLE:
                return 200;
        }
        return 0; // noCaseScenario
    }

    private double getMultiplier() {
        // amount of enemy Sections attacking this Section
        int activeEnemyInputs = 0;

        for (Section neighbour : neighbours) {
            if (neighbour != null && neighbour.state != State.NULL && neighbour.owner != owner && neighbour.state != State.LOCKED)
                activeEnemyInputs++;
        }

        switch (activeEnemyInputs) {
            case 0:
                return 1;
            case 1:
                return 1;
            case 2:
                return 1.25;
            case 3:
                return 1.7;
            case 4:
                return 3;
        }

        return 1;
    }

    void applySection(Section s) {
        // reAssign pointers of neighbors to this Section
        Section[] neighbors = s.neighbours;
        try {neighbors[0].neighbours[2] = this;} catch (NullPointerException ignored){}
        try {neighbors[1].neighbours[3] = this;} catch (NullPointerException ignored){}
        try {neighbors[2].neighbours[0] = this;} catch (NullPointerException ignored){}
        try {neighbors[3].neighbours[1] = this;} catch (NullPointerException ignored){}

        this.setOwner(s.getOwner());
        this.setUnits(s.getUnits());
        this.map = s.map;
        this.neighbours = s.getNeighbours();
        this.bufferedUnitCount = s.bufferedUnitCount;
    }
}
