package com.example.fileupload.catalog;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "catalog_item_dtls")
public class CatalogItemDtls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "catalog_id")
    @JsonBackReference
    private Catalog catalog;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "item_code")
    private Integer itemCode;

    @Column(name = "price")
    private Double price;

}
