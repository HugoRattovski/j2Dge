package boxes;

import static org.lwjgl.opengl.GL11.*;

public class Tile {

    private EnumTextureId textureId;
    private SubTextureCoords textureCoords;

    public Tile(EnumTextureId textureId, SpriteSheet spriteSheet) {
        this.textureId = textureId;
        this.textureCoords = spriteSheet.getSubTextureCoords(this.textureId);
    }

    public void drawTextured(int lowerLeftCornerX, int lowerLeftCornerY, int edgeLength) {
        if (textureCoords != null) {
            glEnable(GL_TEXTURE_2D);
            glBegin(GL_QUADS);
            // lower left
            glTexCoord2d(textureCoords.upperLeftX, textureCoords.lowerRightY);
            glVertex2i(lowerLeftCornerX, lowerLeftCornerY);
            // lower right
            glTexCoord2d(textureCoords.lowerRightX, textureCoords.lowerRightY);
            glVertex2i(lowerLeftCornerX + edgeLength, lowerLeftCornerY);
            // upper right
            glTexCoord2d(textureCoords.lowerRightX, textureCoords.upperLeftY);
            glVertex2i(lowerLeftCornerX + edgeLength, lowerLeftCornerY + edgeLength);
            // upper left
            glTexCoord2d(textureCoords.upperLeftX, textureCoords.upperLeftY);
            glVertex2i(lowerLeftCornerX, lowerLeftCornerY + edgeLength);
            glEnd();
            glDisable(GL_TEXTURE_2D);
        }
    }
}
