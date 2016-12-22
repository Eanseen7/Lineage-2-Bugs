package studio.lineage2.bugs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studio.lineage2.bugs.model.MAccount;

@Repository public interface MAccountRepository extends JpaRepository<MAccount, Long>
{}