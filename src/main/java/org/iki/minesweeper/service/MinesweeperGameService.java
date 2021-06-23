package org.iki.minesweeper.service;

import org.iki.minesweeper.model.Game;
import org.iki.minesweeper.persistence.GamePersistenceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MinesweeperGameService {

    @Autowired
    GamePersistenceController gamePersistenceController;

    public Game initGame(Integer columns, Integer rows, Integer bombs) {

        Game newGame = new Game(columns,rows,bombs);

        gamePersistenceController.save(newGame);

        return newGame;
    }

    public Game getGame(String id) {
        return gamePersistenceController.getGame(id);
    }

    public String getGamePrint(String id) {
        Game game = gamePersistenceController.getGame(id);
        return game == null ? null : game.printGrid();
    }
}
