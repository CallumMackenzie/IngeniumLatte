import com.jogamp.opengl.GL2;

import ingenium.*;

public class App extends Ingenium {
    public static void main(String[] args) throws Exception {
        new App().start();
    }

    App() {
        super("Ingneium Latte", 1600, 900);
        setClearColour(0xabcdef);
        System.out.println("App start");
    }

    @Override
    public void onCreate(GL2 gl) {

    }

    @Override
    public void onUpdate() {

    }
}
