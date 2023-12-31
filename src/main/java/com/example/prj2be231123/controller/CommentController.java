package com.example.prj2be231123.controller;

import com.example.prj2be231123.domain.Comment;
import com.example.prj2be231123.domain.Member;
import com.example.prj2be231123.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService service;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Comment comment,
                              @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            // 로그인 했는지 안했는지 확인
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // validate 로 필요한 데이터가 전부 있는지 검증하기
        if (service.validate(comment)) {
            // Comment 객체를 받아서 service로 보내 일을 시킴
            if (service.add(comment, login)) {
                // 로그인 데이터를 받을수 있도록 파라미터 추가
                return ResponseEntity.ok().build();
            } else {
                // 서버에 문제 발생
                return ResponseEntity.internalServerError().build();
            }
        } else {
            // 요청이 잘못됨
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("list")
    public List<Comment> list(@RequestParam("id") Integer reviewId) {
        return service.list(reviewId);
    }

    @DeleteMapping("{no}")
    public ResponseEntity remove(@PathVariable Integer no,
                                         @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        if (service.hasAccess(no, login)) {
            if (service.remove(no)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    @PutMapping("edit")
    public ResponseEntity update(@RequestBody Comment comment,
                       @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (service.hasAccess(comment.getNo(), login)) {
            if (!service.editValidate(comment)) {
                // reviewId가 필요없기 때문에 다시 만든다
                return ResponseEntity.badRequest().build();
            }

             if (service.update(comment)) {
                 return ResponseEntity.ok().build();
             } else {
                 return ResponseEntity.internalServerError().build();
             }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }
}
