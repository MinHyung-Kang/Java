
/**
 * Class used to represent a 2-d or 3-d coordinate for the whole program
 * Created by user on 2015-07-03.
 */
public class Coordinate {
    //Each double represents the x,y,z locations.
    private double x;
    private double y;
    private double z;

    //Constructor for 2d coordinates (z has no default value)
    //It is up for the user who defines the Coordinate to determine if it is 2d or 3d
    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //Constructor for 3d coordinates
    public Coordinate(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //Getter methods
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

}
