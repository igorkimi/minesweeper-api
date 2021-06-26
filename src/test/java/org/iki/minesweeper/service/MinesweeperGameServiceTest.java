package org.iki.minesweeper.service;

import okhttp3.Response;
import org.iki.minesweeper.model.CellDisplay;
import org.iki.minesweeper.model.Game;
import org.iki.minesweeper.model.MinesweeperApiException;
import org.iki.minesweeper.model.ResponseWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MinesweeperGameServiceTest {

    @Autowired
    private MinesweeperGameService minesweeperGameService;

    @Test
    @DisplayName("When creating a Minesweeper game, columns, rows and bombs must be set")
    public void minesweeperGameServiceInitEmptyParams(){

        MinesweeperApiException ex = Assertions.assertThrows(
            MinesweeperApiException.class,
            () -> minesweeperGameService.initGame(null, null, null, "user")
        );
        Assertions.assertEquals("Number of rows, columns and bombs must be higher than zero", ex.getMessage());
    }

    @Test
    @DisplayName("When creating a Minesweeper game, columns, rows and bombs must be higher than zero")
    public void minesweeperGameServiceInitInvalidParams(){

        MinesweeperApiException ex = Assertions.assertThrows(
            MinesweeperApiException.class,
            () -> minesweeperGameService.initGame(-1, 0, -1, "user")
        );
        Assertions.assertEquals("Number of rows, columns and bombs must be higher than zero", ex.getMessage());
    }

    @Test
    @DisplayName("When creating a Minesweeper game, the created game must match the requested parameters")
    public void minesweeperGameServiceInitGameParameterValidation(){

        Game game = Assertions.assertDoesNotThrow(
             () -> minesweeperGameService.initGame(10, 15, 20, "user")
        ).getGame();

        Assertions.assertEquals(15, game.getGameMatrix().length);
        Assertions.assertEquals(15, game.getRowNumber());
        Assertions.assertEquals(10, game.getColNumber());
        Assertions.assertEquals(10, game.getGameMatrix()[0].length);
        Assertions.assertEquals(20, game.getBombNumber());
    }

    @Test
    @DisplayName("When creating a Minesweeper game, the created game must have all cells hidden")
    public void minesweeperGameServiceInitBombsLeft(){
        Game game = Assertions.assertDoesNotThrow(
            () -> minesweeperGameService.initGame(10, 15, 20, "user")
        ).getGame();

        Assertions.assertEquals(20, game.getBombsLeft());
    }

    @Test
    @DisplayName("When getting a Minesweeper game, user should be able to have a game he created previously")
    public void minesweeperGameServiceGetGame(){
        Game returnedGame = Assertions.assertDoesNotThrow(
            () -> {
                Game createdGame = minesweeperGameService.initGame(10, 15, 20, "user").getGame();
                return minesweeperGameService.getGame(createdGame.getGameId(), "user");
            }
        ).getGame();

        Assertions.assertEquals(20, returnedGame.getBombNumber());
        Assertions.assertEquals(15, returnedGame.getRowNumber());
        Assertions.assertEquals(10, returnedGame.getColNumber());
    }

    @Test
    @DisplayName("When getting a Minesweeper game, user should not be able to get other users games")
    public void minesweeperGameServiceGetGameFromOtherUser(){

        MinesweeperApiException ex = Assertions.assertThrows(
                MinesweeperApiException.class,
                () -> {
                    Game createdGame = minesweeperGameService.initGame(10, 15, 20, "user").getGame();
                    minesweeperGameService.getGame(createdGame.getGameId(), "otherUser");
                }
        );
        Assertions.assertEquals("Authentication Failed", ex.getMessage());
    }

    @Test
    @DisplayName("When getting a Minesweeper game list, user should be able to have all games he created previously")
    public void minesweeperGameServiceGetGames(){
        List<Game> list = Assertions.assertDoesNotThrow(
                () -> {
                    minesweeperGameService.initGame(10, 15, 20, "user").getGame();
                    minesweeperGameService.initGame(10, 15, 20, "user").getGame();
                    minesweeperGameService.initGame(10, 15, 20, "user").getGame();
                    minesweeperGameService.initGame(10, 15, 20, "user").getGame();
                    minesweeperGameService.initGame(10, 15, 20, "user").getGame();

                    return minesweeperGameService.getGames("user").getGames();
                }
        );

        Assertions.assertEquals(5, list.size());
    }

    @Test
    @DisplayName("When playing a Minesweeper game, user should be able to set a flag on a hidden cell")
    public void minesweeperGameServiceSetCellFlag(){

        Game returnedGame = Assertions.assertDoesNotThrow(
            () -> {
                Game createdGame = minesweeperGameService.initGame(10, 15, 20, "user").getGame();
                return minesweeperGameService.setGameCellFlag(createdGame.getGameId(), 1, 1, CellDisplay.RED_FLAG, "user");
            }
        ).getGame();
        Assertions.assertEquals(CellDisplay.RED_FLAG, returnedGame.getGameMatrix()[0][0].getHiddenDisplay());
    }

    @Test
    @DisplayName("When playing a Minesweeper game, user should be able to open a cell")
    public void minesweeperGameServiceOpenCell(){

        Game returnedGame = Assertions.assertDoesNotThrow(
            () -> {
                Game createdGame = minesweeperGameService.initGame(10, 15, 20, "user").getGame();
                return minesweeperGameService.setGameCellOpen(createdGame.getGameId(), 1, 1, "user");
            }
        ).getGame();
        Assertions.assertEquals(false, returnedGame.getGameMatrix()[0][0].getIsHidden());
    }

    @Test
    @DisplayName("When playing a Minesweeper game, user should be able to pause the game, and its last move date should be cleared")
    public void minesweeperGameServicePause(){

        Game returnedGame = Assertions.assertDoesNotThrow(
            () -> {
                Game createdGame = minesweeperGameService.initGame(10, 15, 20, "user").getGame();
                minesweeperGameService.setGameCellOpen(createdGame.getGameId(), 1, 1, "user");
                return minesweeperGameService.pauseGame(createdGame.getGameId(), "user");
            }
        ).getGame();
        Assertions.assertEquals(null, returnedGame.getLastMoveDate());
    }
}
