package com.example.practicespring.auth.repository;


import com.example.practicespring.auth.entity.Board;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByAuthor(String author);

}
