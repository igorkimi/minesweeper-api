# Mineswepeer API
This project aims to implement a simple Minesweeper game web implementation, including a back-end rest api and a front-end client.

###Client
The client application is provided as a static single html file, which uses bootstrap for the user interface style and javascript for managing user inputs and connecting with the back-end api.

###Back-end
The backend is a Spring Boot Java application, which uses the Nitrite no-sql in-memory database for data persistence. 
Nitrite was selected due to the small size of the project, as well as to have quick responses from the persistence layer.
Due to the document-oriented nature of the database, it could be replaced by others such as MongoDB.

###Rest API 
Two controllers were implemented on this api:
- user-controller: responsible for registering an user and authenticating it;
- minesweeper-controller: responsible for managing the user games. 
Provides endpoints for actions such as starting a new game, setting a flag, opening a cell, pausing a game and listing all games from a user.

Path | Method | Description
---- | -----  | -----
/minesweeper/game | GET | Returns a list of all games from an user.
/minesweeper/game | POST | Creates a new game for an user, receiving the number of rows, columns and bombs of this game. Returns the data of the game created.
/minesweeper/game/{gameId} | GET | Returns information from a single game.
/minesweeper/game/{gameId}/{column}/{row}/flag | POST | Sets a flag on a cell for a specific game a return the information of that game after the move.
/minesweeper/game/{gameId}/{column}/{row}/open | POST | Opens a cell for a specific game a return the information of that game after the move.
/minesweeper/game/{gameId}/pause | POST | Pauses a game.
/minesweeper/user | POST | Creates a new user.
/minesweeper/user | GET | Confirms credentials of an existing user.

For all APIs, and 200 code is returned of successfull operations, otherwise a 400 code will be returned added by a custom error message.

For all /minesweeper/game apis, user password and user name must be sent as headers, for authentication purposes.

API Objects:
* game: overall minesweeper game object;
    *  bombNumber: number of bombs set on the game;
    *  colNumber: number of columns set on the game;
    *  rowNumber: number of rows set on the game;
    *  bombsLeft: number of bombs minus number of cells marked as RED_FLAG or QUESTION_MARK;
    *  gameId: UUID of the game;
    *  gameStatus: the status of the game (WON, LOST or ONGOING);
    *  sessionStartDate: date and time when the game started (ISO format);
    *  lastMoveDate: date and time when the moved was made in this game, will be null for paused games;
    *  timePlayed: the sum of all periods between moves since the game started, in nanoseconds; The game time can be calculated on the client by adding this number to the interval between the last move and current datetime.
    *  gameMatrix: it's a matrix where each item is a cell from the game board.
        *  isHidden: flags if the cell has been exposed by the user or not.
        *  hiddenDisplay: returns any flag set by the user on that cell. Possible values are NONE, RED_FLAG and QUESTION_MARK.
        *  hasBomb: returns true if that cell has a bomb. This value is only returned if the cell is not hidden.
        *  bombsAround: returns the number of bombs in adjacent cells of a specific cell. This value is only returned if the cell is not hidden. 

The APIs can be found in detail on the application Swagger UI, accessible at /swagger-ui.html