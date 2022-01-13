package components;

import flame.Component;

public class SpriteRenderer extends Component {
    private boolean ft = false;
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
}
