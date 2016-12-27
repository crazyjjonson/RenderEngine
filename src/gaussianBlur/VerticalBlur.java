package gaussianBlur;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import postProcessing.ImageProcesser;

public class VerticalBlur extends ImageProcesser{
	
	private VerticalBlurShader shader;
	
	public VerticalBlur(int width, int height){
		super(width, height);
		shader = new VerticalBlurShader();
		shader.start();
		shader.loadTargetHeight(height);
		shader.stop();
	}
	
	@Override
	public void renderToOutputTexture(float... param) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, (int)param[0]);
		getImageRenderer().renderQuadToOutputTexture();
		shader.stop();
	}


	@Override
	public void renderToScreen(float... param) {	
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, (int)param[0]);
		getImageRenderer().renderQuadToScreen();
		shader.stop();
	}
	
	@Override
	public void cleanUp(){
		super.cleanUp();
		shader.cleanUp();
	}
}
