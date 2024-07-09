package com.example.skybox.util;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

import com.example.skybox.LoggerConfig;

import android.util.Log;

public class ShaderHelper {
	private static final String TAG = "ShaderHelper";
	
	public static int compileVertexShader(String shaderCode){
		return compileShader(GL_VERTEX_SHADER, shaderCode);
	}

	public static int compileFragmentShader(String shaderCode){
		return compileShader(GL_FRAGMENT_SHADER, shaderCode);
	}
	
	/**
	 * Generate shader program from shader code
	 * @param type        Shader Types
	 * @param shaderCode  Shader code
	 * @return            Shader Programs
	 */
	private static int compileShader(int type, String shaderCode) {
		// TODO Auto-generated method stub
		final int shaderObjectId = glCreateShader(type);		
		if(shaderObjectId==0){
			if(LoggerConfig.ON){
				Log.w(TAG, "Unable to create new shader object");
			}
			return 0;
		}
		glShaderSource(shaderObjectId, shaderCode);
		glCompileShader(shaderObjectId);
		final int[] compileStatus = new int[1];
		glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);
		if(LoggerConfig.ON){
			Log.v(TAG, "result of compiling program: \n"
					+ shaderCode + "\n" 
					+ glGetProgramInfoLog(shaderObjectId));
		}
		if(compileStatus[0]==0){
			glDeleteProgram(shaderObjectId);
			if(LoggerConfig.ON){
				Log.w(TAG, "Failed to create shader object");
			}
			return 0;
		}
		return shaderObjectId;	
	}
	
	/**
	 * Linking vertex shader and fragment shader programs
	 * @param vertexShaderId   Vertex shader program
	 * @param fragmentShaderId Fragment shader program
	 * @return
	 */
	public static int linkProgram(int vertexShaderId,int fragmentShaderId){
		final int programObjectId = glCreateProgram();
		if(programObjectId==0){
			if(LoggerConfig.ON){
				Log.w(TAG, "Cannot create new linked shader program");
			}
			return 0;
		}
		glAttachShader(programObjectId, vertexShaderId);
		glAttachShader(programObjectId, fragmentShaderId);
		glLinkProgram(programObjectId);
		final int[] linkStatus = new int[1];
		glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
		if(LoggerConfig.ON){
			Log.v(TAG, "result of linking program: "+ glGetProgramInfoLog(programObjectId));
		}
		if(linkStatus[0]==0){
			glDeleteProgram(programObjectId);
			if(LoggerConfig.ON){
				Log.w(TAG, "Linker creation failed");
			}
			return 0;
		}
		return programObjectId;
	}
	
	/**
	 * Verifying shader programs
	 * @param programObjectId
	 * @return
	 */
	public static boolean validateProgram(int programObjectId){
		glValidateProgram(programObjectId);
		
		final int[] validateStatus = new int[1];
		glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
		
		Log.v(TAG, "result of validating program: "+glGetProgramInfoLog(programObjectId));
	
		return validateStatus[0]!=0;
	}
	
	/**
	 * Generate shader program
	 * @param vertexShaderSoure    Vertex shader code
	 * @param fragmentShaderSoure  Fragment shader code
	 * @return
	 */
	public static int buildProgram(String vertexShaderSoure,String fragmentShaderSoure){
		int program;
		
		//Get the corresponding shader
		int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSoure);
		int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSoure);
		
		program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
		
		if (LoggerConfig.ON) {
			ShaderHelper.validateProgram(program);
		}
		
		return program;		
	}
}
