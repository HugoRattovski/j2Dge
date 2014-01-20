package boxes;

/**
 * @author yes-man
 * @version 0.1
 */
import java.util.Random;
import org.lwjgl.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;

public class BoxesTest {

    // statics
    private static final int DISPLAY_WIDTH = 500;
    private static final int DISPLAY_HEIGHT = 300;
    private static final int DISPLAY_SYNC_RATE = 60;
    private static final int BASE_TILE_SIZE = 80;
    // X,Y-position
    private Coords2dI renderShift = new Coords2dI(-5 * BASE_TILE_SIZE, -5 * BASE_TILE_SIZE);
    // zoom (old & current)
    private double renderZoomFactorOld = 0;
    private double renderZoomFactorCurrent = 0.5f;
    // actual field
    private TileManager field;
    private EntityManager entityManager;
    // time delta & fps counting
    private long lastFpsTimeStamp;
    private long lastFrameTimeStamp;
    private int lastFpsCounter;
    private int currentFpsCounter;
    // textures
    private OpenGlFont font;
    private SpriteSheet tileSpriteSheet;
    private SpriteSheet entitySpriteSheet;
    private FrameAnimationSheet entityAnimationSheet;

    public static void main(String[] args) {
        BoxesTest boxTest;
        try {
            boxTest = new BoxesTest();
            boxTest.init();
            boxTest.run();
        }
        catch (LWJGLException ex) {
            System.out.println(ex.toString());
        }
    }

    private void init() throws LWJGLException {
//        FrameAnimationData animData = new FrameAnimationData();
//        animData.SheetDimensions = new Coords2dI(1, 1);
//        animData.Animations = new HashMap<>();
//        FrameAnimation anim = new FrameAnimation();
//        anim.Sequences = new HashMap<>();
//        FrameSequence seq = new FrameSequence();
//        seq.FrameCount = 1;
//        seq.TimeMs = 0;
//        seq.FrameCoords = new Coords2dI[1];
//        seq.FrameCoords[0] = new Coords2dI(0, 0);
//        anim.Sequences.put(EnumFrameSequenceId.SEQ_IDLE, seq);
//        animData.Animations.put(EnumFrameAnimationId.ANIM_DUMMY, anim);
//        GameUtils.writeObjectToXmlFile(animData, "AnimationSpriteSheetData.xml");
        // load external files
        this.tileSpriteSheet = new SpriteSheet("res/BackgroundSpriteSheet");
        this.entitySpriteSheet = new SpriteSheet("res/EntitySpriteSheet");
        this.entityAnimationSheet = new FrameAnimationSheet("res/AnimationSpriteSheetData");

        // create display
        Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
        Display.setFullscreen(false);
        Display.setTitle(""); // will be set by frame counter
        Display.create();

        // keyboard
        Keyboard.create();

        // mouse
        Mouse.setGrabbed(false);
        Mouse.create();

        // openGl
        // switch to the projection matrix for the next operations
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0f, DISPLAY_WIDTH, 0.0f, DISPLAY_HEIGHT, 1, -1);
        // switch back to the model view matrix
        glMatrixMode(GL_MODELVIEW);

        // smooth shading model (as opposed to GL_FLAT)
        glShadeModel(GL_SMOOTH);
        // enable blending for transparent colors
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        // alpha test for transparent images
        glEnable(GL_ALPHA_TEST);
        glAlphaFunc(GL_GREATER, 0.0f);

        // load texture data from HDD
        this.tileSpriteSheet.initSheet();
        this.entitySpriteSheet.initSheet();
        this.entityAnimationSheet.initSheet();
        this.font = new OpenGlFont("res/font.png", "res/fontCharWidths.xml");

