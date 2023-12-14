package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Part;
import no.pgr209.machinefactory.model.PartDTO;
import no.pgr209.machinefactory.model.Subassembly;
import no.pgr209.machinefactory.repo.PartRepo;
import no.pgr209.machinefactory.repo.SubassemblyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartService {
    private final PartRepo partRepo;
    private final SubassemblyRepo subassemblyRepo;

    @Autowired
    public PartService(PartRepo partRepo, SubassemblyRepo subassemblyRepo) {
        this.partRepo = partRepo;
        this.subassemblyRepo = subassemblyRepo;
    }

    public List<Part> getAllParts() {
        return partRepo.findAll();
    }

    public List<Part> getPartsByPage(int pageNr) {
        return partRepo.findAll(PageRequest.of(pageNr, 3)).stream().toList();
    }

    public Part getPartById(Long id) {
        return partRepo.findById(id).orElse(null);
    }

    public Part createPart(PartDTO partDTO) {
        if(partDTO.getPartName() == null || partDTO.getPartName().isEmpty()){
            return null;
        }

        Part newPart = new Part();

        newPart.setPartName(partDTO.getPartName());

        return partRepo.save(newPart);
    }

    public void deletePartById(Long id) {
        Part part = partRepo.findById(id).orElse(null);

        if(part != null) {
            for(Subassembly subassembly : subassemblyRepo.findAll()) {
                if(subassembly.getParts().contains(part)) {
                    subassembly.getParts().remove(part);
                    subassemblyRepo.save(subassembly);
                }
            }
            partRepo.delete(part);
        }
    }

    public boolean partExists(Long id) {
        return partRepo.existsById(id);
    }

    public Part updatePart(Long id, PartDTO partDTO) {
        Part existingPart = partRepo.findById(id).orElse(null);

        if (existingPart != null && partDTO.getPartName() != null && !partDTO.getPartName().isEmpty()) {
            existingPart.setPartName(partDTO.getPartName());
            return partRepo.save(existingPart);
        }
        return null;
    }
}
