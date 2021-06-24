package org.iki.minesweeper.controller;

import org.iki.minesweeper.model.Cell;
import org.iki.minesweeper.model.CellDisplay;
import org.iki.minesweeper.model.Game;
import org.iki.minesweeper.service.MinesweeperGameService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Game getGameById(@PathVariable("gameId") String id) {
        return minesweeperGameService.getGame(id);
    }

    @RequestMapping(path="/minesweeper/game/{gameId}/print", method = RequestMethod.GET)
    public String getGamePrintById(@PathVariable("gameId") String id) {
        return minesweeperGameService.getGamePrint(id);
    }

    @RequestMapping(path="/minesweeper/game/{gameId}/{column}/{row}", method = RequestMethod.GET)
    public Object getGameCellById(@PathVariable("gameId") String id,@PathVariable("column") Integer column,@PathVariable("row") Integer row) {
        try {
            return minesweeperGameService.getGameCell(id, column, row);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping(path="/minesweeper/game/{gameId}/{column}/{row}/flag", method = RequestMethod.POST)
    public Object setGameCellFlagById(@PathVariable("gameId") String id,@PathVariable("column") Integer column,@PathVariable("row") Integer row, @RequestHeader("flag") CellDisplay flag) {
        try {
            minesweeperGameService.setGameCellFlag(id, column, row, flag);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping(path="/minesweeper/game/{gameId}/{column}/{row}/open", method = RequestMethod.POST)
    public Object openGameCellById(@PathVariable("gameId") String id,@PathVariable("column") Integer column,@PathVariable("row") Integer row) {
        try {
            minesweeperGameService.setGameCellOpen(id, column, row);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
