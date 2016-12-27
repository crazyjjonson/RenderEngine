
package postProcessing;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class FrameBuffer {

	public static final int NONE = 0;
	public static final int TEXTURE = 1;
	public static final int RENDER_BUFFER = 2;
	
	private final int width;
	private final int height;

	private int frameBuffer;

	private int samples;
	private int[] colourAttachments;
	private int depthAttachment;
	private int[] types;

	/**
	 * Creates a new FrameBuffer and sets its color and depth attachments.
	 * @param width
	 * 		- the width of the frameBuffer.
	 * @param height
	 * 		- the height of the frameBuffer.
	 * @param samples
	 * 		- if samples is greater than 1, the frameBuffer will have multiple samples per pixel,
	 * 		  specified by samples, else it will be singleSampled.  
	 * @param types
	 * 		- the types of the color and depth attachments, where the first type is specifying the depth attachment. 
	 * 		  The following types are specifying the color attachments.
	 */
	public FrameBuffer(int width, int height, int samples, int... types){
		this.width = width;
		this.height = height;
		this.types = types;
		this.samples = samples;
		this.colourAttachments = new int[types.length - 1];
	
		createFrameBuffer();
		
		if(types[0] == TEXTURE)
			depthAttachment = createDepthTexture();
		else if(types[0] == RENDER_BUFFER)
			depthAttachment = createDepthRenderBuffer();
		
		for(int i = 1; i < types.length; i++){
			if(types[i] == TEXTURE)
				colourAttachments[i-1] = createColourTexture(GL30.GL_COLOR_ATTACHMENT0 + i - 1);
			else if(types[i] == RENDER_BUFFER)
				colourAttachments[i-1] = createColourRenderBuffer(GL30.GL_COLOR_ATTACHMENT0 + i - 1);			
			}
		
		unbindFrameBuffer();
		
	}
	
	/**
	 * Creates a new frame buffer object and sets the buffer to which drawing
	 * will occur - colour attachment 0. This is the attachment where the colour
	 * buffer texture is.
	 * 
	 */
	private void createFrameBuffer() {
		frameBuffer = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		determineDrawBuffers();
	}
	
	/**
	 * Passes the used color attachments for the Fbo to openGL.
	 */
	private void determineDrawBuffers(){
		IntBuffer buffer = BufferUtils.createIntBuffer(colourAttachments.length);
		if(types.length <= 1 || types[1] == NONE){
			GL11.glDrawBuffer(GL11.GL_NONE);
		}else{
			for(int i = 0; i < colourAttachments.length; i++)
				buffer.put(GL30.GL_COLOR_ATTACHMENT0 + i);
			buffer.flip();
			GL20.glDrawBuffers(buffer);
		}
	}

	/**
	 * Deletes the frame buffer and its attachments when the game closes.
	 */
	public void cleanUp() {
		GL30.glDeleteFramebuffers(frameBuffer);
		for(int i = 0; i < colourAttachments.length; i++){
			if(types[i + 1] == TEXTURE)
				GL11.glDeleteTextures(colourAttachments[i]);
			else if(types[i+1] == RENDER_BUFFER)
				GL30.glDeleteRenderbuffers(colourAttachments[i]);
		}
		if(types[0] == TEXTURE)
			GL11.glDeleteTextures(depthAttachment);
		else if(types[0] == RENDER_BUFFER)
			GL30.glDeleteRenderbuffers(depthAttachment);
	}

	/**
	 * Binds the frame buffer, setting it as the current render target. Anything
	 * rendered after this will be rendered to this FBO, and not to the screen.
	 */
	public void bindFrameBuffer() {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, frameBuffer);
		GL11.glViewport(0, 0, width, height);
	}

	/**
	 * Unbinds the frame buffer, setting the default frame buffer as the current
	 * render target. Anything rendered after this will be rendered to the
	 * screen, and not this FBO.
	 */
	public void unbindFrameBuffer() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	/**
	 * Binds the current FBO to be read from (not used in tutorial 43).
	 */
	public void bindToRead() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, frameBuffer);
		GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
	}

	/**
	 * Creates a texture and sets it as the color buffer attachment for this
	 * FBO.
	 * @param attachment
	 * 		the attachment the created texture should be attached to.
	 * @return
	 * 		the id of the texture.
	 */
	private int createColourTexture(int attachment) {
		int id = GL11.glGenTextures();
		if(samples > 1){
			GL11.glBindTexture(GL32.GL_TEXTURE_2D_MULTISAMPLE, id);
			GL32.glTexImage2DMultisample(GL32.GL_TEXTURE_2D_MULTISAMPLE, samples, GL11.GL_RGBA, width, height, true);
			GL11.glTexParameteri(GL32.GL_TEXTURE_2D_MULTISAMPLE, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL32.GL_TEXTURE_2D_MULTISAMPLE, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL32.GL_TEXTURE_2D_MULTISAMPLE, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL32.GL_TEXTURE_2D_MULTISAMPLE, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment, GL32.GL_TEXTURE_2D_MULTISAMPLE, id, 0);
		}else{
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment, GL11.GL_TEXTURE_2D, id, 0);
		}
		return id;
	}
	
	/**
	 * Creates a texture and attaches it to the frameBuffer.
	 * @param attachment
	 * 		the attachment the created texture should be attached to.
	 * @return
	 * 		the id of the texture
	 */
	private int createColourRenderBuffer(int attachment){
		int id = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, id);
		if(samples > 1)
			GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, samples, GL11.GL_RGBA8, width, height);	
		else
			GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_RGBA8, width, height);	
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachment, GL30.GL_RENDERBUFFER, id);
		return id;
	}
	
	/**
	 * Adds a depth buffer to the FBO in the form of a texture, which can later
	 * be sampled.
	 */
	private int createDepthTexture() {
		int id = GL11.glGenTextures();
		if(samples > 1){
			GL11.glBindTexture(GL32.GL_TEXTURE_2D_MULTISAMPLE, id);
			GL32.glTexImage2DMultisample(GL32.GL_TEXTURE_2D_MULTISAMPLE, samples, GL11.GL_DEPTH_COMPONENT, width, height, true);
			GL11.glTexParameteri(GL32.GL_TEXTURE_2D_MULTISAMPLE, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL32.GL_TEXTURE_2D_MULTISAMPLE, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL32.GL_TEXTURE_2D_MULTISAMPLE, id, 0);
			
		}else{
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, id, 0);
		}
		return id;
	}

	/**
	 * Adds a depth buffer to the FBO in the form of a render buffer. This can't
	 * be used for sampling in the shaders.
	 */
	private int createDepthRenderBuffer() {
		int id = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, id);
		if(samples > 1)
			GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, samples, GL14.GL_DEPTH_COMPONENT24, width, height);	
		else
			GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, id);
		return id;
	}
	
	/**
	 * @param attachment
	 * 		the attachment that should be returned.
	 * @return The ID of the attachment containing the color attachment of the FBO.
	 */
	public int getColourAttachment(int attachment) {
		return colourAttachments[attachment];
	}

	/**
	 * @return The attachment containing the FBOs depth attachment.
	 */
	public int getDepthAttachment() {
		return depthAttachment;
	}
	
	/**
	 * Copies the color and depth attachment to the frameBuffer output.
	 * @param output
	 * 		the frameBuffer we copy the attachment of the current frameBuffer to.
	 * @param readBuffer
	 * 		the color attachment of the current frameBuffer that should be copied.
	 */
	public void resolveToFbo(FrameBuffer output, int readBuffer){
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, output.frameBuffer);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, frameBuffer);
		GL11.glReadBuffer(readBuffer);
		GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, output.width, output.height, GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
		unbindFrameBuffer();
	}
	
	/**
	 * copies the color attachment of the fbo to the default frameBuffer.
	 */
	public void resolveToScreen(){
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, frameBuffer);
		GL11.glDrawBuffer(GL11.GL_BACK);
		GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, Display.getWidth(), Display.getHeight(), GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);
		unbindFrameBuffer();
	}
	

}
