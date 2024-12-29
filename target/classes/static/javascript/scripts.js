/**
 * 
 */
 document.addEventListener("DOMContentLoaded", function(){
	createGrid();
	var startPoint = document.getElementById("node 0").style.backgroundColor= "red";
	findHighlightedPath();
	addWall();
	
	
	function findHighlightedPath(){
		//Check once path is submitted, if a path exists
		
		if(document.getElementById("pathExists?") != null){
			
			if (document.getElementById("pathExists?").innerHTML.includes("true")){
			    //Get our Path and manipulate into an array
			   
				var pathExists = document.getElementById("pathExists?").innerHTML;
			    var pathExists = pathExists.substring(35);
				pathExists = pathExists.slice(0, -1);
				dest = dest.replace(",", "");
				dest = dest.trim();
			    console.log("Path substringed: " + pathExists);
			    console.log(dest);
				var temp = new Array();
				temp = pathExists.split(",").map(function(item) {
					  return item.trim();
				});
				
				//Tmeout function
				function sleep(ms) {
					  return new Promise(resolve => setTimeout(resolve, ms));
				}
			 
				async function highlightPath() {
					  await sleep(2000);
					  // Highlight the path square by square
				temp.reverse();
						 for(var i = 0; i<=temp.length; i++){
							 await sleep(150);
							 var path = document.getElementById("node " + temp[i]);
							 if (!path) {
								    return; // don't do the rest
								  }
							 if(temp[i] != dest){
							 path.setAttribute("class", "node " + temp[i] + " path");
							 }else if(temp[i] == dest){
								 console.log("yes it does")
							 path.setAttribute("class", "node " + temp[i] + " final"); 
							 }
							 
						}
					}

					highlightPath(); }
			
		}
	}
	
});




function addWall() {
	 //Using localStorage to store random generated wall
	if (Object.keys(localStorage).length === 0) {
 				console.log("There are no keys so we can create!!");
 				
 				for(var i = 0; i<125; i++){
 					var x = Math.floor((Math.random()*399)+1);
 					var wall = document.getElementById("node " + x).setAttribute("class", "node " + x + " wall");
 					localStorage.setItem("wall ID" + x, "wall ID" + x);
 					document.getElementById(x).setAttribute("value", x + " wall");
 				}
			}else{
				console.log("Local Storage already exists so not creating new");
			}
			//If localStorage wall set, retain those walls on page refresh
			for (var x in localStorage) {
				   if(x!= null){
					   var x = x.substring(7);
					   document.getElementById("node " + x).setAttribute("class", "node " + x + " wall");
					   document.getElementById(x).setAttribute("value", x + " wall");
				   }
				}
	
}

function resetWall(){
	localStorage.clear();
	window.location.reload();
	
}

function createGrid(){
	let columns = 20;
	let space = '';
	let k =0;
	for(let i=0; i<columns; i++) {
		  space+= '<div class="row">'
		  for(let j=0; j<columns; j++){ 
			  space+= `<div class="node ${(k)}" id="node ${(k)}"> ${(k)}  </div>`;
		      space+= `<input type="hidden" id="${(k)}" class="${(k)}" value= "${(k)}" name="payload">`;
		      k++;
		  };
		 
		  space+= '</div>'
		  
		}

		container.innerHTML = space;

		var addclass = 'destination';
		var $endPoint = $('.node').click(function(e) {
		    $endPoint.removeClass(addclass);
		    $(this).addClass(addclass);
		    
		    var x = document.getElementsByClassName("destination");
			var y =  x[0].innerHTML.trim();
			  
			  
		    if (document.getElementById("node " + y).className === "node " + y + ' destination')  {
		        //do something
		         console.log(y);
		    	 document.getElementById(y).setAttribute("value", y + " destination");
		    	
		      }
		        
			for(var x = 0; x<401; x++){
				 var nodes = document.getElementsByClassName("node " + x);
				    for (var i=0; i < nodes.length; i++) {
				    	//Filter all the nodes that are not the destination
				        if(nodes[i].className == "node "+x) {
				            // Reset back to original value
				           document.getElementById(x).setAttribute("value", x);
				        }
				    }
			  } 
		   
			 
			    
			
		});
		
}
