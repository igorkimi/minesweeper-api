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

    public ResponseWrapper initGame(Integer columns, Integer rows, Integer bombs, String username) throws Exception {
        Game newGame = new Game(columns,rows,bombs);
        gamePersistenceController.saveGame(newGame, username);

        return ResponseWrapper.of(newGame, null);
    }

    public ResponseWrapper getGame(String id, String username) throws Exception {
        return ResponseWrapper.of(gamePersistenceController.getGame(id,username), null);
    }

    public ResponseWrapper getGames(String username) throws Exception {
        return ResponseWrapper.of(gamePersistenceController.getGames(username));
    }

    public ResponseWrapper setGameCellFlag(String id, Integer column, Integer row, CellDisplay flag, String username) throws Exception {
        Game game = gamePersistenceController.getGame(id, username);
        game.setCellFlag(row, column, flag);
        gamePersistenceController.saveGame(game, username);
        return ResponseWrapper.of(game, null);
    }

    public ResponseWrapper setGameCellOpen(String id, Integer column, Integer row, String username) throws Exception {
        Game game = gamePersistenceController.getGame(id, username);
        game.setCellOpen(row, column);
        gamePersistenceController.saveGame(game, username);
        return ResponseWrapper.of(game, null);
    }
}
