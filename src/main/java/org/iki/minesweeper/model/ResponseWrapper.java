package org.iki.minesweeper.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ResponseWrapper {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Game game;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorMessage;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Game> games;

    public static ResponseWrapper of(Game game, String errorMessage){
        return new ResponseWrapper(game, errorMessage, null);
    }

    public static ResponseWrapper of(List<Game> games){
        return new ResponseWrapper(null, null, games);
    }
}
