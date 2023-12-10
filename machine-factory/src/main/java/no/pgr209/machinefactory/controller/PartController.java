package no.pgr209.machinefactory.controller;

import no.pgr209.machinefactory.model.Part;
import no.pgr209.machinefactory.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/part")
public class PartController {

    private final PartService partService;

    @Autowired
    public PartController(PartService partService) {
        this.partService = partService;
    }

    //Get all part
    @GetMapping()
    public ResponseEntity<List<Part>> getAllParts() {
        List<Part> allParts = partService.getAllParts();

        if(!allParts.isEmpty()){
            return new ResponseEntity<>(allParts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Get parts by page
    @GetMapping("/page/{pageNr}")
    public List<Part> getPartsByPage(@PathVariable int pageNr) {
        return partService.getPartsByPage(pageNr);
    }

    @GetMapping("/{id}")
    public Part getPartById(@PathVariable Long id) {
        return partService.getPartById(id);
    }

    @PostMapping
    public Part createPart(@RequestBody Part part) {
        return partService.createPart(part);
    }

    @DeleteMapping("/{id}")
    public void deletePart(@PathVariable Long id) {
        partService.deletePartById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Part> updatePart(@PathVariable Long id, @RequestBody Part updatedPart) {
        return partService.updatePart(id, updatedPart);
    }
}
