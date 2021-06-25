package org.iki.minesweeper.service;

import lombok.SneakyThrows;
import org.iki.minesweeper.model.CellDisplay;
import org.iki.minesweeper.model.Game;
import org.iki.minesweeper.model.ResponseWrapper;
import org.iki.minesweeper.persistence.GamePersistenceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.ByteArrayInputStream;

@Component
public class MinesweeperUserService {

    @Autowired
    GamePersistenceController gamePersistenceController;

    @SneakyThrows
    private static String md5(String value){
        return DigestUtils.md5DigestAsHex(new ByteArrayInputStream(value.getBytes()));
    }

    public ResponseWrapper createUser(String username, String password) throws Exception {
        if(username == null || username.equals("") || password == null || password.equals(""))
            throw new Exception("User and Password must be set");
        gamePersistenceController.createUser(username, md5(password));
        return ResponseWrapper.of(null, null);
    }

    public void authenticate(String username, String password) throws Exception {
        if(username == null || username.equals("") || password == null || password.equals(""))
            throw new Exception("User and Password must be set");

        gamePersistenceController.authenticate(username, md5(password));
    }


}
