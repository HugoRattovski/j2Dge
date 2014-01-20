package boxes;

public class Coords2dD implements Comparable<Coords2dD> {

    public double X, Y;

    public Coords2dD(double x, double y) {
        this.X = x;
        this.Y = y;
    }

    public void set(double x, double y) {
        this.X = x;
        this.Y = y;
    }

    @Override
    public int compareTo(Coords2dD other) {
        if (this.X != other.X) {
            return Double.compare(this.X, other.X);
        }
        else {
            return Double.compare(this.Y, other.Y);
        }
    }

    public boolean isNearTo(Coords2dD other, double distance) {
        return distance > Math.sqrt(Math.pow(this.X - other.X, 2) + Math.pow(this.Y - other.Y, 2));
    }

    public boolean isNearToCb(Coords2dD other, double distance) {
        return distance > Math.max(Math.abs(this.X - other.X), Math.abs(this.Y - other.Y));
    }

    @Override
    public String toString() {
        return "x:" + this.X + " y:" + this.Y;
    }
}
