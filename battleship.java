package battleship;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    static class Ship {             // Ship 
        String name;
        int length;
        ArrayList<String> coordinates;
        int hits;
        
        Ship(String name, int length) {
            this.name = name;
            this.length = length;
            this.coordinates = new ArrayList<>();
            this.hits = 0;
        }
        
        boolean isSunk() {
            return hits == length;
        }
        
        void addHit() {           // Counter for hits
            hits++;
        }
        
        boolean hasCoordinate(int row, int col) {          // Check if coordinate belongs to this ship
            String coord = "" + (char)('A' + row) + (col + 1);
            return coordinates.contains(coord);
        }
    }
    
    static class Player {               // Player
        String name;
        char[][] field;
        ArrayList<Ship> ships;
        
        Player(String name) {
            this.name = name;
            this.field = new char[10][10];
            this.ships = new ArrayList<>();
            
            for (int i = 0; i < 10; i++) {             // Initialize field with fog of war
                for (int j = 0; j < 10; j++) {
                    field[i][j] = '~';
                }
            }
        }
        
        boolean allShipsSunk() {
            for (Ship ship : ships) {
                if (!ship.isSunk()) {
                    return false;
                }
            }
            return true;
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        
        String[] shipNames = {"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
        int[] shipLengths = {5, 4, 3, 3, 2};
        
        System.out.println(player1.name + ", place your ships on the game field");                   // Player 1 places ships
        System.out.println();
        printField(player1.field);
        System.out.println();
        
        for (int i = 0; i < shipNames.length; i++) {
            Ship ship = new Ship(shipNames[i], shipLengths[i]);
            placeShipWithValidation(scanner, player1.field, ship);
            player1.ships.add(ship);
        }
        
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
        System.out.println();
        
        System.out.println(player2.name + ", place your ships to the game field");               // Player 2 places ships
        System.out.println();
        printField(player2.field);
        System.out.println();
        
        for (int i = 0; i < shipNames.length; i++) {
            Ship ship = new Ship(shipNames[i], shipLengths[i]);
            placeShipWithValidation(scanner, player2.field, ship);
            player2.ships.add(ship);
        }
        
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
        System.out.println();
        
        Player currentPlayer = player1;             // Main game loop
        Player opponent = player2;
        
        while (!player1.allShipsSunk() && !player2.allShipsSunk()) {
            printFieldWithFog(opponent.field);                                                      // Display both fields
            System.out.println("---------------------");
            printField(currentPlayer.field);
            System.out.println();
            System.out.println(currentPlayer.name + ", it's your turn:");
            System.out.println();
            
            takeShot(scanner, opponent.field, opponent.ships);
            
            if (opponent.allShipsSunk()) {                                   // Check if game is over
                System.out.println("You sank the last ship. You won. Congratulations!");
                break;
            }
            
            System.out.println("Press Enter and pass the move to another player");
            scanner.nextLine();
            System.out.println();
            
            Player temp = currentPlayer;                             // Switch players
            currentPlayer = opponent;
            opponent = temp;
        }
    }
    
    private static void takeShot(Scanner scanner, char[][] field, ArrayList<Ship> ships) {
        boolean validShot = false;
        
        while (!validShot) {
            String input = scanner.nextLine();
            
            if (!isValidShotFormat(input)) {
                System.out.println();
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                System.out.println();
                continue;
            }
            
            int row = input.charAt(0) - 'A';
            int col = Integer.parseInt(input.substring(1)) - 1;
            
            char target = field[row][col];
            
            if (target == 'O' || target == 'X') {
                field[row][col] = 'X';
                validShot = true;
                
                Ship hitShip = null;
                if (target == 'O') {                                             // Check what's at the target location
                    for (Ship ship : ships) {
                        if (ship.hasCoordinate(row, col)) {                     // Find which ship was hit 
                            ship.addHit();
                            hitShip = ship;                                     // Assigns specific ship to hitShip
                            break;
                        }
                    }
                }
                
                System.out.println();
                
                if (hitShip != null && hitShip.isSunk()) {
                    System.out.println("You sank a ship!");
                } else {
                    System.out.println("You hit a ship!");
                }
            } else {
                field[row][col] = 'M';                                        // Missed a ship
                validShot = true;
                System.out.println();
                System.out.println("You missed!");
            }
        }
    }
    
    private static void printFieldWithFog(char[][] field) {           // Hide ship from opponent
        System.out.print("  ");                                       // Print column numbers
        for (int i = 1; i <= 10; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        
        for (int i = 0; i < 10; i++) {                              // Print rows
            System.out.print((char)('A' + i) + " ");
            for (int j = 0; j < 10; j++) {
                char cell = field[i][j];
                if (cell == 'O') {
                    System.out.print("~ ");
                } else {
                    System.out.print(cell + " ");                    // Show X and M 
                }
            }
            System.out.println();
        }
    }
    
    private static boolean isValidShotFormat(String shot) {               // Validate shot coordinate format
        if (shot.length() < 2) {
            return false;
        }
        
        char rowChar = shot.charAt(0);                                  // Extract row letter
        
        if (rowChar < 'A' || rowChar > 'J') {
            return false;
        }
        
        int col;
        try {
            col = Integer.parseInt(shot.substring(1));               // Extract column number
        } catch (NumberFormatException e) {
            return false;
        }
        
        if (col < 1 || col > 10) {
            return false;
        }
        
        return true;
    }
    
    private static void placeShipWithValidation(Scanner scanner, char[][] field, Ship ship) {
        boolean validPlacement = false;
        
        while (!validPlacement) {
            System.out.println("Enter the coordinates of the " + ship.name + " (" + ship.length + " cells):");
            System.out.println();
            String input = scanner.nextLine();
            
            String[] coords = input.split(" ");                          // Parse the coordinates
            String start = coords[0];
            String end = coords[1];
            
            if (!isValidFormat(start, end)) {
                System.out.println();
                System.out.println("Error! Invalid coordinates! Try again:");
                System.out.println();
                continue;
            }
            
            int startRow = start.charAt(0) - 'A';
            int startCol = Integer.parseInt(start.substring(1)) - 1;
            
            int endRow = end.charAt(0) - 'A';
            int endCol = Integer.parseInt(end.substring(1)) - 1;
            
            if (startRow != endRow && startCol != endCol) {                 // Check if coordinates are not diagonal
                System.out.println();
                System.out.println("Error! Wrong ship location! Try again:");
                System.out.println();
                continue;
            }
            
            int length;
            if (startRow == endRow) {                               // Horizontal ship
                length = Math.abs(endCol - startCol) + 1;
            } else {                                                // Vertical ship
                length = Math.abs(endRow - startRow) + 1;
            }
            
            if (length != ship.length) {
                System.out.println();
                System.out.println("Error! Wrong length of the " + ship.name + "! Try again:");
                System.out.println();
                continue;
            }
            
            if (isTooClose(field, startRow, startCol, endRow, endCol)) {            // Check if ship placement is too close to another ship
                System.out.println();
                System.out.println("Error! You placed it too close to another one. Try again:");
                System.out.println();
                continue;
            }
            
            placeShip(field, ship, startRow, startCol, endRow, endCol);
            validPlacement = true;
            
            System.out.println();
            printField(field);
            System.out.println();
        }
    }
    
    private static boolean isValidFormat(String start, String end) {
        if (start.length() < 2 || end.length() < 2) {
            return false;
        }
        
        char startRowChar = start.charAt(0);                          // Extract row letters
        char endRowChar = end.charAt(0);
        
        if (startRowChar < 'A' || startRowChar > 'J' || endRowChar < 'A' || endRowChar > 'J') {
            return false;
        }
        
        int startCol;
        int endCol;
        try {
            startCol = Integer.parseInt(start.substring(1));            // Extract column numbers
            endCol = Integer.parseInt(end.substring(1));
        } catch (NumberFormatException e) {
            return false;
        }
        
        if (startCol < 1 || startCol > 10 || endCol < 1 || endCol > 10) {
            return false;
        }
        
        return true;
    }
    
    private static boolean isTooClose(char[][] field, int startRow, int startCol, int endRow, int endCol) {          // Check if ship placement is too close to another ship
        int minRow = Math.min(startRow, endRow);
        int maxRow = Math.max(startRow, endRow);
        int minCol = Math.min(startCol, endCol);
        int maxCol = Math.max(startCol, endCol);
        
        for (int row = minRow - 1; row <= maxRow + 1; row++) {                      // Check all cells around the ship (including diagonals)
            for (int col = minCol - 1; col <= maxCol + 1; col++) {
                if (row < 0 || row >= 10 || col < 0 || col >= 10) {                // Skip cells outside the field
                    continue;
                }
                
                if (field[row][col] == 'O') {                   // If there's already a ship part, it's too close
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private static void placeShip(char[][] field, Ship ship, int startRow, int startCol, int endRow, int endCol) {
        if (startRow == endRow) {                              // Horizontal ship
            int minCol = Math.min(startCol, endCol);
            int maxCol = Math.max(startCol, endCol);
            
            for (int col = minCol; col <= maxCol; col++) {
                field[startRow][col] = 'O';
                String coord = "" + (char)('A' + startRow) + (col + 1);
                ship.coordinates.add(coord);
            }
        } else {                                              // Vertical ship
            int minRow = Math.min(startRow, endRow);
            int maxRow = Math.max(startRow, endRow);
            
            for (int row = minRow; row <= maxRow; row++) {
                field[row][startCol] = 'O';
                String coord = "" + (char)('A' + row) + (startCol + 1);
                ship.coordinates.add(coord);
            }
        }
    }
    
    private static void printField(char[][] field) {
        System.out.print("  ");                                          // Print column numbers
        for (int i = 1; i <= 10; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        
        for (int i = 0; i < 10; i++) {                                 // Print rows
            System.out.print((char)('A' + i) + " ");
            for (int j = 0; j < 10; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
    }
}
