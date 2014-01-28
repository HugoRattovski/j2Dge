package boxes;

public interface IDrawableEntity {

    public boolean Visible = true;

    public Coords2dI getScaledWorldPosition(int baseSize);

    public void update(long delta);

    public void draw(int shiftX, int shiftY, int baseSize, double zoom, long delta);
}
