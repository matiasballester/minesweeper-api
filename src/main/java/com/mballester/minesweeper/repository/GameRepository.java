package com.mballester.minesweeper.repository;

import com.mballester.minesweeper.model.Game;
import com.mballester.minesweeper.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<List<Game>> findByUser(User user, Sort sort);
}
