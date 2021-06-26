package org.iki.minesweeper.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Random;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.NANOS;
import static java.time.temporal.ChronoUnit.SECONDS;

@Getter
@Setter
public class Game implements Serializable {

    private String gameId;
    private GameStatus gameStatus;
    private LocalDateTime sessionStartDate;
    private LocalDateTime lastMoveDate;
    private Long timePlayed;
    private Integer colNumber;
    private Integer rowNumber;
    private Integer bombNumber;
    private Cell[][] gameMatrix;

    public Game(Integer colNumber, Integer rowNumber, Integer bombNumber){
        this.gameId = UUID.randomUUID().toString();
        this.gameStatus = GameStatus.ONGOING;
        this.colNumber = colNumber;
        this.rowNumber = rowNumber;
        this.bombNumber = bombNumber;
        this.sessionStartDate = LocalDateTime.now();
        this.lastMoveDate = LocalDateTime.now();
        this.timePlayed = Long.valueOf(0);
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
    }

    @JsonProperty("bombsLeft")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer getBombsLeft(){
        Integer bombs = this.bombNumber;

        for (int i=0;i<rowNumber;i++){
            for (int j=0;j<colNumber;j++){
                if(this.gameMatrix[i][j].getHiddenDisplay() != CellDisplay.NONE && this.gameMatrix[i][j].getIsHidden()) bombs--;
            }
        }

        return bombs;
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

    public void setCellFlag(Integer row, Integer col, CellDisplay flag) throws MinesweeperApiException {
        if(row > this.rowNumber || row < 1 || col < 1 || col > this.colNumber) throw new MinesweeperApiException("Invalid Cell");
        this.updateTimePlayed();
        this.gameMatrix[row-1][col-1].setHiddenDisplay(flag);
    }


    public String printGrid(){
        StringBuilder builder = new StringBuilder();
        builder.append("\n");

        for (int i=0;i<rowNumber;i++){
            for (int j=0;j<colNumber;j++){

                if(this.gameMatrix[i][j].getIsHidden()){
                    if(this.gameMatrix[i][j].getHiddenDisplay() == CellDisplay.QUESTION_MARK) builder.append(" ? ");
                    if(this.gameMatrix[i][j].getHiddenDisplay() == CellDisplay.RED_FLAG) builder.append(" R ");
                    else builder.append(" _ ");
                }else{
                    if(this.gameMatrix[i][j].getHasBomb()) builder.append(" B ");
                    else builder.append(String.format(" %s ",this.gameMatrix[i][j].getBombsAround()));
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public void setCellOpen(Integer row, Integer col) throws MinesweeperApiException {
        if(row > this.rowNumber || row < 1 || col < 1 || col > this.colNumber) throw new MinesweeperApiException("Invalid Cell");

        this.updateTimePlayed();
        this.gameMatrix[row-1][col-1].setIsHidden(false);

        if(this.gameMatrix[row-1][col-1].getBombsAround() == 0) setCellOpenAround(row-1, col-1);

        if(this.gameMatrix[row-1][col-1].getHasBomb()) this.loseGame();
        else this.validateWinCondition();
    }

    private void updateTimePlayed() {
        if(this.lastMoveDate != null) {
            Duration duration = Duration.between(lastMoveDate, LocalDateTime.now());
            this.timePlayed += (duration.get(SECONDS)*1000000000 + duration.get(NANOS));
        }

        this.lastMoveDate = LocalDateTime.now();
    }

    private void validateWinCondition() {
        int hiddenCellsCount = 0;
        for (int i=0;i<rowNumber;i++){
            for (int j=0;j<colNumber;j++){
                if (this.gameMatrix[i][j].getIsHidden()) hiddenCellsCount++;
            }
        }

        if(hiddenCellsCount == this.bombNumber){
            for (int i=0;i<rowNumber;i++){
                for (int j=0;j<colNumber;j++){
                    this.gameMatrix[i][j].setIsHidden(false);
                }
            }
            this.setGameStatus(GameStatus.WON);
            this.lastMoveDate = null;
        }
    }

    private void loseGame() {
        this.gameStatus = GameStatus.LOST;
        this.lastMoveDate = null;

        //opening all cells
        for (int i=0;i<rowNumber;i++){
            for (int j=0;j<colNumber;j++){
                this.gameMatrix[i][j].setIsHidden(false);
            }
        }
    }

    private void setCellOpenAround(int row, int col) {

        int bombsAround = 0;
        for (int i=-1;i<=1;i++){
            for (int j=-1;j<=1;j++){
                if( (i+row >= 0 && i+row < this.rowNumber)
                        && (j+col >= 0 && j+col < this.colNumber)
                        && this.gameMatrix[i+row][j+col].getIsHidden()
                ){
                    this.gameMatrix[i+row][j+col].setIsHidden(false);
                    if(this.gameMatrix[i+row][j+col].getBombsAround() == 0) setCellOpenAround(i+row, j+col);
                }
            }
        }
        this.gameMatrix[row][col].setBombsAround(bombsAround);
    }

    public void pause() {
        this.updateTimePlayed();
        this.lastMoveDate = null;
    }
}
