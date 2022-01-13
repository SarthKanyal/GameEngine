package render;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class Shader {

    private int shaderProgramID;
    private String vertexSource;
    private String fragmentSource;
    private String filepath;
    private boolean beingUsed;

    public Shader(String filepath){
        this.filepath = filepath;
        try{
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            int typeStartIndex = source.indexOf("#type")+6;
            int typeEndIndex= source.indexOf("\r\n",typeStartIndex);

            String firstPattern = source.substring(typeStartIndex,typeEndIndex).trim();

            typeStartIndex = source.indexOf("#type",typeEndIndex) + 6;
            typeEndIndex = source.indexOf("\r\n",typeStartIndex);

            String secondPattern = source.substring(typeStartIndex,typeEndIndex);

            if(firstPattern.equals("vertex")){
                vertexSource = splitString[1];
            }else if(firstPattern.equals("fragment")){
                fragmentSource = splitString[1];
            }else {
                throw new IOException("Unexpected token '"+firstPattern+"'");
            }

            if(secondPattern.equals("vertex")){
                vertexSource = splitString[2];
            }else if(secondPattern.equals("fragment")){
                fragmentSource = splitString[2];
            }else {
                throw new IOException("Unexpected token '"+secondPattern+"'");
            }
        }catch (IOException e){
            e.printStackTrace();
            assert false: "ERROR: Could not open shade file: '"+filepath+"'";
        }

        /*System.out.println(vertexSource);
        System.out.println(fragmentSource);*/


    }

    public void compile(){
        int vertexID,fragmentID;
        //first load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        //PASS THE SHADER SOURCE TO THE GPU
        glShaderSource(vertexID,vertexSource);
        glCompileShader(vertexID);

        //check for errors in compilation process
        int success = glGetShaderi(vertexID,GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(vertexID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '"+filepath+"'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID,len));
            assert false : "";

        }
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        //PASS THE SHADER SOURCE TO THE GPU
        glShaderSource(fragmentID,fragmentSource);
        glCompileShader(fragmentID);

        //check for errors in compilation process
        success = glGetShaderi(fragmentID,GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(fragmentID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '"+filepath+"'\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID,len));
            assert false : "";

        }

        //link shaders and check for errors

        shaderProgramID= glCreateProgram();
        glAttachShader(shaderProgramID,vertexID);
        glAttachShader(shaderProgramID,fragmentID);
        glLinkProgram(shaderProgramID);

        //check for link errors
        success = glGetProgrami(shaderProgramID,GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetProgrami(shaderProgramID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '"+filepath+"'\n\tProgram compilation failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramID,len));
            assert false : "";

        }

    }


    public void use(){
        if(!beingUsed){
            glUseProgram(shaderProgramID);
            beingUsed = true;
        }
    }

    public void detach(){
        glUseProgram(0);
        beingUsed = false;
    }

    public void uploadMat4(String varName, Matrix4f mat4){
        int varLoc = glGetUniformLocation(shaderProgramID,varName);
        use();
        FloatBuffer matBuff = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuff);
        glUniformMatrix4fv(varLoc,false,matBuff);
    }

    public void uploadVec4f(String name, Vector4f vec){
        int varLoc = glGetUniformLocation(shaderProgramID,name);
        use();
        glUniform4f(varLoc,vec.x,vec.y,vec.z,vec.w);
    }

    public void uploadFloat(String name, float value){
        int varLoc = glGetUniformLocation(shaderProgramID,name);
        use();
        glUniform1f(varLoc,value);
    }

    public void uploadInt(String name, int value){
        int varLoc = glGetUniformLocation(shaderProgramID,name);
        use();
        glUniform1i(varLoc,value);
    }

    public void uploadVec3f(String name, Vector3f vec){
        int varLoc = glGetUniformLocation(shaderProgramID,name);
        use();
        glUniform3f(varLoc,vec.x,vec.y,vec.z);
    }

    public void uploadVec2f(String name, Vector2f vec){
        int varLoc = glGetUniformLocation(shaderProgramID,name);
        use();
        glUniform2f(varLoc,vec.x,vec.y);
    }

    public void uploadMat3(String varName, Matrix3f matrix3f){
        int varLoc = glGetUniformLocation(shaderProgramID,varName);
        use();
        FloatBuffer matBuff = BufferUtils.createFloatBuffer(9);
        matrix3f.get(matBuff);
        glUniformMatrix3fv(varLoc,false,matBuff);
    }

    public void uploadText(String name, int slot){
        int varLoc = glGetUniformLocation(shaderProgramID,name);
        use();
        glUniform1i(varLoc,slot);
    }

}
