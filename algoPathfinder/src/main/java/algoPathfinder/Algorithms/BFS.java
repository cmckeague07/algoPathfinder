package algoPathfinder.Algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import algoPathfinder.Algorithms.DFS.Node;


public class BFS {

	//Store the Nodes in the Graph
	public HashMap<Integer, Node> nodeLookup = new HashMap<Integer, Node>();
	public ArrayList<Integer> pathList = new ArrayList<>();
	
	//Allow us to create the Nodes in the graph
    public static class Node{
		public int id; 
		LinkedList<Node> adjacent = new LinkedList<Node>();
		public Node(int id) {
			this.id = id;
		}
	}
    
	//Get the specific Node in the HashMap by ID
	private Node getNode(int id) {
		return nodeLookup.get(id);
		}
	
	//Add edge between Nodes
	public void addEdge(int source, int destination) {
		Node s = getNode(source);
		Node d = getNode(destination);
		s.adjacent.add(d);
		d.adjacent.add(s);
		
	}
	
	public boolean hasPathBFS(int source, int destination) {
		return hasPathBFS(getNode(source), getNode(destination));
	}
	
	public boolean hasPathBFS(Node source, Node destination) {
		LinkedList<Node> nextToVisit = new LinkedList<Node>();
		HashSet<Integer>visited = new HashSet<Integer>();
		nextToVisit.add(source);
		while(!nextToVisit.isEmpty()) {
			Node node = nextToVisit.remove();
			pathList.add(node.id);
			if(node == destination) {
				pathList.add(destination.id);
				return true;
			}
			if(visited.contains(node.id)) {
				continue;
			}
			visited.add(node.id);
			for(Node child: node.adjacent) {
				nextToVisit.add(child);
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
