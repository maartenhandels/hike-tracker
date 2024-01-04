package com.lm.hiketracker.hiketracker.controller;

import com.lm.hiketracker.hiketracker.dto.ApiResponse;
import com.lm.hiketracker.hiketracker.dto.Hike;
import com.lm.hiketracker.hiketracker.exception.DbReadingException;
import com.lm.hiketracker.hiketracker.service.HikeService;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("api/v1/hikes")
public class HikeGetController {

  private final HikeService hikeService;

  public HikeGetController(HikeService hikeService) {
    this.hikeService = hikeService;
  }

  @GetMapping("/all")
  public ApiResponse<List<Hike>> getAllHikes() {
    try {
      return new ApiResponse<>(hikeService.getAllHikes());
    } catch (DbReadingException e) {
      log.error(e.getMessage());
      return new ApiResponse<>("Failed so read Hikes.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
