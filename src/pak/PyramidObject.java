package pak;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.util.ArrayList;

import rafgl.RGL;
import rafgl.RGL.VertexPUN;
import rafgl.jglm.Vec2;
import rafgl.jglm.Vec3;

/**
 * @author Nevena Dresevic
 *
 */
public class PyramidObject extends GameObject {
	ArrayList<VertexPUN> vertexListPUN = new ArrayList<>();

	public PyramidObject() {
		super();
		vertexListPUN = getPyramidVertexList();
		this.model = getPyramidModel();
		
	}
	
	public ArrayList<VertexPUN> getPyramidVertexList() {
		
	    ArrayList<VertexPUN> vertexList = new ArrayList<VertexPUN>();
	    vertexList.add(new VertexPUN(new Vec3(-1.0f, -1.0f, -1.0f), new Vec2(), new Vec3()));
	    vertexList.add(new VertexPUN(new Vec3( 1.0f, -1.0f, -1.0f), new Vec2(), new Vec3()));
	    vertexList.add(new VertexPUN(new Vec3( 0.0f,  1.0f,  0.0f), new Vec2(), new Vec3()));
	    
	    vertexList.add(new VertexPUN(new Vec3(-1.0f, -1.0f,  1.0f), new Vec2(), new Vec3()));
	    vertexList.add(new VertexPUN(new Vec3( 1.0f, -1.0f,  1.0f), new Vec2(), new Vec3()));
	    vertexList.add(new VertexPUN(new Vec3( 0.0f,  1.0f,  0.0f), new Vec2(), new Vec3()));
	    
	    vertexList.add(new VertexPUN(new Vec3(-1.0f, -1.0f, -1.0f), new Vec2(), new Vec3()));
	    vertexList.add(new VertexPUN(new Vec3(-1.0f, -1.0f,  1.0f), new Vec2(), new Vec3()));
	    vertexList.add(new VertexPUN(new Vec3( 0.0f,  1.0f,  0.0f), new Vec2(), new Vec3()));
	    
	    vertexList.add(new VertexPUN(new Vec3( 1.0f, -1.0f, -1.0f), new Vec2(), new Vec3()));
	    vertexList.add(new VertexPUN(new Vec3( 1.0f, -1.0f,  1.0f), new Vec2(), new Vec3()));
	    vertexList.add(new VertexPUN(new Vec3( 0.0f,  1.0f,  0.0f), new Vec2(), new Vec3()));
	    
	    vertexList.add(new VertexPUN(new Vec3(-1.0f, -1.0f, -1.0f), new Vec2(), new Vec3()));
	    vertexList.add(new VertexPUN(new Vec3(-1.0f, -1.0f,  1.0f), new Vec2(), new Vec3()));
	    vertexList.add(new VertexPUN(new Vec3( 1.0f, -1.0f,  1.0f), new Vec2(), new Vec3()));
	    
	    vertexList.add(new VertexPUN(new Vec3( 1.0f, -1.0f,  1.0f), new Vec2(), new Vec3()));
	    vertexList.add(new VertexPUN(new Vec3( 1.0f, -1.0f, -1.0f), new Vec2(), new Vec3()));
	    vertexList.add(new VertexPUN(new Vec3(-1.0f, -1.0f, -1.0f), new Vec2(), new Vec3()));
	    
	    return vertexList;
	}
	
	public RGL.Model getPyramidModel() {
		RGL.Model model = new RGL.Model();

		model.vertices = vertexListPUN;
		model.numVertices =  model.vertices.size();
		model.numTriangles = model.numVertices / 3;
		model.data = RGL.bufferFromArray(model.vertices);
		
		int vertexBuffer = glGenBuffers();
	    glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
	    glBufferData(GL_ARRAY_BUFFER,  model.data, GL_STATIC_DRAW);
	    
	    model.vboID = vertexBuffer;
	    model.loaded = true;
		
		return model;
	}
	
}
