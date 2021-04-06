package ingenium.world;

import ingenium.math.Vec3;

public abstract class Position3D {
    protected Vec3 position;
    protected Vec3 rotation;
    protected Vec3 rotationPoint;

    /**
     * 
     * @return the position
     */
    public Vec3 getPosition() {
        return position;
    }

    /**
     * 
     * @param position the position to set
     */
    public void setPosition(Vec3 position) {
        this.position = position;
    }

    /**
     * 
     * @return the rotation point
     */
    public Vec3 getRotationPoint() {
        return rotationPoint;
    }

    /**
     * 
     * @param rotationPoint the rotation point to set
     */
    public void setRotationPoint(Vec3 rotationPoint) {
        this.rotationPoint = rotationPoint;
    }

    /**
     * 
     * @return the rotation
     */
    public Vec3 getRotation() {
        return rotation;
    }

    /**
     * 
     * @param rotation the rotation to set
     */
    public void setRotation(Vec3 rotation) {
        this.rotation = rotation;
    }
}
