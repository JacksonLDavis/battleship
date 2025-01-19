/**
 * Code Written by Jackson L. Davis
 *
 * A Map contains information on where the Ships are, and what spaces have been shot at.
 */
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Map {
    private Ship[] ships;
    private final int[] shipSizes = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2};
    private int shipIndex;
    private Ship[][] shipsOnMap; // array entries contain references to Ships
    private boolean[][] shotsOnMap;
    private final char[] validRows  = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private final char[] validColumns = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
    private String[] mapSpaces;


    /**
     * Constructor method for a Map, the Map will start out blank with no assigned Ships
     */
    public Map() {
        this.ships = new Ship[10];
        this.shipIndex = 0;
        this.shipsOnMap = new Ship[10][10];
        this.shotsOnMap = new boolean[10][10];
        this.mapSpaces = new String[]{"0A", "0B", "0C", "0D", "0E", "0F", "0G", "0H", "0I", "0J",
                                      "1A", "1B", "1C", "1D", "1E", "1F", "1G", "1H", "1I", "1J",
                                      "2A", "2B", "2C", "2D", "2E", "2F", "2G", "2H", "2I", "2J",
                                      "3A", "3B", "3C", "3D", "3E", "3F", "3G", "3H", "3I", "3J",
                                      "4A", "4B", "4C", "4D", "4E", "4F", "4G", "4H", "4I", "4J",
                                      "5A", "5B", "5C", "5D", "5E", "5F", "5G", "5H", "5I", "5J",
                                      "6A", "6B", "6C", "6D", "6E", "6F", "6G", "6H", "6I", "6J",
                                      "7A", "7B", "7C", "7D", "7E", "7F", "7G", "7H", "7I", "7J",
                                      "8A", "8B", "8C", "8D", "8E", "8F", "8G", "8H", "8I", "8J",
                                      "9A", "9B", "9C", "9D", "9E", "9F", "9G", "9H", "9I", "9J"};
    }

    /**
     * @return the 2D array containing references to Ships
     */
    public Ship[][] getShipsOnMap() {
        return this.shipsOnMap;
    }

    /**
     * Determine if a Ship with the given parameters will fit on the Map
     * @param sz the size of the proposed Ship
     * @param horiz the orientation of the proposed Ship
     * @param rowc the row coordinate of the top or left side of the proposed Ship
     * @param colc the column coordinate of the top or left side of the proposed Ship
     * @precond 2 <= sz <= 5 (i.e. the size of the proposed Ship must be valid)
     * @precond 0 <= rowc <= 9 && 0 <= colc <= 9
     * @return true if the Ship will fit on the Map, false otherwise
     */
    public boolean willShipFit(int sz, boolean horiz, int rowc, int colc) {
        // check preconditions
        if (sz < 2 || sz > 5 || rowc < 0 || rowc > 9 || colc < 0 || colc > 9) {
            return false;
        }
        else {
            // check if the Ship would be fully on the Map
            if (horiz && colc + sz - 1 <= 9) {
                // check if the spaces are occupied
                for (int i = 0; i < sz; i++) {
                    if (this.shipsOnMap[rowc][colc + i] != null) {
                        return false;
                    } else {}
                }
                return true;
            }
            else if (!horiz && rowc + sz - 1 <= 9) {
                // check if the spaces are occupied
                for (int i = 0; i < sz; i++) {
                    if (this.shipsOnMap[rowc + i][colc] != null) {
                        return false;
                    } else {}
                }
                return true;
            }
            else {
                return false;
            }
        }
    }

    /**
     * Add a Ship with the given parameters to the Map if possible
     * @param sz the size of the proposed Ship
     * @param horiz the orientation of the proposed Ship
     * @param rowc the row coordinate of the top or left side of the proposed Ship
     * @param colc the column coordinate of the top or left side of the proposed Ship
     * @precond this.willShipFit() && this.shipIndex < 10
     * @postcond the proposed Ship will be added to this.ships and to this.shipsOnMap, this.shipIndex will increase by 1
     * @return true if the Ship was successfully added to the Map, false otherwise
     */
    public boolean addShip(int sz, boolean horiz, int rowc, int colc) {
        if (willShipFit(sz, horiz, rowc, colc) && this.shipIndex < 10) {
            // create and add the Ship
            Ship sh = new Ship(sz, horiz, rowc, colc);
            this.ships[this.shipIndex] = sh;
            this.shipIndex += 1;
            // place the Ship on the Map
            if (horiz) {
                for (int i = 0; i < sz; i++) {
                    this.shipsOnMap[rowc][colc + i] = sh;
                }
            }
            else {
                for (int i = 0; i < sz; i++) {
                    this.shipsOnMap[rowc + i][colc] = sh;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Add ten Ships to the Map randomly. Here are the Ships to be placed:
     * 1 ship  of length 5
     * 2 ships of length 4
     * 3 ships of length 3
     * 4 ships of length 2
     * The largest Ships will be placed first because the smaller Ships are easier to fit in later
     * @precond this.shipIndex == 0 (i.e. no Ships should have been placed yet)
     * @postcond this.mapSpaces will be shuffled to determine the locations of the Ships
     * @postcond ten Ships will be added to this.ships, shipIndex = 10, and the Ships will be placed on this.shipsOnMap
     * @return 0 if all Ships were placed successfully,
     *         -1 if the precondition was not met,
     *         1 if the precondition was met, but not all Ships were placed successfully
     */
    public int addShipsRandomly() {
        if (this.shipIndex != 0) {
            return -1;
        }
        else {
            // prepare objects for use
            int index;
            String entry;
            int[] convertedCoords;
            int sz;
            boolean horiz;
            boolean shipPlaced;
            // shuffle this.mapSpaces using Fisher-Yates shuffle
            Random rand = new Random();
            for (int i = this.mapSpaces.length - 1; i > 0; i--) {
                index = rand.nextInt(i + 1);
                // swap entries
                entry = this.mapSpaces[index];
                this.mapSpaces[index] = this.mapSpaces[i];
                this.mapSpaces[i] = entry;
            }
            // place Ships on the Map
            for (int s = 0; s < this.shipsOnMap.length; s++) {
                shipPlaced = false;
                // test random spaces on the Map
                for (String sp : this.mapSpaces) {
                    try {
                        convertedCoords = this.convertShotToNumberCoordinates(sp);
                        sz = this.shipSizes[s];
                        // randomly choose between horizontal or vertical placement, and try both options before moving to another space
                        horiz = rand.nextBoolean();
                        if (horiz) {
                            if (this.willShipFit(sz, true, convertedCoords[0], convertedCoords[1])) {
                                if (this.addShip(sz, true, convertedCoords[0], convertedCoords[1])) {
                                    shipPlaced = true;
                                } else {}
                            }
                            else if (this.willShipFit(sz, false, convertedCoords[0], convertedCoords[1])) {
                                if (this.addShip(sz, false, convertedCoords[0], convertedCoords[1])) {
                                    shipPlaced = true;
                                } else {}
                            } else {}
                        }
                        else {
                            if (this.willShipFit(sz, false, convertedCoords[0], convertedCoords[1])) {
                                if (this.addShip(sz, false, convertedCoords[0], convertedCoords[1])) {
                                    shipPlaced = true;
                                } else {}
                            }
                            else if (this.willShipFit(sz, true, convertedCoords[0], convertedCoords[1])) {
                                if (this.addShip(sz, true, convertedCoords[0], convertedCoords[1])) {
                                    shipPlaced = true;
                                } else {}
                            } else {}
                        }
                    }
                    catch (Exception e) {
                        // this should not happen, but report it if it does
                        System.out.println(e.getMessage());
                        return 1;
                    }
                    if (shipPlaced) {
                        break;
                    } else {}
                }
            }
            // make sure ten Ships were placed
            if (this.shipIndex == 10) {
                return 0;
            }
            else {
                // this should not happen, but check just in case
                return 1;
            }
        }
    }

    /**
     * Determine if the location of a shot is valid
     * @param shot the coordinates of the shot to take, should be two characters where
     *             one of the two characters is a letter from A-J,
     *             and the other character is a number from 0-9,
     *             it does not matter which comes first
     * @return true if the shot is valid, false otherwise
     */
    public boolean isShotValid(String shot) {
        if (shot.length() != 2) {
            return false;
        }
        else {
            boolean hasNumber = false;
            boolean hasLetter = false;
            for (char num : this.validRows) {
                if (num == shot.charAt(0) || num == shot.charAt(1)) {
                    hasNumber = true;
                    break;
                }
            }
            if (hasNumber) {
                for (char let : this.validColumns) {
                    if (let == Character.toUpperCase(shot.charAt(0)) || let == Character.toUpperCase(shot.charAt(1))) {
                        hasLetter = true;
                        break;
                    }
                }
                return hasLetter;
            }
            else {
                return false;
            }
        }
    }

    /**
     * Convert the shot (String input) to number coordinates for array indexing on the Map
     * @param shot the coordinates of the shot
     * @precond this.isShotValid(shot)
     * @return an array of the form [rowCoordinate, columnCoordinate]
     * @throws RuntimeException if the precondition is not met
     */
    public int[] convertShotToNumberCoordinates(String shot) {
        if (!isShotValid(shot)) {
            throw new RuntimeException("Error in convertShotToNumberCoordinates(): input shot is not valid");
        }
        else {
            int[] coords = {-1, -1};
            // convert row coordinate
            for (int i = 0; i < this.validRows.length; i++) {
                if (this.validRows[i] == shot.charAt(0) || this.validRows[i] == shot.charAt(1)) {
                    coords[0] = i;
                    break;
                }
            }
            if (coords[0] == -1) {
                // this should not happen, but check just in case
                throw new RuntimeException("Error in convertShotToNumberCoordinates(): row coordinate was not found");
            } else {}
            // convert column coordinate
            for (int j = 0; j < this.validColumns.length; j++) {
                if (this.validColumns[j] == Character.toUpperCase(shot.charAt(0)) || this.validColumns[j] == Character.toUpperCase(shot.charAt(1))) {
                    coords[1] = j;
                    break;
                }
            }
            if (coords[1] == -1) {
                // this should not happen, but check just in case
                throw new RuntimeException("Error in convertShotToNumberCoordinates(): column coordinate was not found");
            } else {}
            return coords;
        }
    }

    /**
     * Convert the number coordinates to a shot
     * @param rowc the row coordinate
     * @param colc the column coordinate
     * @precond 0 <= rowc <= 9 && 0 <= colc <= 9
     * @return a String representing the shot, ex. "0A"
     * @throws RuntimeException if the precondition is not met
     */
    public String convertNumberCoordinatesToShot(int rowc, int colc) {
        if (rowc < 0 || rowc > 9 || colc < 0 || colc > 9) {
            throw new RuntimeException("Error in convertNumberCoordinatesToShot: the coordinates are not valid");
        }
        else {
            return "" + this.validRows[rowc] + this.validColumns[colc];
        }
    }

    /**
     * Determine if a location of the Map has already been shot at
     * @param shot the coordinates of the shot
     * @precond this.isShotValid(shot)
     * @return true if the location has been shot at, false otherwise
     * @throws RuntimeException if the precondition is not met
     */
    public boolean alreadyShotHere(String shot) {
        try {
            int[] convertedShot = this.convertShotToNumberCoordinates(shot);
            return this.alreadyShotHere(convertedShot[0], convertedShot[1]);
        }
        catch (Exception e) {
            throw new RuntimeException("Error in alreadyShotHere(): input shot is not valid");
        }
    }

    /**
     * Determine if a location of the Map has already been shot at
     * @param rowc the row coordinate of the location to check
     * @param colc to column coordinate of the location to check
     * @precond 0 <= rowc <= 9 && 0 <= colc <= 9
     * @return true if the location has been shot at, false otherwise
     * @throws RuntimeException if the precondition is not met
     */
    public boolean alreadyShotHere(int rowc, int colc) {
        if (rowc >= 0 && rowc <= 9 && colc >= 0 && colc <= 9) {
            return this.shotsOnMap[rowc][colc];
        }
        else {
            throw new RuntimeException("Error in alreadyShotHere(): the coordinates are not valid");
        }
    }

    /**
     * Attempt to shoot at the specified location
     * @param shot the location of the shot
     * @postcond the shot is recorded on this.shotsOnMap, if a Ship is located where the shot is, it will record a hit
     * @return a String giving details about the results of the shot
     */
    public String shoot(String shot) {
        if (!this.isShotValid(shot)) {
            return "That is not a valid location.";
        }
        else {
            try {
                int[] coords = this.convertShotToNumberCoordinates(shot);
                if (alreadyShotHere(coords[0], coords[1])) {
                    return "You already shot here.";
                }
                else {
                    // make the shot
                    this.shotsOnMap[coords[0]][coords[1]] = true;
                    Ship sh = this.shipsOnMap[coords[0]][coords[1]];
                    if (sh == null) {
                        return "Miss.";
                    }
                    else {
                        String result = "Hit!";
                        sh.addHit();
                        if (sh.hasShipSunk()) {
                            result += "\nSunk a ship of length ";
                            result += sh.getSize();
                            result += ".";
                        } else {}
                        return result;
                    }
                }
            }
            catch (Exception e) {
                // this should not happen, but check just in case
                return e.getMessage();
            }
        }
    }

    /**
     * Determine if all Ships have sunk
     * @return true if all Ships have sunk, false otherwise
     */
    public boolean allShipsSunk() {
        for (Ship sh : this.ships) {
            if (sh != null) {
                if (!sh.hasShipSunk()) {
                    return false;
                } else {}
            } else {}
        }
        return true;
    }

    /**
     * Display the Ships on the Map
     * @return a String showing where the Ships are on the Map
     */
    public String displayShipsOnMap() {
        String st = " |ABCDEFGHIJ\n";
        st += "-+----------\n";
        for (int i = 0; i < 10; i++) {
            st += i;
            st += "|";
            for (int j = 0; j < 10; j++) {
                if (this.shipsOnMap[i][j] == null) {
                    st += " ";
                }
                else {
                    st += this.shipsOnMap[i][j].getSize();
                }
            }
            st += "\n";
        }
        return st;
    }

    /**
     * Display the shots on the Map, reveal which shots are hits, and reveal any Ships that have sunk
     * @return a String showing the shots, hits, and sunk Ships on the Map
     */
    public String displayShotsOnMap() {
        String st = " |ABCDEFGHIJ\n";
        st += "-+----------\n";
        for (int i = 0; i < 10; i++) {
            st += i;
            st += "|";
            for (int j = 0; j < 10; j++) {
                // shot
                if (this.shotsOnMap[i][j]) {
                    Ship sh = this.shipsOnMap[i][j];
                    // miss
                    if (sh == null) {
                        st += ".";
                    }
                    // hit
                    else {
                        // hit mark
                        if (!sh.hasShipSunk()) {
                            st += "*";
                        }
                        // sunk Ships are revealed
                        else {
                            st += sh.getSize();
                        }
                    }
                }
                // no shot
                else {
                    st += " ";
                }
            }
            st += "\n";
        }
        return st;
    }

    /**
     * Display the Ships and the shots on the Map, Ships that have sunk will be covered with hit marks
     * @return a String showing the Ships and the shots on the Map
     */
    public String displayShipsAndShotsOnMap() {
        String st = " |ABCDEFGHIJ\n";
        st += "-+----------\n";
        for (int i = 0; i < 10; i++) {
            st += i;
            st += "|";
            for (int j = 0; j < 10; j++) {
                // display shots
                if (this.shotsOnMap[i][j]) {
                    Ship sh = this.shipsOnMap[i][j];
                    // miss
                    if (sh == null) {
                        st += ".";
                    }
                    // hit
                    else {
                        st += "*";
                    }
                }
                // display Ships
                else {
                    if (this.shipsOnMap[i][j] == null) {
                        st += " ";
                    }
                    else {
                        st += this.shipsOnMap[i][j].getSize();
                    }
                }
            }
            st += "\n";
        }
        return st;
    }

    public static void main(String[] args){
        System.out.println("Testing Map.java");
        int uErrors = 0; // number of unintentional errors

        // test Map() constructor
        try {
            Map testMap = new Map();
        }
        catch (Exception e) {
            System.out.println("Error: Map() constructor threw the following exception:");
            System.out.println(e.getMessage());
            uErrors += 1;
        }

        // assuming the Map() constructor works, test the rest of the Map methods
        Map m = new Map();

        // prepare test cases with inputs and expected outputs
        String[] shotsVar1 = {"0A", "0B", "0C", "0D", "0E", "0F", "0G", "0H", "0I", "0J",
                              "1A", "1B", "1C", "1D", "1E", "1F", "1G", "1H", "1I", "1J",
                              "2A", "2B", "2C", "2D", "2E", "2F", "2G", "2H", "2I", "2J",
                              "3A", "3B", "3C", "3D", "3E", "3F", "3G", "3H", "3I", "3J",
                              "4A", "4B", "4C", "4D", "4E", "4F", "4G", "4H", "4I", "4J",
                              "5A", "5B", "5C", "5D", "5E", "5F", "5G", "5H", "5I", "5J",
                              "6A", "6B", "6C", "6D", "6E", "6F", "6G", "6H", "6I", "6J",
                              "7A", "7B", "7C", "7D", "7E", "7F", "7G", "7H", "7I", "7J",
                              "8A", "8B", "8C", "8D", "8E", "8F", "8G", "8H", "8I", "8J",
                              "9A", "9B", "9C", "9D", "9E", "9F", "9G", "9H", "9I", "9J"};

        String[] shotsVar2 = {"0a", "0b", "0c", "0d", "0e", "0f", "0g", "0h", "0i", "0j",
                              "1a", "1b", "1c", "1d", "1e", "1f", "1g", "1h", "1i", "1j",
                              "2a", "2b", "2c", "2d", "2e", "2f", "2g", "2h", "2i", "2j",
                              "3a", "3b", "3c", "3d", "3e", "3f", "3g", "3h", "3i", "3j",
                              "4a", "4b", "4c", "4d", "4e", "4f", "4g", "4h", "4i", "4j",
                              "5a", "5b", "5c", "5d", "5e", "5f", "5g", "5h", "5i", "5j",
                              "6a", "6b", "6c", "6d", "6e", "6f", "6g", "6h", "6i", "6j",
                              "7a", "7b", "7c", "7d", "7e", "7f", "7g", "7h", "7i", "7j",
                              "8a", "8b", "8c", "8d", "8e", "8f", "8g", "8h", "8i", "8j",
                              "9a", "9b", "9c", "9d", "9e", "9f", "9g", "9h", "9i", "9j"};

        String[] shotsVar3 = {"A0", "B0", "C0", "D0", "E0", "F0", "G0", "H0", "I0", "J0",
                              "A1", "B1", "C1", "D1", "E1", "F1", "G1", "H1", "I1", "J1",
                              "A2", "B2", "C2", "D2", "E2", "F2", "G2", "H2", "I2", "J2",
                              "A3", "B3", "C3", "D3", "E3", "F3", "G3", "H3", "I3", "J3",
                              "A4", "B4", "C4", "D4", "E4", "F4", "G4", "H4", "I4", "J4",
                              "A5", "B5", "C5", "D5", "E5", "F5", "G5", "H5", "I5", "J5",
                              "A6", "B6", "C6", "D6", "E6", "F6", "G6", "H6", "I6", "J6",
                              "A7", "B7", "C7", "D7", "E7", "F7", "G7", "H7", "I7", "J7",
                              "A8", "B8", "C8", "D8", "E8", "F8", "G8", "H8", "I8", "J8",
                              "A9", "B9", "C9", "D9", "E9", "F9", "G9", "H9", "I9", "J9"};

        String[] shotsVar4 = {"a0", "b0", "c0", "d0", "e0", "f0", "g0", "h0", "i0", "j0",
                              "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1", "i1", "j1",
                              "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2", "i2", "j2",
                              "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3", "i3", "j3",
                              "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4", "i4", "j4",
                              "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5", "i5", "j5",
                              "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6", "i6", "j6",
                              "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7", "i7", "j7",
                              "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8", "i8", "j8",
                              "a9", "b9", "c9", "d9", "e9", "f9", "g9", "h9", "i9", "j9"};

        int[][] expectedCoords = {{0,0},{0,1},{0,2},{0,3},{0,4},{0,5},{0,6},{0,7},{0,8},{0,9},
                                  {1,0},{1,1},{1,2},{1,3},{1,4},{1,5},{1,6},{1,7},{1,8},{1,9},
                                  {2,0},{2,1},{2,2},{2,3},{2,4},{2,5},{2,6},{2,7},{2,8},{2,9},
                                  {3,0},{3,1},{3,2},{3,3},{3,4},{3,5},{3,6},{3,7},{3,8},{3,9},
                                  {4,0},{4,1},{4,2},{4,3},{4,4},{4,5},{4,6},{4,7},{4,8},{4,9},
                                  {5,0},{5,1},{5,2},{5,3},{5,4},{5,5},{5,6},{5,7},{5,8},{5,9},
                                  {6,0},{6,1},{6,2},{6,3},{6,4},{6,5},{6,6},{6,7},{6,8},{6,9},
                                  {7,0},{7,1},{7,2},{7,3},{7,4},{7,5},{7,6},{7,7},{7,8},{7,9},
                                  {8,0},{8,1},{8,2},{8,3},{8,4},{8,5},{8,6},{8,7},{8,8},{8,9},
                                  {9,0},{9,1},{9,2},{9,3},{9,4},{9,5},{9,6},{9,7},{9,8},{9,9}};

        String[] invalidShots = {"00", "AA", "bb", "Cc", "10A", "0K", "0k", "11A", "7", "J", ""};
        int[] invalidCoords;

        // test isShotValid() and convertShotToNumberCoordinates()
        for (int s1 = 0; s1 < shotsVar1.length; s1++) {
            if (!m.isShotValid(shotsVar1[s1])) {
                System.out.println("Error: isShotValid() should have returned true for " + shotsVar1[s1] + ", but returned false");
                uErrors += 1;
            } else {}
            try {
                if (!Arrays.equals(m.convertShotToNumberCoordinates(shotsVar1[s1]), expectedCoords[s1])) {
                    System.out.println("Error: convertShotToNumberCoordinates() returned wrong coordinates for "
                            + shotsVar1[s1] + ", expected " + Arrays.toString(expectedCoords[s1]) + ", returned "
                            + Arrays.toString(m.convertShotToNumberCoordinates(shotsVar1[s1])));
                    uErrors += 1;
                } else {}
            }
            catch (Exception e) {
                System.out.println("Error: convertShotToNumberCoordinates() threw an exception for valid input");
                uErrors += 1;
            }
        }
        for (int s2 = 0; s2 < shotsVar2.length; s2++) {
            if (!m.isShotValid(shotsVar2[s2])) {
                System.out.println("Error: isShotValid() should have returned true for " + shotsVar2[s2] + ", but returned false");
                uErrors += 1;
            } else {}
            try {
                if (!Arrays.equals(m.convertShotToNumberCoordinates(shotsVar2[s2]), expectedCoords[s2])) {
                    System.out.println("Error: convertShotToNumberCoordinates() returned wrong coordinates for "
                            + shotsVar2[s2] + ", expected " + Arrays.toString(expectedCoords[s2]) + ", returned "
                            + Arrays.toString(m.convertShotToNumberCoordinates(shotsVar2[s2])));
                    uErrors += 1;
                } else {}
            }
            catch (Exception e) {
                System.out.println("Error: convertShotToNumberCoordinates() threw an exception for valid input");
                uErrors += 1;
            }
        }
        for (int s3 = 0; s3 < shotsVar3.length; s3++) {
            if (!m.isShotValid(shotsVar3[s3])) {
                System.out.println("Error: isShotValid() should have returned true for " + shotsVar3[s3] + ", but returned false");
                uErrors += 1;
            } else {}
            try {
                if (!Arrays.equals(m.convertShotToNumberCoordinates(shotsVar3[s3]), expectedCoords[s3])) {
                    System.out.println("Error: convertShotToNumberCoordinates() returned wrong coordinates for "
                            + shotsVar3[s3] + ", expected " + Arrays.toString(expectedCoords[s3]) + ", returned "
                            + Arrays.toString(m.convertShotToNumberCoordinates(shotsVar3[s3])));
                    uErrors += 1;
                } else {}
            }
            catch (Exception e) {
                System.out.println("Error: convertShotToNumberCoordinates() threw an exception for valid input");
                uErrors += 1;
            }
        }
        for (int s4 = 0; s4 < shotsVar4.length; s4++) {
            if (!m.isShotValid(shotsVar4[s4])) {
                System.out.println("Error: isShotValid() should have returned true for " + shotsVar4[s4] + ", but returned false");
                uErrors += 1;
            } else {}
            try {
                if (!Arrays.equals(m.convertShotToNumberCoordinates(shotsVar4[s4]), expectedCoords[s4])) {
                    System.out.println("Error: convertShotToNumberCoordinates() returned wrong coordinates for "
                            + shotsVar4[s4] + ", expected " + Arrays.toString(expectedCoords[s4]) + ", returned "
                            + Arrays.toString(m.convertShotToNumberCoordinates(shotsVar4[s4])));
                    uErrors += 1;
                } else {}
            }
            catch (Exception e) {
                System.out.println("Error: convertShotToNumberCoordinates() threw an exception for valid input");
                uErrors += 1;
            }
        }
        for (String invalidShot : invalidShots) {
            if (m.isShotValid(invalidShot)) {
                System.out.println("Error: isShotValid() should have returned false for " + invalidShot + ", but returned true");
                uErrors += 1;
            } else {}
            try {
                invalidCoords = m.convertShotToNumberCoordinates(invalidShot);
                System.out.println("Error: convertShotToNumberCoordinates() returned " + Arrays.toString(invalidCoords) + " for invalid input " + invalidShot);
                uErrors += 1;
            } catch (Exception e) {
                // expected to catch exceptions here
            }
        }

        // test convertNumberCoordinatesToShot
        String convertedToShot;
        try {
            convertedToShot = m.convertNumberCoordinatesToShot(-1, 0);
            System.out.println("Error: convertNumberCoordinatesToShot() did not throw an exception for invalid input");
            uErrors += 1;
        }
        catch (Exception e) {
            // expected result
        }
        try {
            convertedToShot = m.convertNumberCoordinatesToShot(0, -1);
            System.out.println("Error: convertNumberCoordinatesToShot() did not throw an exception for invalid input");
            uErrors += 1;
        }
        catch (Exception e) {
            // expected result
        }
        try {
            convertedToShot = m.convertNumberCoordinatesToShot(10, 0);
            System.out.println("Error: convertNumberCoordinatesToShot() did not throw an exception for invalid input");
            uErrors += 1;
        }
        catch (Exception e) {
            // expected result
        }
        try {
            convertedToShot = m.convertNumberCoordinatesToShot(0, 10);
            System.out.println("Error: convertNumberCoordinatesToShot() did not throw an exception for invalid input");
            uErrors += 1;
        }
        catch (Exception e) {
            // expected result
        }
        for (int numc = 0; numc < expectedCoords.length; numc++) {
            try {
                convertedToShot = m.convertNumberCoordinatesToShot(expectedCoords[numc][0], expectedCoords[numc][1]);
                if (!Objects.equals(convertedToShot, shotsVar1[numc])) {
                    System.out.println("Error: convertNumberCoordinatesToShot() returned wrong shot for "
                    + Arrays.toString(expectedCoords[numc]) + ", expected " + shotsVar1[numc] + ", returned "
                    + convertedToShot);
                    uErrors += 1;
                } else {}
            }
            catch (Exception e) {
                System.out.println("Error: convertNumberCoordinatesToShot() threw an exception for valid input");
                uErrors += 1;
            }
        }

        // test displayShipsOnMap(), displayShotsOnMap(), and displayShipsAndShotsOnMap() with empty Maps
        System.out.println("Here is a map of the ships, with no ships");
        System.out.println(m.displayShipsOnMap());
        System.out.println("Here is a map of the shots, with no shots");
        System.out.println(m.displayShotsOnMap());
        System.out.println("Here is a map of the ships and shots, with no ships or shots");
        System.out.println(m.displayShipsAndShotsOnMap());

        // test willShipFit() without any Ships added
        if (m.willShipFit(1, true, 0, 0)) {
            System.out.println("Error: willShipFit() returned true for a Ship of invalid size, less than 2");
            uErrors += 1;
        } else {}
        if (m.willShipFit(6, true, 0, 0)) {
            System.out.println("Error: willShipFit() returned true for a Ship of invalid size, greater than 5");
            uErrors += 1;
        } else {}
        if (m.willShipFit(2, true, -1, 0)) {
            System.out.println("Error: willShipFit() returned true for a Ship whose row coordinate is less than 0");
            uErrors += 1;
        } else {}
        if (m.willShipFit(2, true, 0, -1)) {
            System.out.println("Error: willShipFit() returned true for a Ship whose column coordinate is less than 0");
            uErrors += 1;
        } else {}
        if (m.willShipFit(2, true, 10, 0)) {
            System.out.println("Error: willShipFit() returned true for a Ship whose row coordinate is greater than 9");
            uErrors += 1;
        } else {}
        if (m.willShipFit(2, true, 0, 10)) {
            System.out.println("Error: willShipFit() returned true for a Ship whose column coordinate is greater than 9");
            uErrors += 1;
        } else {}
        if (m.willShipFit(2, true, 9, 9)) {
            System.out.println("Error: willShipFit() returned true for a Ship that does not fit on the Map");
            uErrors += 1;
        } else {}
        if (m.willShipFit(5, true, 9, 6)) {
            System.out.println("Error: willShipFit() returned true for a Ship that does not fit on the Map");
            uErrors += 1;
        } else {}
        if (m.willShipFit(5, true, 6, 9)) {
            System.out.println("Error: willShipFit() returned true for a Ship that does not fit on the Map");
            uErrors += 1;
        } else {}
        if (!m.willShipFit(2, true, 0, 0)) {
            System.out.println("Error: willShipFit() returned false for a valid Ship that fits on the Map");
            uErrors += 1;
        } else {}
        if (!m.willShipFit(2, true, 9, 8)) {
            System.out.println("Error: willShipFit() returned false for a valid Ship that fits on the Map");
            uErrors += 1;
        } else {}
        if (!m.willShipFit(2, false, 8, 9)) {
            System.out.println("Error: willShipFit() returned false for a valid Ship that fits on the Map");
            uErrors += 1;
        } else {}
        if (!m.willShipFit(5, true, 9, 5)) {
            System.out.println("Error: willShipFit() returned false for a valid Ship that fits on the Map");
            uErrors += 1;
        } else {}
        if (!m.willShipFit(5, false, 5, 9)) {
            System.out.println("Error: willShipFit() returned false for a valid Ship that fits on the Map");
            uErrors += 1;
        } else {}

        // test addShip()
        if (m.addShip(1, true, 0, 0)) {
            System.out.println("Error: addShip() returned true for a Ship of invalid size, less than 2");
            uErrors += 1;
        } else {}
        if (m.addShip(6, true, 0, 0)) {
            System.out.println("Error: addShip() returned true for a Ship of invalid size, greater than 5");
            uErrors += 1;
        } else {}
        if (m.addShip(2, true, -1, 0)) {
            System.out.println("Error: addShip() returned true for a Ship whose row coordinate is less than 0");
            uErrors += 1;
        } else {}
        if (m.addShip(2, true, 0, -1)) {
            System.out.println("Error: addShip() returned true for a Ship whose column coordinate is less than 0");
            uErrors += 1;
        } else {}
        if (m.addShip(2, true, 10, 0)) {
            System.out.println("Error: addShip() returned true for a Ship whose row coordinate is greater than 9");
            uErrors += 1;
        } else {}
        if (m.addShip(2, true, 0, 10)) {
            System.out.println("Error: addShip() returned true for a Ship whose column coordinate is greater than 9");
            uErrors += 1;
        } else {}
        if (m.addShip(2, true, 9, 9)) {
            System.out.println("Error: addShip() returned true for a Ship that does not fit on the Map");
            uErrors += 1;
        } else {}
        if (m.addShip(5, true, 9, 6)) {
            System.out.println("Error: addShip() returned true for a Ship that does not fit on the Map");
            uErrors += 1;
        } else {}
        if (m.addShip(5, true, 6, 9)) {
            System.out.println("Error: addShip() returned true for a Ship that does not fit on the Map");
            uErrors += 1;
        } else {}
        if (!m.addShip(5, false, 5, 9)) {
            System.out.println("Error: addShip() returned false for a Ship that fits on the Map");
            uErrors += 1;
        } else {}

        // test addShipsRandomly() with a Ship added
        int randomAttempt = m.addShipsRandomly();
        if (randomAttempt != -1) {
            System.out.println("Error: addShipsRandomly() should have returned -1 for being called without " +
                    "meeting the precondition, but returned " + randomAttempt);
            uErrors += 1;
        } else {}

        // continue testing addShip()
        if (!m.addShip(4, true, 2, 6)) {
            System.out.println("Error: addShip() returned false for a Ship that fits on the Map");
            uErrors += 1;
        } else {}
        if (!m.addShip(3, false, 1, 3)) {
            System.out.println("Error: addShip() returned false for a Ship that fits on the Map");
            uErrors += 1;
        } else {}
        if (!m.addShip(2, true, 0, 0)) {
            System.out.println("Error: addShip() returned false for a Ship that fits on the Map");
            uErrors += 1;
        } else {}
        if (!m.addShip(2, true, 9, 0)) {
            System.out.println("Error: addShip() returned false for a Ship that fits on the Map");
            uErrors += 1;
        } else {}

        // test displayShipsOnMap()
        System.out.println("Here is a Map with five out of ten Ships added");
        System.out.println(m.displayShipsOnMap());

        // test willShipFit() with Ships added
        if (m.willShipFit(2, true, 0, 0)) {
            System.out.println("Error: willShipFit() returned true for a Ship that does not fit on the Map because another Ship is in the way");
            uErrors += 1;
        } else {}
        if (m.willShipFit(2, true, 0, 1)) {
            System.out.println("Error: willShipFit() returned true for a Ship that does not fit on the Map because another Ship is in the way");
            uErrors += 1;
        } else {}
        if (m.willShipFit(5, true, 9, 5)) {
            System.out.println("Error: willShipFit() returned true for a Ship that does not fit on the Map because another Ship is in the way");
            uErrors += 1;
        } else {}
        if (m.willShipFit(3, false, 1, 8)) {
            System.out.println("Error: willShipFit() returned true for a Ship that does not fit on the Map because another Ship is in the way");
            uErrors += 1;
        } else {}

        // test addShip() with Ships added
        if (m.addShip(2, true, 0, 0)) {
            System.out.println("Error: addShip() returned true for a Ship that does not fit on the Map because another Ship is in the way");
            uErrors += 1;
        } else {}
        if (m.addShip(2, true, 0, 1)) {
            System.out.println("Error: addShip() returned true for a Ship that does not fit on the Map because another Ship is in the way");
            uErrors += 1;
        } else {}
        if (m.addShip(5, true, 9, 5)) {
            System.out.println("Error: addShip() returned true for a Ship that does not fit on the Map because another Ship is in the way");
            uErrors += 1;
        } else {}
        if (m.addShip(3, false, 1, 8)) {
            System.out.println("Error: addShip() returned true for a Ship that does not fit on the Map because another Ship is in the way");
            uErrors += 1;
        } else {}
        if (!m.addShip(4, true, 4, 0)) {
            System.out.println("Error: addShip() returned false for a Ship that fits on the Map");
            uErrors += 1;
        } else {}
        if (!m.addShip(3, true, 7, 6)) {
            System.out.println("Error: addShip() returned false for a Ship that fits on the Map");
            uErrors += 1;
        } else {}
        if (!m.addShip(3, false, 1, 1)) {
            System.out.println("Error: addShip() returned false for a Ship that fits on the Map");
            uErrors += 1;
        } else {}
        if (!m.addShip(2, false, 5, 5)) {
            System.out.println("Error: addShip() returned false for a Ship that fits on the Map");
            uErrors += 1;
        } else {}
        if (!m.addShip(2, true, 0, 8)) {
            System.out.println("Error: addShip() returned false for a Ship that fits on the Map");
            uErrors += 1;
        } else {}
        if (m.addShip(2, true, 8, 0)) {
            System.out.println("Error: addShip() returned true even though there are already ten Ships on the Map");
            uErrors += 1;
        } else {}
        System.out.println("Here is a Map with ten out of ten Ships added");
        System.out.println(m.displayShipsOnMap());

        // test alreadyShotHere(int, int) with no shots
        try {
            m.alreadyShotHere(-1, 0);
            System.out.println("Error: alreadyShotHere() did not throw an exception for invalid input, row coordinate less than 0");
            uErrors += 1;
        }
        catch (Exception e) {
            // expected
        }
        try {
            m.alreadyShotHere(0, -1);
            System.out.println("Error: alreadyShotHere() did not throw an exception for invalid input, column coordinate less than 0");
            uErrors += 1;
        }
        catch (Exception e) {
            // expected
        }
        try {
            m.alreadyShotHere(10, 0);
            System.out.println("Error: alreadyShotHere() did not throw an exception for invalid input, row coordinate greater than 10");
            uErrors += 1;
        }
        catch (Exception e) {
            // expected
        }
        try {
            m.alreadyShotHere(0, 10);
            System.out.println("Error: alreadyShotHere() did not throw an exception for invalid input, column coordinate greater than 10");
            uErrors += 1;
        }
        catch (Exception e) {
            // expected
        }
        try {
            if (m.alreadyShotHere(0, 0)) {
                System.out.println("Error: alreadyShotHere() returned true for a spot on the Map that was not shot at");
                uErrors += 1;
            } else {}
        }
        catch (Exception e) {
            System.out.println("Error: alreadyShotHere() threw an exception for valid input");
            uErrors += 1;
        }
        try {
            if (m.alreadyShotHere(9, 9)) {
                System.out.println("Error: alreadyShotHere() returned true for a spot on the Map that was not shot at");
                uErrors += 1;
            } else {}
        }
        catch (Exception e) {
            System.out.println("Error: alreadyShotHere() threw an exception for valid input");
            uErrors += 1;
        }

        // test alreadyShotHere(String) with no shots
        for (String inv : invalidShots) {
            try {
                boolean alreadyShot = m.alreadyShotHere(inv);
                System.out.println("Error: alreadyShotHere() did not throw an exception for invalid input");
                uErrors += 1;
            }
            catch (Exception e) {
                // expected
            }
        }
        try {
            if (m.alreadyShotHere("0A")) {
                System.out.println("Error: alreadyShotHere() returned true for a spot on the Map that was not shot at");
                uErrors += 1;
            } else {}
        }
        catch (Exception e) {
            System.out.println("Error: alreadyShotHere() threw an exception for valid input");
            uErrors += 1;
        }
        try {
            if (m.alreadyShotHere("9J")) {
                System.out.println("Error: alreadyShotHere() returned true for a spot on the Map that was not shot at");
                uErrors += 1;
            } else {}
        }
        catch (Exception e) {
            System.out.println("Error: alreadyShotHere() threw an exception for valid input");
            uErrors += 1;
        }

        // test shoot()
        String[] shootMessages = {"That is not a valid location.",
                                  "Miss.",
                                  "Hit!",
                                  "You already shot here.",
                                  "Hit!\nSunk a ship of length 5.",
                                  "Hit!\nSunk a ship of length 4.",
                                  "Hit!\nSunk a ship of length 3.",
                                  "Hit!\nSunk a ship of length 2."};
        String message = m.shoot("A");
        if (!message.equals(shootMessages[0])) {
            System.out.println("Error: shoot() displayed the wrong message.");
            System.out.println("Expected: " + shootMessages[0]);
            System.out.println("Returned: " + message);
            uErrors += 1;
        } else {}
        message = m.shoot("111");
        if (!message.equals(shootMessages[0])) {
            System.out.println("Error: shoot() displayed the wrong message.");
            System.out.println("Expected: " + shootMessages[0]);
            System.out.println("Returned: " + message);
            uErrors += 1;
        } else {}
        message = m.shoot("A");
        if (!message.equals(shootMessages[0])) {
            System.out.println("Error: shoot() displayed the wrong message.");
            System.out.println("Expected: " + shootMessages[0]);
            System.out.println("Returned: " + message);
            uErrors += 1;
        } else {}
        message = m.shoot("6A");
        if (!message.equals(shootMessages[1])) {
            System.out.println("Error: shoot() displayed the wrong message.");
            System.out.println("Expected: " + shootMessages[1]);
            System.out.println("Returned: " + message);
            uErrors += 1;
        } else {}
        message = m.shoot("6A");
        if (!message.equals(shootMessages[3])) {
            System.out.println("Error: shoot() displayed the wrong message.");
            System.out.println("Expected: " + shootMessages[3]);
            System.out.println("Returned: " + message);
            uErrors += 1;
        } else {}
        message = m.shoot("3D");
        if (!message.equals(shootMessages[2])) {
            System.out.println("Error: shoot() displayed the wrong message.");
            System.out.println("Expected: " + shootMessages[2]);
            System.out.println("Returned: " + message);
            uErrors += 1;
        } else {}
        message = m.shoot("3D");
        if (!message.equals(shootMessages[3])) {
            System.out.println("Error: shoot() displayed the wrong message.");
            System.out.println("Expected: " + shootMessages[3]);
            System.out.println("Returned: " + message);
            uErrors += 1;
        } else {}

        // test alreadyShotHere(int, int)
        if (!m.alreadyShotHere(6, 0)) {
            System.out.println("Error: alreadyShotHere() returned false for a spot on the Map that has been shot at");
            uErrors += 1;
        } else {}
        if (!m.alreadyShotHere(3, 3)) {
            System.out.println("Error: alreadyShotHere() returned false for a spot on the Map that has been shot at");
            uErrors += 1;
        } else {}

        // test alreadyShotHere(String)
        if (!m.alreadyShotHere("6A")) {
            System.out.println("Error: alreadyShotHere() returned false for a spot on the Map that has been shot at");
            uErrors += 1;
        } else {}
        if (!m.alreadyShotHere("3D")) {
            System.out.println("Error: alreadyShotHere() returned false for a spot on the Map that has been shot at");
            uErrors += 1;
        } else {}

        // test displayShotsOnMap()
        System.out.println("Here is a miss and a hit on the Map of the shots");
        System.out.println(m.displayShotsOnMap());

        // test displayShipsAndShotsOnMap()
        System.out.println("Here are the shots along with the Ships");
        System.out.println(m.displayShipsAndShotsOnMap());

        // test allShipsSunk() with no Ships sunk
        if (m.allShipsSunk()) {
            System.out.println("Error: allShipsSunk() returned true with all Ships remaining");
            uErrors += 1;
        } else {}

        // test shoot() and sink some Ships
        m.shoot("5J");
        m.shoot("6J");
        m.shoot("7J");
        m.shoot("8J");
        message = m.shoot("9J");
        if (!message.equals(shootMessages[4])) {
            System.out.println("Error: shoot() displayed the wrong message.");
            System.out.println("Expected: " + shootMessages[4]);
            System.out.println("Returned: " + message);
            uErrors += 1;
        } else {}
        m.shoot("4A");
        m.shoot("4B");
        m.shoot("4C");
        message = m.shoot("4D");
        if (!message.equals(shootMessages[5])) {
            System.out.println("Error: shoot() displayed the wrong message.");
            System.out.println("Expected: " + shootMessages[5]);
            System.out.println("Returned: " + message);
            uErrors += 1;
        } else {}
        m.shoot("2D");
        message = m.shoot("1D");
        if (!message.equals(shootMessages[6])) {
            System.out.println("Error: shoot() displayed the wrong message.");
            System.out.println("Expected: " + shootMessages[6]);
            System.out.println("Returned: " + message);
            uErrors += 1;
        } else {}
        m.shoot("0A");
        message = m.shoot("0B");
        if (!message.equals(shootMessages[7])) {
            System.out.println("Error: shoot() displayed the wrong message.");
            System.out.println("Expected: " + shootMessages[7]);
            System.out.println("Returned: " + message);
            uErrors += 1;
        } else {}

        // test allShipsSunk() with some Ships remaining
        if (m.allShipsSunk()) {
            System.out.println("Error: allShipsSunk() returned true with some Ships remaining");
            uErrors += 1;
        } else {}

        // test displayShotsOnMap() and displayShipsAndShotsOnMap() with some Ships sunk
        System.out.println("Here is the Map of the shots with some Ships sunk");
        System.out.println("Remember that sunk Ships on this Map will appear as numbers");
        System.out.println(m.displayShotsOnMap());
        System.out.println("Here are the shots along with the Ships");
        System.out.println("Remember that sunk Ships on this Map will appear as hit marks");
        System.out.println(m.displayShipsAndShotsOnMap());

        // test shoot() until all Ships have sunk
        m.shoot("1B");
        m.shoot("2B");
        m.shoot("3B");
        m.shoot("I0");
        m.shoot("J0");
        m.shoot("a9");
        m.shoot("b9");
        m.shoot("5f");
        m.shoot("6f");
        m.shoot("2G");
        m.shoot("2H");
        m.shoot("2I");
        m.shoot("2J");
        m.shoot("G7");
        m.shoot("H7");
        m.shoot("I7");

        // test allShipsSunk() when all Ships have sunk
        if (!m.allShipsSunk()) {
            System.out.println("Error: allShipsSunk() returned false when all Ships have sunk");
            uErrors += 1;
        } else {}

        // test displayShotsOnMap() and displayShipsAndShotsOnMap() with all Ships sunk
        System.out.println("Here is the Map of the shots with all Ships sunk");
        System.out.println("Remember that sunk Ships on this Map will appear as numbers");
        System.out.println(m.displayShotsOnMap());
        System.out.println("Here are the shots along with the Ships");
        System.out.println("Remember that sunk Ships on this Map will appear as hit marks");
        System.out.println(m.displayShipsAndShotsOnMap());

        // test getShipsOnMap
        Ship[][] shipsMap = m.getShipsOnMap();
        if (!shipsMap[1][1].hasShipSunk()) {
            System.out.println("Error: getShipsOnMap() did not correctly return shipsOnMap 2D array");
            uErrors += 1;
        } else {};

        // test addShipsRandomly()
        System.out.println("Now here comes the real test...");
        System.out.println("The computer will now randomly place Ships on a new Map...");
        Map compMap = new Map();
        int randomPlacement = compMap.addShipsRandomly();
        if (randomPlacement == 0) {
            System.out.println("Here is what the computer came up with");
            System.out.println(compMap.displayShipsOnMap());
        }
        else {
            System.out.println("Error: addShipsRandomly() should have returned 0 for being called with the " +
                    " precondition met, but returned " + randomPlacement);
            uErrors += 1;
        }

        // shoot at the random Map
        compMap.shoot("0A");
        compMap.shoot("0C");
        compMap.shoot("0E");
        compMap.shoot("0G");
        compMap.shoot("0I");
        compMap.shoot("1B");
        compMap.shoot("1D");
        compMap.shoot("1F");
        compMap.shoot("1H");
        compMap.shoot("1J");
        compMap.shoot("2A");
        compMap.shoot("2C");
        compMap.shoot("2E");
        compMap.shoot("2G");
        compMap.shoot("2I");
        compMap.shoot("3B");
        compMap.shoot("3D");
        compMap.shoot("3F");
        compMap.shoot("3H");
        compMap.shoot("3J");
        compMap.shoot("4A");
        compMap.shoot("4C");
        compMap.shoot("4E");
        compMap.shoot("4G");
        compMap.shoot("4I");
        compMap.shoot("5B");
        compMap.shoot("5D");
        compMap.shoot("5F");
        compMap.shoot("5H");
        compMap.shoot("5J");
        compMap.shoot("6A");
        compMap.shoot("6C");
        compMap.shoot("6E");
        compMap.shoot("6G");
        compMap.shoot("6I");
        compMap.shoot("7B");
        compMap.shoot("7D");
        compMap.shoot("7F");
        compMap.shoot("7H");
        compMap.shoot("7J");
        compMap.shoot("8A");
        compMap.shoot("8C");
        compMap.shoot("8E");
        compMap.shoot("8G");
        compMap.shoot("8I");
        compMap.shoot("9B");
        compMap.shoot("9D");
        compMap.shoot("9F");
        compMap.shoot("9H");
        compMap.shoot("9J");
        System.out.println("Here are some shots on the new Map");
        System.out.println(compMap.displayShotsOnMap());
        System.out.println("Here are the Ships along with the shots");
        System.out.println(compMap.displayShipsAndShotsOnMap());

        System.out.println("Testing complete with " + uErrors + " unintentional errors.");
    }
}
