package com.example.fileupload;

import com.example.fileupload.catalog.CatalogDtlsRepo;
import com.example.fileupload.catalog.CatalogItemDtls;
import com.example.fileupload.item.ItemRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload2.jakarta.JakartaServletFileUpload;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/file-upload")
public class Controller {


    private final CatalogDtlsRepo catalogDtlsRepo;
    private final ItemRepo itemRepo;

    public Controller(CatalogDtlsRepo catalogDtlsRepo, ItemRepo itemRepo) {
        this.catalogDtlsRepo = catalogDtlsRepo;
        this.itemRepo = itemRepo;
    }

    @PostMapping("/upload")
    public void upload(HttpServletRequest request) throws IOException {
        new JakartaServletFileUpload<>().getItemIterator(request).forEachRemaining(item -> {
            InputStream stream = item.getInputStream();
            if (!item.isFormField()) {
                UUID uuid = UUID.randomUUID();
                Path destinationPath = Paths.get("upload" + File.separator + uuid + "_" + item.getName()).toAbsolutePath().normalize();
                Files.copy(stream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                log.info("{} is successfully uploaded.", item.getName());
            }
        });
    }

    @PostMapping("/validateExcel")
    public ResponseEntity<List<QuoteContractDtlsDTO>> validateExcel(HttpServletRequest request) {
        List<QuoteContractDtlsDTO> contractDtlsDTOList = new ArrayList<>();
        try {
            new JakartaServletFileUpload<>().getItemIterator(request).forEachRemaining(item -> {
                InputStream stream = item.getInputStream();
                if (!item.isFormField()) {

                    XSSFWorkbook workbook = new XSSFWorkbook(stream);
                    XSSFSheet sheet = workbook.getSheetAt(0);
                    Iterator<Row> iterator = sheet.iterator();


                    // Skip the header row if necessary
                    if (iterator.hasNext()) {
                        iterator.next();
                    }

                    iterator.forEachRemaining(row -> {

                        QuoteContractDtlsDTO dto = new QuoteContractDtlsDTO();
                        int index = 0;

                        try {
                            long numericValue = (long) row.getCell(index).getNumericCellValue();
                            dto.setRowNo(row.getRowNum());
                            dto.setErrorMsg(":-)");
                            itemRepo.findByItemCode(numericValue).ifPresent(data -> {
                                dto.setName("TEST");
                                dto.setItemCode(data.getItemCode().toString());
                                dto.setShelfLine(data.getShelfLine());
                                dto.setUom(data.getUom());
                            });
                            CatalogItemDtls catalogItemDtls = catalogDtlsRepo.findByItemCode((int) numericValue);
                            if (null != catalogItemDtls) {
                                dto.setPrice(catalogItemDtls.getPrice());
                            }
                        } catch (Exception ex) {
                            dto.setErrorMsg("Code is not valid");
                            dto.setRowNo(row.getRowNum());
                        }

                        contractDtlsDTOList.add(dto);

                    });

                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(contractDtlsDTOList, HttpStatusCode.valueOf(200));

    }

}
