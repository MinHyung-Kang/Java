/**
 * Class used to calculate the conversion from 2d coordinate to 3d coordinate
 * Created by user on 2015-08-04.
 * <p>
 * Methods :
 * //Given the parameters, compute the 3d coordinate of the 2d coordinate
 * public static Coordinate convert2dto3d(double x_new, double y_new, double alpha_view, double beta_view)
 * <p>
 * //Return answer of quadratic formula
 * public static double quadraticFormulaPlus(double a, double b, double c)
 * <p>
 * //Return answer of quadratic formula (negative)
 * public static double quadraticFormulaNeg(double a, double b, double c)
 * <p>
 * //Tells if the value x is between low and high
 * public static boolean isBetween(double x, double low, double high)
 * <p>
 * //Tells if the input is correct
 * public static boolean isValid(double x, double y, double z)
 */
public class Calculator {

    //Empty constructor
    public Calculator() {
    }

    //Given the parameters, compute the 3d coordinate of the 2d coordinate
    public static Coordinate convert2dto3d(double x_new, double y_new, double alpha_view, double beta_view) {

        double a, b, c; //constants used to compute the value of z
        double x, y, z; //constants representing the value of clicked coordinate in 3D

        //Special case when sin(beta_view)=0
        if (isBetween(Math.sin(beta_view), -Math.pow(10, -10), Math.pow(10, -10))) {

            //If cos(alpha_view)=0
            if (isBetween(Math.cos(alpha_view), -Math.pow(10, -10), Math.pow(10, -10))) {
                x = y_new / Math.sin(alpha_view);
                z = -x_new / Math.cos(beta_view);
                y = Math.sqrt(1 - Math.pow(z, 2) - Math.pow(x, 2));

                //Consider plusminus case
                if (!isValid(x, y, z)) {
                    y = -y;
                    if (!isValid(x, y, z)) {
                        return null;
                    }
                }
            } else {
                a = Math.pow(Math.cos(beta_view), 2);
                b = -2 * (Math.sin(alpha_view) * Math.pow(Math.cos(beta_view), 2) * y_new);
                c = Math.pow(Math.cos(beta_view) * y_new, 2) + Math.pow(Math.cos(alpha_view) * x_new, 2) -
                        Math.pow(Math.cos(alpha_view) * Math.cos(beta_view), 2);
                x = quadraticFormulaPlus(a, b, c);
                z = -x_new / Math.cos(beta_view);
                y = (x * Math.sin(alpha_view) - y_new) / (Math.cos(alpha_view));

                //If the value is not valid, try other form of quadratic equation
                if (!isValid(x, y, z)) {
                    x = quadraticFormulaNeg(a, b, c);
                    z = -x_new / Math.cos(beta_view);
                    y = (x * Math.sin(alpha_view) - y_new) / (Math.cos(alpha_view));
                    if (!isValid(x, y, z)) {
                        // return null;
                    }
                }
            }
        } else {
            a = 1;
            b = 2 * x_new * Math.cos(beta_view);
            c = Math.pow(x_new, 2) + (Math.pow(y_new, 2) - 1) * Math.pow(Math.sin(beta_view), 2);
            z = quadraticFormulaPlus(a, b, c);

            //Compute the 3d coordinates
            x = (x_new * Math.cos(alpha_view) + y_new * Math.sin(alpha_view) * Math.sin(beta_view) +
                    z * Math.cos(alpha_view) * Math.cos(beta_view)) / Math.sin(beta_view);
            y = (x_new * Math.sin(alpha_view) - y_new * Math.cos(alpha_view) * Math.sin(beta_view) +
                    z * Math.sin(alpha_view) * Math.cos(beta_view)) / Math.sin(beta_view);


            //Test if the result is valid
            if (!isValid(x, y, z)) {
                z = quadraticFormulaNeg(a, b, c);
                x = (x_new * Math.cos(alpha_view) + y_new * Math.sin(alpha_view) * Math.sin(beta_view) +
                        z * Math.cos(alpha_view) * Math.cos(beta_view)) / Math.sin(beta_view);
                y = (x_new * Math.sin(alpha_view) - y_new * Math.cos(alpha_view) * Math.sin(beta_view) +
                        z * Math.sin(alpha_view) * Math.cos(beta_view)) / Math.sin(beta_view);

                if (!isValid(x, y, z)) {
                    return null;
                }

            }

        }

        //return the 3d coordinate
        return new Coordinate(x, y, z);
    }


    //Return answer of quadratic formula
    public static double quadraticFormulaPlus(double a, double b, double c) {
        return (-b + Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a);
    }

    //Return answer of quadratic formula (negative)
    public static double quadraticFormulaNeg(double a, double b, double c) {
        return (-b - Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a);
    }

    //Tells if the value x is between low and high
    public static boolean isBetween(double x, double low, double high) {
        return (x >= low && x <= high);
    }

    //Tells if the input is correct
    public static boolean isValid(double x, double y, double z) {
        double det = Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2);
        //If it is not visible, we have wrong coordinates


        if (!MainPanel.isVisible(x, y, z))
            return false;

        //We also check if the values are logical
        return (isBetween(x, -1, 1) && isBetween(y, -1, 1) && isBetween(z, -1, 1)
                && isBetween(det, 0.99, 1.01));
    }

}
