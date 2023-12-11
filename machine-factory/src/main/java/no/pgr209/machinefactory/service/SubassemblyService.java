package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Part;
import no.pgr209.machinefactory.model.Subassembly;
import no.pgr209.machinefactory.model.SubassemblyDTO;
import no.pgr209.machinefactory.repo.PartRepo;
import no.pgr209.machinefactory.repo.SubassemblyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubassemblyService {
    private final SubassemblyRepo subassemblyRepo;
    private final PartRepo partRepo;

    @Autowired
    public SubassemblyService(SubassemblyRepo subassemblyRepo, PartRepo partRepo) {
        this.subassemblyRepo = subassemblyRepo;
        this.partRepo = partRepo;
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

    public Subassembly createSubassembly(SubassemblyDTO subassemblyDTO) {
        Subassembly newSubassembly = new Subassembly();

        if(subassemblyDTO.getSubassemblyName() == null) {
            return null;
        }
        newSubassembly.setSubassemblyName(subassemblyDTO.getSubassemblyName());

        List<Long> partIds = subassemblyDTO.getPartId();
        if(!partIds.stream().allMatch(partRepo::existsById)) {
            return null;
        }
        newSubassembly.setParts(partRepo.findAllById(partIds));

        return subassemblyRepo.save(newSubassembly);
    }

    public void deleteSubassemblyById(Long id) {
        subassemblyRepo.deleteById(id);
    }

    public boolean subassemblyExists(Long id) {
        return subassemblyRepo.existsById(id);
    }

    public Subassembly updateSubassembly(Long id, SubassemblyDTO subassemblyDTO) {
        Subassembly existingSubassembly = subassemblyRepo.findById(id).orElse(null);

        if(existingSubassembly != null) {

            if(subassemblyDTO.getSubassemblyName() != null) {
                existingSubassembly.setSubassemblyName(subassemblyDTO.getSubassemblyName());
            }

            if(existingSubassembly.getSubassemblyId() != null) {
                List<Part> parts = partRepo.findAllById(subassemblyDTO.getPartId());
                existingSubassembly.setParts(parts);
            }

            return subassemblyRepo.save(existingSubassembly);

        } else {
            return null;
        }
    }
}
