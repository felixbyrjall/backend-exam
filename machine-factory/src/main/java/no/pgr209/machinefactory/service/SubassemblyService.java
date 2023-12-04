package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Subassembly;
import no.pgr209.machinefactory.repo.SubassemblyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubassemblyService {
    private final SubassemblyRepo subassemblyRepo;

    @Autowired
    public SubassemblyService(SubassemblyRepo subassemblyRepo) {
        this.subassemblyRepo = subassemblyRepo;
    }

    public List<Subassembly> getAllSubassemblies() {
        return subassemblyRepo.findAll();
    }

    public Subassembly getSubassemblyById(Long id) {
        return subassemblyRepo.findById(id).orElse(null);
    }

    public Subassembly createSubassembly(Subassembly subassembly) {
        return subassemblyRepo.save(subassembly);
    }

    public void deleteSubassembly(Long id) {
        subassemblyRepo.deleteById(id);
    }
}
