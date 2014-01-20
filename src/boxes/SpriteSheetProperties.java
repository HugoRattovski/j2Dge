package boxes;

import java.util.HashMap;

public class SpriteSheetProperties {
    public Coords2dI SheetDimensions;
    public HashMap<EnumTextureId, Coords2dI> TextureCoordMap;

    public SpriteSheetProperties(){
        this.SheetDimensions = new Coords2dI(0, 0);
        this.TextureCoordMap = new HashMap<>();
    }
}
