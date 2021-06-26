package org.iki.minesweeper.controller;

import org.iki.minesweeper.model.ResponseWrapper;
import org.iki.minesweeper.service.MinesweeperUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Map;

@RestController
public class StatusController {

    @RequestMapping(path="/status", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> status() {
        return ResponseEntity.ok(
            Map.of("status", "up", "datetime", ZonedDateTime.now())
        );
    }
}
