package org.iki.minesweeper.controller;

import org.iki.minesweeper.model.Cell;
import org.iki.minesweeper.model.CellDisplay;
import org.iki.minesweeper.model.Game;
import org.iki.minesweeper.model.ResponseWrapper;
import org.iki.minesweeper.service.MinesweeperGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MinesweeperController {

    @Autowired
    MinesweeperGameService minesweeperGameService;

    @RequestMapping(path="/minesweeper/game", method = RequestMethod.POST)
    public Game postGame(@RequestHeader("columns") Integer columns,
                         @RequestHeader("rows") Integer rows,
                         @RequestHeader("bombs") Integer bombs) {

        return minesweeperGameService.initGame(columns,rows,bombs);
    }

    @RequestMapping(path="/minesweeper/game/{gameId}", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper> getGameById(@PathVariable("gameId") String id) {
        return ResponseEntity.ok(
            minesweeperGameService.getGame(id)
        );
    }

    @RequestMapping(path="/minesweeper/game/{gameId}/print", method = RequestMethod.GET)
    public String getGamePrintById(@PathVariable("gameId") String id) {
        return minesweeperGameService.getGamePrint(id);
    }

    @RequestMapping(path="/minesweeper/game/{gameId}/{column}/{row}/flag", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper> setGameCellFlagById(@PathVariable("gameId") String id,@PathVariable("column") Integer column,@PathVariable("row") Integer row, @RequestHeader("flag") CellDisplay flag) {
        try {
            return ResponseEntity.ok(
                minesweeperGameService.setGameCellFlag(id, column, row, flag)
            );
        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                ResponseWrapper.of(null, e.getMessage())
            );
        }
    }

    @RequestMapping(path="/minesweeper/game/{gameId}/{column}/{row}/open", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper> openGameCellById(@PathVariable("gameId") String id,@PathVariable("column") Integer column,@PathVariable("row") Integer row) {
        try {
            return ResponseEntity.ok(
                minesweeperGameService.setGameCellOpen(id, column, row)
            );
        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                ResponseWrapper.of(null, e.getMessage())
            );
        }
    }
}
