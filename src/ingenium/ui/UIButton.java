package ingenium.ui;

import com.jogamp.opengl.GL2;

import ingenium.Ingenium;
import ingenium.math.Vec2;
import ingenium.math.Vec3;
import ingenium.mesh.Mesh2D;
import ingenium.world.Camera2D;

public class UIButton extends Mesh2D {
    
    public UIButton (Vec2 pos, Vec2 dimensions) {
        super(pos);
        this.scale = dimensions;
    }

    // public boolean isClicked (ingenium.Input input) {
    //     Vec2 mPos = input.getRawMousePos();
    //     return (mPos.getX())
    // }

    public static UIButton createAndMake (GL2 gl, Vec2 pos, Vec2 dimensions, String texturePath) {
        UIButton uic = new UIButton(pos, dimensions);
        uic.make(gl, texturePath);
        return uic;
    }
}
