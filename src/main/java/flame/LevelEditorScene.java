package flame;

import components.FontRenderer;
import components.SpriteRenderer;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.CallbackI;
import render.Shader;
import render.Texture;
import util.Time;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private int vertexID,fragmentID,shaderProgram;

    private float[] vertexArray = {
      //position                  //color                        //UV coords
      50.5f, -50.5f, 0.0f,           1.0f, 0.0f, 0.0f, 1.0f,     1,0, //bottom right 0
     -50.5f,  50.5f, 0.0f,           0.0f, 1.0f, 0.0f, 1.0f,     0,1, //top left 1
      50.5f,  50.5f, 0.0f,           0.0f, 0.0f, 1.0f, 1.0f,     1,1, // top right 2
     -50.5f, -50.5f, 0.0f,           1.0f, 1.0f, 0.0f, 1.0f,     0,0//bottom left 3
    };

    //Important must be in counter-clockwise order
    private int[] elementArray = {
        2, 1, 0,
        0, 1, 3
    };

    private int vaoID, vboID, eboID;
    private Shader defaultShader;
    private Texture testTex;
    private GameObject testObj;
    private boolean firstTime = false;

    public LevelEditorScene() {

    }

    @Override
    public void update(float dt) {

        double x = Math.random();
        camera.position.x -= dt*20.0f*Math.exp(3*x);
        camera.position.y -= dt*50.0f*Math.exp(3*x);
        defaultShader.use();
        defaultShader.uploadText("TEX_SAMPLER",0);
        glActiveTexture(GL_TEXTURE0);
        testTex.bind();
        defaultShader.uploadMat4("uProjection",camera.getProjectionMatrix());
        defaultShader.uploadMat4("uView",camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());
        //binding the vao which we are using
        glBindVertexArray(vaoID);
        //enable vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES,elementArray.length,GL_UNSIGNED_INT,0);

        //unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

       glBindVertexArray(0);
       defaultShader.detach();


        if (!firstTime) {
            System.out.println("Creating gameObject!");
            GameObject go = new GameObject("Game Test 2");
            go.addComponent(new SpriteRenderer());
            this.addObjToScene(go);
            firstTime = true;
        }


        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

    }

    @Override
    public void init(){

        this.testObj = new GameObject("test object");
        System.out.println("creating test object");
        this.testObj.addComponent(new SpriteRenderer());
        this.testObj.addComponent(new FontRenderer());
        this.addObjToScene(this.testObj);

        this.camera = new Camera(new Vector2f());
        camera.adjustProjection();
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

        this.testTex = new Texture("assets/textures/covid_whitebg.jpg");

        //generate vao, vbo and ebo buffer objects and buffer it to gpu
        vaoID = glGenVertexArrays();
        //everything after this line happens to vaoID(this is binding)
        glBindVertexArray(vaoID);

        //create a float buffer of vertices. We send a float buffer to openGL as it expects that

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip(); //flip orients buffer correctly for openGL

        //create vbo and upload vertex buffer

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //create the indices and upload element buffer
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,elementBuffer,GL_STATIC_DRAW);

        //add the vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int floatSizeBytes = Float.BYTES;
        int textureSize = 2; //uv size
        int vertexSizeBytes = (positionSize+colorSize+textureSize)*floatSizeBytes;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT,false, vertexSizeBytes, positionSize*floatSizeBytes);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, textureSize, GL_FLOAT, false, vertexSizeBytes, (positionSize+colorSize)*floatSizeBytes);
        glEnableVertexAttribArray(2);
    }
}