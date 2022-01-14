package flame;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    private boolean isRunning = false;
    protected Camera camera;
    protected List<GameObject> gameObjects = new ArrayList<>();
    public Scene(){

    }

    public abstract void update(float dt);

    public void init(){

    }

    public void start(){
        for(GameObject g : gameObjects){
            g.start();
        }
        isRunning = true;
    }

    public void addObjToScene(GameObject obj){
        if(!isRunning){
            gameObjects.add(obj);
        }else {
            gameObjects.add(obj);
            obj.start();
        }
    }

    public Camera getCamera(){
        return this.camera;
    }


}
