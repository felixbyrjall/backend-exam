package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Subassembly;
import no.pgr209.machinefactory.repo.SubassemblyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubassemblyService {
    private final SubassemblyRepo subassemblyRepo;

    @Autowired
    public SubassemblyService(SubassemblyRepo subassemblyRepo) {
        this.subassemblyRepo = subassemblyRepo;
    }

    //Get ALL subassemblies
    public List<Subassembly> getAllSubassemblies() {
        return subassemblyRepo.findAll();
    }

    //Get subassemblies by page
    public List<Subassembly> getSubassembliesByPage(int pageNr) {
        return subassemblyRepo.findAll(PageRequest.of(pageNr, 10)).stream().toList();
    }

    public Subassembly getSubassemblyById(Long id) {
        return subassemblyRepo.findById(id).orElse(null);
    }

    public Subassembly createSubassembly(Subassembly subassembly) {
        return subassemblyRepo.save(subassembly);
    }

    public void deleteSubassemblyById(Long id) {
        subassemblyRepo.deleteById(id);
    }

    public ResponseEntity<Subassembly> updateSubassembly(Long id, Subassembly updatedSubassembly) {
        Subassembly existingSubassembly = subassemblyRepo.findById(id).orElse(null);

        if(existingSubassembly != null) {

            existingSubassembly.setSubassemblyName(updatedSubassembly.getSubassemblyName());
            return new ResponseEntity<>(subassemblyRepo.save(existingSubassembly), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
