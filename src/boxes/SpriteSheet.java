package boxes;

import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.opengl.Texture;

public class SpriteSheet {

    private String sourceFilename;
    public Texture sheetTexture;
    private SpriteSheetProperties sheetProperties;
    private HashMap<EnumTextureId, SubTextureCoordsD> subtextureCoordMap;

    /*
     * Define a new sprite sheet. It will be initialized
     * later on from two files called 'filename.png' (the
     * actual sheet) and 'filename.xml' (description file).
     */
    public SpriteSheet(String filename) {
        this.sourceFilename = filename;
    }

    /*
     * Initialize this sprite sheet from the two corresponding
     * files (png and xml). After completing this operation
     * successfully the public property 'SheetTexture' will be
     * accessible.
     */
    public void initSheet() {
        // load from hdd
        this.sheetTexture = GameUtils.loadTexturePng(this.sourceFilename + ".png");
        this.sheetProperties = GameUtils.loadObjectFromXmlFile(this.sheetProperties, sourceFilename + ".xml");
        // calculate sheet fractions
        double fractionX = (double) 1 / this.sheetProperties.SheetDimensions.X;
        double fractionY = (double) 1 / this.sheetProperties.SheetDimensions.Y;
        // calculate coord map
        this.subtextureCoordMap = new HashMap<>();
        for (Map.Entry<EnumTextureId, Coords2dI> entry : this.sheetProperties.TextureCoordMap.entrySet()) {
            Coords2dI spriteCoords = entry.getValue();
            this.subtextureCoordMap.put(entry.getKey(), new SubTextureCoordsD(fractionX * spriteCoords.X, fractionY * spriteCoords.Y,
                    fractionX * (spriteCoords.X + 1), fractionY * (spriteCoords.Y + 1)));
        }
    }

    public SubTextureCoordsD getSubTextureCoords(EnumTextureId textureId) {
        if (this.subtextureCoordMap.containsKey(textureId)) {
            return this.subtextureCoordMap.get(textureId);
        }
        else {
            return null;
        }
    }
}
