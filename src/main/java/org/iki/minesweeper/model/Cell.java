package org.iki.minesweeper.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cell {
    private Boolean isHidden;
    private CellDisplay hiddenDisplay;
    private Boolean hasBomb;
    private Integer bombsAround;

    Cell(){
        this.hasBomb = false;
        this.isHidden = true;
        this.hiddenDisplay = CellDisplay.NONE;
    }

    @JsonProperty("hasBomb")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean jsonHasbomb() {
        return isHidden ? null : hasBomb;
    }

    @JsonProperty("bombsAround")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer jsonBombsAround() {
        return isHidden ? null : bombsAround;
    }
}
