package com.lm.hiketracker.hiketracker.service;

import com.lm.hiketracker.hiketracker.dto.Hike;
import org.springframework.web.multipart.MultipartFile;

public interface HikeService {

  Hike saveHike(Hike hike);

  void saveHikeImage(String hikeId, MultipartFile image);
}
