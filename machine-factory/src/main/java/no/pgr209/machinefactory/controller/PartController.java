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
    public ResponseEntity<List<Part>> getPartsByPage(@PathVariable int pageNr) {
        List<Part> partsByPage = partService.getPartsByPage(pageNr);

        if(!partsByPage.isEmpty()){
            return new ResponseEntity<>(partsByPage, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Part> getPartById(@PathVariable Long id) {
        Part partById = partService.getPartById(id);

        if(partById != null){
            return new ResponseEntity<>(partById, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public Part createPart(@RequestBody Part part) {
        return partService.createPart(part);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePart(@PathVariable Long id) {
        if (partService.partExists(id)) {
            partService.deletePartById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Part> updatePart(@PathVariable Long id, @RequestBody Part updatedPart) {
        return partService.updatePart(id, updatedPart);
    }
}
