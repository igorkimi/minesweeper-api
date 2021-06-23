package org.iki.minesweeper.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cell {
    private Boolean isHidden;
    private CellDisplay display;
    private Boolean hasBomb;

    Cell(){
        this.hasBomb = false;
        this.isHidden = true;
        this.display = CellDisplay.NONE;
    }
}
