package org.iki.minesweeper.controller;

import org.iki.minesweeper.config.MinesweeperApiException;
import org.iki.minesweeper.model.*;
import org.iki.minesweeper.service.MinesweeperGameService;
import org.iki.minesweeper.service.MinesweeperUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MinesweeperController {

    @Autowired
    MinesweeperGameService minesweeperGameService;

    @Autowired
    MinesweeperUserService minesweeperUserService;

    @RequestMapping(path="/minesweeper/game", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper> createGame(@RequestHeader(value="columns",required=false) Integer columns,
                         @RequestHeader(value="rows",required=false) Integer rows,
                         @RequestHeader(value="bombs",required=false) Integer bombs,
                         @RequestHeader("username") String username,
                         @RequestHeader("password") String password) {

        try {
            minesweeperUserService.authenticate(username,password);
            return ResponseEntity.ok(
                    minesweeperGameService.initGame(columns,rows,bombs,username)
            );
        } catch (MinesweeperApiException e) {
            return ResponseEntity.status(400).body(
                    ResponseWrapper.of(null, e.getMessage())
            );
        }
    }

    @RequestMapping(path="/minesweeper/game/{gameId}", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper> getGameById(@PathVariable("gameId") String id,
                        @RequestHeader("username") String username,
                        @RequestHeader("password") String password) {
        try {
            minesweeperUserService.authenticate(username,password);
            return ResponseEntity.ok(
                    minesweeperGameService.getGame(id, username)
            );
        } catch (MinesweeperApiException e) {
            return ResponseEntity.status(400).body(
                    ResponseWrapper.of(null, e.getMessage())
            );
        }
    }

    @RequestMapping(path="/minesweeper/game", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper> getGames(@RequestHeader("username") String username, @RequestHeader("password") String password) {
        try {
            minesweeperUserService.authenticate(username,password);
            return ResponseEntity.ok(
                    minesweeperGameService.getGames(username)
            );
        } catch (MinesweeperApiException e) {
            return ResponseEntity.status(400).body(
                    ResponseWrapper.of(null, e.getMessage())
            );
        }
    }

    @RequestMapping(path="/minesweeper/game/{gameId}/{column}/{row}/flag", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper> setGameCellFlagById(@PathVariable("gameId") String id,
                        @PathVariable("column") Integer column,
                        @PathVariable("row") Integer row,
                        @RequestHeader("flag") CellDisplay flag,
                        @RequestHeader("username") String username,
                        @RequestHeader("password") String password) {
        try {
            minesweeperUserService.authenticate(username,password);
            return ResponseEntity.ok(
                minesweeperGameService.setGameCellFlag(id, column, row, flag, username)
            );
        } catch (MinesweeperApiException e) {
            return ResponseEntity.status(400).body(
                ResponseWrapper.of(null, e.getMessage())
            );
        }
    }

    @RequestMapping(path="/minesweeper/game/{gameId}/{column}/{row}/open", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper> openGameCellById(@PathVariable("gameId") String id,
                        @PathVariable("column") Integer column,
                        @PathVariable("row") Integer row,
                        @RequestHeader("username") String username,
                        @RequestHeader("password") String password) {
        try {
            minesweeperUserService.authenticate(username,password);
            return ResponseEntity.ok(
                minesweeperGameService.setGameCellOpen(id, column, row, username)
            );
        } catch (MinesweeperApiException e) {
            return ResponseEntity.status(400).body(
                ResponseWrapper.of(null, e.getMessage())
            );
        }
    }

    @RequestMapping(path="/minesweeper/game/{gameId}/pause", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper> pauseGameById(@PathVariable("gameId") String id,
                                                            @RequestHeader("username") String username,
                                                            @RequestHeader("password") String password) {
        try {
            minesweeperUserService.authenticate(username,password);
            return ResponseEntity.ok(
                    minesweeperGameService.pauseGame(id, username)
            );
        } catch (MinesweeperApiException e) {
            return ResponseEntity.status(400).body(
                    ResponseWrapper.of(null, e.getMessage())
            );
        }
    }
}
