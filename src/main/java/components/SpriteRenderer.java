package components;

import flame.Component;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {
    private boolean ft = false;
    private Vector4f color;

    public SpriteRenderer(Vector4f color){
        this.color=color;
    }
    @Override
    public void update(float dt) {
        if(!ft){
            System.out.println("I am updating");
            ft = true;
        }

    }
    @Override
    public void start(){
        System.out.println("I am starting");
    }

    public Vector4f getColor(){
        return this.color;
    }

}
