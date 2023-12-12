package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.repo.MachineRepo;
import no.pgr209.machinefactory.repo.PartRepo;
import no.pgr209.machinefactory.repo.SubassemblyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SubassemblyService {
    private final SubassemblyRepo subassemblyRepo;
    private final PartRepo partRepo;
    private final MachineRepo machineRepo;

    @Autowired
    public SubassemblyService(SubassemblyRepo subassemblyRepo, PartRepo partRepo, MachineRepo machineRepo) {
        this.subassemblyRepo = subassemblyRepo;
        this.partRepo = partRepo;
        this.machineRepo = machineRepo;
    }

    //Get ALL subassemblies
    public List<Subassembly> getAllSubassemblies() {
        return subassemblyRepo.findAll();
    }

    //Get subassemblies by page
    public List<Subassembly> getSubassembliesByPage(int pageNr) {
        return subassemblyRepo.findAll(PageRequest.of(pageNr, 10)).stream().toList();
    }

    public Subassembly getSubassemblyById(Long id) {
        return subassemblyRepo.findById(id).orElse(null);
    }

    public Subassembly createSubassembly(SubassemblyDTO subassemblyDTO) {
        if (subassemblyDTO.getSubassemblyName() == null || subassemblyDTO.getSubassemblyName().isEmpty() ||
                subassemblyDTO.getPartId() == null || !subassemblyDTO.getPartId().stream().allMatch(partRepo::existsById)) {
            return null;
        }

        Subassembly newSubassembly = new Subassembly();
        newSubassembly.setSubassemblyName(subassemblyDTO.getSubassemblyName());
        newSubassembly.setParts(partRepo.findAllById(subassemblyDTO.getPartId()));

        return subassemblyRepo.save(newSubassembly);
    }

    public void deleteSubassemblyById(Long id) {
        Subassembly subassembly = subassemblyRepo.findById(id).orElse(null);

        if(subassembly != null) {
            for(Machine machine : machineRepo.findAll()) {
                if(machine.getSubassemblies().contains(subassembly)) {
                    machine.getSubassemblies().remove(subassembly);
                    machineRepo.save(machine);
                }
            }

            subassemblyRepo.delete(subassembly);
        }
    }

    public boolean subassemblyExists(Long id) {
        return subassemblyRepo.existsById(id);
    }

    public Subassembly updateSubassembly(Long id, SubassemblyDTO subassemblyDTO) {
        Subassembly existingSubassembly = subassemblyRepo.findById(id).orElse(null);

        if (existingSubassembly == null || subassemblyDTO.getSubassemblyName() == null ||
                subassemblyDTO.getSubassemblyName().isEmpty() || subassemblyDTO.getPartId() == null) {
            return null;
        }

        existingSubassembly.setSubassemblyName(subassemblyDTO.getSubassemblyName());

        List<Long> partIds = subassemblyDTO.getPartId();

        if (!partIds.isEmpty()) {
            List<Part> parts = partRepo.findAllById(partIds);

            if (parts.size() == partIds.size()) {
                existingSubassembly.setParts(parts);
            } else {
                return null;
            }
        } else {
            existingSubassembly.setParts(Collections.emptyList());
        }

        return subassemblyRepo.save(existingSubassembly);
    }
}
