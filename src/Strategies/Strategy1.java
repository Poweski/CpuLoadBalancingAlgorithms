package Strategies;

import MyObjects.CPU;
import MyObjects.Process;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Strategy1 implements Strategy {
    private final ArrayList<CPU> listOfCPUs;
    private final ArrayList<Process> listOfProcesses;
    private final ArrayList<Process> listOfDeadProcesses;
    private final LinkedList<LinkedList<Integer>> listOfListsOfDemand;
    private final int P;
    private final int Z;
    private final Random generator;
    private int demandRequestCounter;
    private int migrationCounter;
    private int globalTime;
    private int counter = 0;

    public Strategy1(ArrayList<CPU> listOfCPUs, ArrayList<Process> listOfProcesses, int P, int Z) {
        this.listOfCPUs = listOfCPUs;
        this.listOfProcesses = listOfProcesses;
        listOfDeadProcesses = new ArrayList<>();
        listOfListsOfDemand = new LinkedList<>();
        this.P = P;
        this.Z = Z;
        globalTime = 0;
        generator = new Random();
    }

    public void run() {

        int numberOfProcesses = listOfProcesses.size();
        int startingCpuID;
        int requestedCpuID;
        int attempts;

        boolean processWasNotSent;
        boolean newCpuWasNotFound;
        boolean processWasNotAdded;

        while (listOfDeadProcesses.size() < numberOfProcesses) {

            if (!listOfProcesses.isEmpty()) {

                Process currentProcess = listOfProcesses.removeFirst();
                startingCpuID = currentProcess.getCPUID();
                requestedCpuID = startingCpuID;
                processWasNotSent = true;
                newCpuWasNotFound = true;
                processWasNotAdded = true;
                attempts = 0;

                while (currentProcess.getAppearTime() > globalTime) {
                    increaseGlobalTime();
                }

                while (processWasNotSent && attempts < Z) {
                    while (newCpuWasNotFound) {
                        requestedCpuID = generator.nextInt(listOfCPUs.size()) ;
                        if (requestedCpuID != startingCpuID) {
                            newCpuWasNotFound = false;
                        }
                    }

                    int requestedCPUDemand = listOfCPUs.get(requestedCpuID).getTotalDemand();

                    if (requestedCPUDemand < P && requestedCPUDemand + currentProcess.getDemand() <= 100) {
                        listOfCPUs.get(requestedCpuID).addNewProcess(currentProcess);
                        migrationCounter++;
                        processWasNotSent = false;
                    }

                    newCpuWasNotFound = true;
                    demandRequestCounter++;
                    attempts++;
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
                }
            }
            else {
                increaseGlobalTime();
            }
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
