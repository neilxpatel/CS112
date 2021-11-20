package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		if(g == null || p1 == null || p2 == null) {
			return null;
		}
		ArrayList<String> shortestPath = new ArrayList<String>();
		boolean[] visit = new boolean[g.members.length];
		Queue <Person> queueForBFS = new Queue<Person>();
		Person[] visited = new Person[g.members.length];
		
		int index = g.map.get(p1);
		
		queueForBFS.enqueue(g.members[index]);
		visit[index] = true;
		
		while(queueForBFS.isEmpty() == false) {
			Person pivot = queueForBFS.dequeue();
			int pivotIndex = g.map.get(pivot.name);
			visit[pivotIndex] = true;
			
			Friend neighbor = pivot.first;
			
			if(neighbor == null) {
				return null;
			}
			while (neighbor != null) {
				if(visit[neighbor.fnum] == false) {
					visit[neighbor.fnum] = true;
					visited[neighbor.fnum] = pivot;
					queueForBFS.enqueue(g.members[neighbor.fnum]);
					
					if(g.members[neighbor.fnum].name.equals(p2)) {
						pivot = g.members[neighbor.fnum];
					 
						while(pivot.name.equals(p1) == false) {
							shortestPath.add(0, pivot.name);
							pivot = visited[g.map.get(pivot.name)];
						}
						shortestPath.add(0, p1);
					}
				}
				neighbor = neighbor.next;
			}
		}
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return shortestPath;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		/** COMPLETE THIS METHOD **/
		if(g == null || school == null) {
			return null;
		}
		ArrayList<String> nullCheck = new ArrayList<>();
		ArrayList<ArrayList<String>> cliqueResult = new ArrayList<>();
		boolean[] visited = new boolean[g.members.length];
		
		for(int i = 0; i < visited.length; i++) {
			visited[i] = false;
		}
		for(int j = 0; j < g.members.length; j++) {
			if(g.members[j].school != null && g.members[j].school.equals(school)) {
				if(!visited[j]) {
					visited[j] = true;
					nullCheck = BFS(j, g, school, visited);
					if(nullCheck != null) {
						cliqueResult.add(nullCheck);
					}
				}
			}
		}
		if(cliqueResult.isEmpty()) {
			return null;
		}
		return cliqueResult;
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		
	}
	private static ArrayList <String> BFS(int startIndex, Graph g, String school, boolean[] visited){
		
		ArrayList<String> cliques = new ArrayList<>();
		Queue<Integer> q = new Queue();
		
		int firstIndex = startIndex;
		q.enqueue(startIndex);
		visited[startIndex] = true;
		cliques.add(g.members[startIndex].name);
		
		while(!q.isEmpty()) {
			int current = q.dequeue();
			visited[current] = true;
			Person curr = g.members[current];
			Friend friendsClique = curr.first;
			
			while(friendsClique != null && g.members[friendsClique.fnum].school != null && g.members[friendsClique.fnum].school.equals(school)){
				if(!visited[friendsClique.fnum]) {
					q.enqueue(friendsClique.fnum);
					cliques.add(g.members[friendsClique.fnum].name);
					visited[friendsClique.fnum] = true;
				}
				friendsClique = friendsClique.next;
			}
			
		}
		return cliques;
	}
	
	
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		/** COMPLETE THIS METHOD **/
		if(g == null) {
			return null;
		}
		ArrayList<String> connectors = new ArrayList<String>();
		boolean[] visited = new boolean[g.members.length];
		ArrayList<String> replace = new ArrayList<String>();
		
		int[] DFSNumbers = new int[g.members.length];
		int[] beforeValues = new int[g.members.length];
		
		for(int i = 0; i < g.members.length; i++) {
			if(visited[i] == false) {
				connectors = DFS(connectors, g, g.members[i], visited, new int[] {0,0}, DFSNumbers, beforeValues, replace, true);
					
				}
			}
			return connectors;
		}
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		
		
	private static ArrayList<String> DFS(ArrayList<String> connectors, Graph g, Person startIndex, boolean[] visited, int[] count, int[] DFSNumbers, int[] beforeValues, ArrayList<String> replace, boolean started){
		visited[g.map.get(startIndex.name)] = true;
		Friend neighbor = startIndex.first;
		DFSNumbers[g.map.get(startIndex.name)] = count[0];
		beforeValues[g.map.get(startIndex.name)] = count[1];
		
		while(neighbor != null) {
			if(visited[neighbor.fnum] == false) {
				count[0]++;
				count[1]++;
				connectors = DFS(connectors, g, g.members[neighbor.fnum], visited, count, DFSNumbers, beforeValues, replace, false );
				
				if(DFSNumbers[g.map.get(startIndex.name)] <= beforeValues[neighbor.fnum]) {
					if(connectors.contains(startIndex.name) == false && replace.contains(startIndex.name) || connectors.contains(startIndex.name) == false && started == false) {
						connectors.add(startIndex.name);
					}
				} else {
					int first = beforeValues[g.map.get(startIndex.name)];
					int second = beforeValues[neighbor.fnum];
					
					if(first < second) {
						beforeValues[g.map.get(startIndex.name)] = first;
					} else {
						beforeValues[g.map.get(startIndex.name)] = second;
					}
				}
				replace.add(startIndex.name);
			}
			else {
				int third = beforeValues[g.map.get(startIndex.name)];
				int fourth = DFSNumbers[neighbor.fnum];
				
				if(third < fourth) {
					beforeValues[g.map.get(startIndex.name)] = third;	
				} else {
					beforeValues[g.map.get(startIndex.name)] = fourth;
				}
			}
			neighbor = neighbor.next;
		}
		return connectors;
	}
}