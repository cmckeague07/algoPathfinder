package algoPathfinder.Service;

import algoPathfinder.Model.PathfindingResult;
import algoPathfinder.Repository.PathfindingResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PathFindingResultService {

    @Autowired
    private PathfindingResultRepository repository;

    public PathfindingResult saveResult(String algorithm, Integer startNode, Integer endNode,
                                        String pathTaken, Integer stepsCount, Long executionTimeMs,
                                        Boolean pathFound) {

        PathfindingResult result = new PathfindingResult(
                algorithm,
                startNode,
                endNode,
                pathTaken,
                stepsCount,
                executionTimeMs,
                pathFound,
                LocalDateTime.now()
        );

        return repository.save(result);
    }

    public List<PathfindingResult> getAllResults() {
        return repository.findAll();
    }
}
