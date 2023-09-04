package Other;

import MyObjects.CPU;
import MyObjects.Process;

import java.util.ArrayList;
import java.util.Random;

public class Generator {

    public ArrayList<CPU> generateListOfCPU()
    {
        ArrayList<CPU> cpuList = new ArrayList<>();

        for (int i = 0; i < Main.N; i++)
            cpuList.add(new CPU(i));

        return cpuList;
    }

    public ArrayList<Process> generateListOfProcesses(ArrayList<CPU> cpuList)
    {
        ArrayList<Process> processesList = new ArrayList<>();
        Random generator = new Random();

        int appearTime;
        int exeTime;
        int demand;
        int cpuID;

        for (int i = 0; i < Main.N * Main.DEGREE_OF_NUMBER_OF_PROCESSES; i++)
        {
            appearTime = generator.nextInt(Main.MAX_PROCESS_APPEAR_TIME-Main.MIN_PROCESS_APPEAR_TIME)
                    + Main.MIN_PROCESS_APPEAR_TIME;
            exeTime = generator.nextInt(Main.MAX_PROCESS_EXECUTION_TIME-Main.MIN_PROCESS_EXECUTION_TIME)
                    + Main.MIN_PROCESS_EXECUTION_TIME;
            demand = generator.nextInt(Main.MAX_PROCESS_DEMAND-Main.MIN_PROCESS_DEMAND)
                    + Main.MIN_PROCESS_DEMAND;
            cpuID = generator.nextInt(cpuList.size());

            processesList.add(new Process(i,demand, appearTime, exeTime, cpuID));
        }

        processesList.sort(new AppearTimeComparator());

        return processesList;
    }

    public ArrayList<CPU> copyCPUList(ArrayList<CPU> originalList)
    {
        ArrayList<CPU> newList = new ArrayList<>();

        for (CPU cpu : originalList)
            newList.add(new CPU(cpu.getCPUID()));

        return newList;
    }

    public ArrayList<Process> copyProcessList(ArrayList<Process> originalList)
    {
        ArrayList<Process> newList = new ArrayList<>();

        for (Process process : originalList)
            newList.add(new Process(process.getProcessID(), process.getDemand(), process.getAppearTime(),
                    process.getStartingExecutionTime(), process.getCPUID()));

        return newList;
    }
}
