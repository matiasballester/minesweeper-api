# Minesweeper Game - Backend API

This project exposes a RESTful API for the game Minesweeper [GitHub Pages](https://github.com/matiasballester/minesweeper-api).
 

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Usage](#usage) 
* [Important Notes](#important-notes)
* [Features implemented](#features-implemented)


## General info 
This project exposes these rest endpoints:

@PostMapping("/createUser")[@RequestBody UserInputRequest]: retrieves the user register request
* userName: user's nickname
* password: user's password

@PostMapping("/authenticateUser")[@RequestBody UserInputRequest]: authenticate an user
* userName: user's nickname
* password: user's password

@GetMapping("/users/{id}"): returns the user by id

@PostMapping("/startGame)[@RequestBody GameBoardInputRequest]: retrieves the game's configuration selected by the user
* userName: user's identification
* rows: amount of rows used to create the board
* columns: amount of columns used to create the board
* mines: amount of mines to be loaded in the board

@PostMapping("/playGame)[@RequestBody GameBoardActionInputRequest]: user can start playing the game by selecting or flagging a cell
* userName: user's identification
* row: row component of the selected cell
* column: column component of the selected cell

@PostMapping("/addFlag)[@RequestBody GameBoardActionInputRequest]: user can flag a cell to think it twice. As a result of it the cell will be marked as flagged
* userName: user's identification
* row: row component of the selected cell
* column: column component of the selected cell

@GetMapping("/games/users/{id}")[@PathVariable Long id]: returns user's games by user id

@GetMapping("/games/{id}")[@PathVariable Long id]: returns a game by id

@GetMapping("/games"): return all the persisted games

Please refer to https://mballester-minesweeper-api.herokuapp.com/ for full api documentation

## Technologies
 * Spring Boot 2.4.2
 * Postgresql
 * Springdoc-openapi
 * Junit
 
## Usage
[Heroku - MinesWeeper API](https://mballester-minesweeper-api.herokuapp.com/)

From here you can test the rest endpoint sending them in json format

## Important Notes

API was created using RestController to handle rest api calls.
Once the request is validated (user's input validation), a service is taking care of calling to the domain objects 
to accomplish the game's logic. Once the game logic is ready it's persisted using a JPA crud repository:
 
- userName: user's identification
- board: json that contains user's actions
- state: game states (ACTICE, VICTORY, LOST)
- startTime: when the game was started
- endTime: when the game is finished (only win or loss)

## Features implemented

- Ability to 'flag' a cell with a question mark or red flag
  - "/addFlag" endpoint allows to the user to 'flag' a cell with a red flag
 
- Detect when game is over
  - "/playGame" endpoint takes care of game's logic, resulting in 3 possible states for a started game: 
    - ACTIVE: user is still playing
    - LOST: game is over, user lost the game
    - VICTORY: game is over, user won the game
    
- Persistence
    - Each user's action is persisted in a postgresql database
    
- Time tracking
    - When the game is started, a new entry is added in the game table to store the start time
    - When the game is ended, the end date is updated for the user's active game session
     
- Ability to start a new game and preserve/resume the old ones
    - A logged user is able to see all his games, and resume the active ones
    - Each game is persisted in a postgresql database 
    
- Ability to select the game parameters: number of rows, columns, and mines
    - "/startGame" endpoint allows to the user to configure the number of rows, columns and mines desired for each new game
    
- Ability to support multiple users/accounts
    - "/createUser" endpoint handle the user registration
    - "/authenticateUser" endpoint handle the user authentication
    - "/startGame" and "/playGame" will use the user logged id
    



