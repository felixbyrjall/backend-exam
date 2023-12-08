package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Part;
import no.pgr209.machinefactory.repo.PartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartService {
    private final PartRepo partRepo;

    @Autowired
    public PartService(PartRepo partRepo) {
        this.partRepo = partRepo;
    }

    //Get ALL parts
    public List<Part> getAllParts() {
        return partRepo.findAll();
    }

    //Get parts by page
    public List<Part> getPartsByPage(int pageNr) {
        return partRepo.findAll(PageRequest.of(pageNr, 10)).stream().toList();
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

    public ResponseEntity<Part> updatePart(Long id, Part updatedPart) {
        Part existingPart = partRepo.findById(id).orElse(null);

        if(existingPart != null) {

            existingPart.setPartName(updatedPart.getPartName());
            return new ResponseEntity<>(partRepo.save(existingPart), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
