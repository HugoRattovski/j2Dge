package boxes;

public class FrameSequence {

    public int TimeMs;
    public int FrameCount;
    public Coords2dI[] FrameCoords;
    private float singleFrameFraction;

    public void init() {
        this.singleFrameFraction = (float) this.TimeMs / this.FrameCoords.length;
    }

    public Coords2dI getFrameCoords(long delta) {
        if (this.TimeMs != 0) {
            long animationTime = delta % this.TimeMs;
            return this.FrameCoords[(int) (animationTime / singleFrameFraction)];
        }
        return this.FrameCoords[0];
    }

    public int modDelta(int delta) {
        return delta % this.TimeMs;
    }
}
