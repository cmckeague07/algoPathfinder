package algoPathfinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import algoPathfinder.Algorithms.AStar.*;
import algoPathfinder.Algorithms.AStar;
import algoPathfinder.Algorithms.BFS;
import algoPathfinder.Algorithms.DFS;

@Controller
public class AlgoController {
	@GetMapping("/")
    public String Load(Model model) {
		
		return "index";
    }
	@GetMapping("/BFS.html")
    public String loadBFS(Model model) {
		
		return "BFS";
    }
	@GetMapping("/AStar.html")
    public String loadAStar(Model model) {
		
		return "AStar";
    }
	@GetMapping("/About.html")
    public String loadAbout(Model model) {
		
		return "About";
    }
	
    @PostMapping("/findPathDFS")
	public String findPathDFS(@RequestParam("payload") String results, RedirectAttributes redirectAttrs){
		String fileName = results;
		String[] nodes = fileName.split(",");
		String destination = null;
	    for(int i =0; i<nodes.length; i++) {
			if(nodes[i].contains("destination")) {
				destination = nodes[i];
			}
			
		}
	    if(destination != null) {
	    	String destID = destination.replaceAll("destination", "");
			destID = destID.replaceAll("wall", "");
			int x = Integer.parseInt(destID.trim());
			//Create graph object
			DFS dfs = new DFS();
	        // Creating graph 
			dfs.createGraph(results);
			
			
			 // Traversing with DFS
			boolean hasPathDFS = dfs.hasPathDFS(0, x);
			
			redirectAttrs.addFlashAttribute("message", "" + "Path exists: " + hasPathDFS 
			+ "\n Nodes visited: " + Arrays.toString(dfs.pathList.toArray()) );
			
			redirectAttrs.addFlashAttribute("alertClass", "alert-success");
			
			return "redirect:/";
	    }else {
	    	
	    	redirectAttrs.addFlashAttribute("message2", "" + "You have not entered a destination, please Reset and choose another Destination.");
	    	redirectAttrs.addFlashAttribute("alertClass", "alert-danger");
	    	return "redirect:/";
	    }
		
		
	}
    
    @PostMapping("/findPathBFS")
	public String findPathBFS(@RequestParam("payload") String results, RedirectAttributes redirectAttrs){
		String fileName = results;
		String[] nodes = fileName.split(",");
		String destination = null;
	    for(int i =0; i<nodes.length; i++) {
			if(nodes[i].contains("destination")) {
				destination = nodes[i];
			}
			
		}
	    if(destination!= null) {
		String destID = destination.replaceAll("destination", "");
		destID = destID.replaceAll("wall", "");
		int x = Integer.parseInt(destID.trim());
		//Create graph object
		BFS bfs = new BFS();
        // Creating graph 
		bfs.createGraph(results);
		
		
		 // Traversing with DFS
		boolean hasPathDFS = bfs.hasPathBFS(0, x);
		
		redirectAttrs.addFlashAttribute("message", "" + "Path exists: " + hasPathDFS 
		+ "\n Nodes visited: " + Arrays.toString(bfs.pathList.toArray()) );
		
		redirectAttrs.addFlashAttribute("alertClass", "alert-success");
		
		return "redirect:/BFS.html";
		}
		
		else {
	    	
	    	redirectAttrs.addFlashAttribute("message2", "" + "You have not entered a destination, please Reset and choose another Destination.");
	    			
	    	redirectAttrs.addFlashAttribute("alertClass", "alert-danger");
	    	return "redirect:/BFS.html";
	    }
		
	}
    
   
    @PostMapping("/findPathAStar")
  	public String findPathAStar(@RequestParam("payload") String results, RedirectAttributes redirectAttrs){
    	//Manipulate the results
    	String[]maze = results.split(",");
    	String destination = "'";
    	//Sort the results into a string that can be passed to AStar
    	for(int i = 0; i<maze.length; i++) {
    		if(maze[i].contains("destination")) {
    			destination = maze[i];
    			maze[i] = maze[i].replaceAll("destination", "");
    		}
    		if(maze[i].length()>4) {
    		maze[i] = maze[i].substring(0, 4);
    		}
    		if(maze[i].contains("x")) {
    		maze[i] = maze[i].substring(0,2);
    		}
    		maze[i] = maze[i].trim();
    	}
    	
    	String resultsSorted = Arrays.toString(maze);
    	System.out.println(destination);
    	String destID = destination.replaceAll("destination", "").trim();
    	
    	try{
    		int dest = Integer.parseInt(destID);
    		//Create AStar class object to call our algorithm
      	  AStar star = new AStar();
      	  List<Node> nodes=new ArrayList<Node>();
      	  int xvalue = 0; int yvalue = 19; 
  		  for(int i = 0; i<400; i++) {
  			 nodes.add(new Node(i, xvalue, yvalue, 0, 0, 0, false));
  		     //@param set x&y to correct values for 20*20 grid
  			  xvalue++;
  			  if(xvalue==20) {xvalue=0; yvalue--;}
  		  }
      	  Node endNode = nodes.get(dest);
      	  double fcost = 10 + star.heuristic(0, 19, endNode.x, endNode.y);
      	  double fcostend = 10 + star.heuristic(endNode.x, endNode.y, endNode.x, endNode.y);
      	  AStar.Node start = new Node(0, 0, 19, 10, star.heuristic(0, 19, endNode.x, endNode.y), fcost, false); 
  		  AStar.Node end = new Node(dest, endNode.x, endNode.y, 10, star.heuristic(endNode.x, endNode.y, endNode.x, endNode.y), fcostend, false); 
  		  List<List<Node>> path = star.findPathAStar(start, end, resultsSorted);
  		  List<Integer> visitedNodes = new ArrayList<Integer>();
  		  for(Node node: path.get(0)) {
  			 visitedNodes.add(node.id);
  		  }
  		  List<Integer> finalPath = new ArrayList<Integer>();
  		  for(Node node: path.get(1)) {
  			  if(node.id == 0) {
  				  continue;
  			  }
  			 finalPath.add(node.id);
  		  }
  		  
  		  redirectAttrs.addFlashAttribute("message", "" + "Path exists: " + Arrays.toString(finalPath.toArray()));
  		  redirectAttrs.addFlashAttribute("messag2", "" + "\n Nodes visited: " + Arrays.toString(visitedNodes.toArray()));
  		  
      	
      	redirectAttrs.addFlashAttribute("alertClass", "alert-success");
  		return "redirect:/AStar.html";
    		}
    	catch(NumberFormatException e ) {
    		redirectAttrs.addFlashAttribute("message", "" + "You have not entered a destination, please choose a Destination.");
			
	    	redirectAttrs.addFlashAttribute("alertClass", "alert-danger");
	    	return "redirect:/AStar.html";
    	}
    	
    	
    	
  	}

    
	
}


