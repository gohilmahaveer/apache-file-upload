package com.example.fileupload.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_code")
    private Long itemCode;

    @Column(name = "uom")
    private String uom;

    @Column(name = "shelf_life")
    private Integer shelfLine;

}
