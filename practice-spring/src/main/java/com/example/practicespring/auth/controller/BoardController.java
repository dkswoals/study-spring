package com.example.practicespring.auth.controller;

import com.example.practicespring.auth.dto.BoardListResponse;
import com.example.practicespring.auth.dto.SaveBoardRequest;
import com.example.practicespring.auth.entity.LoginUser;
import com.example.practicespring.auth.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "게시판 서비스")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시물 등록 API, email은 매개변수가 아님 무시할 것(토큰만 넘기기)")
    @PostMapping
    public ResponseEntity<Void> addBoard(@LoginUser String email,
        @RequestBody SaveBoardRequest board) {
        boardService.saveBoard(board, email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "본인이 작성한 게시물을 조회하는 API, email은 매개변수가 아님 무시할 것(토큰만 넘기기)")
    @GetMapping("/my")
    public ResponseEntity<BoardListResponse> getMyBoards(@LoginUser String email) {
        BoardListResponse boardListResponse = boardService.getBoardByEmail(email);
        return ResponseEntity.ok().body(boardListResponse);
    }

    @Operation(summary = "모든 게시물을 조회하는 API")
    @GetMapping("/all")
    public ResponseEntity<BoardListResponse> getAllBoards() {
        return ResponseEntity.ok().body(boardService.getAllBoard());
    }
}
