package org.iki.minesweeper.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseWrapper {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Game game;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorMessage;

    public static ResponseWrapper of(Game game, String errorMessage){
        return new ResponseWrapper(game, errorMessage);
    }
}
