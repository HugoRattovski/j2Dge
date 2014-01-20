package boxes;

import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.Texture;

public class OpenGlFont {

    private final Coords2dI DIM = new Coords2dI(16, 16);
    private Texture fontTexture;
    private byte[] fontCharWidths;
    private Coords2dI singleCharDimensions;

    public OpenGlFont(String filenamePng, String filenameCharWidths) {
        // this class always expects a texture with 16*16 (256) chars
        this.fontTexture = GameUtils.loadTexturePng(filenamePng);
        this.singleCharDimensions = new Coords2dI(this.fontTexture.getImageWidth() / DIM.X, this.fontTexture.getImageHeight() / DIM.Y);
        this.fontCharWidths = GameUtils.loadObjectFromXmlFile(this.fontCharWidths, filenameCharWidths);
    }

    public int calculateWidth(String textToDraw, int pixelHeight) {
        return this.calculateWidth(textToDraw, pixelHeight, 0);
    }

    public int calculateWidth(String textToDraw, int pixelHeight, int additionalKerning) {
        int width = 0;
        byte[] bytesToDraw = textToDraw.getBytes();
        for (int i = 0; i < textToDraw.length(); i++) {
            try {
                width += (int) (this.fontCharWidths[bytesToDraw[i]] * ((double) pixelHeight / this.singleCharDimensions.Y)) + additionalKerning;
            }
            catch (Exception ex) {
                return -1;
            }
        }
        return width;
    }

    public void draw(String textToDraw, int drawPosX, int drawPosY, int pixelHeight) {
        this.draw(textToDraw, drawPosX, drawPosY, pixelHeight, 0);
    }

    public void draw(String textToDraw, int drawPosX, int drawPosY, int pixelHeight, int additionalKerning) {
        this.fontTexture.bind();
        byte[] bytesToDraw = textToDraw.getBytes();
        double singleCharFractionX = (double) 1 / DIM.X;
        double singleCharFractionY = (double) 1 / DIM.Y;

        int offset = 0;
        for (int i = 0; i < textToDraw.length(); i++) {
            try {
                int x = bytesToDraw[i] % DIM.X;
                int y = bytesToDraw[i] / DIM.X;
                int pixelWidth = (int) (this.fontCharWidths[bytesToDraw[i]] * ((double) pixelHeight / this.singleCharDimensions.Y));

                glEnable(GL_TEXTURE_2D);
                glBegin(GL_QUADS);
                // lower left
                glTexCoord2d(x * singleCharFractionX, (y + 1) * singleCharFractionY);
                glVertex2i(drawPosX + offset, drawPosY);
                // lower right
                glTexCoord2d((x + (double) this.fontCharWidths[bytesToDraw[i]] / this.singleCharDimensions.X) * singleCharFractionX, (y + 1) * singleCharFractionY);
                glVertex2i(drawPosX + offset + pixelWidth, drawPosY);
                // upper right
                glTexCoord2d((x + (double) this.fontCharWidths[bytesToDraw[i]] / this.singleCharDimensions.X) * singleCharFractionX, y * singleCharFractionY);
                glVertex2i(drawPosX + offset + pixelWidth, drawPosY + pixelHeight);
                // upper left
                glTexCoord2d(x * singleCharFractionX, y * singleCharFractionY);
                glVertex2i(drawPosX + offset, drawPosY + pixelHeight);
                glEnd();
                glDisable(GL_TEXTURE_2D);

//                glColor4f(0.0f, 1.0f, 1.0f, 0.5f);
//                glRecti(drawPosX + offset, drawPosY, drawPosX + offset + pixelWidth, drawPosY + pixelHeight);
//                glColor3f(1.0f, 1.0f, 1.0f);

                offset += pixelWidth + additionalKerning;
            }
            catch (Exception ex) {
                System.out.println("Character '" + textToDraw.charAt(i) + "' not in found in font.");
            }
        }
    }
}
