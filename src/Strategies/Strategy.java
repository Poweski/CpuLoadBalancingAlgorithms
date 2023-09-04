package Strategies;

import java.util.LinkedList;

public interface Strategy {
    void run();
    int getDemandRequestCounter();
    int getMigrationCounter();
    int getGlobalTime();
    LinkedList<LinkedList<Integer>> getListOfListsOfDemand();
}
