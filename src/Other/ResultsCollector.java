package Other;

import Strategies.Strategy;

import java.util.LinkedList;

public class ResultsCollector {

    public void collectResults(Strategy strategy) {

        int queries = strategy.getDemandRequestCounter();
        int migrations = strategy.getMigrationCounter();
        int time = strategy.getGlobalTime();

        double[] result = calculate(strategy.getListOfListsOfDemand());

        double avgCPULoad = result[0];
        double avgCPULoadStdDvg = result[1];

        System.out.format("Avg CPU load: %.2f +- %.2f %n",avgCPULoad,avgCPULoadStdDvg);
        System.out.println("Time: " + time);
        System.out.println("Queries: " + queries);
        System.out.println("Migrations: " + migrations);
    }

    public double[] calculate(LinkedList<LinkedList<Integer>> listOfLists) {

        LinkedList<Double> averages = new LinkedList<>();
        LinkedList<Double> deviations = new LinkedList<>();

        for (LinkedList<Integer> list : listOfLists) {

            double average = calculateAverage(list);
            double deviation = calculateStandardDeviation(list);

            averages.add(average);
            deviations.add(deviation);
        }

        double[] result = new double[2];
        result[0] = calculateAverageDouble(averages);
        result[1] = calculateAverageDouble(deviations);

        return result;
    }

    public double calculateAverage(LinkedList<Integer> list) {
        int sum = 0;
        for (int num : list) {
            sum += num;
        }
        return (double) sum / list.size();
    }

    public double calculateAverageDouble(LinkedList<Double> list) {
        double sum = 0;
        for (double num : list) {
            sum += num;
        }
        return sum / list.size();
    }

    public double calculateStandardDeviation(LinkedList<Integer> list) {
        double average = calculateAverage(list);
        double sum = 0;
        for (int num : list) {
            sum += Math.pow(num - average, 2);
        }
        double variance = sum / (list.size() - 1);
        return Math.sqrt(variance);
    }
}
