package com.lm.hiketracker.hiketracker.service.impl;

import com.lm.hiketracker.hiketracker.dto.Hike;
import com.lm.hiketracker.hiketracker.exception.DbCreationException;
import com.lm.hiketracker.hiketracker.exception.DbReadingException;
import com.lm.hiketracker.hiketracker.exception.ImageStorageException;
import com.lm.hiketracker.hiketracker.service.FirestoreService;
import com.lm.hiketracker.hiketracker.service.HikeService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
public class HikeServiceImpl implements HikeService {

  private final FirestoreService firestoreService;
  private final Path imagesDirectory;
  private static final String HIKES_COLLECTION = "hikes";

  public HikeServiceImpl(
      FirestoreService firestoreService,
      @Value("${hike-tracker.images-folder}") String imagesFolder) {
    this.firestoreService = firestoreService;
    this.imagesDirectory = Path.of(imagesFolder);
  }

  public Hike saveHike(Hike hike) {
    try {
      hike.setDocumentId(firestoreService.saveDocument(hike, HIKES_COLLECTION));
    } catch (ExecutionException | InterruptedException e) {
      throw new DbCreationException("Failed to save Hike.", e);
    }
    return hike;
  }

  public void saveHikeImage(String hikeId, MultipartFile image) {
    try {
      Hike hike = firestoreService.findByDocumentId(HIKES_COLLECTION, hikeId, Hike.class);
      if (hike == null) {
        throw new ImageStorageException("No Hike with id " + hikeId + " found.");
      }
      if (hike.getImagePaths() == null) {
        hike.setImagePaths(new ArrayList<>());
      }
      hike.getImagePaths().add(saveImage(hikeId, image).toString());
      firestoreService.updateDocument(HIKES_COLLECTION, hikeId, hike);
    } catch (ExecutionException | InterruptedException e) {
      throw new ImageStorageException("Failed to load the Hike from the DB.", e);
    } catch (IOException e) {
      throw new ImageStorageException("Failed to store image.", e);
    }
  }

  @Override
  public List<Hike> getAllHikes() {
    try {
      return firestoreService.findAllByCollection(HIKES_COLLECTION, Hike.class);
    } catch (ExecutionException | InterruptedException e) {
      throw new DbReadingException("Failed to read from Firestore", e);
    }
  }

  private Path saveImage(String hikeId, MultipartFile image) throws IOException {
    String originalFilename = image.getOriginalFilename();
    if (!StringUtils.hasText(originalFilename)) {
      throw new ImageStorageException("Original Filename is null.");
    }
    // Add timestamp to filename
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    String filename = appendTimestamp(originalFilename, timestamp);
    // Relative Path starting inside the images folder (images folder not included)
    Path relativeImageFilePath = Path.of(HIKES_COLLECTION, hikeId, filename);
    Path absoluteImageFilePath =
        this.imagesDirectory.resolve(relativeImageFilePath).toAbsolutePath();
    // Create folders if they do not exist
    createFoldersIfNotExisting(absoluteImageFilePath);

    try (var inputStream = image.getInputStream()) {
      Files.copy(inputStream, absoluteImageFilePath, StandardCopyOption.REPLACE_EXISTING);
    }
    return relativeImageFilePath;
  }

  private String appendTimestamp(String filename, String timestamp) {
    int dotIndex = filename.lastIndexOf(".");
    if (dotIndex > 0) {
      // If there's an extension, insert the timestamp before it
      return filename.substring(0, dotIndex) + "_" + timestamp + filename.substring(dotIndex);
    } else {
      // If there's no extension, just append the timestamp
      return filename + "_" + timestamp;
    }
  }

  private void createFoldersIfNotExisting(Path hikeImageAbsolutePath) throws IOException {
    // If the images root directory does not exist, throw exception
    if (!Files.exists(this.imagesDirectory)) {
      throw new ImageStorageException(this.imagesDirectory + " is not a valida directory.");
    }
    // If the hike specific directory does not exist, create it
    if (!Files.exists(hikeImageAbsolutePath)) {
      Files.createDirectories(hikeImageAbsolutePath);
    }
  }
}
