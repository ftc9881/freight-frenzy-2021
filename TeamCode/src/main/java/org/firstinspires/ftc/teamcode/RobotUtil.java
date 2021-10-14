package org.firstinspires.ftc.teamcode;

public class RobotUtil {
    public static double arrayMax(double[] arr) {
        double max = Double.NEGATIVE_INFINITY;

        for(double cur: arr)
            max = Math.max(max, cur);

        return max;
    }

    public static double arrayMin(double[] arr) {
        double min = Double.POSITIVE_INFINITY;

        for(double cur: arr)
            min = Math.min(min, cur);

        return min;
    }

    public static double arrayMean(double[] arr) {
        double sum = 0;

        for(double cur: arr)
            sum += cur;

        return sum / arr.length;
    }

    public static int arrayMax(int[] arr) {
        int max = Integer.MIN_VALUE;

        for(int cur: arr)
            max = Math.max(max, cur);

        return max;
    }

    public static int arrayMin(int[] arr) {
        int min = Integer.MAX_VALUE;

        for(int cur: arr)
            min = Math.min(min, cur);

        return min;
    }

    public static int arrayMean(int[] arr) {
        int sum = 0;

        for(int cur: arr)
            sum += cur;

        return sum / arr.length;
    }

    public static double sign(double value) {
        if(value > 0) return 1;
        else if(value < 0) return -1;
        else return 0;
    }

    public static void arrayAbs(double[] positions) {
        for(int i = 0; i < positions.length; ++i) {
            positions[i] = Math.abs(positions[i]);
        }
    }

    public static void arrayAbs(int[] positions) {
        for(int i = 0; i < positions.length; ++i) {
            positions[i] = Math.abs(positions[i]);
        }
    }

}
