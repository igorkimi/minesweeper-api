package org.iki.minesweeper.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cell {
    private Boolean isHidden;
    private CellDisplay display;
    private Boolean hasBomb;
    private Integer bombsAround;

    Cell(){
        this.hasBomb = false;
        this.isHidden = true;
        this.display = CellDisplay.NONE;
    }
}
