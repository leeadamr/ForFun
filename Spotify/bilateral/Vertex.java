import java.util.*;

public class Vertex{

	public int id;
	public int depth;
	private LinkedList<Vertex> adjacent;
	public static Vertex NIL= new Vertex(-1);
	public static int INFINITY = -1;

	Vertex(int id){
		this.id = id;
		this.adjacent = new LinkedList<Vertex>();
		this.depth = 0;
	}

	boolean addAdjacentVertex(Vertex adjacent_vertex){
		return adjacent.add(adjacent_vertex);
	}

	boolean removeAdjacentVertex(Vertex adjacent_vertex){
		return adjacent.remove(adjacent_vertex);
	}

	void removeAllAdjacentVertices(){
		adjacent.clear();
	}

	Iterator<Vertex> adjacentVertices(){
		return adjacent.iterator();
	}
}