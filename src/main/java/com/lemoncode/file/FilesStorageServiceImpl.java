package com.lemoncode.file;

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

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    @Value("${imageDir}")
    private String imageDir;

    private static final Logger LOG = LoggerFactory.getLogger(FilesStorageServiceImpl.class);

    @Autowired
    DocumentSanitizer sanitizer;

    @Override
    public void save(String name, MultipartFile uploaded) {
        Path tmpPath = null;
        try {
            File tmpFile = File.createTempFile("uploaded-", null);
            tmpPath = tmpFile.toPath();
            long copiedBytesCount = Files.copy(uploaded.getInputStream(), tmpPath, StandardCopyOption.REPLACE_EXISTING);
            if (copiedBytesCount != uploaded.getSize()) {
                throw new IOException(String.format("Error during stream copy to temporary disk (copied: %s / expected: %s !", copiedBytesCount, uploaded.getSize()));
            }

            boolean isSafe = sanitizer.madeSafe(tmpFile);

            if (!isSafe) {
                LOG.warn("Detection of a unsafe file upload or cannot sanitize uploaded document !");
                safelyRemoveFile(tmpPath);
                throw new RuntimeException("File not safe");
            } else {
                // Create a HASH of the file to check the integrity of the uploaded content
                byte[] content = Files.readAllBytes(tmpPath);
                MessageDigest digester = MessageDigest.getInstance("sha-256");
                byte[] hash = digester.digest(content);
                String hashHex = DatatypeConverter.printHexBinary(hash);
                LOG.info("Received temp file SHA256 : {}\n", hashHex);


                File dir = new File(imageDir);
                File file = new File(dir, name);
                Files.copy(uploaded.getInputStream(), file.toPath());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }

    }

    @Override
    public Resource load(String filename) {
        try {
            Path root = Paths.get(imageDir );
            Path file = root.resolve(filename);
            LOG.info("FileName" + file.toFile());
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
            LOG.warn("Cannot safely remove file !", e);
        }
    }


}