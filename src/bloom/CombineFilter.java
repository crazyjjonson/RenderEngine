package bloom;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import postProcessing.ImageProcesser;

public class CombineFilter extends ImageProcesser{
	
	private CombineShader shader;
	
	public CombineFilter(){
		super();
		shader = new CombineShader();
		shader.start();
		shader.connectTextureUnits();
		shader.stop();
	}

	@Override
	public void renderToOutputTexture(float... param) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, (int)param[0]);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, (int)param[1]);
		getImageRenderer().renderQuadToOutputTexture();
		shader.stop();
	}

	@Override
	public void renderToScreen(float... param) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, (int)param[0]);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, (int)param[1]);
		getImageRenderer().renderQuadToScreen();
		shader.stop();
	}
	
	@Override
	public void cleanUp(){
		super.cleanUp();
		shader.cleanUp();
	}
}
