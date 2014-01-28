package boxes;

import java.util.ArrayList;

public class EntityContainer extends EntityBase implements IDrawableEntity {

    private int maxCapacity;
    private ArrayList<EntityBase> containedEntities;

    public EntityContainer(double x, double y, EnumEntityId eid, EnumFrameAnimationId aid) {
        super(x, y, eid, aid);
        this.containedEntities = new ArrayList<>();
        this.maxCapacity = 1;
    }

    public EntityContainer(double x, double y, EnumEntityId eid, EnumFrameAnimationId aid, int capacity) {
        super(x, y, eid, aid);
        this.containedEntities = new ArrayList<>();
        this.maxCapacity = (capacity > 0) ? capacity : 1;
    }

    public boolean setMaxCapacity(int capacity) {
        if (capacity >= this.containedEntities.size()) {
            this.maxCapacity = capacity;
            return true;
        }
        else {
            return false;
        }
    }

    public boolean addEntity(EntityBase entity) {
        if (this.containedEntities.size() < this.maxCapacity) {
            if (this.containedEntities.add(entity)) {
                entity.Visible = false;
                return true;
            }
        }
        return false;
    }

    public boolean removeEntity(EntityBase entity) {
        if (this.containedEntities.remove(entity)) {
            entity.Visible = true;
            return true;
        }
        return false;
    }

    public ArrayList<EntityBase> getContent() {
        return this.containedEntities;
    }

    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    public int getFreeCapacity() {
        return this.maxCapacity - this.containedEntities.size();
    }
}
