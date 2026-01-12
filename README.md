# Battleship with Java

This Java program allows two players to set up and play a classic Battleship game. This is the last project of the beginner track from Hyperskill. Players place their ships on a 10x10 grid and take turns firing shots until one player's ships are all sunk.

## Features  
Two-player setup with ship placement validation  
Supports standard ships: Aircraft Carrier, Battleship, Submarine, Cruiser, Destroyer  
Validates ship placement (length, adjacency, and position)  
Turn-based gameplay with input validation  
Displays fog of war for opponent's field during gameplay  
Detects hits, misses, sinking ships, and game end  

## How to Use
Run the program.  
Player 1 places ships by entering start and end coordinates for each ship.  
Pass control to Player 2 by pressing Enter.  
Player 2 places ships similarly.  
Players alternate turns, firing shots by entering coordinates.  
The game ends when one player sinks all opponent ships.   

## Code Structure  
Ship class: Tracks ship name, size, coordinates, and hits.  
Player class: Manages player name, game field, and ships.  
Main class: Handles game flow including setup, placement validation, turn management, and input handling.    

## Validation & Rules  
Ships must be placed either horizontally or vertically.  
Ship length must match the predefined ship sizes.  
Ships cannot be placed adjacent to each other, including diagonally.  
Shots are validated for correct format and bounds.  
Repeated shots on the same cell are allowed but will be marked as hits or misses accordingly.  
The game continues until all ships of a player are sunk.  
