package org.iki.minesweeper.service;

import org.iki.minesweeper.config.MinesweeperApiException;
import org.iki.minesweeper.model.*;
import org.iki.minesweeper.persistence.GamePersistenceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MinesweeperGameService {

    @Autowired
    GamePersistenceController gamePersistenceController;

    public ResponseWrapper initGame(Integer columns, Integer rows, Integer bombs, String username) throws MinesweeperApiException {

        if(columns == null || columns <= 0 || rows == null || rows <= 0 || bombs == null || bombs <= 0)
            throw new MinesweeperApiException("Number of rows, columns and bombs must be higher than zero");

        if(columns * rows <= bombs)
            throw new MinesweeperApiException("Number of bombs should be less than number of squares");

        Game newGame = new Game(columns,rows,bombs);
        gamePersistenceController.saveGame(newGame, username);

        return ResponseWrapper.of(newGame, null);
    }

    public ResponseWrapper getGame(String id, String username) throws MinesweeperApiException {
        return ResponseWrapper.of(gamePersistenceController.getGame(id,username), null);
    }

    public ResponseWrapper getGames(String username) throws MinesweeperApiException {
        return ResponseWrapper.of(gamePersistenceController.getGames(username));
    }

    public ResponseWrapper setGameCellFlag(String id, Integer column, Integer row, CellDisplay flag, String username) throws MinesweeperApiException {
        Game game = gamePersistenceController.getGame(id, username);
        game.setCellFlag(row, column, flag);
        gamePersistenceController.saveGame(game, username);
        return ResponseWrapper.of(game, null);
    }

    public ResponseWrapper setGameCellOpen(String id, Integer column, Integer row, String username) throws MinesweeperApiException {
        Game game = gamePersistenceController.getGame(id, username);
        game.setCellOpen(row, column);
        gamePersistenceController.saveGame(game, username);
        return ResponseWrapper.of(game, null);
    }

    public ResponseWrapper pauseGame(String id, String username) throws MinesweeperApiException {
        Game game = gamePersistenceController.getGame(id, username);
        game.pause();
        gamePersistenceController.saveGame(game, username);
        return ResponseWrapper.of(game, null);
    }
}
