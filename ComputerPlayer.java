/**
 * Code Written by Jackson L. Davis
 *
 * A ComputerPlayer will try to figure out where the Ships on a player's Map are.
 */
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class ComputerPlayer {
    private Map opponentMap;
    private String[] checkerSpaces1;
    private String[] checkerSpaces2;
    private int checkerIndex1;
    private int checkerIndex2;
    private ArrayList<String> spacesNotSunk; // spaces that were shot at where a Ship was hit, but the Ship has not sunk

    /**
     * Constructor method for a ComputerPlayer
     * @param m the opponent's Map
     */
    public ComputerPlayer(Map m) {
        this.opponentMap = m;
        this.checkerSpaces1 = new String[]{"0A", "0C", "0E", "0G", "0I",
                                           "1B", "1D", "1F", "1H", "1J",
                                           "2A", "2C", "2E", "2G", "2I",
                                           "3B", "3D", "3F", "3H", "3J",
                                           "4A", "4C", "4E", "4G", "4I",
                                           "5B", "5D", "5F", "5H", "5J",
                                           "6A", "6C", "6E", "6G", "6I",
                                           "7B", "7D", "7F", "7H", "7J",
                                           "8A", "8C", "8E", "8G", "8I",
                                           "9B", "9D", "9F", "9H", "9J"};
        this.checkerSpaces2 = new String[]{"0B", "0D", "0F", "0H", "0J",
                                           "1A", "1C", "1E", "1G", "1I",
                                           "2B", "2D", "2F", "2H", "2J",
                                           "3A", "3C", "3E", "3G", "3I",
                                           "4B", "4D", "4F", "4H", "4J",
                                           "5A", "5C", "5E", "5G", "5I",
                                           "6B", "6D", "6F", "6H", "6J",
                                           "7A", "7C", "7E", "7G", "7I",
                                           "8B", "8D", "8F", "8H", "8J",
                                           "9A", "9C", "9E", "9G", "9I"};
        this.checkerIndex1 = 0;
        this.checkerIndex2 = 0;
        this.spacesNotSunk = new ArrayList<String>();

        // shuffle this.checkerSpaces1 and this.checkerSpaces2 using Fisher-Yates shuffle
        int index;
        String entry;
        Random rand = new Random();
        for (int i = this.checkerSpaces1.length - 1; i > 0; i--) {
            index = rand.nextInt(i + 1);
            // swap entries
            entry = this.checkerSpaces1[index];
            this.checkerSpaces1[index] = this.checkerSpaces1[i];
            this.checkerSpaces1[i] = entry;
        }
        for (int j = this.checkerSpaces2.length - 1; j > 0; j--) {
            index = rand.nextInt(j + 1);
            // swap entries
            entry = this.checkerSpaces2[index];
            this.checkerSpaces2[index] = this.checkerSpaces2[j];
            this.checkerSpaces2[j] = entry;
        }
    }

    /**
     * The ComputerPlayer makes a shot based on the information it has,
     * it will shoot at random spaces in a checkerboard pattern so that it is guaranteed to hit parts of every Ship
     * on the Map, when it hits part of a Ship, it will shoot at the neighbouring spaces to hit the rest of the Ship
     * and sink it.
     * @postcond the results of the ComputerPlayer's shot will be printed to the console
     * @postcond this.opponentMap, this.checkerIndex1, this.checkerIndex2, and this.spacesNotSunk will be adjusted as necessary
     * @return true if a next shot was made, false otherwise
     */
    public boolean makeNextShot() {
        if (!this.computerPlayerWon()) {
            boolean madeNextShot = false;
            // check if there are Ships that have been shot at that have not sunk
            // if there are none, shot at a random space
            if (this.spacesNotSunk.size() == 0) {
                String message;
                while (!madeNextShot) {
                    // pick a space from this.checkerSpaces1
                    if (this.checkerIndex1 < this.checkerSpaces1.length) {
                        if (!this.opponentMap.alreadyShotHere(this.checkerSpaces1[this.checkerIndex1])) {
                            System.out.println("Computer Player's next shot: " + this.checkerSpaces1[this.checkerIndex1]);
                            message = this.opponentMap.shoot(this.checkerSpaces1[this.checkerIndex1]);
                            System.out.println(message);
                            madeNextShot = true;
                            if (Objects.equals(message, "Hit!")) {
                                this.spacesNotSunk.add(this.checkerSpaces1[this.checkerIndex1]);
                            } else {}
                        }
                        this.checkerIndex1 += 1;
                    }
                    // this should not happen, but pick a space from this.checkerSpaces2
                    else if (this.checkerIndex2 < this.checkerSpaces2.length) {
                        if (!this.opponentMap.alreadyShotHere(this.checkerSpaces2[this.checkerIndex2])) {
                            System.out.println("Computer Player's next shot: " + this.checkerSpaces2[this.checkerIndex2]);
                            message = this.opponentMap.shoot(this.checkerSpaces2[this.checkerIndex2]);
                            System.out.println(message);
                            madeNextShot = true;
                            if (Objects.equals(message, "Hit!")) {
                                this.spacesNotSunk.add(this.checkerSpaces2[this.checkerIndex2]);
                            } else {}
                        }
                        this.checkerIndex2 += 1;
                    }
                    // this really should not happen, but check just in case
                    else {
                        System.out.println("Every space has already been shot at.");
                        return false;
                    }
                }
                // check if the ComputerPlayer won
                if (this.computerPlayerWon()) {
                    System.out.println("The Computer Player wins.");
                } else {}
                return true;
            }
            // if there is a Ship that has been shot at that has not sunk, shoot around it
            else {
                String shipOfInterest = this.spacesNotSunk.get(0);
                int[] shipCoords = this.opponentMap.convertShotToNumberCoordinates(shipOfInterest);
                int[] nextShotCoords;
                boolean directionStillAvailable = true;
                int searchResult;

                // try shooting up
                nextShotCoords = shipCoords.clone();
                while (!madeNextShot && directionStillAvailable) {
                    nextShotCoords[0] -= 1;
                    // make sure new coordinates are valid
                    if (nextShotCoords[0] >= 0) {
                        searchResult = this.searchSpace(nextShotCoords[0], nextShotCoords[1]);
                        if (searchResult == 1) {
                            // a shot was made
                            madeNextShot = true;
                        }
                        else if (searchResult == 0) {
                            // no shot was made, keep searching
                        }
                        else {
                            // no shot was made, but cannot keep searching in this direction
                            directionStillAvailable = false;
                        }
                    }
                    else {
                        directionStillAvailable = false;
                    }
                }
                directionStillAvailable = true;

                // try shooting down
                nextShotCoords = shipCoords.clone();
                while (!madeNextShot && directionStillAvailable) {
                    nextShotCoords[0] += 1;
                    // make sure new coordinates are valid
                    if (nextShotCoords[0] <= 9) {
                        searchResult = this.searchSpace(nextShotCoords[0], nextShotCoords[1]);
                        if (searchResult == 1) {
                            // a shot was made
                            madeNextShot = true;
                        }
                        else if (searchResult == 0) {
                            // no shot was made, keep searching
                        }
                        else {
                            // no shot was made, but cannot keep searching in this direction
                            directionStillAvailable = false;
                        }
                    }
                    else {
                        directionStillAvailable = false;
                    }
                }
                directionStillAvailable = true;

                // try shooting to the left
                nextShotCoords = shipCoords.clone();
                while (!madeNextShot && directionStillAvailable) {
                    nextShotCoords[1] -= 1;
                    // make sure new coordinates are valid
                    if (nextShotCoords[1] >= 0) {
                        searchResult = this.searchSpace(nextShotCoords[0], nextShotCoords[1]);
                        if (searchResult == 1) {
                            // a shot was made
                            madeNextShot = true;
                        }
                        else if (searchResult == 0) {
                            // no shot was made, keep searching
                        }
                        else {
                            // no shot was made, but cannot keep searching in this direction
                            directionStillAvailable = false;
                        }
                    }
                    else {
                        directionStillAvailable = false;
                    }
                }
                directionStillAvailable = true;

                // try shooting to the right
                nextShotCoords = shipCoords.clone();
                while (!madeNextShot && directionStillAvailable) {
                    nextShotCoords[1] += 1;
                    // make sure new coordinates are valid
                    if (nextShotCoords[1] <= 9) {
                        searchResult = this.searchSpace(nextShotCoords[0], nextShotCoords[1]);
                        if (searchResult == 1) {
                            // a shot was made
                            madeNextShot = true;
                        }
                        else if (searchResult == 0) {
                            // no shot was made, keep searching
                        }
                        else {
                            // no shot was made, but cannot keep searching in this direction
                            directionStillAvailable = false;
                        }
                    }
                    else {
                        directionStillAvailable = false;
                    }
                }

                // this should not happen, but if the method actually gets here, stop immediately
                if (!madeNextShot) {
                    return false;
                } else {}

                // check if the ComputerPlayer won
                if (this.computerPlayerWon()) {
                    System.out.println("The Computer Player wins.");
                } else {}
                return true;
            }
        }
        else {
            System.out.println("The Computer Player has already won.");
            return false;
        }
    }

    /**
     * Helper method for this.makeNextShot(),
     * check a space and determine whether it should be shot at, and determine if this.makeNextShot() should continue
     * searching in the direction it is searching in
     * @param rowc the row coordinate of the space to check
     * @param colc the column coordinate of the space to check
     * @precond 0 <= rowc <= 9 && 0 <= colc <= 9
     * @postcond this.spacesNotSunk is adjusted according to the outcome of the shot if a shot was made
     * @return 1 if a shot was made,
     * 0 if no shot was made, but this.makeNextShot() can still search in the direction it is searching in,
     * and -1 if no shot was made, but this.makeNextShot() cannot continue searching in the direction it is searching in
     */
    public int searchSpace(int rowc, int colc) {
        // if this space has not yet been shot at, shoot at it
        if (!this.opponentMap.alreadyShotHere(rowc, colc)) {
            String nextShot = this.opponentMap.convertNumberCoordinatesToShot(rowc, colc);
            System.out.println("Computer Player's next shot: " + nextShot);
            String message = this.opponentMap.shoot(nextShot);
            System.out.println(message);
            // check if a Ship was hit but not sunk
            if (Objects.equals(message, "Hit!")) {
                this.spacesNotSunk.add(nextShot);
            }
            // check if a Ship was sunk
            else if (message.contains("Sunk")) {
                this.removeSunkShipsFromSpacesNotSunk();
            }
            else {}
            return 1;
        }
        // if this space has been shot at, and it is a Ship that has not been sunk, keep searching in this direction
        else if (this.inSpacesNotSunk(this.opponentMap.convertNumberCoordinatesToShot(rowc, colc))) {
            return 0;
        }
        // otherwise, stop searching in this direction
        else {
            return -1;
        }
    }

    /**
     * Check if a shot is in this.spacesNotSunk
     * @param shot a String representing a shot
     * @return true if the parameter shot is in this.spacesNotSunk, false otherwise
     */
    public boolean inSpacesNotSunk(String shot) {
        for (String sp : this.spacesNotSunk) {
            if (Objects.equals(sp, shot)) {
                return true;
            } else {}
        }
        return false;
    }

    /**
     * Remove the spaces from this.spacesNotSunk that have a sunk Ship
     * @postcond the spaces containing a sunk Ship are removed from this.spacesNotSunk
     */
    public void removeSunkShipsFromSpacesNotSunk() {
        int[] coords;
        Ship[][] shipsMap = this.opponentMap.getShipsOnMap();
        ArrayList<String> spacesToRemove = new ArrayList<String>();
        for (String sp : this.spacesNotSunk) {
            coords = this.opponentMap.convertShotToNumberCoordinates(sp);
            if (shipsMap[coords[0]][coords[1]].hasShipSunk()) {
                spacesToRemove.add(sp);
            } else {}
        }
        // remove spaces containing a sunk Ship
        for (String sr : spacesToRemove) {
            for (int i = 0; i < this.spacesNotSunk.size(); i++) {
                if (Objects.equals(sr, this.spacesNotSunk.get(i))) {
                    this.spacesNotSunk.remove(i);
                    break;
                } else {}
            }
        }
        spacesToRemove.clear();
    }

    /**
     * Determine if the ComputerPlayer has won
     * @return true if the ComputerPlayer has won, false otherwise
     */
    public boolean computerPlayerWon() {
        return this.opponentMap.allShipsSunk();
    }

    public static void main(String[] args){
        System.out.println("Testing ComputerPlayer.java");
        int uErrors = 0; // number of unintentional errors

        // test ComputerPlayer() constructor
        try {
            Map testMap = new Map();
            ComputerPlayer testCP = new ComputerPlayer(testMap);
        }
        catch (Exception e) {
            System.out.println("Error: ComputerPlayer() constructor threw the following exception:");
            System.out.println(e.getMessage());
            uErrors += 1;
        }

        // assuming the ComputerPlayer() constructor works, test the rest of the ComputerPlayer methods
        // set up a Map
        Map m = new Map();
        m.addShip(5, false, 5, 5);
        m.addShip(4, true, 9, 6);
        m.addShip(4, false, 3, 0);
        m.addShip(3, true, 1, 5);
        m.addShip(3, true, 2, 5);
        m.addShip(3, true, 3, 5);
        m.addShip(2, true, 6, 8);
        m.addShip(2, true, 2, 1);
        m.addShip(2, false, 8, 0);
        m.addShip(2, false, 8, 1);
        ComputerPlayer cp = new ComputerPlayer(m);

        System.out.println("Here is a test Map");
        System.out.println(m.displayShipsOnMap());

        // test computerPlayerWon() and makeNextShot()
        // searchSpace(), inSpacesNotSunk(), and removeSunkShipsFromSpacesNotSunk() will not be tested explicitly
        System.out.println("Now, the ComputerPlayer will try to sink the Ships on this Map.");
        System.out.println("The ComputerPlayer's perspective will be printed to the console.");

        if (cp.computerPlayerWon()) {
            System.out.println("Error: computerPlayerWon() returned true when the ComputerPlayer has not won");
            uErrors += 1;
        } else {}

        boolean madeNextShot = true;
        while (!cp.computerPlayerWon() && madeNextShot) {
            madeNextShot = cp.makeNextShot();
            System.out.println(m.displayShotsOnMap());
        }
        if (!madeNextShot && !cp.computerPlayerWon()) {
            System.out.println("Error: makeNextShot() did not successfully make a next shot even though the ComputerPlayer has not won");
            uErrors += 1;
        }
        else if (!cp.computerPlayerWon()) {
            System.out.println("Error: computerPlayerWon() did not return true even though the ComputerPlayer should have won");
            uErrors += 1;
        }
        else {}

        System.out.println("If the ComputerPlayer tries to make another shot, it should not because the ComputerPlayer already won");
        if (cp.makeNextShot()) {
            System.out.println("Error: makeNextShot() returned true even though the ComputerPlayer already won");
            uErrors += 1;
        } else {}

        System.out.println("Now the ComputerPlayer will try to sink the Ships on a randomly generated Map");
        Map mRand = new Map();
        mRand.addShipsRandomly();
        System.out.println(mRand.displayShipsOnMap());
        ComputerPlayer cp2 = new ComputerPlayer(mRand);
        madeNextShot = true;
        while (!cp2.computerPlayerWon() && madeNextShot) {
            madeNextShot = cp2.makeNextShot();
            System.out.println(mRand.displayShotsOnMap());
        }
        if (!madeNextShot && !cp2.computerPlayerWon()) {
            System.out.println("Error: makeNextShot() did not successfully make a next shot even though the ComputerPlayer has not won");
            uErrors += 1;
        }
        else if (!cp2.computerPlayerWon()) {
            System.out.println("Error: computerPlayerWon() did not return true even though the ComputerPlayer should have won");
            uErrors += 1;
        }
        else {}

        System.out.println("Testing complete with " + uErrors + " unintentional errors.");
    }
}
