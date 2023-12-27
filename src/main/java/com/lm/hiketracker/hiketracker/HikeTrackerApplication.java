package com.lm.hiketracker.hiketracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class HikeTrackerApplication {

  public static void main(String[] args) {
    SpringApplication.run(HikeTrackerApplication.class, args);
  }
}
