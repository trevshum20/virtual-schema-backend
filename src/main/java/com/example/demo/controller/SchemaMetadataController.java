package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.SchemaMetadata;
import com.example.demo.repository.SchemaMetadataRepository;

@RestController
@RequestMapping("/api/schema-metadata")
public class SchemaMetadataController {

    private final SchemaMetadataRepository repository;

    public SchemaMetadataController(SchemaMetadataRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<SchemaMetadata> getAllSchemaMetadata() {
        return repository.findAll();
    }

    @PostMapping
    public SchemaMetadata createSchemaMetadata(@RequestBody SchemaMetadata schemaMetadata) {
        schemaMetadata.setId(null);
        return repository.save(schemaMetadata);
    }

    @PutMapping("/{id}")
    public SchemaMetadata updateSchemaMetadata(@PathVariable Integer id, @RequestBody SchemaMetadata schemaMetadata) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setObjectName(schemaMetadata.getObjectName());
                    existing.setFieldName(schemaMetadata.getFieldName());
                    existing.setDataType(schemaMetadata.getDataType());
                    existing.setCreatedByName(schemaMetadata.getCreatedByName());
                    existing.setMaxLength(schemaMetadata.getMaxLength());
                    existing.setRequiredField(schemaMetadata.getRequiredField());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("SchemaMetadata not found with id " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteSchemaMetadata(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}
