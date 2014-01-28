package boxes;

import java.util.Map;
import org.newdawn.slick.opengl.Texture;

public class FrameAnimationSheet {

    private String sourceFilename;
    private Texture SheetTexture;
    private FrameAnimationData Animations;
    private double fractionX, fractionY;

    /*
     * Define a new sprite sheet. It will be initialized
     * later on from two files called 'filename.png' (the
     * actual sheet) and 'filename.xml' (description file).
     */
    public FrameAnimationSheet(String filename) {
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
        this.SheetTexture = GameUtils.loadTexturePng(this.sourceFilename + ".png");
        this.Animations = GameUtils.loadObjectFromXmlFile(this.Animations, sourceFilename + ".xml");
        // calculate sheet fractions
        this.fractionX = (double) 1 / this.Animations.SheetDimensions.X;
        this.fractionY = (double) 1 / this.Animations.SheetDimensions.Y;

        // init sequences
        for (Map.Entry<EnumFrameAnimationId, FrameAnimation> anim : this.Animations.Animations.entrySet()) {
            for (Map.Entry<EnumFrameSequenceId, FrameSequence> seq : anim.getValue().Sequences.entrySet()) {
                seq.getValue().init();
            }
        }
    }

    /*
     * Private method to securely get an animation or at least a dummy.
     */
    private FrameAnimation getAnimationById(EnumFrameAnimationId id) {
        if (this.Animations.Animations.containsKey(id)) {
            return this.Animations.Animations.get(id);
        }
        else {
            return this.Animations.Animations.get(EnumFrameAnimationId.ANIM_DUMMY);
        }
    }

    public void bindTexture() {
        this.SheetTexture.bind();
    }

    public SubTextureCoordsD getSubTextureCoordsByIds(EnumFrameAnimationId animId, EnumFrameSequenceId seqId, long delta) {
        SubTextureCoordsI spriteCoords = this.getAnimationById(animId).getSequence(seqId).getFrameCoords(delta);
        if (spriteCoords.flippedHorizontally) {
            if (spriteCoords.flippedVertically) {
                return new SubTextureCoordsD(fractionX * (spriteCoords.X + 1), fractionY * (spriteCoords.Y + 1), fractionX * spriteCoords.X, fractionY * spriteCoords.Y);
            }
            else {
                return new SubTextureCoordsD(fractionX * spriteCoords.X, fractionY * (spriteCoords.Y + 1), fractionX * (spriteCoords.X + 1), fractionY * spriteCoords.Y);
            }
        }
        else {
            if (spriteCoords.flippedVertically) {
                return new SubTextureCoordsD(fractionX * (spriteCoords.X + 1), fractionY * spriteCoords.Y, fractionX * spriteCoords.X, fractionY * (spriteCoords.Y + 1));
            }
            else {
                return new SubTextureCoordsD(fractionX * spriteCoords.X, fractionY * spriteCoords.Y, fractionX * (spriteCoords.X + 1), fractionY * (spriteCoords.Y + 1));
            }
        }

    }

    public int getSequenceDuration(EnumFrameAnimationId animId, EnumFrameSequenceId seqId) {
        return this.getAnimationById(animId).getSequence(seqId).TimeMs;
    }

    public boolean sequenceExists(EnumFrameAnimationId animId, EnumFrameSequenceId seqId) {
        return this.getAnimationById(animId).Sequences.containsKey(seqId);
    }
}
