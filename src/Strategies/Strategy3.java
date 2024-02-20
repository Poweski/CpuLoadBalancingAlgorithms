package Strategies;

import MyObjects.CPU;
import MyObjects.Process;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Strategy3 implements Strategy {
    private final ArrayList<CPU> listOfCPUs;
    private final ArrayList<Process> listOfProcesses;
    private final ArrayList<Process> listOfDeadProcesses;
    private final LinkedList<LinkedList<Integer>> listOfListsOfDemand;
    private final int PERCENTAGE_OF_MAX_CPU_LOAD_RELEASED;
    private final int P;
    private final int R;
    private final Random generator;
    private int demandRequestCounter;
    private int migrationCounter;
    private int globalTime;
    private int counter = 0;

    public Strategy3(ArrayList<CPU> listOfCPUs, ArrayList<Process> listOfProcesses,
                     int P, int R, int PERCENTAGE_OF_MAX_CPU_LOAD_RELEASED) {
        this.listOfCPUs = listOfCPUs;
        this.listOfProcesses = listOfProcesses;
        listOfDeadProcesses = new ArrayList<>();
        listOfListsOfDemand = new LinkedList<>();
        this.PERCENTAGE_OF_MAX_CPU_LOAD_RELEASED = PERCENTAGE_OF_MAX_CPU_LOAD_RELEASED;
        this.P = P;
        this.R = R;
        globalTime = 0;
        generator = new Random();
    }

    public void run() {

        int numberOfProcesses = listOfProcesses.size();
        int startingCpuID;
        int requestedCpuID;
        boolean processWasNotSent;
        boolean newCpuWasNotFound;
        boolean newCpuIsNeeded;
        boolean processWasNotAdded;

        while (listOfDeadProcesses.size() < numberOfProcesses) {
            if (!listOfProcesses.isEmpty()) {

                Process currentProcess = listOfProcesses.removeFirst();
                startingCpuID = currentProcess.getCPUID();
                requestedCpuID = startingCpuID;
                processWasNotSent = true;
                newCpuWasNotFound = true;
                processWasNotAdded = true;

                while (currentProcess.getAppearTime() > globalTime) {
                    increaseGlobalTime();
                }

                newCpuIsNeeded = listOfCPUs.get(startingCpuID).getTotalDemand() >= P;

                while (processWasNotSent && newCpuIsNeeded) {
                    while (newCpuWasNotFound) {
                        requestedCpuID = generator.nextInt(listOfCPUs.size());
                        if (requestedCpuID != startingCpuID) {
                            newCpuWasNotFound = false;
                        }
                    }

                    int requestedCPUDemand = listOfCPUs.get(requestedCpuID).getTotalDemand();

                    if (requestedCPUDemand < P && requestedCPUDemand + currentProcess.getDemand() <= 100) {
                        listOfCPUs.get(requestedCpuID).addNewProcess(currentProcess);

                        if (listOfCPUs.get(requestedCpuID).getTotalDemand() < R) {
                            offloadOtherCPUs(requestedCpuID);
                        }

                        migrationCounter++;
                        processWasNotSent = false;
                    }

                    if (processWasNotSent) {
                        increaseGlobalTime();
                    }

                    newCpuWasNotFound = true;
                    demandRequestCounter++;
                }

                if (processWasNotSent) {
                    while (processWasNotAdded) {
                        if (listOfCPUs.get(startingCpuID).getTotalDemand() + currentProcess.getDemand() <= 100) {
                            listOfCPUs.get(startingCpuID).addNewProcess(currentProcess);
                            processWasNotAdded = false;
                        }
                        else {
                            increaseGlobalTime();
                        }
                    }

                    if (listOfCPUs.get(startingCpuID).getTotalDemand() < R) {
                        offloadOtherCPUs(startingCpuID);
                    }
                }
            }
            else increaseGlobalTime();
        }
    }

    private void increaseGlobalTime() {

        ArrayList<Process> processesToRemove;
        LinkedList<Integer> listOfDemand = new LinkedList<>();
        globalTime++;

        for (CPU currentCPU : listOfCPUs) {
            if (!currentCPU.getListOfProcesses().isEmpty()) {

                processesToRemove = new ArrayList<>();

                for (Process process : currentCPU.getListOfProcesses()) {
                    if (process.getExecutionTimeLeft() == 0) {
                        processesToRemove.add(process);
                        listOfDeadProcesses.add(process);
                    }
                    else {
                        process.changeExecutionTimeLeft(-1);
                    }
                }

                if (!processesToRemove.isEmpty()) {
                    for (Process processToRemove : processesToRemove) {
                        currentCPU.removeProcess(processToRemove);
                    }
                }
            }

            listOfDemand.add(currentCPU.getTotalDemand());
        }

        if (counter % 20 == 0) {
            listOfListsOfDemand.add(listOfDemand);
        }
        counter++;
    }

    private void offloadOtherCPUs(int startingCpuID) {

        CPU startingCPU = listOfCPUs.get(startingCpuID);
        int freeSpace = P - startingCPU.getTotalDemand();

        for (CPU currentCPU : listOfCPUs) {
            if (currentCPU != startingCPU) {
                if (currentCPU.getTotalDemand() > P) {

                    int migratedDemand = 0;
                    int highestDemand = currentCPU.getProcessWithHighestDemand().getDemand();
                    int lowestDemand = currentCPU.getProcessWithLowestDemand().getDemand();

                    while (freeSpace - highestDemand > 0 && migratedDemand < PERCENTAGE_OF_MAX_CPU_LOAD_RELEASED) {
                        Process processToMigrate = currentCPU.removeProcessWithHighestDemand();
                        startingCPU.addNewProcess(processToMigrate);
                        freeSpace -= highestDemand;
                        migratedDemand += highestDemand;
                        migrationCounter++;
                        highestDemand = currentCPU.getProcessWithHighestDemand().getDemand();
                    }

                    while (freeSpace - lowestDemand > 0 && migratedDemand < PERCENTAGE_OF_MAX_CPU_LOAD_RELEASED) {
                        Process processToMigrate = currentCPU.removeProcessWithLowestDemand();
                        startingCPU.addNewProcess(processToMigrate);
                        freeSpace -= lowestDemand;
                        migratedDemand += lowestDemand;
                        migrationCounter++;
                        lowestDemand = currentCPU.getProcessWithLowestDemand().getDemand();
                    }

                    break;
                }

                demandRequestCounter++;
            }
        }
    }

    public int getDemandRequestCounter() {
        return demandRequestCounter;
    }

    public int getMigrationCounter() {
        return migrationCounter;
    }

    public int getGlobalTime() {
        return globalTime;
    }

    public LinkedList<LinkedList<Integer>> getListOfListsOfDemand() {
        return listOfListsOfDemand;
    }
}
