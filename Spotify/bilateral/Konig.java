import java.util.*;

/*
Implements Konig's Theorem. More or less, the logic was taken from Wikipedia.
*/
public class Konig{

	private HashMap<Integer, Vertex> L;
	private HashMap<Integer, Vertex> R;
	private HashMap<Integer, Vertex> L_matchings;
	private HashMap<Integer, Vertex> R_matchings;

	Konig(HashMap<Integer, Vertex> L, HashMap<Integer, Vertex> R, HashMap<Integer, Vertex> L_matchings, HashMap<Integer, Vertex> R_matchings){
		this.L = L;
		this.R = R;
		this.L_matchings = L_matchings;
		this.R_matchings = R_matchings;
	}

	public HashMap<Integer, Vertex> minVertexCover(){
		HashMap<Integer, Vertex> T = new HashMap<Integer, Vertex>();
		HashMap<Integer, Vertex> min_cover = new HashMap<Integer, Vertex>();

		// A helper variable for finding matched edges in both the left and right partition
		HashMap<Integer, Vertex> all_matchings = new HashMap<Integer, Vertex>();
		all_matchings.putAll(R_matchings);
		all_matchings.putAll(L_matchings);

		// T will consist of all unmatched vertices from L (the special NIL vertex indicates this).
		Iterator<Integer> i = L_matchings.keySet().iterator();
		while(i.hasNext()){
			Integer id = i.next();
			Vertex v = L.get(id);
			if(L_matchings.get(id).equals(Vertex.NIL)){
				T.put(id, v);
			}		
		}

		// Start will all vertices from T (unmatched vertices from L). 
		// Note: They will have  a depth of 0
		HashMap<Integer, Vertex> visited = new HashMap<Integer, Vertex>();
		LinkedList<Vertex> Q = new LinkedList<Vertex>();
		Iterator<Vertex> it = T.values().iterator();
		while(it.hasNext()){
			Vertex v = it.next();
			v.depth = 0;
			Q.offer(v);
		}

		// This traversal alternates between traversing only matched edges and all edges.
		// Even level nodes traverse all edges. Odd level nodes only traverse matched edges.
		// Also add each node to T
		while(!Q.isEmpty()){
			Vertex v = Q.remove();
			visited.put(v.id, v);
			T.put(v.id, v);
			// even
			if((v.depth & 1) == 0){
				Iterator<Vertex> adjacent = v.adjacentVertices();
				while(adjacent.hasNext()){
					Vertex adj = adjacent.next();
					if(visited.get(adj.id) == null){
						adj.depth = v.depth + 1;
						Q.offer(adj);
					}
				}
			}else{
				Vertex match = all_matchings.get(v.id);
				if(!match.equals(Vertex.NIL) && visited.get(match.id) == null)
				{
					Q.offer(match);
				}

			}

		}

		// Now we will use T to compute the minimum vertex cover.
		// Vertices in L that are not in T are part of the minimum cover.
		it = L.values().iterator();
		while(it.hasNext()){
			Vertex v = it.next();
			if(T.get(v.id) == null){
				min_cover.put(v.id, v);
			}
		}

		// Vertices in R that are not in T are part of the minimum cover.
		it = R.values().iterator();
		while(it.hasNext()){
			Vertex v = it.next();
			if(T.get(v.id) != null){
				min_cover.put(v.id, v);
			}
		}

		return min_cover;
	}

}