package org.iki.minesweeper.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Getter
@Setter
public class Game {

    private Integer colNumber;
    private Integer rowNumber;
    private Integer bombNumber;
    private Cell[][] gameMatrix;
    private String id;
    private LocalDateTime startDate;

    public Game(Integer colNumber, Integer rowNumber, Integer bombNumber){
        this.colNumber = colNumber;
        this.rowNumber = rowNumber;
        this.bombNumber = bombNumber;
        this.startDate = LocalDateTime.now();
        this.id = UUID.randomUUID().toString();
        this.gameMatrix = new Cell[rowNumber][colNumber];

        for (int i=0;i<rowNumber;i++){
            for (int j=0;j<colNumber;j++){
                this.gameMatrix[i][j] = new Cell();
            }
        }

        Random rand = new Random();
        for(int i=bombNumber;i>0;i--){
            int col = rand.nextInt(this.colNumber);
            int row = rand.nextInt(this.rowNumber);

            if(!this.gameMatrix[row][col].getHasBomb()){
                this.gameMatrix[row][col].setHasBomb(true);
            }else{
                i++;
            }
        }

    }

}
