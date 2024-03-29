package com.lm.hiketracker.hiketracker.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hike {

  private String documentId;
  private String title;
  private Coordinates coordinates;
  private List<String> imagePaths;
}
