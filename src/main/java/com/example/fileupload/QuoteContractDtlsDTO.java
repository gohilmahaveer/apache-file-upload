package com.example.fileupload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuoteContractDtlsDTO {

    private String name;
    private String itemCode;
    private String uom;
    private Double price;
    private Integer shelfLine;
    private String errorMsg;
    private Integer rowNo;
}