package Strategies;

import MyObjects.CPU;
import MyObjects.Process;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Strategy2 implements Strategy {
    private final ArrayList<CPU> listOfCPUs;
    private final ArrayList<Process> listOfProcesses;
    private final ArrayList<Process> listOfDeadProcesses;
    private final LinkedList<LinkedList<Integer>> listOfListsOfDemand;
    private final int P;
    private final Random generator;
    private int demandRequestCounter;
    private int migrationCounter;
    private int globalTime;
    private int counter = 0;

    public Strategy2(ArrayList<CPU> listOfCPUs, ArrayList<Process> listOfProcesses, int P) {
        this.listOfCPUs = listOfCPUs;
        this.listOfProcesses = listOfProcesses;
        listOfDeadProcesses = new ArrayList<>();
        listOfListsOfDemand = new LinkedList<>();
        this.P = P;
        globalTime = 0;
        generator = new Random();
    }

    public void run()
    {
        int numberOfProcesses = listOfProcesses.size();
        int startingCpuID;
        int requestedCpuID;
        boolean processWasNotSent;
        boolean newCpuWasNotFound;
        boolean newCpuIsNeeded;
        boolean processWasNotAdded;

        while (listOfDeadProcesses.size() < numberOfProcesses)
        {
            if (listOfProcesses.size() > 0)
            {
                Process currentProcess = listOfProcesses.remove(0);
                startingCpuID = currentProcess.getCPUID();
                requestedCpuID = startingCpuID;
                processWasNotSent = true;
                newCpuWasNotFound = true;
                processWasNotAdded = true;

                while (currentProcess.getAppearTime() > globalTime)
                    increaseGlobalTime();

                newCpuIsNeeded = listOfCPUs.get(startingCpuID).getTotalDemand() >= P;

                while (processWasNotSent && newCpuIsNeeded)
                {
                    while (newCpuWasNotFound)
                    {
                        requestedCpuID = generator.nextInt(listOfCPUs.size());

                        if (requestedCpuID != startingCpuID)
                            newCpuWasNotFound = false;
                    }

                    int requestedCPUDemand = listOfCPUs.get(requestedCpuID).getTotalDemand();

                    if (requestedCPUDemand < P && requestedCPUDemand + currentProcess.getDemand() <= 100)
                    {
                        listOfCPUs.get(requestedCpuID).addNewProcess(currentProcess);
                        migrationCounter++;
                        processWasNotSent = false;
                    }

                    if (processWasNotSent)
                        increaseGlobalTime();

                    newCpuWasNotFound = true;
                    demandRequestCounter++;
                }

                if (processWasNotSent)
                {
                    while (processWasNotAdded)
                    {
                        if (listOfCPUs.get(startingCpuID).getTotalDemand() + currentProcess.getDemand() <= 100)
                        {
                            listOfCPUs.get(startingCpuID).addNewProcess(currentProcess);
                            processWasNotAdded = false;
                        }
                        else increaseGlobalTime();
                    }
                }
            }
            else increaseGlobalTime();
        }
    }

    private void increaseGlobalTime()
    {
        ArrayList<Process> processesToRemove;

        LinkedList<Integer> listOfDemand = new LinkedList<>();

        globalTime++;

        for (CPU currentCPU : listOfCPUs)
        {
            if (currentCPU.getListOfProcesses().size() != 0)
            {
                processesToRemove = new ArrayList<>();

                for (Process proc : currentCPU.getListOfProcesses())
                {
                    if (proc.getExecutionTimeLeft() == 0)
                    {
                        processesToRemove.add(proc);
                        listOfDeadProcesses.add(proc);
                    }
                    else
                        proc.changeExecutionTimeLeft(-1);
                }

                if (processesToRemove.size() != 0)
                    for (Process processToRemove : processesToRemove)
                        currentCPU.removeProcess(processToRemove);
            }

            listOfDemand.add(currentCPU.getTotalDemand());
        }

        if (counter % 20 == 0)
            listOfListsOfDemand.add(listOfDemand);
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
