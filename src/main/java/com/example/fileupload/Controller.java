package com.example.fileupload;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload2.jakarta.JakartaServletFileUpload;
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
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/file-upload")
public class Controller {

    @PostMapping("/upload")
    public void upload(HttpServletRequest request) throws IOException {
        new JakartaServletFileUpload<>().getItemIterator(request).forEachRemaining(item -> {
            String name = item.getFieldName();
            InputStream stream = item.getInputStream();
            if (!item.isFormField()) {
                UUID uuid = UUID.randomUUID();
                Path destinationPath = Paths.get("upload" + File.separator + uuid + "_" + item.getName()).toAbsolutePath().normalize();
                Files.copy(stream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                log.info(item.getName() + " is successfully uploaded.");
            }
        });
    }

}
