package sun;

import org.lwjgl.util.vector.Matrix4f;

import shaders.ShaderProgram;

public class SunShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/sun/sunVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/sun/sunFragmentShader.txt";
	private int location_modelViewMatrix;
	private int location_projectionMatrix;
	
	public SunShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_modelViewMatrix = super.getUniformLocation("modelView");
		location_projectionMatrix = super.getUniformLocation("projection");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	protected void loadTransformationViewMatrix(Matrix4f transformationViewMatrix){
		super.loadMatrix(location_modelViewMatrix, transformationViewMatrix);
	}
	
	protected void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
}
