package no.pgr209.machinefactory.repo;

import no.pgr209.machinefactory.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepo extends JpaRepository<Machine, Long> {
}
