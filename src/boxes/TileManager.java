package boxes;

import java.util.Random;
import static org.lwjgl.opengl.GL11.*;

public class TileManager {

    private int boxSizeBase = 50;
    private Coords2dI maxDimensions;
    private Coords2dI activeTile = new Coords2dI(-1, -1);
    private Tile[][] field;
    private SpriteSheet spriteSheet;

    public TileManager(int dimX, int dimY, int baseSize, SpriteSheet spriteSheet) {
        this.maxDimensions = new Coords2dI(dimX, dimY);
        this.boxSizeBase = baseSize;
        this.spriteSheet = spriteSheet;

        Random rng = new Random(1337);

        this.field = new Tile[this.maxDimensions.Y][this.maxDimensions.X];
        for (int y = 0; y < this.maxDimensions.Y; y++) {
            for (int x = 0; x < this.maxDimensions.X; x++) {
                switch (rng.nextInt(5)) {
                    case 0:
                        this.field[y][x] = new Tile(EnumTextureId.TEX_BG_FERTILE, this.spriteSheet);
                        break;
                    case 1:
                        this.field[y][x] = new Tile(EnumTextureId.TEX_BG_NORMAL_NNNN, this.spriteSheet);
                        break;
                    case 2:
                        this.field[y][x] = new Tile(EnumTextureId.TEX_BG_DESERT, this.spriteSheet);
                        break;
                    case 3:
                        this.field[y][x] = new Tile(EnumTextureId.TEX_BG_WATER, this.spriteSheet);
                        break;
                    case 4:
                        this.field[y][x] = new Tile(EnumTextureId.TEX_BG_SNOW, this.spriteSheet);
                        break;
                    case 5:
                        this.field[y][x] = new Tile(EnumTextureId.TEX_BG_CREEPER, this.spriteSheet);
                        break;
                }
            }
        }
    }

    public void setActiveTile(int x, int y) {
        if ((x >= 0) && (y >= 0) && (x < this.maxDimensions.X) && (y < this.maxDimensions.Y)) {
            this.activeTile.X = x;
            this.activeTile.Y = y;
        }
    }

    public int draw(int shiftX, int shiftY, int displayX, int displayY, double zoom) {
        int firstTileX = (shiftX >= 0) ? shiftX / this.boxSizeBase : shiftX / this.boxSizeBase - 1;
        //@me: BEWARE numeric error
        int lastTileX = (int) Math.ceil(displayX / zoom / this.boxSizeBase + firstTileX);
        int firstTileY = (shiftY >= 0) ? shiftY / this.boxSizeBase : shiftY / this.boxSizeBase - 1;
        int lastTileY = (int) Math.ceil(displayY / zoom / this.boxSizeBase + firstTileY);
        int zoomedBoxSize = (int) (this.boxSizeBase * zoom);
        int zoomedShiftX = (int) (shiftX * zoom);
        int zoomedShiftY = (int) (shiftY * zoom);

        int boxesDrawn = 0;

        this.spriteSheet.sheetTexture.bind();
        for (int y = (firstTileY >= 0 ? firstTileY : 0); y <= (lastTileY < this.field.length ? lastTileY : this.field.length - 1); y++) {
            for (int x = (firstTileX >= 0 ? firstTileX : 0); x <= (lastTileX < this.field[0].length ? lastTileX : this.field[0].length - 1); x++) {
                this.field[y][x].drawTextured(x * zoomedBoxSize - zoomedShiftX, y * zoomedBoxSize - zoomedShiftY, zoomedBoxSize);
                boxesDrawn++;
                if ((this.activeTile.X == x) && (this.activeTile.Y == y)) {
                    glColor4f(0.3f, 0.3f, 1.0f, 0.5f);
                    glRecti(x * zoomedBoxSize - zoomedShiftX, y * zoomedBoxSize - zoomedShiftY, x * zoomedBoxSize - zoomedShiftX + zoomedBoxSize, y * zoomedBoxSize - zoomedShiftY + zoomedBoxSize);
                    glColor3f(1.0f, 1.0f, 1.0f);
                }
            }
        }

        return boxesDrawn;
    }
}
