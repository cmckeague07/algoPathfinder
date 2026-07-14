package algoPathfinder.Repository;

import algoPathfinder.Model.PathfindingResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PathfindingResultRepository extends JpaRepository<PathfindingResult, Long> {

}