package boxes;

public class SubTextureCoords {

    public double upperLeftX;
    public double upperLeftY;
    public double lowerRightX;
    public double lowerRightY;

    public SubTextureCoords(double ulX, double ulY, double lrX, double lrY) {
        this.upperLeftX = ulX;
        this.upperLeftY = ulY;
        this.lowerRightX = lrX;
        this.lowerRightY = lrY;
    }
}
