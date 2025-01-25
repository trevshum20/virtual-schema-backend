package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.VirtualFieldValue;

@Repository
public interface VirtualFieldValueRepository extends JpaRepository<VirtualFieldValue, Integer> {
    List<VirtualFieldValue> findByRecordId(Integer recordId);
    List<VirtualFieldValue> findBySchemaMetadataId(Integer schemaMetadataId);
}
