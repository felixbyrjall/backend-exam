package no.pgr209.machinefactory.controller;

import no.pgr209.machinefactory.model.Subassembly;
import no.pgr209.machinefactory.service.SubassemblyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subassembly")
public class SubassemblyController {

    private final SubassemblyService subassemblyService;

    @Autowired
    public SubassemblyController(SubassemblyService subassemblyService) {
        this.subassemblyService = subassemblyService;
    }

    //Get all subassembly
    @GetMapping()
    public ResponseEntity<List<Subassembly>> getAllSubassemblies() {
        List<Subassembly> allSubassemblies = subassemblyService.getAllSubassemblies();

        if(!allSubassemblies.isEmpty()) {
            return new ResponseEntity<>(allSubassemblies, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Get subassemblies by page
    @GetMapping("/page/{pageNr}")
    public ResponseEntity<List<Subassembly>> getSubassembliesByPage(@PathVariable int pageNr) {
        List<Subassembly> subassembliesByPage = subassemblyService.getSubassembliesByPage(pageNr);

        if(!subassembliesByPage.isEmpty()) {
            return new ResponseEntity<>(subassembliesByPage, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public Subassembly getSubassemblyById(@PathVariable Long id) {
        return subassemblyService.getSubassemblyById(id);
    }

    @PostMapping
    public Subassembly createSubassembly(Subassembly subassembly) {
        return subassemblyService.createSubassembly(subassembly);
    }

    @DeleteMapping("/{id}")
    public void deleteSubassemblyById(@PathVariable Long id) {
        subassemblyService.deleteSubassemblyById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subassembly> updateSubassembly(@PathVariable Long id, @RequestBody Subassembly updatedSubassembly) {
        return subassemblyService.updateSubassembly(id, updatedSubassembly);
    }
}
