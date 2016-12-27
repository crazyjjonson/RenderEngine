package sun;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import models.RawModel;
import renderEngine.Loader;
import textures.ModelTexture;
import toolbox.Maths;

public class SunRenderer {

	private final RawModel quad;
	private final ModelTexture sunTexture;
	private SunShader shader;
	
	public SunRenderer(Loader loader, Matrix4f projection){
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		this.quad = loader.loadToVAO(positions, 2);
		this.sunTexture = new ModelTexture(loader.loadTexture("sun"));
		this.shader = new SunShader();
		shader.start();
		shader.loadProjectionMatrix(projection);
		shader.stop();
	}
	
	public void render(Sun sun, Camera camera){
		Matrix4f modelView = updateModelViewMatrix(sun, Maths.createViewMatrix(camera));
		shader.start();
		shader.loadTransformationViewMatrix(modelView);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, sunTexture.getID());
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
		
	}
	
	public Matrix4f updateModelViewMatrix(Sun sun, Matrix4f view){
		Vector2f scale = sun.getScale();
		Matrix4f modelMatrix = new Matrix4f();
		Matrix4f.translate(sun.getPosition(), modelMatrix, modelMatrix);
		modelMatrix.m00 = view.m00;
		modelMatrix.m01 = view.m10;
		modelMatrix.m02 = view.m20;
		modelMatrix.m10 = view.m01;
		modelMatrix.m11 = view.m11;
		modelMatrix.m12 = view.m21;
		modelMatrix.m20 = view.m02;
		modelMatrix.m21 = view.m12;
		modelMatrix.m22 = view.m22;
		Matrix4f modelViewMatrix = Matrix4f.mul(view, modelMatrix, null);
		Matrix4f.rotate((float) Math.toRadians(sun.getRotationZ()), new Vector3f(0, 0, 1), modelViewMatrix, modelViewMatrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1.0f), modelViewMatrix, modelViewMatrix);
		return modelViewMatrix;
	}
	
	public void cleanUp(){
		shader.cleanUp();
	}
}
