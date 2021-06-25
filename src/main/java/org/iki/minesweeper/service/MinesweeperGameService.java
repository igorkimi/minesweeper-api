package org.iki.minesweeper.service;

import org.iki.minesweeper.model.Cell;
import org.iki.minesweeper.model.CellDisplay;
import org.iki.minesweeper.model.Game;
import org.iki.minesweeper.model.ResponseWrapper;
import org.iki.minesweeper.persistence.GamePersistenceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MinesweeperGameService {

    @Autowired
    GamePersistenceController gamePersistenceController;

    public Game initGame(Integer columns, Integer rows, Integer bombs) {

        Game newGame = new Game(columns,rows,bombs);

        gamePersistenceController.saveGame(newGame);

        return newGame;
    }

    public ResponseWrapper getGame(String id) {
        return ResponseWrapper.of(gamePersistenceController.getGame(id), null);
    }

    public String getGamePrint(String id) {
        Game game = gamePersistenceController.getGame(id);
        return game == null ? null : game.printGrid();
    }

    public ResponseWrapper setGameCellFlag(String id, Integer column, Integer row, CellDisplay flag) throws Exception {
        Game game = gamePersistenceController.getGame(id);
        game.setCellFlag(row, column, flag);
        gamePersistenceController.saveGame(game);
        return ResponseWrapper.of(game, null);
    }

    public ResponseWrapper setGameCellOpen(String id, Integer column, Integer row) throws Exception {
        Game game = gamePersistenceController.getGame(id);
        game.setCellOpen(row, column);
        gamePersistenceController.saveGame(game);
        return ResponseWrapper.of(game, null);
    }
}
