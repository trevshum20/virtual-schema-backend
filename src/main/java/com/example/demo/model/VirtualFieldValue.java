package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "virtual_field_value")
@SequenceGenerator(
    name = "virtual_field_value_seq_gen",
    sequenceName = "virtual_field_value_seq",
    allocationSize = 1
)
public class VirtualFieldValue {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "virtual_field_value_seq_gen")
    private Integer id;

    @Column(name = "schema_metadata_id", nullable = false)
    private Integer schemaMetadataId;

    @Column(name = "record_id", nullable = false)
    private Integer recordId;

    @Column(name = "value", nullable = true)
    private String value;

    public VirtualFieldValue() {
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSchemaMetadataId() {
        return schemaMetadataId;
    }
    public void setSchemaMetadataId(Integer schemaMetadataId) {
        this.schemaMetadataId = schemaMetadataId;
    }

    public Integer getRecordId() {
        return recordId;
    }
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
