package MyObjects;

import java.util.ArrayList;

public class CPU {
    private final int cpuID;
    private final ArrayList<Process> listOfProcesses;
    private final ArrayList<Process> listOfWaitingProcesses;
    private int totalDemand;

    public CPU(int cpuID) {
        this.cpuID = cpuID;
        listOfProcesses = new ArrayList<>();
        listOfWaitingProcesses = new ArrayList<>();
        totalDemand = 0;
    }

    public int getCPUID() {
        return cpuID;
    }

    public ArrayList<Process> getListOfProcesses() {
        return listOfProcesses;
    }

    public int getTotalDemand() {
        return totalDemand;
    }

    public void addNewProcess(Process newProcess) {
        if (totalDemand + newProcess.getDemand() > 100) {
            newProcess.setCPUID(cpuID);
            listOfWaitingProcesses.add(newProcess);
        }
        else {
            totalDemand += newProcess.getDemand();
            newProcess.setCPUID(cpuID);
            listOfProcesses.add(newProcess);
        }
    }

    public void removeProcess(Process processToRemove) {
        if (processToRemove != null) {
            totalDemand -= processToRemove.getDemand();
            processToRemove.setCPUID(-1);
            addWaitingProcess();
            listOfProcesses.remove(processToRemove);
        }
    }

    public Process removeProcessWithHighestDemand() {
        Process processWithHighestDemand = getProcessWithHighestDemand();
        if (processWithHighestDemand != null) {
            totalDemand -= processWithHighestDemand.getDemand();
            processWithHighestDemand.setCPUID(-1);
            listOfProcesses.remove(processWithHighestDemand);
            addWaitingProcess();
        }
        return processWithHighestDemand;
    }

    public Process removeProcessWithLowestDemand() {
        Process processWithLowestDemand = getProcessWithLowestDemand();
        if (processWithLowestDemand != null) {
            totalDemand -= processWithLowestDemand.getDemand();
            processWithLowestDemand.setCPUID(-1);
            listOfProcesses.remove(processWithLowestDemand);
            addWaitingProcess();
        }
        return processWithLowestDemand;
    }

    public Process getProcessWithHighestDemand() {
        int highestDemand = 0;
        Process processWithHighestDemand = null;
        for (Process process : listOfProcesses) {
            if (process.getDemand() > highestDemand) {
                highestDemand = process.getDemand();
                processWithHighestDemand = process;
            }
        }
        return processWithHighestDemand;
    }

    public Process getProcessWithLowestDemand() {
        int lowestDemand = 100;
        Process processWithLowestDemand = null;
        for (Process process : listOfProcesses) {
            if (process.getDemand() < lowestDemand) {
                lowestDemand = process.getDemand();
                processWithLowestDemand = process;
            }
        }
        return processWithLowestDemand;
    }

    private void addWaitingProcess() {
        if (!listOfWaitingProcesses.isEmpty()) {
            ArrayList<Process> processesToAdd = new ArrayList<>();
            int spaceLeft = 100 - totalDemand;
            for (Process waitingProcess : listOfWaitingProcesses) {
                if (waitingProcess.getDemand() <= spaceLeft) {
                    processesToAdd.add(waitingProcess);
                    spaceLeft -= waitingProcess.getDemand();
                }
            }
            for (Process processToAdd : processesToAdd) {
                listOfWaitingProcesses.remove(processToAdd);
                addNewProcess(processToAdd);
            }
        }
    }
}
