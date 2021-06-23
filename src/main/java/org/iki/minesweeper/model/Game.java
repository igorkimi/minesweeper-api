package org.iki.minesweeper.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Getter
@Setter
public class Game {

    private String gameId;
    private Integer colNumber;
    private Integer rowNumber;
    private Integer bombNumber;
    private Cell[][] gameMatrix;
    private LocalDateTime sessionStartDate;

    public Game(Integer colNumber, Integer rowNumber, Integer bombNumber){
        this.colNumber = colNumber;
        this.rowNumber = rowNumber;
        this.bombNumber = bombNumber;
        this.sessionStartDate = LocalDateTime.now();
        this.gameId = UUID.randomUUID().toString();
        this.gameMatrix = new Cell[rowNumber][colNumber];

        //Initializing Cell Matrix
        for (int i=0;i<rowNumber;i++){
            for (int j=0;j<colNumber;j++){
                this.gameMatrix[i][j] = new Cell();
            }
        }

        //Setting bombs randomly on grid
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

        //Setting bombs count around each cell
        for (int i=0;i<rowNumber;i++){
            for (int j=0;j<colNumber;j++){
                setBombsAroundCells(i,j);
            }
        }

        System.out.println(printGrid());
    }

    private void setBombsAroundCells(int row, int col) {

        int bombsAround = 0;
        for (int i=-1;i<=1;i++){
            for (int j=-1;j<=1;j++){
                if( (i+row >= 0 && i+row < this.rowNumber)
                    && (j+col >= 0 && j+col < this.colNumber)
                    && this.gameMatrix[i+row][j+col].getHasBomb()
                ) bombsAround++;
            }
        }
        this.gameMatrix[row][col].setBombsAround(bombsAround);
    }

    public String printGrid(){
        StringBuilder builder = new StringBuilder();

        for (int i=0;i<rowNumber;i++){
            for (int j=0;j<colNumber;j++){
                if(this.gameMatrix[i][j].getHasBomb()) builder.append(" B ");
                else builder.append(String.format(" %s ",this.gameMatrix[i][j].getBombsAround()));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

}
