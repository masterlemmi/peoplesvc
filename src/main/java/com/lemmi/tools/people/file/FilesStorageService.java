package com.lemmi.tools.people.file;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {


  public void save(String name,  MultipartFile file);

  public Resource load(String filename);

  public void deleteAll();

}