package no.pgr209.machinefactory.controller;

import no.pgr209.machinefactory.model.Subassembly;
import no.pgr209.machinefactory.service.SubassemblyService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Subassembly> getAllSubassemblies() {
        return subassemblyService.getAllSubassemblies();
    }

    //Get subassemblies by page
    @GetMapping("/page/{pageNr}")
    public List<Subassembly> getSubassembliesByPage(@PathVariable int pageNr) {
        return subassemblyService.getSubassembliesByPage(pageNr);
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
}