        // field
        this.field = new TileManager(200, 100, BASE_TILE_SIZE, this.tileSpriteSheet);
        // entities
        this.entityManager = new EntityManager(BASE_TILE_SIZE, this.entityAnimationSheet);
        Random rng = new Random(1337);
        for (int x = 0; x < 200; x++) {
            for (int y = 0; y < 100; y++) {
                if (rng.nextInt(10) == 7) {
                    this.entityManager.addEntity(new EntityBase(x + 0.5d, y + 0.5d, EnumEntityId.ENTITY_TREE, EnumFrameAnimationId.ANIM_TREE_01));
                }
            }
        }
        this.entityManager.addEntity(new EntityBase(0.5d, 0.5d, EnumEntityId.ENTITY_TREE, EnumFrameAnimationId.ANIM_CHEST_01));
        this.entityManager.addEntity(new EntityBase(1.5d, 0.5d, EnumEntityId.ENTITY_TREE, EnumFrameAnimationId.ANIM_FIREBALL));
    }

    private void run() {
        // init frame counter and delta counter before loop
        long delta = 0;
        lastFpsTimeStamp = getTime();
        lastFrameTimeStamp = getTime();
        // main loop
        while (!Display.isCloseRequested()) {
            // process input
            processKeyboard();
            processMouse();
            // get delta time to last frame
            delta = getTime() - lastFrameTimeStamp;
            // update game state (selection, orders etc)
            updateGameState(delta);
            // render new game state
            render(delta);
            updateFpsCounter();
            // swap buffers etc
            Display.update();
//            Display.sync(DISPLAY_SYNC_RATE);
        }
        Display.destroy();
    }

    private void processKeyboard() {
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            this.renderShift.Y -= BASE_TILE_SIZE;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            this.renderShift.Y += BASE_TILE_SIZE;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            this.renderShift.X += BASE_TILE_SIZE;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            this.renderShift.X -= BASE_TILE_SIZE;
        }
    }

    private void processMouse() {
        int mouseX = Mouse.getX();
        int mouseY = Mouse.getY();
        int mouseDX = Mouse.getDX();
        int mouseDY = Mouse.getDY();
        int mouseDW = Mouse.getDWheel();
        int onScreenWorldX = (int) (DISPLAY_WIDTH / this.renderZoomFactorCurrent);
        int onScreenWorldY = (int) (DISPLAY_HEIGHT / this.renderZoomFactorCurrent);
        double percentX = (double) mouseX / DISPLAY_WIDTH;
        double percentY = (double) mouseY / DISPLAY_HEIGHT;
        int gamePosX = (int) (percentX * onScreenWorldX) + this.renderShift.X;
        int gamePosY = (int) (percentY * onScreenWorldY) + this.renderShift.Y;
        int tileUnderCursorX = (gamePosX >= 0) ? gamePosX / BASE_TILE_SIZE : gamePosX / BASE_TILE_SIZE - 1;
        int tileUnderCursorY = (gamePosY >= 0) ? gamePosY / BASE_TILE_SIZE : gamePosY / BASE_TILE_SIZE - 1;

//        System.out.println(gamePosX + ":" + gamePosY + "  -  " + tileUnderCursorX + ":" + tileUnderCursorY + "  -  " + mouseX + "(" + mouseDX + ") : " + mouseY + "(" + mouseDY + ")");

        if (Mouse.isButtonDown(0)) {
            this.field.setActiveTile(tileUnderCursorX, tileUnderCursorY);
            this.renderShift.X -= Math.ceil((double) mouseDX / this.renderZoomFactorCurrent);
            this.renderShift.Y -= Math.ceil((double) mouseDY / this.renderZoomFactorCurrent);
        }
        if (mouseDW != 0) {
            // backup old values
            this.renderZoomFactorOld = this.renderZoomFactorCurrent;
            // zoom out
            if (mouseDW < 0) {
                this.renderZoomFactorCurrent -= (this.renderZoomFactorCurrent > 0.0625f) ? 0.0625f : 0;
            }
            // zoom in
            else {
                this.renderZoomFactorCurrent += 0.0625f;
                if (this.renderZoomFactorCurrent > 1) {
                    this.renderZoomFactorCurrent = 1;
                }
            }
            // calculate new values
            int distanceShiftToScaleCenterX = gamePosX - this.renderShift.X;
            int distanceShiftToScaleCenterY = gamePosY - this.renderShift.Y;
            this.renderShift.X = gamePosX - (int) (distanceShiftToScaleCenterX * (this.renderZoomFactorOld / this.renderZoomFactorCurrent));
            this.renderShift.Y = gamePosY - (int) (distanceShiftToScaleCenterY * (this.renderZoomFactorOld / this.renderZoomFactorCurrent));
        }
    }

    private void updateGameState(long timeDelta) {
    }

    private void render(long timeDelta) {
        // reset buffers
        glClear(GL_COLOR_BUFFER_BIT);// | GL_DEPTH_BUFFER_BIT);
        // draw field
        int boxCount = this.field.draw(this.renderShift.X, this.renderShift.Y, DISPLAY_WIDTH, DISPLAY_HEIGHT, this.renderZoomFactorCurrent);
        int entityCount = this.entityManager.drawAllEntities(this.renderShift.X, this.renderShift.Y, DISPLAY_WIDTH, DISPLAY_HEIGHT, this.renderZoomFactorCurrent, timeDelta);
        String boxString = "Tiles: " + String.valueOf(boxCount);
        String entityString = "Entities: " + String.valueOf(entityCount);
        this.font.draw(boxString, DISPLAY_WIDTH - this.font.calculateWidth(boxString, 20) - 30, DISPLAY_HEIGHT - 40, 20);
        this.font.draw(entityString, DISPLAY_WIDTH - this.font.calculateWidth(entityString, 20) - 30, DISPLAY_HEIGHT - 60, 20);
    }

    private long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    private void updateFpsCounter() {
        String fpsString = "FPS: " + String.valueOf(this.lastFpsCounter);
        this.font.draw(fpsString, DISPLAY_WIDTH - this.font.calculateWidth(fpsString, 20) - 30, DISPLAY_HEIGHT - 20, 20);
        if (getTime() - this.lastFpsTimeStamp > 1000) {
            this.lastFpsCounter = this.currentFpsCounter;
            currentFpsCounter = 0;
            this.lastFpsTimeStamp += 1000;
        }
        currentFpsCounter++;
    }
}
