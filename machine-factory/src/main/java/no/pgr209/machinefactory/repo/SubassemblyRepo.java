package no.pgr209.machinefactory.repo;

import no.pgr209.machinefactory.model.Subassembly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubassemblyRepo extends JpaRepository<Subassembly, Long> {
}
