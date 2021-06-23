package org.iki.minesweeper.controller;

import org.iki.minesweeper.model.Game;
import org.iki.minesweeper.service.MinesweeperGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MinesweeperController {

    @Autowired
    MinesweeperGameService minesweeperGameService;

    @RequestMapping(path="/minesweeper/game", method = RequestMethod.GET)
    public String getGame() {
        return "game";
    }

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
}
