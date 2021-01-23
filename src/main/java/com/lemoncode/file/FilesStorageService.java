package com.lemoncode.file;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {


  public void save(long personId, String name,  MultipartFile file);

  public Resource load(long personId, String filename);

  public void deleteAll();

}