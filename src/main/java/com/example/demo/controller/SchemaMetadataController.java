package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
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

    @CrossOrigin(origins = "http://virtual-schema-react.s3-website-us-east-1.amazonaws.com")
    @GetMapping
    public List<SchemaMetadata> getAllSchemaMetadata() {
        return repository.findAll();
    }

    @CrossOrigin(origins = "http://virtual-schema-react.s3-website-us-east-1.amazonaws.com")
    @PostMapping
    public SchemaMetadata createSchemaMetadata(@RequestBody SchemaMetadata schemaMetadata) {
        schemaMetadata.setId(null);
        return repository.save(schemaMetadata);
    }

    @CrossOrigin(origins = "http://virtual-schema-react.s3-website-us-east-1.amazonaws.com")
    @PutMapping("/{id}")
    public SchemaMetadata updateSchemaMetadata(@PathVariable Integer id, @RequestBody SchemaMetadata schemaMetadata) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setObjectName(schemaMetadata.getObjectName());
                    existing.setFieldName(schemaMetadata.getFieldName());
                    existing.setDataType(schemaMetadata.getDataType());
                    existing.setCreatedByName(schemaMetadata.getCreatedByName());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("SchemaMetadata not found with id " + id));
    }

    @CrossOrigin(origins = "http://virtual-schema-react.s3-website-us-east-1.amazonaws.com")
    @DeleteMapping("/{id}")
    public void deleteSchemaMetadata(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}
