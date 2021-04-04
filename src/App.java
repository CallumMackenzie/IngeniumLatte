import com.jogamp.opengl.GL2;

import ingenium.*;
import ingenium.mesh.*;

public class App extends Ingenium {
    Mesh m;

    public static void main(String[] args) throws Exception {
        new App().start();
    }

    App() {
        super("Ingneium Latte", 1600, 900);
        setClearColour(0xabcdef);
        System.out.println("App start");
        m = new Mesh();
        m.loadFromObjData(Utils.getFileAsString("D:\\Coding\\JavaScript\\IngeniumWeb\\resource\\uvsmoothnt.obj"));
    }

    @Override
    public void onCreate(GL2 gl) {
        m.load(gl);
    }

    @Override
    public void onUpdate() {

    }
}
