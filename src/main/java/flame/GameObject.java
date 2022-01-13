package flame;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private String objName;
    private List<Component> objComponents;

    public GameObject(String objName){
        this.objName = objName;
        this.objComponents = new ArrayList<>();
    }

    public <T extends Component> T getComponent(Class<T> componentClass){
        for(Component c : objComponents){
            if(componentClass.isAssignableFrom(c.getClass())){
              try{
                  return componentClass.cast(c);
              }catch (ClassCastException e){
                  assert false : "Error";
              }
            }
        }

        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass){
        for(int i =0; i<objComponents.size(); i++){
            Component c = objComponents.get(i);
            if(componentClass.isAssignableFrom(c.getClass())){
                objComponents.remove(c);
                return;
            }
        }
    }

    public void addComponent(Component c){
        this.objComponents.add(c);
        c.gameObject = this;
    }

    public void update(float dt){
        for(int i=0; i<objComponents.size(); i++){
            objComponents.get(i).update(dt);
        }
    }
    public void start(){
        for(int i=0; i<objComponents.size(); i++){
            objComponents.get(i).start();
        }
    }
 }
