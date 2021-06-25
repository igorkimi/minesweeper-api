package org.iki.minesweeper.persistence;

import org.dizitart.no2.*;
import org.dizitart.no2.filters.Filters;
import org.iki.minesweeper.model.Game;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class GamePersistenceController {

    private Nitrite db = Nitrite.builder().compressed().openOrCreate();

    public void saveGame(Game newGame, String username) throws Exception {
        NitriteCollection collection = this.db.getCollection("game");
        //Tries to update document, if not available, create it.

        Cursor cursor = collection.find(Filters.eq("id", newGame.getGameId()));
        if(cursor.size() == 0){
            Document game = Document.createDocument("id", newGame.getGameId()).put("game", newGame).put("owner", username);
            collection.insert(game);

        }else {
            if(!cursor.firstOrDefault().get("owner", String.class).equals(username))
                throw new Exception("Authentication Failed");

            collection.update(
                Filters.eq("id", newGame.getGameId()),Document.createDocument("game", newGame)
            );
        }
    }

    public Game getGame(String id, String username) throws Exception {
        NitriteCollection collection = this.db.getCollection("game");

        Cursor cursor = collection.find(Filters.eq("id", id));
        if(!cursor.firstOrDefault().get("owner", String.class).equals(username))
            throw new Exception("Authentication Failed");

        return cursor.firstOrDefault().get("game", Game.class);
    }

    public List<Game> getGames(String username) throws Exception {
        NitriteCollection collection = this.db.getCollection("game");

        Cursor cursor = collection.find(Filters.eq("owner", username));
        ArrayList<Game> games = new ArrayList<>();
        for (Document doc:cursor) {
            games.add(
                doc.get("game", Game.class)
            );
        }

        return games;
    }

    public void createUser(String username, String password) throws Exception {
        NitriteCollection collection = this.db.getCollection("user");

        Cursor users = collection.find(Filters.eq("username", username));
        if(users.size() > 0) throw new Exception("User already exists");

        Document game = Document.createDocument("username", username).put("password", password);
        collection.insert(game);
    }

    public void authenticate(String username, String password) throws Exception {
        NitriteCollection collection = this.db.getCollection("user");
        Cursor cursor = collection.find(
            Filters.and(
                Filters.eq("username", username),
                Filters.eq("password",password)
            )
        );

        if(cursor.size() == 0) throw new Exception("Authentication Failed");
    }
}
