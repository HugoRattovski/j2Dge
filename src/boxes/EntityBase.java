package boxes;

import static org.lwjgl.opengl.GL11.*;

public class EntityBase implements IDrawableEntity {

    protected Coords2dD Position;
    private EnumEntityId entityId;
    private EnumFrameAnimationId currentAnimId;
    protected EnumFrameAnimationType animationType;
    private EnumFrameSequenceId currentSeqId;
    private FrameAnimationSheet animationSpriteSheet;
    private long timeDelta;
    public boolean Visible;

    // place this entity exactly on the given point
    public EntityBase(double x, double y, EnumEntityId eid, EnumFrameAnimationId aid) {
        this.Position = new Coords2dD(x, y);
        this.entityId = eid;
        this.setAnimation(aid);
        this.currentSeqId = EnumFrameSequenceId.SEQ_IDLE;
        this.timeDelta = 0;
        this.Visible = true;
    }

    public void setAnimation(EnumFrameAnimationId id) {
        if (this.animationSpriteSheet != null) {
            if (this.animationSpriteSheet.sequenceExists(id, EnumFrameSequenceId.SEQ_WALK_NORTHEAST)) {
                this.animationType = EnumFrameAnimationType.ANIM_TYPE_8_DIRECTIONS;
            }
            else {
                if (this.animationSpriteSheet.sequenceExists(id, EnumFrameSequenceId.SEQ_WALK_NORTH)) {
                    this.animationType = EnumFrameAnimationType.ANIM_TYPE_4_DIRECTIONS;
                }
                else {
                    this.animationType = EnumFrameAnimationType.ANIM_TYPE_NO_DIRECTIONS;
                }
            }
        }
        this.currentAnimId = id;
    }

    public void setSequence(EnumFrameSequenceId id) {
        this.currentSeqId = id;
    }

    public void setSpriteSheet(FrameAnimationSheet animationSpriteSheet) {
        this.animationSpriteSheet = animationSpriteSheet;
        this.setAnimation(this.currentAnimId);
    }

    // place this entity exactly on the given point
    public void setPosition(double x, double y) {
        this.Position.X = x;
        this.Position.Y = y;
    }

    public EnumFrameAnimationType getAnimationType() {
        return this.animationType;
    }

    @Override
    public Coords2dI getScaledWorldPosition(int baseSize) {
        return new Coords2dI((int) (this.Position.X * baseSize), (int) (this.Position.Y * baseSize));
    }

    @Override
    public void update(long delta) {
    }

    @Override
    public void draw(int shiftX, int shiftY, int baseSize, double zoom, long delta) {
        // delta anpassen
        int animationDuration = this.animationSpriteSheet.getSequenceDuration(currentAnimId, currentSeqId);
        if (animationDuration != 0) {
            this.timeDelta = (this.timeDelta + delta) % animationDuration;
        }
        SubTextureCoordsD textureCoords = this.animationSpriteSheet.getSubTextureCoordsByIds(currentAnimId, currentSeqId, this.timeDelta);
        if (textureCoords != null) {
            // shift the texture half the size the lower left (-0.5d)
            int llx = (int) (zoom * ((this.Position.X - 0.5d) * baseSize - shiftX));
            int lly = (int) (zoom * ((this.Position.Y - 0.5d) * baseSize - shiftY));
            int edge = (int) (zoom * baseSize);
            glEnable(GL_TEXTURE_2D);
            glBegin(GL_QUADS);
            // lower left
            glTexCoord2d(textureCoords.upperLeftX, textureCoords.lowerRightY);
            glVertex2i(llx, lly);
            // lower right
            glTexCoord2d(textureCoords.lowerRightX, textureCoords.lowerRightY);
            glVertex2i(llx + edge, lly);
            // upper right
            glTexCoord2d(textureCoords.lowerRightX, textureCoords.upperLeftY);
            glVertex2i(llx + edge, lly + edge);
            // upper left
            glTexCoord2d(textureCoords.upperLeftX, textureCoords.upperLeftY);
            glVertex2i(llx, lly + edge);
            glEnd();
            glDisable(GL_TEXTURE_2D);
        }
    }
}
