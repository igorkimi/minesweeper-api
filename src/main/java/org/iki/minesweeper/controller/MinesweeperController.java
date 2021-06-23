package org.iki.minesweeper.controller;

import org.iki.minesweeper.model.Game;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MinesweeperController {

    @RequestMapping(value = "/minesweeper/game", method = RequestMethod.GET)
    public String getGame() {
        return "game";
    }

    @RequestMapping(value = "/minesweeper/game", method = RequestMethod.POST)
    public Game postGame(@RequestHeader("columns") Integer columns,
                         @RequestHeader("rows") Integer rows,
                         @RequestHeader("bombs") Integer bombs) {
        return new Game(columns,rows,bombs);
    }


}
