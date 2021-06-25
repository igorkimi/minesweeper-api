package org.iki.minesweeper.persistence;

import org.dizitart.no2.*;
import org.dizitart.no2.filters.Filters;
import org.iki.minesweeper.model.Game;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class GamePersistenceController {

    private Nitrite db = Nitrite.builder().compressed().openOrCreate();

    public void saveGame(Game newGame) {
        NitriteCollection collection = this.db.getCollection("game");

        //Tries to update document, if not available, create it.
        if(collection.update(
            Filters.eq("id", newGame.getGameId()),Document.createDocument("game", newGame)
        ).getAffectedCount() == 0){
            Document game = Document.createDocument("id", newGame.getGameId()).put("game", newGame);
            collection.insert(game);
        }
    }

    public Game getGame(String id) {
        NitriteCollection collection = this.db.getCollection("game");

        Cursor cursor = collection.find(Filters.eq("id", id));

        return cursor.firstOrDefault().get("game", Game.class);
    }
}
