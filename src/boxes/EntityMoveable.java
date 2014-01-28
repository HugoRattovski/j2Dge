package boxes;

import java.util.Random;

public class EntityMoveable extends EntityContainer implements IDrawableEntity {

    private double currentSpeed, targetSpeed, maxSpeed, acceleration;
    private double currentDirection, targetDirection, turnSpeed;
    private Random rng = new Random();

    public EntityMoveable(double x, double y, EnumEntityId eid, EnumFrameAnimationId aid) {
        super(x, y, eid, aid);
        this.currentSpeed = 0;
        this.targetSpeed = 0;
        this.maxSpeed = 1;
        this.acceleration = 1;
        this.currentDirection = 0;
        this.targetDirection = 0;
        this.turnSpeed = Math.PI;
    }

    public EntityMoveable(double x, double y, EnumEntityId eid, EnumFrameAnimationId aid, double accel, double maxSpeed) {
        super(x, y, eid, aid);
        this.currentSpeed = 0;
        this.targetSpeed = 0;
        this.currentDirection = 0;
        this.targetDirection = 0;
        this.turnSpeed = Math.PI;
    }

    public double getMaxSpeed() {
        return this.maxSpeed;
    }

    public void setMaxSpeed(double tilesPerSecond) {
        if (tilesPerSecond > 0) {
            this.maxSpeed = tilesPerSecond;
        }
    }

    public double getTargetSpeed() {
        return this.targetSpeed;
    }

    public void setTargetSpeed(double tilesPerSecond) {
        if (tilesPerSecond > 0) {
            this.targetSpeed = (tilesPerSecond <= this.maxSpeed) ? tilesPerSecond : this.maxSpeed;
        }
        else {
            this.targetSpeed = 0;
        }
    }

    public double getAcceleration() {
        return this.acceleration;
    }

    public void setAcceleration(double accel) {
        if (accel > 0) {
            this.acceleration = accel;
        }
    }

    private static int radToDeg(double rad) {
        return (int) (rad / Math.PI * 180);
    }

    private static double degToRad(int deg) {
        return (double) deg / 180 * Math.PI;
    }

    public int getDirectionDeg() {
        return radToDeg(this.targetDirection);
    }

    public void setDirectionDeg(int degrees) {
        if (degrees > 0) {
            this.targetDirection = degToRad(degrees);
        }
    }

    public double getDirectionRad() {
        return this.targetDirection;
    }

    public void setDirectionRad(double rad) {
        if ((rad >= 0) && (rad <= Math.PI * 2)) {
            this.targetDirection = rad;
        }
    }

    public int getMaxTurnSpeedDeg() {
        return radToDeg(this.turnSpeed);
    }

    public void setMaxTurnSpeedDeg(int degrees) {
        if (degrees > 0) {
            this.turnSpeed = degToRad(degrees);
        }
    }

    public double getMaxTurnSpeedRad() {
        return this.turnSpeed;
    }

    public void setMaxTurnSpeedRad(double rad) {
        if (rad > 0) {
            this.turnSpeed = rad;
        }
    }

    private static EnumFrameSequenceId getSequence4D(double direction, double speed) {
        int tmp = (int) (direction / (Math.PI / 4));
        if (speed > 0) {
            switch (tmp) {
                case 0:
                case 7:
                    return EnumFrameSequenceId.SEQ_WALK_NORTH;
                case 6:
                case 5:
                    return EnumFrameSequenceId.SEQ_WALK_WEST;
                case 4:
                case 3:
                    return EnumFrameSequenceId.SEQ_WALK_SOUTH;
                case 2:
                case 1:
                    return EnumFrameSequenceId.SEQ_WALK_EAST;
                default:
                    return EnumFrameSequenceId.SEQ_IDLE;
            }
        }
        else {
            switch (tmp) {
                case 0:
                case 7:
                    return EnumFrameSequenceId.SEQ_IDLE_NORTH;
                case 6:
                case 5:
                    return EnumFrameSequenceId.SEQ_IDLE_WEST;
                case 4:
                case 3:
                    return EnumFrameSequenceId.SEQ_IDLE_SOUTH;
                case 2:
                case 1:
                    return EnumFrameSequenceId.SEQ_IDLE_EAST;
                default:
                    return EnumFrameSequenceId.SEQ_IDLE;
            }
        }
    }

