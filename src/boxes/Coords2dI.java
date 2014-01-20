package boxes;

public class Coords2dI implements Comparable<Coords2dI> {

    public int X, Y;

    public Coords2dI(int x, int y) {
        this.X = x;
        this.Y = y;
    }

    public void set(int x, int y) {
        this.X = x;
        this.Y = y;
    }

    @Override
    public int compareTo(Coords2dI other) {
        if (this.X != other.X) {
            return Integer.compare(this.X, other.X);
        }
        else {
            return Integer.compare(this.Y, other.Y);
        }
    }

    public boolean isNearTo(Coords2dI other, int distance) {
        return distance > Math.sqrt(Math.pow(this.X - other.X, 2) + Math.pow(this.Y - other.Y, 2));
    }

    public boolean isNearToCb(Coords2dI other, int distance) {
        return distance > Math.max(Math.abs(this.X - other.X), Math.abs(this.Y - other.Y));
    }

    @Override
    public String toString() {
        return "x:" + this.X + " y:" + this.Y;
    }
}
