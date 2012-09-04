import java.util.*;

/*
Implements the Hopcrof-Karp Algorithm. More or less, the logic was taken straight from Wikipedia.
Check out Wikipedia for a full description of how the logic works.
*/
public class HopcroftKarp{
	
	private static HashMap<Integer, Vertex> G1;
	private static HashMap<Integer, Vertex> G2;
	private static HashMap<Integer, Vertex> pair_G1;
	private static HashMap<Integer, Vertex> pair_G2;
	private static HashMap<Integer, Integer> dist;
	private static LinkedList<Vertex> Q;

	HopcroftKarp(HashMap<Integer, Vertex> G1, HashMap<Integer, Vertex> G2){
		this.G1 = G1;
		this.G2 = G2;
	}

	// Returns the maximum cardinality matching of the bipartite graph. The matchings for each partition of the
	// graph are placed into G1_matchings and G2_matchings
	public static int maximumMatching(HashMap<Integer, Vertex> G1_matchings, HashMap<Integer, Vertex> G2_matchings){
		// Initialize with a special NIL vertex. When the algorithm is finished
		// any items left with a NIL vertex are unmatched. This is important
		// and is necessary to find the minimum vertex conver using Konig's theorem
		pair_G1 = new HashMap<Integer, Vertex>();
		pair_G2 = new HashMap<Integer, Vertex>();
		Iterator<Vertex> i = G1.values().iterator();
		while(i.hasNext()){
			Vertex v = i.next();
			pair_G1.put(v.id, Vertex.NIL);
		}

		i = G2.values().iterator();
		while(i.hasNext()){
			Vertex v = i.next();
			pair_G2.put(v.id, Vertex.NIL);
		}

		dist = new HashMap<Integer, Integer>();
		Q = new LinkedList<Vertex>();	

		// The heart of the Hopcroft-Karp algorithm
		int matching = 0;
		while(BFS() == true){
			Iterator<Vertex> it = G1.values().iterator();
			while(it.hasNext()){
				Vertex v = it.next();
				if(pair_G1.get(v.id).equals(Vertex.NIL)){
					if(DFS(v) == true){
						matching++;
					}
				}

			}
		}

		// Copy the matched vertices(max cover) to the variables we were passed.
		G1_matchings.putAll(pair_G1);
		G2_matchings.putAll(pair_G2);

		return matching;
	}


	private static boolean BFS(){
		Iterator<Vertex> it = G1.values().iterator();
		while(it.hasNext()){
			Vertex v = it.next();
			if(pair_G1.get(v.id).equals(Vertex.NIL)){
				dist.put(v.id, 0);
				Q.offer(v);
			}
			else{
				dist.put(v.id, Vertex.INFINITY);
			}
		}
		dist.put(Vertex.NIL.id, Vertex.INFINITY);

		while(!Q.isEmpty()){
			Vertex v = Q.remove();

			Iterator<Vertex> adj = v.adjacentVertices();
			while(adj.hasNext()){
				Vertex u = adj.next();
				if(dist.get(pair_G2.get(u.id).id) == Vertex.INFINITY){
					dist.put(pair_G2.get(u.id).id, dist.get(v.id) + 1);
					Q.offer(pair_G2.get(u.id));
				}
			}


		}
		return dist.get(Vertex.NIL.id) != Vertex.INFINITY;

	}

	private static boolean DFS(Vertex v){
		if(!v.equals(Vertex.NIL)){
			Iterator<Vertex> adj = v.adjacentVertices();
			while(adj.hasNext()){
				Vertex u = adj.next();
				if(dist.get(pair_G2.get(u.id).id) == (dist.get(v.id) + 1)){
					if(DFS(pair_G2.get(u.id)) == true){
						pair_G2.put(u.id, v);
						pair_G1.put(v.id, u);
						return true;
					}
				}

			}
			dist.put(v.id, Vertex.INFINITY);
			return false;
		}

		return true; 
	}


}