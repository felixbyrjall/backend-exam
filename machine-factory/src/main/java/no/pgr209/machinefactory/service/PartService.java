package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Part;
import no.pgr209.machinefactory.repo.PartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartService {
    private final PartRepo partRepo;

    @Autowired
    public PartService(PartRepo partRepo) {
        this.partRepo = partRepo;
    }

    public Page<Part> getParts(Pageable pageable) {
        return partRepo.findAll(pageable);
    }

    public Part getPartById(Long id) {
        return partRepo.findById(id).orElse(null);
    }

    public Part createPart(Part part) {
        return partRepo.save(part);
    }

    public void deletePartById(Long id) {
        partRepo.deleteById(id);
    }
}
