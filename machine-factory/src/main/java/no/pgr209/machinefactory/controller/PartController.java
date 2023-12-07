package no.pgr209.machinefactory.controller;

import no.pgr209.machinefactory.model.Part;
import no.pgr209.machinefactory.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Part> getAllParts() {
        return partService.getAllParts();
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
}
