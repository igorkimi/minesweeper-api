package org.iki.minesweeper.persistence;

import org.iki.minesweeper.model.Game;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class GamePersistenceController {

    private HashMap<String, Game> gameDictionary = new HashMap<>();

    public void save(Game newGame) {
        this.gameDictionary.put(newGame.getGameId(), newGame);
    }

    public Game getGame(String id) {
        return this.gameDictionary.get(id);
    }
}
