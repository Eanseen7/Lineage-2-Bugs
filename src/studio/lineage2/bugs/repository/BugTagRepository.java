package studio.lineage2.bugs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studio.lineage2.bugs.model.BugTag;

/**
 Eanseen
 23.06.2016
 */
@Repository public interface BugTagRepository extends JpaRepository<BugTag, Long>
{}