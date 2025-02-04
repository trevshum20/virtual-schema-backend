package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.VirtualFieldValue;
import com.example.demo.repository.VirtualFieldValueRepository;

@RestController
@RequestMapping("/api/virtual-field-values")
public class VirtualFieldValueController {

    private final VirtualFieldValueRepository vfValueRepo;
    private static final Logger logger = LoggerFactory.getLogger(VirtualFieldValueController.class);

    public VirtualFieldValueController(VirtualFieldValueRepository vfValueRepo) {
        this.vfValueRepo = vfValueRepo;
    }

    @GetMapping
    public List<VirtualFieldValue> getAll() {
        return vfValueRepo.findAll();
    }

    @GetMapping("/record/{recordId}")
    public List<VirtualFieldValue> getByRecordId(@PathVariable Integer recordId) {
        return vfValueRepo.findByRecordId(recordId);
    }

    @GetMapping("/mass-truncate/{metadataId}/{length}")
    public ResponseEntity<String> massTruncate(@PathVariable Integer metadataId, @PathVariable Integer length) {
        List<VirtualFieldValue> records = vfValueRepo.findBySchemaMetadataId(metadataId);

        for (VirtualFieldValue record : records) {
            if (record.getValue() != null && record.getValue().length() > length) {
                record.setValue(record.getValue().substring(0, length));
            }
        }

        vfValueRepo.saveAll(records);
        return ResponseEntity.ok("Truncated " + records.size() + " records.");
    }

    @GetMapping("/mass-type-change/{metadataId}")
    public ResponseEntity<String> massTypeChange(@PathVariable Integer metadataId) {
        List<VirtualFieldValue> records = vfValueRepo.findBySchemaMetadataId(metadataId);

        for (VirtualFieldValue record : records) {
            record.setValue(null); // Delete value
        }

        vfValueRepo.saveAll(records);
        return ResponseEntity.ok("Cleared values for " + records.size() + " records.");
    }

    @PostMapping
    public VirtualFieldValue createValue(@RequestBody VirtualFieldValue vfv) {
        return vfValueRepo.save(vfv);
    }

    @PutMapping("/{id}")
    public VirtualFieldValue updateValue(@PathVariable Integer id, @RequestBody VirtualFieldValue updated) {
        System.out.println("Received request to update ID: " + id);
        System.out.println("Incoming updated object: " + updated);
        return vfValueRepo.findById(id)
                .map(existing -> {
                    existing.setValue(updated.getValue());
                    // If needed, also update schemaMetadataId or recordId
                    return vfValueRepo.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Value not found with id: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteValue(@PathVariable Integer id) {
        vfValueRepo.deleteById(id);
    }

    // DELETE all values for a given record_id
    @DeleteMapping("/record/{recordId}")
    public void deleteByRecordId(@PathVariable Integer recordId) {
        List<VirtualFieldValue> values = vfValueRepo.findByRecordId(recordId);
        vfValueRepo.deleteAll(values);
    }
}
