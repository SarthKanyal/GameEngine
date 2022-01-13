package components;

import flame.Component;

public class FontRenderer extends Component {
    private boolean ft = false;
    @Override
    public void start(){
        if(gameObject.getComponent(SpriteRenderer.class)!= null){
            System.out.println("Found Font Renderer!");
        }
    }
    @Override
    public void update(float dt) {
        if(!ft){
            System.out.println("I am updating too");
            ft = true;
        }
    }


}
