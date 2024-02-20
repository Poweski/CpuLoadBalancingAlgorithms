package MyObjects;

public class Process {
    private final int processID;
    private final int demand;
    private final int appearTime;
    private final int startingExecutionTime;
    private int executionTimeLeft;
    private int cpuID;

    public Process(int processID, int demand, int appearTime, int executionTime, int cpuID) {
        this.processID = processID;
        this.demand = demand;
        this.appearTime = appearTime;
        startingExecutionTime = executionTime;
        executionTimeLeft = executionTime;
        this.cpuID = cpuID;
    }

    public int getProcessID() {
        return processID;
    }

    public int getDemand() {
        return demand;
    }

    public int getAppearTime() {
        return appearTime;
    }
    public int getStartingExecutionTime() {
        return startingExecutionTime;
    }
    public int getExecutionTimeLeft() {
        return executionTimeLeft;
    }
    public int getCPUID() {
        return cpuID;
    }
    public void changeExecutionTimeLeft(int change) {
        executionTimeLeft += change;
    }
    public void setCPUID(int cpuID) {
        this.cpuID = cpuID;
    }
}
