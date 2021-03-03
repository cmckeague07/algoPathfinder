package algoPathfinder.Algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class DFS {

	public HashMap<Integer, Node> nodeLookup = new HashMap<Integer, Node>();
	public ArrayList<Integer> pathList = new ArrayList<>(); 
	
	public static class Node{
		public int id; 
		LinkedList<Node> adjacent = new LinkedList<Node>();
		public Node(int id) {
			this.id = id;
		}
	}
	public Node getNode(int id) {
		return nodeLookup.get(id);    }
	
	public void addEdge(int source, int destination) {
		Node s = getNode(source);
		Node d = getNode(destination);
		//Add adjacent node
		s.adjacent.add(d);
		d.adjacent.add(s);
		for(Node node : s.adjacent) {
			//System.out.print("Source ID: " + s.id + ", Edges: " + node.id + ",\n");
			//System.out.println(Arrays.toString(s.adjacent.toArray()));
		}
		
	}
	
	//Recursive hasPathDFS Call
	public boolean hasPathDFS(int source, int destination) {
		Node s = getNode(source);
		Node d = getNode(destination);
		HashSet<Integer> visited = new HashSet<Integer>();
		return hasPathDFS(s, d, visited);
	}
	//Second DFS Call
	public boolean hasPathDFS(Node source, Node destination, HashSet<Integer> visited) {
		// Reached at source again : found cycle
		if (visited.contains(source.id)) {
		   return false;
			}
						
		// Add to visited list before going to visit
		visited.add(source.id);
		if (source.id == destination.id) {
		    return true;
		}
						
		if(source == destination) {
			return true;
		}
		//Loop through adjacent nodes and check if source is destination
		for(Node child: source.adjacent) {
			if(hasPathDFS(child, destination, visited)) {
				pathList.add(child.id);
				return true;
			}
		}
		return false;
	}
	

	public void createGraph(String walls) {
		//Create the nodes for the GRAPH
		  for(int i = 0; i<400; i++) {
			// Create graph Nodes
			this.nodeLookup.put(i, new Node(i));
		  }
		  
		 String[] isWalls = walls.split(",");
	    	for(int i =0; i<isWalls.length; i++) {
	    		isWalls[i]=isWalls[i].trim();
	    	}
	  	//Add the edges
			for(int i = 0; i<400; i++) {
					int x = i, a = i, b = i, c= i, d= i, e= i, f= i, y= i, z= i;
					
					if(x==0) {
						if(!isWalls[i].contains("wall")) {
							int below = a + 20;
							if(!isWalls[below].contains("wall")) {
								this.addEdge(i, b+20);
							}
							int next = y +1;
							if(!isWalls[next].contains("wall")) {
								this.addEdge(i, c+1);
							}
						}
					    
					}else if(x>0 && x<379 && x!= 19 && x !=39 && x !=59 && x !=79 && x !=99 && x !=119 && x !=139 && x !=159 && x != 179 && x != 199 &&  x != 219
							&& x != 239 && x != 259 && x != 279 && x != 299 && x !=319 && x != 339 && x != 359 && x != 379 &&  x !=399 || x ==1) {
						if(!isWalls[i].contains("wall")) {
						
						int below = a + 20;
						if(!isWalls[below].contains("wall")) {
							this.addEdge(i, b+20);
						}
						int next = y +1;
						if(!isWalls[next].contains("wall")) {
							this.addEdge(i, c+1);
						}
				}
						
						
					}else if( x == 19  || x ==39 || x ==59 || x ==79 || x ==99 || x ==119 || x ==139 || x ==159 || x ==179 || x ==199 ||  x == 219
							|| x ==239 || x ==259 || x ==279 || x == 299 || x ==319 || x ==339 || x == 359 || x ==379) {
						if(!isWalls[i].contains("wall")) {
							int below = d +20;
							if(!isWalls[below].contains("wall")) {
								this.addEdge(i, e+20);
							}
							
						}
					}else if( x> 379 && x < 399) {
						if(!isWalls[i].contains("wall")) {
						int next = z +1;
						if(!isWalls[next].contains("wall")) {
							this.addEdge(i, f+1);
						}
						}
						
					}
				}
			}
	

}
