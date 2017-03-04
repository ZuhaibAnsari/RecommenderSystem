package util;


/**
 * Class for Maths helper as the native Maths library was performing slow
 * Created by Zuhaib on 11/17/2016.
 */
public class MathsHelper {

    /**
     * Method to find squareRoot for a number as the base method of java.lang.Math are very slow hence a custom implementation
     * @param number for which the square root needs to be calculated
     * @return the square root
     */
    public static double squareRoot(double number) {
        double baseNumber,squareRoot = number / 2;

        do {
            baseNumber = squareRoot;
            squareRoot = (baseNumber + (number / baseNumber)) / 2;
        } while ((baseNumber - squareRoot) != 0);

        return squareRoot;
    }

}

