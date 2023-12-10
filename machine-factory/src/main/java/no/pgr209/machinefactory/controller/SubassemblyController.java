package no.pgr209.machinefactory.controller;

import no.pgr209.machinefactory.model.Subassembly;
import no.pgr209.machinefactory.model.SubassemblyDTO;
import no.pgr209.machinefactory.service.SubassemblyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<Subassembly> getSubassemblyById(@PathVariable Long id) {
        Subassembly subassemblyById = subassemblyService.getSubassemblyById(id);

        if(subassemblyById != null) {
            return new ResponseEntity<>(subassemblyById, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Subassembly> createSubassembly(@RequestBody SubassemblyDTO subassemblyDTO) {
        Subassembly createdSubassembly = subassemblyService.createSubassembly(subassemblyDTO);

        if(createdSubassembly != null) {
            return new ResponseEntity<>(createdSubassembly, HttpStatus.CREATED);
        } else {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Error", "One or more fields are invalid");
            return new ResponseEntity<>(responseHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubassemblyById(@PathVariable Long id) {
        if (subassemblyService.subassemblyExists(id)) {
            subassemblyService.deleteSubassemblyById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subassembly> updateSubassembly(@PathVariable Long id, @RequestBody SubassemblyDTO subassemblyDTO) {
        Subassembly updatedSubassembly = subassemblyService.updateSubassembly(id, subassemblyDTO);

        if(updatedSubassembly != null && updatedSubassembly.getSubassemblyName() != null && updatedSubassembly.getParts() != null) {
            return new ResponseEntity<>(updatedSubassembly, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
