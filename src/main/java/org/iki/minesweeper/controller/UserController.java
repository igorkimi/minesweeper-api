package org.iki.minesweeper.controller;

import org.iki.minesweeper.model.ResponseWrapper;
import org.iki.minesweeper.service.MinesweeperGameService;
import org.iki.minesweeper.service.MinesweeperUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
public class UserController {

    @Autowired
    MinesweeperUserService minesweeperUserService;

    @RequestMapping(path="/minesweeper/user", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper> createUser(@RequestHeader("username") String username, @RequestHeader("password") String password) {
        try {
            return ResponseEntity.ok(
                minesweeperUserService.createUser(username, password)
            );
        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                ResponseWrapper.of(null, e.getMessage())
            );
        }
    }

    @RequestMapping(path="/minesweeper/user", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper> authenticate(@RequestHeader("username") String username, @RequestHeader("password") String password) {
        try {
            minesweeperUserService.authenticate(username, password);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(
                    ResponseWrapper.of(null, e.getMessage())
            );
        }
    }
}
