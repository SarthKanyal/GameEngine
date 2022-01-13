package render;

import org.lwjgl.BufferUtils;
import org.w3c.dom.Text;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private String filepath;
    private int textureID;

    public Texture(String filepath){
        this.filepath = filepath;

        //generate texture on gpu
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D,textureID);

        //set texture parameters
        //wrapping the texture
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);

        //when stretching the texture image we want it to look pixelated
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
        //when shrinking we pixelate again
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filepath,width,height,channels,0);

        if(image != null){
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
        }else{
            assert false: "Error : Texture could not load image '" + filepath + "'";
        }

        stbi_image_free(image);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void unbind(){
        glBindTexture(GL_TEXTURE_2D,0);
    }
}
