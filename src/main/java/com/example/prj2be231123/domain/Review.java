package com.example.prj2be231123.domain;

import lombok.Data;
import org.apache.ibatis.annotations.Insert;

import java.time.LocalDateTime;

@Data
public class Review {
    private Integer no;
    private String title;
    private String recommend;
    private String content;
    private String writer;
    private String nickName;
    private Integer restaurantId;
    private LocalDateTime inserted;
    private int starPoint;
}