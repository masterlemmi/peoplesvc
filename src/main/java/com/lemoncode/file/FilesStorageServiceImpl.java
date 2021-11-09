package com.lemoncode.file;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.security.MessageDigest;
import org.apache.commons.io.FileUtils;
@Service
@Slf4j
public class FilesStorageServiceImpl implements FilesStorageService {

    @Value("${imageDir}")
    private String imageDir;

    @Autowired
    DocumentSanitizer sanitizer;

    @Override
        public void save(String name, MultipartFile uploaded) {
        Path tmpPath = null;
        try {
            File tmpFile = File.createTempFile("uploaded-", null);

            tmpPath = tmpFile.toPath();
            log.info("Temp file: {}", tmpPath.toString());
            log.info("TEmp file exists: {}", tmpFile.exists());

            long copiedBytesCount = Files.copy(uploaded.getInputStream(), tmpPath, StandardCopyOption.REPLACE_EXISTING);
            if (copiedBytesCount != uploaded.getSize()) {
                throw new IOException(String.format("Error during stream copy to temporary disk (copied: %s / expected: %s !", copiedBytesCount, uploaded.getSize()));
            }

            log.info("TEmp file exists after tempfile copy: {} size: {}", tmpFile.exists(), copiedBytesCount);

            boolean isSafe = sanitizer.madeSafe(tmpFile);

            if (!isSafe) {
                log.warn("Detection of a unsafe file upload or cannot sanitize uploaded document !");
                safelyRemoveFile(tmpPath);
                throw new RuntimeException("File not safe");
            } else {
                // Create a HASH of the file to check the integrity of the uploaded content
                byte[] content = Files.readAllBytes(tmpPath);
                MessageDigest digester = MessageDigest.getInstance("sha-256");
                byte[] hash = digester.digest(content);
                String hashHex = DatatypeConverter.printHexBinary(hash);
                log.info("Received temp file SHA256 : {}\n", hashHex);


//                File dir = new File(imageDir);
                File file = new File(imageDir + "/" + name);

                log.info("TEmp file exists before copy: {}", tmpFile.exists());
                log.info("Copied file exists before copy: {}", file.exists());

//                boolean movedSuccess = tmpPath.toFile().renameTo(file);
//                log.info("Move File Success: {}", movedSuccess);

                FileUtils.copyFile(tmpPath.toFile(), file);
                safelyRemoveFile(tmpPath);
                log.info("TEmp file exists after copy: {}", tmpFile.exists());
                log.info("Copied file exists after copy: {}", file.exists());


            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("TEmp file exists after copy: {}", tmpPath.toFile().exists());

            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }

    }

    @Override
    public Resource load(String filename) {
        try {
            Path root = Paths.get(imageDir );
            Path file = root.resolve(filename);
            log.info("FileName: {}" , file.toFile());
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        //FileSystemUtils.deleteRecursively(root.toFile());
    }


    private static void safelyRemoveFile(Path p) {
        try {
            if (p != null) {
                // Remove temporary file
                if (!Files.deleteIfExists(p)) {
                    // If remove fail then overwrite content to sanitize it
                    Files.write(p, "-".getBytes("utf8"), StandardOpenOption.CREATE);
                }
            }
        } catch (Exception e) {
            log.warn("Cannot safely remove file !", e);
        }
    }


}