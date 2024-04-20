package com.ez4bk.eztakeout.controller;

import com.ez4bk.eztakeout.common.R;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${eztakeout.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        log.info("Upload file");

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return R.error("Invalid file");
        }
        String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));

        File dir = new File(basePath);
        if (!dir.exists()) {
            log.info("Create directory: {}, result: {}", basePath, dir.mkdirs());
        }

        try {
            file.transferTo(new File(basePath + fileName));
        } catch (Exception e) {
            log.error("Upload file failed", e);
            return R.error("Upload file failed");
        }

        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        log.info("Download file: {}", name);
        try (FileInputStream file = new FileInputStream(basePath + name)) {
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpg");
            int len;
            byte[] buffer = new byte[1024];
            while ((len = file.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }
        } catch (IOException e) {
            log.error("Download file failed", e);
        }

    }
}
