package ingenium;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Input implements java.awt.event.KeyListener {
    private java.util.List<Integer> keys = new ArrayList<Integer>();

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
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        frame.requestFocus();
    }
}