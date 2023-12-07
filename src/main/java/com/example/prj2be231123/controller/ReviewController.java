package com.example.prj2be231123.controller;

import com.example.prj2be231123.domain.Member;
import com.example.prj2be231123.domain.Review;
import com.example.prj2be231123.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService service;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Review review,
                              @SessionAttribute(value = "login", required = false) Member login) {
        System.out.println("login = " + login);
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 맛집정보 받아올때까지 주석
//        if (!service.validate(review)) {
//            return ResponseEntity.badRequest().build();
//        }

        if (service.save(review, login)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    // /api/review/list?p=1
    // /api/review/list?k=words
    @GetMapping("list")
    public Map<String, Object> list(@RequestParam(value = "p", defaultValue = "1") Integer page,
                                    @RequestParam(value = "k", defaultValue = "") String keyword) {
        // 페이지를 나누기 위한 프로퍼티 입력
        // List<Review> 리스트로 데이터를 넘겼는데 Map으로 변경
        return service.list(page, keyword);
    }

    @GetMapping("no/{no}")
    public Review get(@PathVariable Integer no) {
        return service.get(no);
    }

    @DeleteMapping("remove/{no}")
    public ResponseEntity remove(@PathVariable Integer no,
                                 @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!service.hasAccess(no, login)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (service.remove(no)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("edit")
    public ResponseEntity edit(@RequestBody Review review,
                               @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!service.hasAccess(review.getNo(), login)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (service.validate(review)) {
            if (service.update(review)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
