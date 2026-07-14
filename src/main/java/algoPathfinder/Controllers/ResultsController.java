package algoPathfinder.Controllers;

import algoPathfinder.Model.PathfindingResult;
import algoPathfinder.Service.PathFindingResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ResultsController {

    @Autowired
    private PathFindingResultService pathfindingResultService;

    @GetMapping("/results")
    public List<PathfindingResult> getResults() {
        return pathfindingResultService.getAllResults();
    }
}