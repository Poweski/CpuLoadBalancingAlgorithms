package Other;

import MyObjects.CPU;
import MyObjects.Process;
import Strategies.*;

import java.util.ArrayList;

public class Main {

    static final int N = 100; // Number of processors
    static final int P = 90; // Maximum CPU load threshold
    static final int R = 30; // Minimum CPU load threshold
    static final int Z = 20; // Maximum number of queries

    static final int MIN_PROCESS_DEMAND = 5;
    static final int MAX_PROCESS_DEMAND = 10;
    static final int MIN_PROCESS_APPEAR_TIME = 0;
    static final int MAX_PROCESS_APPEAR_TIME = 5;
    static final int MIN_PROCESS_EXECUTION_TIME = 1;
    static final int MAX_PROCESS_EXECUTION_TIME = 2;

    static final int PERCENTAGE_OF_MAX_CPU_LOAD_RELEASED = 40;
    static final int DEGREE_OF_NUMBER_OF_PROCESSES = 100;
    // A number specifying the number of processes, based on a multiple of the number of processors


    public static void main(String[] args) {

        Generator myGenerator = new Generator();
        ResultsCollector resultsCollector = new ResultsCollector();

        ArrayList<CPU> listOfCPUs = myGenerator.generateListOfCPU();
        ArrayList<Process> listOfProcesses = myGenerator.generateListOfProcesses(listOfCPUs);

        ArrayList<CPU> cpuList1 = myGenerator.copyCPUList(listOfCPUs);
        ArrayList<CPU> cpuList2 = myGenerator.copyCPUList(listOfCPUs);
        ArrayList<CPU> cpuList3 = myGenerator.copyCPUList(listOfCPUs);

        ArrayList<Process> processList1 = myGenerator.copyProcessList(listOfProcesses);
        ArrayList<Process> processList2 = myGenerator.copyProcessList(listOfProcesses);
        ArrayList<Process> processList3 = myGenerator.copyProcessList(listOfProcesses);

        Strategy1 strategy1 = new Strategy1(cpuList1,processList1,P,Z);
        Strategy2 strategy2 = new Strategy2(cpuList2,processList2,P);
        Strategy3 strategy3 = new Strategy3(cpuList3,processList3,P,R,PERCENTAGE_OF_MAX_CPU_LOAD_RELEASED);

        strategy1.run();
        strategy2.run();
        strategy3.run();

        System.out.println("\nStrategy 1");
        resultsCollector.collectResults(strategy1);
        System.out.println("\nStrategy 2");
        resultsCollector.collectResults(strategy2);
        System.out.println("\nStrategy 3");
        resultsCollector.collectResults(strategy3);
    }
}