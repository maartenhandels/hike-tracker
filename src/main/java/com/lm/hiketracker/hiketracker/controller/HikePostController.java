package com.lm.hiketracker.hiketracker.controller;

import com.lm.hiketracker.hiketracker.dto.ApiResponse;
import com.lm.hiketracker.hiketracker.dto.Hike;
import com.lm.hiketracker.hiketracker.exception.ImageStorageException;
import com.lm.hiketracker.hiketracker.service.HikeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@RestController
@RequestMapping("api/v1/hikes")
public class HikePostController {

  private final HikeService hikeService;

  public HikePostController(HikeService hikeService) {
    this.hikeService = hikeService;
  }

  @PostMapping("/create")
  public ApiResponse<Hike> createHike(@RequestBody Hike hike) {
    try {
      return new ApiResponse<>(hikeService.saveHike(hike));
    } catch (Exception e) {
      log.error(e.getMessage());
      return new ApiResponse<>("Failed so save Hike.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/upload-image")
  public ApiResponse<String> uploadImage(
      @RequestParam("hikeId") String hikeId, @RequestPart("image") MultipartFile imageFile) {
    try {
      hikeService.saveHikeImage(hikeId, imageFile);
      return new ApiResponse<>("Succes!!");
    } catch (ImageStorageException e) {
      log.error(e.getMessage(), e);
      return new ApiResponse<>("Error saving the image", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
