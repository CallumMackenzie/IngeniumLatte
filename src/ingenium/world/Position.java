package ingenium.world;

public abstract class Position<positionType, rotationType> {
    protected positionType position;
    protected rotationType rotation;
    protected positionType rotationPoint;

    /**
     * 
     * @return the position
     */
    public positionType getPosition() {
        return position;
    }

    /**
     * 
     * @param position the position to set
     */
    public void setPosition(positionType position) {
        this.position = position;
    }

    /**
     * 
     * @return the rotation point
     */
    public positionType getRotationPoint() {
        return rotationPoint;
    }

    /**
     * 
     * @param rotationPoint the rotation point to set
     */
    public void setRotationPoint(positionType rotationPoint) {
        this.rotationPoint = rotationPoint;
    }

    /**
     * 
     * @return the rotation
     */
    public rotationType getRotation() {
        return rotation;
    }

    /**
     * 
     * @param rotation the rotation to set
     */
    public void setRotation(rotationType rotation) {
        this.rotation = rotation;
    }
}
