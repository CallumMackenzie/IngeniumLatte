package ingenium;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import ingenium.math.Vec2;
import ingenium.world.Camera2D;
import java.awt.MouseInfo;

public class Input implements java.awt.event.KeyListener, java.awt.event.MouseListener {
    private java.util.List<Integer> keys = new ArrayList<Integer>();
    private MouseEvent mousePressedEvent;
    private MouseEvent mouseReleasedEvent;
    private MouseEvent mouseEnteredEvent;
    private MouseEvent mouseExitedEvent;
    private MouseEvent mouseClickedEvent;

    public void keyPressed(KeyEvent e) { // When key is pressed
        int key = e.getKeyCode();
        if (!keys.contains(key))
            keys.add(key);
    }

    public void keyReleased(KeyEvent e) { // When key is released
        int key = e.getKeyCode();
        if (keys.contains(key))
            keys.remove(keys.indexOf(key));
    }

    public void keyTyped(KeyEvent e) { // When key is typed
        int key = e.getKeyCode();
        if (!keys.contains(key))
            keys.add(key);
    }

    public boolean getKeyState(int pre) {
        if (keys.contains(pre))
            return true;
        return false;
    }

    public void attach(java.awt.Frame frame) {
        frame.addKeyListener(this);
        frame.addMouseListener(this);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        frame.requestFocus();
    }

    public void mousePressed(MouseEvent e) {
        mousePressedEvent = e;
    }

    public void mouseReleased(MouseEvent e) {
        mousePressedEvent = null;
        mouseReleasedEvent = e;
    }

    public void mouseEntered(MouseEvent e) {
        mouseEnteredEvent = e;
    }

    public void mouseExited(MouseEvent e) {
        mouseExitedEvent = e;
    }

    public void mouseClicked(MouseEvent e) {
        mouseClickedEvent = e;
    }

    public int getRawMouseX() {
        return (int) Math.round(MouseInfo.getPointerInfo().getLocation().getX());
    }

    public int getRawMouseY() {
        return (int) Math.round(MouseInfo.getPointerInfo().getLocation().getY());
    }

    public Vec2 getRawMousePos() {
        return new Vec2(getRawMouseX(), getRawMouseY());
    }

    public Vec2 getCameraMousePos(Camera2D camera, GLWindow window) {
        Vec2 pos = getRawMousePos().sub(window.getFramePos()).div(window.getFrameDimensions());
        pos = pos.mul(new Vec2(1f / camera.getAspect() * 2, -1 * 2)).mulMat2(camera.cameraMatrix()).add(camera.getPosition());
        // System.out.println(pos);
        return pos;//.mulMat2(camera.cameraMatrix()).add(camera.getPosition());
    }

    public boolean getMouseButton(int button) {
        if (mousePressedEvent != null)
            if (mousePressedEvent.getButton() == button)
                return true;
        return false;
    }

    public MouseEvent getMouseClickedEvent() {
        return mouseClickedEvent;
    }

    public MouseEvent getMouseEnteredEvent() {
        return mouseEnteredEvent;
    }

    public MouseEvent getMouseExitedEvent() {
        return mouseExitedEvent;
    }

    public MouseEvent getMousePressedEvent() {
        return mousePressedEvent;
    }

    public MouseEvent getMouseReleasedEvent() {
        return mouseReleasedEvent;
    }
}