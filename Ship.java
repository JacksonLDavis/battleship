/**
 * Code Written by Jackson L. Davis
 *
 * A Ship occupies anywhere from 2 to 5 spaces on a Map.
 * A Ship can be placed on the Map horizontally or vertically.
 */
public class Ship {
    private final int size;
    private final boolean horizontal; // is the Ship on the Map horizontally? true if yes, false if no

    // coordinates of the top or left side of the Ship
    private final int rowCoord;
    private final int colCoord;

    private int hits;

    /**
     * Constructor method for a new Ship object
     * @param sz the length of the Ship
     * @param horiz orientation of the Ship, true for horizontal, false for vertical
     * @param rowc row coordinate of the top or left side of the Ship
     * @param colc column coordinate of the top or left side of the Ship
     */
    public Ship(int sz, boolean horiz, int rowc, int colc) {
        if (sz < 2 || sz > 5) {
            throw new RuntimeException("Ship must have size of at least 2 and no greater than 5");
        }
        else {
            this.size = sz;
            this.horizontal = horiz;
            this.rowCoord = rowc;
            this.colCoord = colc;
            this.hits = 0;
        }
    }

    /**
     * @return the length of the Ship
     */
    public int getSize() {
        return this.size;
    }

    /**
     * @return the orientation of the Ship, true means horizontal, false means vertical
     */
    public boolean isHorizontal() {
        return this.horizontal;
    }

    /**
     * @return the row coordinate of the top or left side of the Ship
     */
    public int getRowCoordinate() {
        return this.rowCoord;
    }

    /**
     * @return the column coordinate of the top or left side of the Ship
     */
    public int getColumnCoordinate() {
        return this.colCoord;
    }

    /**
     * @return the number of hits the Ship has
     */
    public int getHits() {
        return this.hits;
    }

    /**
     * Add a hit to the Ship
     * @postcond hits increases by 1
     */
    public void addHit() {
        this.hits += 1;
    }

    /**
     * Determine if the Ship has sunk
     * @return true if the Ship has as many hits as its length, false otherwise
     */
    public boolean hasShipSunk() {
        return this.hits >= this.size;
    }

    public static void main(String[] args){
        System.out.println("Testing Ship.java");
        int uErrors = 0; // number of unintentional errors

        // test Ship() constructor with invalid size
        try {
            Ship tooShort = new Ship(1, true, 0, 0);
            System.out.println("Error: Ship() constructor did not throw an exception for a Ship that is too short.");
            uErrors += 1;
        }
        catch (Exception e) {
            // expected result
        }
        try {
            Ship tooLong = new Ship(6, true, 0, 0);
            System.out.println("Error: Ship() constructor did not throw an exception for a Ship that is too long.");
            uErrors += 1;
        }
        catch (Exception e) {
            // expected result
        }

        // test Ship() constructor with valid size
        try {
            Ship minSize = new Ship(2, true, 0, 0);
        }
        catch (Exception e) {
            System.out.println("Error: Ship() constructor threw an exception for a Ship with a valid size.");
            uErrors += 1;
        }
        try {
            Ship maxSize = new Ship(5, true, 0, 0);
        }
        catch (Exception e) {
            System.out.println("Error: Ship() constructor threw an exception for a Ship with a valid size.");
            uErrors += 1;
        }

        // assuming that the Ship() constructor works,
        // test getSize(), isHorizontal(), getRowCoordinate(), getColumnCoordinate(), getHits(), and hasShipSunk()
        Ship sizeTwo = new Ship(2, false, 3, 5);
        Ship sizeThree = new Ship(3, true, 1, 7);
        Ship sizeFour = new Ship(4, false, 6, 4);
        Ship sizeFive = new Ship(5, true, 9, 0);

        Ship[] testShips = {sizeTwo, sizeThree, sizeFour, sizeFive};
        int[] expectedSizes = {2, 3, 4, 5};
        boolean[] expectedOrientations = {false, true, false, true};
        int[] expectedRows = {3, 1, 6, 9};
        int[] expectedColumns = {5, 7, 4, 0};

        for (int i = 0; i < testShips.length; i++) {
            if (testShips[i].getSize() != expectedSizes[i]) {
                System.out.println("Error: getSize() returned the wrong size, expected " + expectedSizes[i] + ", returned " + testShips[i].getSize());
                uErrors += 1;
            } else {}
            if (testShips[i].isHorizontal() != expectedOrientations[i]) {
                System.out.println("Error: isHorizontal() returned the wrong orientation, expected " + expectedOrientations[i] + ", returned " + testShips[i].isHorizontal());
                uErrors += 1;
            } else {}
            if (testShips[i].getRowCoordinate() != expectedRows[i]) {
                System.out.println("Error: getRowCoordinate() returned the wrong row coordinate, expected " + expectedRows[i] + ", returned " + testShips[i].getRowCoordinate());
                uErrors += 1;
            } else {}
            if (testShips[i].getColumnCoordinate() != expectedColumns[i]) {
                System.out.println("Error: getColumnCoordinate() returned the wrong column coordinate, expected " + expectedColumns[i] + ", returned " + testShips[i].getColumnCoordinate());
                uErrors += 1;
            } else {}
            if (testShips[i].getHits() != 0) {
                System.out.println("Error: getHits() should return 0 for a new Ship, but returned " + testShips[i].getHits());
                uErrors += 1;
            } else {}
            if (testShips[i].hasShipSunk()) {
                System.out.println("Error: hasShipSunk() returned true for a new Ship that has not been hit");
                uErrors += 1;
            } else {}
        }

        // test addHit() along with getHits() and hasShipSunk()
        sizeTwo.addHit();
        if (sizeTwo.getHits() != 1) {
            System.out.println("Error: addHit() did not modify the number of hits the Ship has");
            uErrors += 1;
        } else {}
        if (sizeTwo.hasShipSunk()) {
            System.out.println("Error: hasShipSunk() returned true for a Ship has has fewer hits than its length");
            uErrors += 1;
        } else {}
        sizeTwo.addHit();
        if (!sizeTwo.hasShipSunk()) {
            System.out.println("Error: hasShipSunk() returned false for a Ship that has as many hits as its length");
            uErrors += 1;
        } else {}

        System.out.println("Testing complete with " + uErrors + " unintentional errors.");
    }
}
