/**
 * Code Written by Jackson L. Davis
 *
 * This is the main Java file to run.
 * This program sets up a game of Battleship.
 * Information about the game "Battleship" can be found here:
 * https://en.wikipedia.org/wiki/Battleship_(game)
 *
 * In this program, the user will play a game of Battleship against a computer player.
 * Here are the ships that the player has:
 * 1 ship  of length 5
 * 2 ships of length 4
 * 3 ships of length 3
 * 4 ships of length 2
 * The player can either place these ships on their map manually, or have them placed on their map randomly.
 */
import java.util.Objects;
import java.util.Scanner;

public class BattleshipGame {
    /**
     * A convenient method to display two Maps side by side,
     * the player's Map will be on the left with the Ships visible,
     * the ComputerPlayer's Map will be on the right with the Ships hidden unless shot at
     * @param compMap the ComputerPlayer's map
     * @param pMap the player's Map
     * @postcond the two Maps will be printed to the console
     */
    public static void printMaps(Map compMap, Map pMap) {
        String cpShips = compMap.displayShotsOnMap();
        String playerShips = pMap.displayShipsAndShotsOnMap();
        System.out.println("  CP Ships         Your Ships");
        System.out.println(cpShips.substring(0, 12) + "     " + playerShips.substring(0, 12));
        System.out.println(cpShips.substring(13, 25) + "     " + playerShips.substring(13, 25));
        System.out.println(cpShips.substring(26, 38) + "     " + playerShips.substring(26, 38));
        System.out.println(cpShips.substring(39, 51) + "     " + playerShips.substring(39, 51));
        System.out.println(cpShips.substring(52, 64) + "     " + playerShips.substring(52, 64));
        System.out.println(cpShips.substring(65, 77) + "     " + playerShips.substring(65, 77));
        System.out.println(cpShips.substring(78, 90) + "     " + playerShips.substring(78, 90));
        System.out.println(cpShips.substring(91, 103) + "     " + playerShips.substring(91, 103));
        System.out.println(cpShips.substring(104, 116) + "     " + playerShips.substring(104, 116));
        System.out.println(cpShips.substring(117, 129) + "     " + playerShips.substring(117, 129));
        System.out.println(cpShips.substring(130, 142) + "     " + playerShips.substring(130, 142));
        System.out.println(cpShips.substring(143, 155) + "     " + playerShips.substring(143, 155));
    }

    public static void main(String[] args){

        // introduction
        System.out.println("Welcome to Battleship!");
        System.out.println("In this game, you will play a game of Battleship against a computer player.");
        System.out.println("You will place ships on your map, and then try sinking the computer player's ships.");
        System.out.println();

        // place Ships on Map
        System.out.println("Press 1 and enter to place the ships on your map manually.");
        System.out.println("Press 2 and enter to have the ships placed on your map randomly.");
        System.out.println("Press Q and enter at any time to quit.");
        String userInput = "0";
        Scanner scanner = new Scanner(System.in);
        while (!Objects.equals(userInput, "1") && !Objects.equals(userInput, "2") && !Objects.equals(userInput, "Q") && !Objects.equals(userInput, "q")) {
            System.out.print("Enter your choice here: ");
            userInput = scanner.nextLine();
        }
        if (Objects.equals(userInput, "Q") || Objects.equals(userInput, "q")) {
            scanner.close();
            return;
        } else {}
        Map playerMap = new Map();
        if (Objects.equals(userInput, "1")) {
            int[] shipSizes = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2};
            boolean horizontal;
            int[] coordinates = {-1, -1};
            boolean shipPlaced;
            for (int shipSize : shipSizes) {
                coordinates[0] = -1;
                coordinates[1] = -1;
                shipPlaced = false;
                System.out.println("Place a ship of size " + shipSize + ".");
                // orientation
                System.out.println("Press H and enter to place the ship horizontally.");
                System.out.println("Press V and enter to place the ship vertically.");
                while (!Objects.equals(userInput, "h") && !Objects.equals(userInput, "H") && !Objects.equals(userInput, "v") && !Objects.equals(userInput, "V")) {
                    System.out.print("Enter your choice here: ");
                    userInput = scanner.nextLine();
                    if (Objects.equals(userInput, "Q") || Objects.equals(userInput, "q")) {
                        scanner.close();
                        return;
                    } else {}
                }
                horizontal = Objects.equals(userInput, "h") || Objects.equals(userInput, "H");
                // location of top/left side
                System.out.println("Enter the location of the top/left side of the ship with a letter and a number.");
                while (coordinates[0] < 0 || coordinates[1] < 0 || !shipPlaced) {
                    System.out.print("Enter your location here: ");
                    userInput = scanner.nextLine();
                    if (Objects.equals(userInput, "Q") || Objects.equals(userInput, "q")) {
                        scanner.close();
                        return;
                    } else {}
                    try {
                        coordinates = playerMap.convertShotToNumberCoordinates(userInput);
                        if (playerMap.addShip(shipSize, horizontal, coordinates[0], coordinates[1])) {
                            shipPlaced = true;
                            System.out.println("Ship placed. Here is the resulting map.");
                            System.out.println(playerMap.displayShipsOnMap());
                        } else {
                            System.out.println("Could not place ship there.");
                        }
                    } catch (Exception e) {
                        System.out.println("That is not a valid location.");
                    }
                }
            }
        }
        else {
            playerMap.addShipsRandomly();
        }

        // set up ComputerPlayer and ComputerPlayer's Map
        Map cpMap = new Map();
        cpMap.addShipsRandomly();
        ComputerPlayer cp = new ComputerPlayer(playerMap);

        // set up gameplay
        System.out.println("The computer player's map will be on the left, and your map will be on the right like so.");
        printMaps(cpMap, playerMap);
        System.out.println("You cannot see the ships on the computer player's map until you shoot them");
        System.out.println("Your shots and the computer player's shots will both be recorded on the maps.");
        System.out.println(". is a miss.");
        System.out.println("* is a hit.");
        System.out.println("You and the computer player will take turns making shots at the other player's map.");

        // gameplay
        boolean madeNextShot;
        boolean playerWin = false;
        String message;
        while (!playerMap.allShipsSunk() && !cpMap.allShipsSunk()) {
            madeNextShot = false;
            // your shot
            while (!madeNextShot) {
                System.out.print("Enter your shot here: ");
                userInput = scanner.nextLine();
                if (Objects.equals(userInput, "Q") || Objects.equals(userInput, "q")) {
                    scanner.close();
                    return;
                } else {}
                message = cpMap.shoot(userInput);
                System.out.println(message);
                if (!Objects.equals(message, "You already shot here.") && !Objects.equals(message, "That is not a valid location.")) {
                    madeNextShot = true;
                } else {}
                if (cpMap.allShipsSunk()) {
                    System.out.println("You win!");
                    playerWin = true;
                } else {}
            }
            // computer player's shot
            if (!playerWin) {
                cp.makeNextShot();
            }
            printMaps(cpMap, playerMap);
        }
        if (!playerWin) {
            System.out.println("Here were the computer player's ships.");
            System.out.println(cpMap.displayShipsOnMap());
        } else {}

        // close scanner at the end of the program
        scanner.close();
    }
}
