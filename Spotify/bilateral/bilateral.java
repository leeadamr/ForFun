import java.util.*;
import java.io.*;


/**
 * A program to solve the Spotify 'Bilateral Projects' coding puzzle.
 * <p>
 * <b>Bliaterl Projects. Problem ID: bilateral</b>
 * <br>
 * A friend of yours works at an undisclosed company in the music streaming 
 * industry, and needs your help. The company has offices in Stockholm and London, and 
 * collaboration between the two offices is extensive. The situation is that each of 
 * the many but small projects is handled by a two-person team with a member in each 
 * city. While emails, faxes, and phones are wonderful, and work well within each team, the 
 * CEO wants a briefing every year on the projects. For this purpose the CEO invites 
 * representatives from the projects to Barbados for a week of beach fun presentations of all 
 * the projects. However, money is tight and a new policy has been created: the CEO wants at 
 * least one person from each project, and additionally, she wants to invite as few people as 
 * possible. This is where you come in. In order to help your friend get a ticket to Barbados, you 
 * are to write a program that, given all the two-person teams, computes the smallest number of 
 * people that must be invited in order to get at least one person from each project, as well 
 * as a list of people to invite. If possible (subject to the set of people being smallest 
 * possible), the list of invitees should include your friend.
 * <p>
 * <b>Input</b>
 * <br>
 * The first line of input contains an integer 1 <= m <= 10 000, the number of teams. 
 * The following m lines each contain two integers, i, j separated by a space, being 
 * the employee IDs of the two employees in that team (the first one is from Stockholm 
 * and the second one is from London). Stockholm employees have IDs in the range 1000 to 
 * 1999 and London employees have IDs in the range 2000 to 2999. An employee can be a 
 * member of several teams, but there cannot be several teams consisting of the same 
 * pair of employees. Your friend has ID 1009.
 * <p>
 * <b>Output</b>
 * <br>
 * Output first a single line with an integer k indicating the smallest number of 
 * employees that must be invited to meet the requirements above. Then output k lines 
 * giving the IDs of employees to invite. If possible (subject to k being smallest possible), 
 * the list should contain your friend.
 * If there are several solutions subject to these constraints, anyone is acceptable.
 * <p>
 * Sample input 1 <br>
 * 2 <br>
 * 1009 2011 <br>
 * 1017 2011 <br>
 * <br>
 * Sample output 1 <br>
 * 1 <br>
 * 2011 <br>
 * <p>
 * Sample input 2 <br>
 * 2 <br>
 * 1009 2000 <br>
 * 1009 2001 <br>
 * 1002 2002 <br>
 * 1003 2002 <br>
 * <br>
 * Sample output 2 <br>
 * 2 <br>
 * 2002 <br>
 * 1009 <br>
 *
*/
public class bilateral{	

	private static int FRIEND_NUMBER = 1009;
	private static HashMap<Integer, Vertex> london = new HashMap<Integer, Vertex>();
	private static HashMap<Integer, Vertex> stockholm = new HashMap<Integer, Vertex>();

	public static void main(String[] args) throws IOException{

		// Process the input and build up our list of employees and their team mates.
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		int num_teams = Integer.parseInt(stdin.readLine());
		String raw_input = stdin.readLine();
		while(raw_input != null){
			String[] parsed_input = raw_input.split(" ");

			int stockholm_id = Integer.parseInt(parsed_input[0]);
			int london_id = Integer.parseInt(parsed_input[1]);
			Vertex s = stockholm.get(stockholm_id);
			Vertex l = london.get(london_id);
			if(s == null){
				s = new Vertex(stockholm_id);
			}
			if(l == null){
				l = new Vertex(london_id);
			}
			s.addAdjacentVertex(l);
			l.addAdjacentVertex(s);
			stockholm.put(stockholm_id, s);
			london.put(london_id, l);

			raw_input = stdin.readLine();
		}

		// Use HopcroftKarp to get a maximum matching of teams (this is a bipartite graph problem)
		HopcroftKarp hk = new HopcroftKarp(stockholm, london);
		HashMap<Integer, Vertex> stockholm_matchings = new HashMap<Integer, Vertex>();
		HashMap<Integer, Vertex> london_matchings = new HashMap<Integer, Vertex>();
		int maximum_matching = hk.maximumMatching(stockholm_matchings, london_matchings);

		// Now that we have a maximum matching, we can use Konig's theorem to find the
		// minimum vertex cover (i.e. the minimum number of team mates going to Barbados)
		Konig converter = new Konig(stockholm, london, stockholm_matchings, london_matchings);
		HashMap<Integer, Vertex> min_cover = converter.minVertexCover();

		// If our friend was not part of the minimum cover, swap the order of the offices
		// that are input into Konig's theorem. To simplify, it will basically swap the order
		// of any "ties". If our friend is part of a min set, they will be in this one.
		if(stockholm.get(FRIEND_NUMBER) != null && min_cover.get(FRIEND_NUMBER) == null){
			Konig converter2 = new Konig(london, stockholm, london_matchings, stockholm_matchings);
			HashMap<Integer, Vertex> min_cover2 = converter2.minVertexCover();
			min_cover = min_cover2;			
		}

		System.out.println(min_cover.size());
		Iterator<Vertex> it = min_cover.values().iterator();
		while(it.hasNext()){
			Vertex v = it.next();
			System.out.println(v.id);
		}
	}
}