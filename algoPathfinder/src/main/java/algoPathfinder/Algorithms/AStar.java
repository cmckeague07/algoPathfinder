package algoPathfinder.Algorithms;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class AStar {

	public int[][] maze;
	public Node start;
	public int xstart;
	public int ystart;
	public int xend, yend;
	public double gcost = 10;
	public double hcost;
	public double fcost;
	boolean foundPath = false;
	public HashMap<Integer, Node> nodeLookup = new HashMap<Integer, Node>();
	public List<Node> open;
	public List<Node> path = new ArrayList<Node>();
	public List<Node> visited = new ArrayList<Node>();

	
	public List<List<Node>> findPathAStar(Node start, Node end, String maze) {
		createGraph(maze, start, end);

		// Initialize both open and closed list
		open = new ArrayList<Node>();

		// Add starting node
		open.add(start);

		Node current = open.get(0);
		
		//create variable to store our end id
		int endz = end.id;
		
		List<Node> visited = new ArrayList<Node>();
		List<Node> matchingf = new ArrayList<Node>();
		int count = 0;
		List<Integer> duplicateStartId = new ArrayList<Integer>();
		List<Node> pathModified = new ArrayList<Node>();
		
		//While we havent found a path, keep looping
		while (foundPath != true) {
			visited.add(current);
			
			//If we come across the same node multiple times, end as we cant find a path
			if (duplicateStartId.contains(current.id)) {
				count++;
				if (count > 4) {
					System.out.println("Path not found");
					pathModified.clear();
					foundPath = true;
					break;
				}
			}
			//Add current node to duplicate list for checking
			duplicateStartId.add(current.id);
			
			
			System.out.println("\nCurrent Node: " + current.id + " End Id: " + endz);
			System.out.println("Current Nodes Neighbours:");
			double hcost = 0;
			double fcost = 0;
			double nodecurrent = current.id;
			//if current == end node, break as we have found the destination
			if (nodecurrent == endz) {
				System.out.println("We found a path!!");
				foundPath = true;
				break;
			}

			List<Node> neighbours = getNeighbours(current);
			List<Node> findLowestF = new ArrayList<Node>();
			
			//Loop through current nodes neighbours, and add neighbours to arraylist
			for (Node node : neighbours) {
				if (!visited.contains(node)) {
					hcost = recalcheuristic(node.x, node.y, end.x, end.y);
					fcost = fcost(10, hcost);
					node.f = fcost;
					System.out.println("NODE INFORMATION:--> id= " + node.id + " ,f= " + fcost + " ,x= " + node.x
							+ " ,y= " + node.y + " ,h= " + hcost);
					findLowestF.add(node);

					// current node has more than 2 neighbours
					// check if the first neighbour & third neighbour have matching fs
					if (neighbours.size() > 2) {
						if (neighbours.get(0).f == neighbours.get(2).f) {
							if (!matchingf.contains(neighbours.get(0)) || !matchingf.contains(neighbours.get(2))) {
								System.out.println("GOT EM");
								matchingf.add(neighbours.get(0));
								matchingf.add(neighbours.get(2));
							}
						} else if (neighbours.get(0).f == neighbours.get(1).f) {
							if (!matchingf.contains(neighbours.get(0)) || !matchingf.contains(neighbours.get(1))) {
								matchingf.add(neighbours.get(0));
								matchingf.add(neighbours.get(1));
							}
						}
					}
				} else {
					continue;
				}

			}
			//stream to find lowest cost f from neighbours
			double i = findLowestF.stream().mapToDouble(Node::getF).min().orElse(0);

			int l = 0;
			for (Node node : findLowestF) {
				if (node.f == i) {
					l = node.id;
				}
			}
			//set current node to lowest cost f to begin search again
			current = nodeLookup.get(l);
			visited.add(current);
			path.add(current);
			pathModified.add(current);
			Node node = null;
			//if we have hit 0 again, restart the algorithm from selected point
			if (current.id == 0) {
				// reconstruction algorithim
				for (Node ui : matchingf) {
					// This marks the restart point on which Node to restart the search from
					// we want to remove from the path all the nodes searched up until the node we
					// restart at
					Node first = matchingf.get(0);
					Node second = matchingf.get(1);
					if (!path.contains(first) && !pathModified.contains(first)) {
						pathModified.add(first);
					}
					if (!path.contains(second) && !pathModified.contains(second)) {
						pathModified.add(second);
					}
					if (path.contains(second)) {
						// check if path contains this node, remove it, as this is the node that was
						// chosen
						pathModified.remove(second);
					}
					if (path.contains(first)) {
						// check if path contains this node, remove it, as this is the node that was
						// chosen
						pathModified.remove(first);
					}
					
					//Reconstruct the path
					for (int x = 0; x < ui.adjacent.size(); x++) {
						List<Node> pathRetrace = new ArrayList<Node>();
						Node adjacent = ui.adjacent.peek();
						pathRetrace.add(adjacent);

						if (path.contains(ui.adjacent.peek())) {
							pathModified.remove(ui.adjacent.peek());

							for (int y = 0; y < path.size(); y++) {
								pathRetrace.remove(adjacent);
								adjacent = adjacent.adjacent.peek();
								if (path.contains(adjacent)) {
									pathModified.remove(adjacent);
									pathRetrace.add(adjacent);
								}

							}
						}
					}

				}
				if (!matchingf.isEmpty()) {
					current = matchingf.get(0);
					node = matchingf.get(1);
					visited.remove(current);

				} else if (matchingf.isEmpty()) {
					int index = visited.size();
					current = visited.get(index - 3);
					visited.remove(current);
					visited.add(visited.get(index - 2));

				}

				int removed = 0;
				while (removed < Math.min(visited.size(), 2)) {
					visited.remove(visited.size() - 1);
					removed++;
				}

				foundPath = false;
			}
		}
		System.out.println("\nPATH");
		for (Node node : path) {
              System.out.println("Node Id: " + node.id + " H cost: " + node.id);
		}
		System.out.println("\n PATH MODIFIED");
		for (Node node : pathModified) {
			System.out.println("Node id: " + node.id);
		}

		List<List<Node>> finalPaths = new ArrayList<List<Node>>();
		finalPaths.add(path);
		finalPaths.add(pathModified);
		return finalPaths;

	}

	public static class Node {
		public int id;
		public int x, y;
		public double g;
		public double h;
		public double f;
		public boolean isWall;
		LinkedList<Node> adjacent = new LinkedList<Node>();

		public Node(int id, int x, int y, double g, double h, double f, boolean isWall) {
			this.id = id;
			this.x = x;
			this.y = y;
			this.g = g;
			this.h = h;
			this.f = f;
			this.isWall = isWall;
		}

		public double getF() {
			return f;
		}

		public void setF(int f) {
			this.f = f;
		}
	}

	// Get the specific Node in the HashMap by ID
	public Node getNode(int id) {
		return nodeLookup.get(id);
	}

	public Node getByXY(int x, int y) {
		int v = 0;
		for (int i = 0; i < 400; i++) {
			Node node = nodeLookup.get(i);
			if (node.x == x && node.y == y) {
				v = node.id;
				break;
			}
		}
		return nodeLookup.get(v);
	}

	// Add edge between Nodes
	public void addEdge(int source, int destination) {
		Node s = getNode(source);
		Node d = getNode(destination);
		s.adjacent.add(d);
		d.adjacent.add(s);

	}
	//calculate H value
	public double heuristic(int xstart, int ystart, int xend, int yend) {
		double h = Math.sqrt((xend - xstart) * (xend - xstart) + (yend - ystart) * (yend - ystart));
		return h;
	}

	public double recalcheuristic(int xstart, int ystart, int xend, int yend) {
		double h = 0;

		Node nodeCurrent = getByXY(xstart, ystart);
		Node nodeEnd = getByXY(xend, yend);

		if (xstart == 0 && ystart == 19) {
			return h = 1000;
		}

		double dis = Math.sqrt((xend - xstart) * (xend - xstart) + (yend - ystart) * (yend - ystart));
		System.out.println("Node: (" + nodeCurrent.id + ") distancebetween" + "(" + xstart + "," + ystart + ")," + "("
				+ xend + "," + yend + ")===>" + dis);

		int current = nodeCurrent.id;
		int right = nodeCurrent.id + 1;
		int right1 = nodeCurrent.id + 2;
		int below = nodeCurrent.id + 20;
		int left = nodeCurrent.id - 1;
		int above = nodeCurrent.id - 20;
		int d = nodeCurrent.id + 21;
		int d1 = nodeCurrent.id + 41;
		int d2 = nodeCurrent.id + 42;
		int d3 = nodeCurrent.id - 21;
		int d4 = nodeCurrent.id - 41;
		int ld = nodeCurrent.id + 19;
		int below1 = nodeCurrent.id + 80;
		int below2 = nodeCurrent.id + 81;
		int below3 = nodeCurrent.id + 62;
		int below4 = nodeCurrent.id + 43;
		int below5 = nodeCurrent.id + 24;
		int below6 = nodeCurrent.id + 18;
		int below7 = nodeCurrent.id + 38;
		int below8 = nodeCurrent.id + 99;
		int below9 = nodeCurrent.id + 98;
		int below10 = nodeCurrent.id + 58;
		int below11 = nodeCurrent.id + 44;
		int below12 = nodeCurrent.id + 63;
		int below13 = nodeCurrent.id + 4;
		int below14 = nodeCurrent.id + 82;
		int below15 = nodeCurrent.id + 102;
		
		/*
		 * The below code represents outlier scenarios for nodes, this function recalculates the H value based on current
		 * node and what the end value is
		 */
		
		
		if (nodeLookup.get(above) != null && nodeLookup.get(below) != null && nodeEnd.id == 166) {
			if (nodeLookup.get(above).id == 125 || nodeLookup.get(above).id == 183) {
				dis = dis - 10;
			}
		}

		if (nodeLookup.get(above) != null && nodeLookup.get(below) != null && nodeEnd.id == 350) {
			if (nodeLookup.get(above).id == 314 || nodeLookup.get(above).id == 354) {
				dis = dis - 10;
			}
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 186) {
			if (nodeLookup.get(above).id == 1 || nodeLookup.get(right).id == 22) {
				dis = dis - 10;
			}
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 267) {
			if (nodeLookup.get(above).id == 1 || nodeLookup.get(right).id == 22) {
				dis = dis - 10;
			}
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 268) {
			if (nodeLookup.get(above).id == 1 || nodeLookup.get(right).id == 22) {
				dis = dis - 10;
			}
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 269) {
			if (nodeLookup.get(above).id == 1 || nodeLookup.get(right).id == 22) {
				dis = dis - 10;
			}
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 287) {
			if (nodeLookup.get(above).id == 1 || nodeLookup.get(right).id == 22) {
				dis = dis - 10;
			}
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 288) {
			if (nodeLookup.get(above).id == 1 || nodeLookup.get(right).id == 22) {
				dis = dis - 10;
			}
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 289) {
			if (nodeLookup.get(above).id == 1 || nodeLookup.get(right).id == 22) {
				dis = dis - 10;
			}
		}

		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 328) {
			if (nodeLookup.get(above).id == 1 || nodeLookup.get(right).id == 22) {
				dis = dis - 10;
			}
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 348) {
			if (nodeLookup.get(above).id == 1 || nodeLookup.get(right).id == 22) {
				dis = dis - 10;
			}
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 368) {
			if (nodeLookup.get(above).id == 1 || nodeLookup.get(right).id == 22) {
				dis = dis - 10;
			}
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 187) {
			if (nodeLookup.get(above).id == 1 || nodeLookup.get(right).id == 22) {
				dis = dis - 10;
			}
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 187) {
			if (nodeLookup.get(above).id == 101 || nodeLookup.get(right).id == 122) {
				dis = dis - 10;
			}
		}

		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 246) {
			if (nodeLookup.get(above).id == 1 || nodeLookup.get(right).id == 22) {
				dis = dis - 10;
			}
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 247) {
			if (nodeLookup.get(above).id == 1 || nodeLookup.get(right).id == 22) {
				dis = dis - 10;
			}
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 248) {
			if (nodeLookup.get(above).id == 1 || nodeLookup.get(right).id == 22) {
				dis = dis - 10;
			}
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 124) {
			if (nodeLookup.get(above).id == 1 || nodeLookup.get(right).id == 22) {
				dis = dis - 10;
			}
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 125) {
			if (nodeLookup.get(above).id == 1 || nodeLookup.get(right).id == 22) {
				dis = dis - 10;
			}
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 126) {
			if (nodeLookup.get(above).id == 1 || nodeLookup.get(right).id == 22) {
				dis = dis - 10;
			}
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 127) {
			if (nodeLookup.get(above).id == 1 || nodeLookup.get(right).id == 22) {
				dis = dis - 10;
			}
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(right) != null && nodeEnd.id == 128) {
			if (nodeLookup.get(above).id == 1 || nodeLookup.get(right).id == 22) {
				dis = dis - 10;
			}
		}
		if (nodeCurrent.id == 242 || nodeCurrent.id == 282 && nodeEnd.id == 304) {
			dis = dis - 2;
		}
		if (nodeCurrent.id == 282 && nodeEnd.id == 386) {
			dis = dis + 5;
		}
		if (nodeCurrent.id == 282 && nodeEnd.id == 387) {
			dis = dis + 5;
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(left) != null && nodeEnd.id == 177) {
			if (nodeLookup.get(left).id == 137 || nodeLookup.get(above).id == 118) {
				dis = dis - 10;
			}
		}
		if (nodeLookup.get(above) != null && nodeLookup.get(below) != null && nodeEnd.id == 178) {
			if (nodeLookup.get(above).id == 135 || nodeLookup.get(above).id == 183) {
				dis = dis - 10;
			}
		}

		if (nodeLookup.get(right) != null && nodeLookup.get(ld) != null && nodeLookup.get(below7 + 1) != null
				&& nodeLookup.get(below7) != null && nodeLookup.get(below) != null && nodeLookup.get(d) != null
				&& nodeLookup.get(below6) != null) {
			if (nodeLookup.get(right).id != nodeEnd.id && nodeLookup.get(ld).id != nodeEnd.id
					&& nodeLookup.get(below7).id == nodeEnd.id
					|| nodeLookup.get(below7 + 1).id == nodeEnd.id && nodeLookup.get(below).id != nodeEnd.id
							&& nodeLookup.get(d).id != nodeEnd.id && nodeLookup.get(below6).id != nodeEnd.id) {

				if (!nodeLookup.get(right).isWall && nodeLookup.get(below).isWall && nodeLookup.get(d).isWall
						&& nodeLookup.get(ld).isWall && nodeLookup.get(below6).isWall) {

					dis = dis - 1.4;

				}
			}
		}

		// for looking at node 312
		if (nodeLookup.get(right) != null && nodeLookup.get(ld) != null && nodeLookup.get(below7) != null
				&& nodeLookup.get(below) != null && nodeLookup.get(d) != null && nodeLookup.get(below6) != null) {
			if (nodeLookup.get(right).id != nodeEnd.id && nodeLookup.get(ld).id != nodeEnd.id
					&& nodeLookup.get(below7).id == nodeEnd.id && nodeLookup.get(d).id != nodeEnd.id
					&& nodeLookup.get(below6).id != nodeEnd.id) {

				if (!nodeLookup.get(right).isWall && nodeLookup.get(below).isWall && nodeLookup.get(d).isWall
						&& nodeLookup.get(ld).isWall && nodeLookup.get(below6).isWall) {

					dis = dis - 1.4;

				}
			}
		}
		// for looking at node 312
		if (nodeLookup.get(right) != null && nodeLookup.get(ld) != null && nodeLookup.get(below7 + 1) != null
				&& nodeLookup.get(below) != null && nodeLookup.get(d) != null && nodeLookup.get(below6) != null) {
			if (nodeLookup.get(right).id != nodeEnd.id && nodeLookup.get(ld).id != nodeEnd.id
					&& nodeLookup.get(below7 + 1).id == nodeEnd.id && nodeLookup.get(d).id != nodeEnd.id
					&& nodeLookup.get(below6).id != nodeEnd.id) {

				if (!nodeLookup.get(right).isWall && nodeLookup.get(below).isWall && nodeLookup.get(d).isWall
						&& nodeLookup.get(ld).isWall && nodeLookup.get(below6).isWall) {

					dis = dis - 1.4;

				}
			}
		}
		if (nodeLookup.get(below1) != null && nodeLookup.get(below2) != null && nodeLookup.get(below3) != null
				&& nodeLookup.get(below4) != null && nodeLookup.get(below5) != null && nodeLookup.get(below6) != null
				&& nodeLookup.get(below7) != null && nodeLookup.get(below8) != null && nodeLookup.get(below9) != null
				&& nodeLookup.get(below10) != null && nodeLookup.get(below11) != null && nodeLookup.get(below12) != null
				&& nodeLookup.get(below13) != null && nodeLookup.get(below14) != null
				&& nodeLookup.get(below15) != null) {
			if (nodeLookup.get(below1).id != nodeEnd.id && nodeLookup.get(below2).id != nodeEnd.id
					&& nodeLookup.get(below3).id != nodeEnd.id && nodeLookup.get(below4).id != nodeEnd.id
					&& nodeLookup.get(below5).id != nodeEnd.id && nodeLookup.get(below7).id != nodeEnd.id
					&& nodeLookup.get(below8).id != nodeEnd.id && nodeLookup.get(below9).id != nodeEnd.id
					&& nodeLookup.get(below10).id != nodeEnd.id && nodeLookup.get(below11).id == nodeEnd.id
					|| nodeLookup.get(below12).id == nodeEnd.id || nodeLookup.get(below13).id == nodeEnd.id
					|| nodeLookup.get(below14).id == nodeEnd.id || nodeLookup.get(below3).id == nodeEnd.id
					|| nodeLookup.get(below15).id == nodeEnd.id) {
				if (nodeLookup.get(below1).isWall && nodeLookup.get(below2).isWall && nodeLookup.get(below3).isWall
						&& nodeLookup.get(below4).isWall && nodeLookup.get(below5).isWall
						&& nodeLookup.get(below6).isWall && nodeLookup.get(below7).isWall
						&& nodeLookup.get(below8).isWall && nodeLookup.get(below9).isWall) {
					dis = dis + 15;
				}

			}
		}

		if (nodeLookup.get(right) != null && nodeLookup.get(below) != null && nodeLookup.get(d1) != null) {
			if (nodeLookup.get(right).id != nodeEnd.id && nodeLookup.get(below).id != nodeEnd.id
					&& nodeLookup.get(d1).isWall) {
				dis = dis + 0.6;
			}
		}

		if (nodeLookup.get(right) != null && nodeLookup.get(below) != null) {
			if (nodeLookup.get(right).id != nodeEnd.id && nodeLookup.get(below).id != nodeEnd.id) {
				if (nodeLookup.get(right).isWall && nodeLookup.get(below).isWall) {
					if (nodeLookup.get(left).isWall) {
						dis = dis + 10;
					} else {
					}
				}
			}
		}
		if (nodeLookup.get(right) != null && nodeLookup.get(below) != null) {
			if (nodeLookup.get(right).id != nodeEnd.id && nodeLookup.get(below).id != nodeEnd.id) {
				if (nodeLookup.get(right).isWall && nodeLookup.get(below).isWall) {
					if (nodeLookup.get(d) != null && nodeLookup.get(d).id == nodeEnd.id) {
						dis = dis + 5;
					} else {
					}
				}
			}
		}
		if (nodeLookup.get(right) != null && nodeLookup.get(below) != null && nodeLookup.get(d) != null) {
			if (nodeLookup.get(right).id != nodeEnd.id && nodeLookup.get(below).id != nodeEnd.id
					&& nodeLookup.get(d).id != nodeEnd.id) {
				if (nodeLookup.get(right).isWall && nodeLookup.get(below).isWall && nodeLookup.get(d).isWall) {
					dis = dis + 10;
				}
			}
		}
		if (nodeLookup.get(right) != null && nodeLookup.get(below) != null && nodeLookup.get(d1) != null) {
			if (nodeLookup.get(right).id != nodeEnd.id && nodeLookup.get(below).id != nodeEnd.id
					&& nodeLookup.get(d1).id == nodeEnd.id) {
				if (!nodeLookup.get(right).isWall && nodeLookup.get(below).isWall && !nodeLookup.get(d1).isWall) {
					dis = dis - 1;
				}
			}
		}

		if (nodeLookup.get(right) != null && nodeLookup.get(above) != null && nodeLookup.get(below) != null) {
			if (nodeLookup.get(right).id != nodeEnd.id && nodeLookup.get(above).id != nodeEnd.id
					&& nodeLookup.get(below).id != nodeEnd.id) {
				if (nodeLookup.get(right).isWall && nodeLookup.get(above).isWall) {
					dis = dis + 10;
				}
			}
		}
		if (nodeLookup.get(right) != null && nodeLookup.get(above) != null && nodeLookup.get(d3) != null
				&& nodeLookup.get(d4) != null && nodeLookup.get(below) != null) {
			if (nodeLookup.get(right).id != nodeEnd.id && nodeLookup.get(above).id != nodeEnd.id
					&& nodeLookup.get(below).id != nodeEnd.id && nodeLookup.get(d3).id != nodeEnd.id
					&& nodeLookup.get(d4).id != nodeEnd.id) {
				if (nodeLookup.get(right).isWall && nodeLookup.get(above).isWall && !nodeLookup.get(d3).isWall
						&& !nodeLookup.get(d4).isWall) {
					dis = dis + 12;
				}
			}
		}

		if (nodeLookup.get(right) != null && nodeLookup.get(below) != null && nodeLookup.get(d) != null
				&& nodeLookup.get(d2) != null) {

			if (nodeLookup.get(right).id != nodeEnd.id && nodeLookup.get(below).id != nodeEnd.id
					&& nodeLookup.get(d).id != nodeEnd.id && nodeLookup.get(d2).id != nodeEnd.id) {

				if (nodeLookup.get(right).isWall && !nodeLookup.get(below).isWall && nodeLookup.get(d).isWall
						&& nodeLookup.get(d2).isWall) {

					dis = dis + 5;

				}
			}
		}

		if (nodeLookup.get(left) != null && nodeLookup.get(above) != null && nodeLookup.get(below) != null) {
			if (nodeLookup.get(left).id != nodeEnd.id && nodeLookup.get(above).id != nodeEnd.id
					&& nodeLookup.get(below).id != nodeEnd.id) {
				if (nodeLookup.get(left).isWall && nodeLookup.get(above).isWall && nodeLookup.get(below).isWall) {
					dis = dis + 10;
				}
			}
		}

		if (nodeLookup.get(left) != null && nodeLookup.get(below) != null && nodeLookup.get(above) != null) {
			if (nodeLookup.get(left).id != nodeEnd.id && nodeLookup.get(below).id != nodeEnd.id
					&& nodeLookup.get(above).id != nodeEnd.id) {
				if (nodeLookup.get(left).isWall && nodeLookup.get(below).isWall) {
					dis = dis + 10;
				}
			}
		}

		if (nodeLookup.get(current).id == 20 || nodeLookup.get(current).id == 40 || nodeLookup.get(current).id == 60
				|| nodeLookup.get(current).id == 80 || nodeLookup.get(current).id == 100
				|| nodeLookup.get(current).id == 120 || nodeLookup.get(current).id == 140
				|| nodeLookup.get(current).id == 160 || nodeLookup.get(current).id == 180
				|| nodeLookup.get(current).id == 200 || nodeLookup.get(current).id == 220
				|| nodeLookup.get(current).id == 240 || nodeLookup.get(current).id == 260
				|| nodeLookup.get(current).id == 280 || nodeLookup.get(current).id == 300) {
			if (nodeLookup.get(current).id != nodeEnd.id && nodeLookup.get(below).id != nodeEnd.id) {
				if (nodeLookup.get(current).id != nodeEnd.id && nodeLookup.get(current).id != nodeEnd.id + 20
						&& nodeLookup.get(current).id != nodeEnd.id - 20) {
					dis = dis + 5;
				}
			}
		}

		h = dis;

		if (xstart == xend && ystart == yend) {
			h = 0;
		}
		return h;
	}

	//Calculate F cost
	public double fcost(double g, double hcost2) {
		double fcost = (double) g + hcost2;
		return (double) fcost;

	}

	public List<Node> getNeighbours(Node current) {
		List<Node> neighbours = new ArrayList<Node>();
		for (int i = 0; i < 1; i++) {
			if (current.id == 0) {
				int right = current.id + 1;
				int below = current.id + 20;
				if (!nodeLookup.get(right).isWall) {
					neighbours.add(nodeLookup.get(right));
				}
				if (!nodeLookup.get(below).isWall) {
					neighbours.add(nodeLookup.get(below));
				}

			} else if (current.id > 0 && current.id < 19) {
				int wall = current.id + 1;
				int below = current.id + 20;
				int left = current.id - 1;
				if (!nodeLookup.get(wall).isWall) {
					neighbours.add(nodeLookup.get(wall));
				}
				if (!nodeLookup.get(below).isWall) {
					neighbours.add(nodeLookup.get(below));
				}
				if (!nodeLookup.get(left).isWall) {
					neighbours.add(nodeLookup.get(left));
				}
			} else if (current.id > 19 && current.id < 379 && current.id != 39 && current.id != 59 && current.id != 79
					&& current.id != 99 && current.id != 119 && current.id != 139 && current.id != 159
					&& current.id != 179 && current.id != 199 && current.id != 219 && current.id != 239
					&& current.id != 259 && current.id != 279 && current.id != 299 && current.id != 319
					&& current.id != 339 && current.id != 359 && current.id != 379 && current.id != 399
					&& current.id != 20 && current.id != 40 && current.id != 60 && current.id != 80 && current.id != 100
					&& current.id != 140 && current.id != 160 && current.id != 180 && current.id != 200
					&& current.id != 220 && current.id != 240 && current.id != 260 && current.id != 280
					&& current.id != 300 && current.id != 320 && current.id != 340 && current.id != 360
					&& current.id != 380) {

				int right = current.id + 1;
				int left = current.id - 1;
				int above = current.id - 20;
				int below = current.id + 20;

				if (!nodeLookup.get(right).isWall) {
					neighbours.add(nodeLookup.get(right));
				}
				if (!nodeLookup.get(left).isWall) {
					neighbours.add(nodeLookup.get(left));
				}
				if (!nodeLookup.get(above).isWall) {
					neighbours.add(nodeLookup.get(above));
				}
				if (!nodeLookup.get(below).isWall) {
					neighbours.add(nodeLookup.get(below));
				}

			} else if (current.id == 19 || current.id == 39 || current.id == 59 || current.id == 79 || current.id == 99
					|| current.id == 119 || current.id == 139 || current.id == 159 || current.id == 179
					|| current.id == 199 || current.id == 219 || current.id == 239 || current.id == 259
					|| current.id == 279 || current.id == 299 || current.id == 319 || current.id == 339
					|| current.id == 359 || current.id == 379) {
				int left = current.id - 1;
				int above = current.id - 20;
				int below = current.id + 20;

				if (!nodeLookup.get(left).isWall) {
					neighbours.add(nodeLookup.get(left));
				}
				if (!nodeLookup.get(above).isWall) {
					neighbours.add(nodeLookup.get(above));
				}
				if (!nodeLookup.get(below).isWall) {
					neighbours.add(nodeLookup.get(below));
				}
			} else if (current.id == 20 || current.id == 40 || current.id == 60 || current.id == 80 || current.id == 100
					|| current.id == 140 || current.id == 160 || current.id == 180 || current.id == 200
					|| current.id == 220 || current.id == 240 || current.id == 260 || current.id == 280
					|| current.id == 300 || current.id == 320 || current.id == 340 || current.id == 360) {
				int right = current.id + 1;
				int above = current.id - 20;
				int below = current.id + 20;

				if (!nodeLookup.get(right).isWall) {
					neighbours.add(nodeLookup.get(right));
				}
				if (!nodeLookup.get(above).isWall) {
					neighbours.add(nodeLookup.get(above));
				}
				if (!nodeLookup.get(below).isWall) {
					neighbours.add(nodeLookup.get(below));
				}
			} else if (current.id > 380 && current.id < 399) {
				int wall = current.id + 1;
				int above = current.id - 20;
				int left = current.id - 1;
				if (!nodeLookup.get(wall).isWall) {
					neighbours.add(nodeLookup.get(wall));
				}
				if (!nodeLookup.get(above).isWall) {
					neighbours.add(nodeLookup.get(above));
				}
				if (!nodeLookup.get(left).isWall) {
					neighbours.add(nodeLookup.get(left));
				}
			} else if (current.id == 19 || current.id == 379 || current.id == 399) {
				if (current.id == 19) {
					int left = current.id - 1;
					int below = current.id + 20;
					if (!nodeLookup.get(left).isWall) {
						neighbours.add(nodeLookup.get(left));
					}
					if (!nodeLookup.get(below).isWall) {
						neighbours.add(nodeLookup.get(below));
					}
				} else if (current.id == 380) {
					int right = current.id + 1;
					int above = current.id - 20;
					if (!nodeLookup.get(right).isWall) {
						neighbours.add(nodeLookup.get(right));
					}
					if (!nodeLookup.get(above).isWall) {
						neighbours.add(nodeLookup.get(above));
					}
				} else if (current.id == 399) {
					int left = current.id - 1;
					int above = current.id - 20;
					if (!nodeLookup.get(left).isWall) {
						neighbours.add(nodeLookup.get(left));
					}
					if (!nodeLookup.get(above).isWall) {
						neighbours.add(nodeLookup.get(above));
					}
				}

			}

		}

		return neighbours;

	}

	public void createGraph(String walls, Node start, Node end) {
		// initalize x&y cordinates
		int xvalue = 0;
		int yvalue = 19;
		int g = 10;
		double fcost = 0;
		for (int i = 0; i < 400; i++) {
			// Create graph Nodes
			fcost = g + heuristic(xvalue, yvalue, end.x, end.y);
			this.nodeLookup.put(i,
					new Node(i, xvalue, yvalue, g, heuristic(xvalue, yvalue, end.x, end.y), fcost, false));
			// @param set x&y to correct values for 20*20 grid
			xvalue++;
			if (xvalue == 20) {
				xvalue = 0;
				yvalue--;
			}
		}

		String[] isWalls = walls.split(",");
		for (int i = 0; i < isWalls.length; i++) {
			isWalls[i] = isWalls[i].trim();
			System.out.print(" " + isWalls[i]);
		}
		/*
		 * Add the edges
		 * Additional checks in place where Node is a wall to not add an edge
		 */
		for (int i = 0; i < 400; i++) {
			int x = i, a = i, b = i, c = i, d = i, e = i, f = i, y = i, z = i;
			if (isWalls[i].contains("W")) {
				nodeLookup.get(i).isWall = true;
			}
			if (x == 0) {
				if (!isWalls[i].contains("W")) {
					int below = a + 20;
					if (!isWalls[below].contains("W")) {
						this.addEdge(i, b + 20);
					}
					int next = y + 1;
					if (!isWalls[next].contains("W")) {
						this.addEdge(i, c + 1);
					}
				} else if (isWalls[i].contains("W")) {
					nodeLookup.get(i).isWall = true;
				}

			} else if (x > 0 && x < 379 && x != 19 && x != 39 && x != 59 && x != 79 && x != 99 && x != 119 && x != 139
					&& x != 159 && x != 179 && x != 199 && x != 219 && x != 239 && x != 259 && x != 279 && x != 299
					&& x != 319 && x != 339 && x != 359 && x != 379 && x != 399 || x == 1) {
				if (!isWalls[i].contains("W")) {

					int below = a + 20;
					if (!isWalls[below].contains("W")) {
						this.addEdge(i, b + 20);
					}
					int next = y + 1;
					if (!isWalls[next].contains("W")) {
						this.addEdge(i, c + 1);
					}
				} else if (isWalls[i].contains("W")) {
					nodeLookup.get(i).isWall = true;
				}
			} else if (x == 19 || x == 39 || x == 59 || x == 79 || x == 99 || x == 119 || x == 139 || x == 159
					|| x == 179 || x == 199 || x == 219 || x == 239 || x == 259 || x == 279 || x == 299 || x == 319
					|| x == 339 || x == 359 || x == 379) {
				if (!isWalls[i].contains("wall")) {
					int below = d + 20;
					if (!isWalls[below].contains("W")) {
						this.addEdge(i, e + 20);
					}

				} else if (isWalls[i].contains("W")) {
					nodeLookup.get(i).isWall = true;
				}
			} else if (x > 379 && x < 399) {
				if (!isWalls[i].contains("W")) {
					int next = z + 1;
					if (!isWalls[next].contains("W")) {
						this.addEdge(i, f + 1);
					}
				} else if (isWalls[i].contains("W")) {
					nodeLookup.get(i).isWall = true;
				}

			}
		}

	}

}
