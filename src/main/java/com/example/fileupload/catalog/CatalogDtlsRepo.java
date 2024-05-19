package com.example.fileupload.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogDtlsRepo extends JpaRepository<CatalogItemDtls, Long> {

    CatalogItemDtls findByItemCode(Integer itemCode);
}
