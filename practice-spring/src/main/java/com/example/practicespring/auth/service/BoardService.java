package com.example.practicespring.auth.service;

import com.example.practicespring.auth.dto.BoardListResponse;
import com.example.practicespring.auth.dto.BoardResponse;
import com.example.practicespring.auth.dto.SaveBoardRequest;
import com.example.practicespring.auth.entity.Board;
import com.example.practicespring.auth.entity.Member;
import com.example.practicespring.auth.repository.BoardRepository;
import com.example.practicespring.auth.repository.MemberRepository;
import com.example.practicespring.global.exception.customException.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void saveBoard(SaveBoardRequest request, String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException("해당 유저는 존재하지 않습니다.", HttpStatus.NOT_FOUND));
        Board board = Board.builder()
            .content(request.content())
            .author(member.getName())
            .title(request.title())
            .build();

        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public BoardListResponse getBoardByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException("해당 유저는 존재하지 않습니다.", HttpStatus.NOT_FOUND));
        List<Board> boardList = boardRepository.findByAuthor(member.getName());
        List<BoardResponse> boardResponseList = boardList.stream()
            .map(o -> BoardResponse.of(o.getTitle(), o.getContent(), o.getAuthor())).toList();
        return new BoardListResponse(boardResponseList);
    }

    @Transactional(readOnly = true)
    public BoardListResponse getAllBoard() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardResponse> boardResponseList = boardList.stream()
            .map(o -> BoardResponse.of(o.getTitle(), o.getContent(), o.getAuthor())).toList();
        return new BoardListResponse(boardResponseList);
    }

}
