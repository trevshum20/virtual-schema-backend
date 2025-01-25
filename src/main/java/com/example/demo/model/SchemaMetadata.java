package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "schema_metadata")
@SequenceGenerator(
    name = "schema_metadata_seq_gen",
    sequenceName = "schema_metadata_seq",
    allocationSize = 1
)

public class SchemaMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "schema_metadata_seq_gen")
    private Integer id;

    @Column(name = "object_name", nullable = false)
    private String objectName;

    @Column(name = "field_name", nullable = false)
    private String fieldName;

    @Column(name = "data_type", nullable = false)
    private String dataType;

    @Column(name = "created_by_name")
    private String createdByName;

    @Column(name = "created_date",
        insertable = false,
        updatable = false,
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDate;

    // Getters and setters
    public SchemaMetadata() {
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}