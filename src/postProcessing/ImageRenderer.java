package postProcessing;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class ImageRenderer {

	private FrameBuffer fbo;

	public ImageRenderer(int width, int height) {
		this.fbo = new FrameBuffer(width, height, 1, FrameBuffer.NONE, FrameBuffer.TEXTURE);
	}

	public ImageRenderer() {
		this.fbo = new FrameBuffer(Display.getWidth(), Display.getHeight(), 1, FrameBuffer.NONE, FrameBuffer.TEXTURE);
	}

	public void renderQuadToOutputTexture() {
		fbo.bindFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		fbo.unbindFrameBuffer();
	}
	
	public void renderQuadToScreen() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
	}

	public int getOutputTexture() {
		return fbo.getColourAttachment(0);
	}

	public void cleanUp() {
		fbo.cleanUp();
	}

}
