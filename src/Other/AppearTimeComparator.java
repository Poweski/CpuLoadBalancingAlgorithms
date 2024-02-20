package Other;

import MyObjects.Process;

import java.util.Comparator;

public class AppearTimeComparator implements Comparator<Process> {

    @Override
    public int compare(Process process1, Process process2) {
        return Integer.compare(process1.getAppearTime(), process2.getAppearTime());
    }
}
