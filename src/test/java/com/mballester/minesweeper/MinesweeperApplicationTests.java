package com.mballester.minesweeper;

import com.mballester.minesweeper.model.Game;
import com.mballester.minesweeper.model.GameBoardSettings;
import com.mballester.minesweeper.service.MinesWeeperService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MinesweeperApplicationTests {

	@Autowired
	MinesWeeperService minesWeeperService;

	@Test
	void contextLoads() {
		assertThat(minesWeeperService).isNotNull();
	}

	void testBoardCreatedSuccessfully() {
		GameBoardSettings gameBoardSettings = new GameBoardSettings("Matias", 2, 2, 1);
		Game game = minesWeeperService.createGame(gameBoardSettings);
		Assert.isTrue(game.getBoard().length == 2, "Board successfully created");
	}

}