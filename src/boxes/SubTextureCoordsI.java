package boxes;

public class SubTextureCoordsI {

    public int X, Y;
    public boolean flippedHorizontally, flippedVertically;

    public SubTextureCoordsI(int x, int y) {
        this.X = x;
        this.Y = y;
        this.flippedHorizontally = false;
        this.flippedVertically = false;
    }

    @Override
    public String toString() {
        return "X:" + this.X + " Y:" + this.Y;
    }
}
