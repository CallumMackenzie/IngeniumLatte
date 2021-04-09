package ingenium.math;

abstract class Vec {
    // Minimum components needed in a vector
    protected float x;
    protected float y;
    protected float w;

    /**
     * 
     * @return the x component
     */
    public float getX() {
        return x;
    }

    /**
     * 
     * @return the y component
     */
    public float getY() {
        return y;
    }

    /**
     * 
     * @return the w component
     */
    public float getW() {
        return w;
    }

    /**
     * 
     * @param x the x value to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * 
     * @param x the x value to set
     */
    public void setX(double x) {
        this.x = (float) x;
    }

    /**
     * 
     * @param y the y value to set
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * 
     * @param y the y value to set
     */
    public void setY(double y) {
        this.y = (float) y;
    }

    /**
     * 
     * @param w the w value to set
     */
    public void setW(float w) {
        this.w = w;
    }

    /**
     * 
     * @param w the w value to set
     */
    public void setW(double w) {
        this.w = (float) w;
    }
}
