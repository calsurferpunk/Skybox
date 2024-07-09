package com.example.skybox.util;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

import com.example.skybox.LoggerConfig;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class TextureHelper {
	private static final String TAG = "TestureHelper";
	/**
	 * Loading Textures
	 * @param context    
	 * @param resourceId  resource id
	 * @return            Texture Data
	 */
	public static int loadTexture(Context context, int resourceId){		
		final int[] textureObjectIds = new int[1];
		glGenTextures(1, textureObjectIds, 0);
		
		if(textureObjectIds[0] == 0){
			if(LoggerConfig.ON){
				Log.w(TAG, "Unable to create texture object");
			}
			return 0;
		}
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;//Image raw data
		
		final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
		
		if(bitmap==null){
			if(LoggerConfig.ON){
				Log.w(TAG, "The resource could not be loaded");
			}
			glDeleteTextures(1, textureObjectIds, 0);
			return 0;
		}
		//Bind the texture before you can operate on it
		glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
		//opengl Texture filtering mode http://blog.csdn.net/pizi0475/article/details/49740879
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);	
		//Loading bitmap data into a texture
		texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
		//Generate MIP maps
		glGenerateMipmap(GL_TEXTURE_2D);
		//Unbind Texture
		glBindTexture(GL_TEXTURE_2D, 0);
		
		return textureObjectIds[0];
	}
	
	public static int loadCubeMap(Context context, int[] cubeResources){
		final int[] textureObjectIds = new int[1];
		glGenTextures(1, textureObjectIds, 0);
		
		if(textureObjectIds[0] == 0){
			if(LoggerConfig.ON){
				Log.w(TAG, "The resource could not be loaded");
			}
			return 0;
		}
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;
		final Bitmap[] cubeBitmaps = new Bitmap[6];
		
		for(int i=0;i<6;i++){
			cubeBitmaps[i] = BitmapFactory.decodeResource(context.getResources(), 
					cubeResources[i], options);
			
			if(cubeBitmaps[i] == null){
				if(LoggerConfig.ON){
					Log.w(TAG, "The resource could not be loaded");
				}
				glDeleteTextures(1, textureObjectIds, 0);
				return 0;
			}
		}
		
		glBindTexture(GL_TEXTURE_CUBE_MAP, textureObjectIds[0]);
		
		//Filter texture mode uses bilinear filtering
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		//Cube left-right, bottom-up, front-back transfer surfaces
		texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, cubeBitmaps[0], 0);
		texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, cubeBitmaps[1], 0);
		
		texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, cubeBitmaps[2], 0);
		texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, cubeBitmaps[3], 0);
		
		texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, cubeBitmaps[4], 0);
		texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, cubeBitmaps[5], 0);
		
		glBindTexture(GL_TEXTURE_2D, 0);
		
		for(Bitmap bitmap : cubeBitmaps){
			bitmap.recycle();
		}
		
		return textureObjectIds[0];
	}
}