    private static EnumFrameSequenceId getSequence8D(double direction, double speed) {
        int tmp = (int) (direction / (Math.PI / 8));
        if (speed > 0) {
            switch (tmp) {
                case 0:
                case 15:
                    return EnumFrameSequenceId.SEQ_WALK_NORTH;
                case 14:
                case 13:
                    return EnumFrameSequenceId.SEQ_WALK_NORTHWEST;
                case 12:
                case 11:
                    return EnumFrameSequenceId.SEQ_WALK_WEST;
                case 10:
                case 9:
                    return EnumFrameSequenceId.SEQ_WALK_SOUTHWEST;
                case 8:
                case 7:
                    return EnumFrameSequenceId.SEQ_WALK_SOUTH;
                case 6:
                case 5:
                    return EnumFrameSequenceId.SEQ_WALK_SOUTHEAST;
                case 4:
                case 3:
                    return EnumFrameSequenceId.SEQ_WALK_EAST;
                case 2:
                case 1:
                    return EnumFrameSequenceId.SEQ_WALK_NORTHEAST;
                default:
                    return EnumFrameSequenceId.SEQ_IDLE;
            }
        }
        else {
            switch (tmp) {
                case 0:
                case 15:
                    return EnumFrameSequenceId.SEQ_IDLE_NORTH;
                case 14:
                case 13:
                    return EnumFrameSequenceId.SEQ_IDLE_NORTHWEST;
                case 12:
                case 11:
                    return EnumFrameSequenceId.SEQ_IDLE_WEST;
                case 10:
                case 9:
                    return EnumFrameSequenceId.SEQ_IDLE_SOUTHWEST;
                case 8:
                case 7:
                    return EnumFrameSequenceId.SEQ_IDLE_SOUTH;
                case 6:
                case 5:
                    return EnumFrameSequenceId.SEQ_IDLE_SOUTHEAST;
                case 4:
                case 3:
                    return EnumFrameSequenceId.SEQ_IDLE_EAST;
                case 2:
                case 1:
                    return EnumFrameSequenceId.SEQ_IDLE_NORTHEAST;
                default:
                    return EnumFrameSequenceId.SEQ_IDLE;
            }
        }
    }

    public void move(long deltaMs) {
        double timeFraction = (double) deltaMs / 1000;
        // adjust speed
        this.currentSpeed += Math.signum(this.targetSpeed - this.currentSpeed) * this.acceleration * timeFraction;
        if (this.currentSpeed < 0) {
            this.currentSpeed = 0;
        }
//        if (this.currentSpeed > this.targetSpeed) {
//            this.currentSpeed = this.targetSpeed;
//        }

        double directionDifferenceAbs = this.targetDirection - this.currentDirection;
        double differenceAbsSignum = Math.signum(directionDifferenceAbs);
        double directionDifferenceSigned = 0;
        // check if there is anything to do at all
        if (differenceAbsSignum != 0) {
            System.out.println("[" + (this.rng.nextInt(9000) + 1000) + "] curDir: " + radToDeg(this.currentDirection) + " tarDir: " + radToDeg(this.targetDirection) + " dirDifAbs: " + radToDeg(directionDifferenceAbs));
            if (differenceAbsSignum == 1) {
                directionDifferenceSigned = (directionDifferenceAbs < Math.PI) ? directionDifferenceAbs : this.targetDirection - 2 * Math.PI - this.currentDirection;
            }
            else {
                directionDifferenceSigned = (directionDifferenceAbs >= -1 * Math.PI) ? directionDifferenceAbs : directionDifferenceAbs + 2 * Math.PI;
            }
        }
        this.currentDirection += Math.signum(directionDifferenceSigned) * this.turnSpeed * timeFraction;
        if (this.currentDirection < 0) {
            this.currentDirection += Math.PI * 2;
        }
        if (this.currentDirection >= Math.PI * 2) {
            this.currentDirection -= Math.PI * 2;
        }
        if (Math.abs(this.targetDirection - this.currentDirection) < 0.01d) {
            this.currentDirection = this.targetDirection;
        }
        // actually move
        this.setPosition(this.Position.X + Math.sin(this.currentDirection) * this.currentSpeed * timeFraction, this.Position.Y + Math.cos(this.currentDirection) * this.currentSpeed * timeFraction);
    }

    @Override
    public void update(long delta) {
        this.move(delta);
    }

    @Override
    public void draw(int shiftX, int shiftY, int baseSize, double zoom, long delta) {
        switch (this.animationType) {
            case ANIM_TYPE_8_DIRECTIONS:
                super.setSequence(getSequence8D(this.currentDirection, this.currentSpeed));
                break;
            case ANIM_TYPE_4_DIRECTIONS:
                super.setSequence(getSequence4D(this.currentDirection, this.currentSpeed));
                break;
        }
        super.draw(shiftX, shiftY, baseSize, zoom, delta);
    }
}
