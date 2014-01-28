package boxes;

import java.util.ArrayList;

public class EntityManager {

    private ArrayList<EntityBase> allEntities;
    private int entityBaseSize;
    private FrameAnimationSheet animationSpriteSheet;

    public EntityManager(int baseSize, FrameAnimationSheet spriteSheet) {
        this.allEntities = new ArrayList<>();
        this.entityBaseSize = baseSize;
        this.animationSpriteSheet = spriteSheet;
    }

    public boolean addEntity(EntityBase entity) {
        entity.setSpriteSheet(this.animationSpriteSheet);
        return this.allEntities.add(entity);
    }

    public boolean remEntity(EntityBase entity) {
        return this.allEntities.remove(entity);
    }

    public int updateAllEntities(long timeDelta){
        int entityCount = 0;
        for (IDrawableEntity entity : this.allEntities) {
            entity.update(timeDelta);
            entityCount++;
        }
        return entityCount;
    }

    public int drawAllEntities(int shiftX, int shiftY, int displayX, int displayY, double zoom, long timeDelta) {
        int entityCount = 0;
        int halfZoomedBaseSize = (int) (entityBaseSize * zoom * 0.5f);
        int minX = shiftX - halfZoomedBaseSize;
        int minY = shiftY - halfZoomedBaseSize;
        int maxX = (int) ((double) displayX / zoom + shiftX) + halfZoomedBaseSize;
        int maxY = (int) ((double) displayY / zoom + shiftY) + halfZoomedBaseSize;

        this.animationSpriteSheet.bindTexture();
        for (IDrawableEntity entity : this.allEntities) {
            Coords2dI position = entity.getScaledWorldPosition(this.entityBaseSize);
            if (entity.Visible && (minX <= position.X) && (maxX >= position.X) && (minY <= position.Y) && (maxY >= position.Y)) {
                entityCount++;
                entity.draw(shiftX, shiftY, this.entityBaseSize, zoom, timeDelta);
            }
        }
        return entityCount;
    }
}
