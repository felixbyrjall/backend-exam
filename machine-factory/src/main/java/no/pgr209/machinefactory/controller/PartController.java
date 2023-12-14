package no.pgr209.machinefactory.controller;

import no.pgr209.machinefactory.model.Part;
import no.pgr209.machinefactory.model.PartDTO;
import no.pgr209.machinefactory.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

    @GetMapping()
    public ResponseEntity<List<Part>> getAllParts() {
        List<Part> allParts = partService.getAllParts();

        if(!allParts.isEmpty()){
            return new ResponseEntity<>(allParts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

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
    public ResponseEntity<Part> createPart(@RequestBody PartDTO partDTO) {
        Part createdPart = partService.createPart(partDTO);

        if(createdPart != null) {
            return new ResponseEntity<>(createdPart, HttpStatus.CREATED);
        } else {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Error", "field is invalid");
            return new ResponseEntity<>(responseHeaders, HttpStatus.NOT_FOUND);
        }
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
    public ResponseEntity<Part> updatePart(@PathVariable Long id, @RequestBody PartDTO partDTO) {
        Part updatedPart = partService.updatePart(id, partDTO);

        if(updatedPart != null) {
            return new ResponseEntity<>(updatedPart, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
