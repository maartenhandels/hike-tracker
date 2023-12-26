package com.lm.hiketracker.hiketracker.dto;

import java.util.List;
import lombok.Data;

@Data
public class Hike {

  private String documentId;

  private String title;

  private List<String> imagePaths;
}
