package render;

import components.SpriteRenderer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {

    //Vertex format
    //--------------
    //Pos                   Color
    //float,float           float,float,float,float

    private final int pos_size = 2;
    private final int color_size = 4;
    private final int vertex_size = 6;
    private final int pos_offset = 0;
    private final int color_offset = pos_offset + pos_size*Float.BYTES;
    private final int vertex_size_bytes = vertex_size*Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean vacancy;
    private float[] vertices; //vertex format mentioned above

    private int vaoID,vboID;

    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int maxBatchSize){
        this.maxBatchSize = maxBatchSize;
        shader = new Shader("assets/shaders/default.glsl");
        shader.compile();
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.vertices = new float[4*maxBatchSize*vertex_size];
        this.numSprites = 0;
        this.vacancy= true;

    }

    public void start(){
        //generate, bind vertex array on cpu
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferData(vboID,vertices.length*Float.BYTES,GL_DYNAMIC_DRAW);

        //create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,indices,GL_STATIC_DRAW);

        //enable buffer attrib pointers
        glVertexAttribPointer(0,pos_size,GL_FLOAT,false,vertex_size_bytes,pos_offset);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,color_size,GL_FLOAT,false,vertex_size_bytes,color_offset);
        glEnableVertexAttribArray(1);

    }

    private int generateIndices(){

        //we need 6 vertices per sprite ie 3 vertices in upper triangle and 3 in the lower triangle.
        int[] elements = new int[maxBatchSize*6];

        for(int i=0; i<maxBatchSize; i++){
            loadElementIndices(elements,i);
        }



        return 0;
    }

    private void loadElementIndices(int[] elements, int index){
        int offSetArray = 6;
    }


}
