package algoPathfinder.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pathfinding_result")
public class PathfindingResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "algorithm")
    private String algorithm;

    @Column(name = "start_node")
    private Integer startNode;

    @Column(name = "end_node")
    private Integer endNode;

    @Column(name = "path_taken", length = 2000)
    private String pathTaken;

    @Column(name = "steps_count")
    private Integer stepsCount;

    @Column(name = "execution_time_ms")
    private Long executionTimeMs;

    @Column(name = "path_found")
    private Boolean pathFound;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    // Required by JPA — no-args constructor
    public PathfindingResult() {
    }

    // Convenience constructor for creating new results
    public PathfindingResult(String algorithm, Integer startNode, Integer endNode,
                             String pathTaken, Integer stepsCount, Long executionTimeMs,
                             Boolean pathFound, LocalDateTime timestamp) {
        this.algorithm = algorithm;
        this.startNode = startNode;
        this.endNode = endNode;
        this.pathTaken = pathTaken;
        this.stepsCount = stepsCount;
        this.executionTimeMs = executionTimeMs;
        this.pathFound = pathFound;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public Integer getStartNode() { return startNode; }
    public void setStartNode(Integer startNode) { this.startNode = startNode; }

    public Integer getEndNode() { return endNode; }
    public void setEndNode(Integer endNode) { this.endNode = endNode; }

    public String getPathTaken() { return pathTaken; }
    public void setPathTaken(String pathTaken) { this.pathTaken = pathTaken; }

    public Integer getStepsCount() { return stepsCount; }
    public void setStepsCount(Integer stepsCount) { this.stepsCount = stepsCount; }

    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public Boolean getPathFound() { return pathFound; }
    public void setPathFound(Boolean pathFound) { this.pathFound = pathFound; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}