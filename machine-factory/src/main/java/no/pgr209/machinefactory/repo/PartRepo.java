package no.pgr209.machinefactory.repo;

import no.pgr209.machinefactory.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepo extends JpaRepository<Part, Long> {
}
