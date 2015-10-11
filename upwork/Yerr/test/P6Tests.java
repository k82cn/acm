// CHANGELOG: 
// 
// Fri Apr 24 09:51:16 EDT 2015 : Minor update to correct some
// additional string constants space_fitsX tests and the
// impossible2.txt data file. The test_main() tests have been
// updated to simply verify that a fit is found or not rather than
// check for a specific solution.
// 
// Thu Apr 23 18:55:37 EDT 2015 : Several tests for main were named
// with "impossible" as in test_main10_impossible when they are very
// possible. These have ben renamed test_main10.
// 
// space_place_succeed6 and space_place_succeed7 had typos which
// resulted in a ragged layout for the space; this has been fixed
//
// Honors section tests have been added starting at 3824; ucomment
// them to enable

/**
 * Example of using unit tests for programming assignment 5.  This is
 * partially how your code will be graded.  Later in the class we will
 * write our own unit tests.  To run them on the command line, make
 * sure that the junit-cs211.jar is in the project directory.
 * <p/>
 * demo$ javac -cp .:junit-cs211.jar *.java     # compile everything
 * demo$ java  -cp .:junit-cs211.jar P6Tests    # run tests
 * <p/>
 * On windows replace : with ; (colon with semicolon)
 * demo$ javac -cp .;junit-cs211.jar *.java     # compile everything
 * demo$ java  -cp .;junit-cs211.jar P6Tests    # run tests
 */

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.Assert.*;

public class P6Tests {
    // Used to represent empty blocks in shapes and space layouts
    public static char empty = '.';
    // Filled block in spaces
    public static char filled = '|';
    public String testingDirectory = "TESTFILES";
    public File testDir;

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("P6Tests");
    }

    // Figure out the display character based on the layout string only
    public static char getDisplayChar(String layout) {
        for (int i = 0; i < layout.length(); i++) {
            char c = layout.charAt(i);
            if (c != empty && c != '\n') {
                return c;
            }
        }
        throw new RuntimeException(String.format("Bad layout:\n%s\n", layout));
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Shape tests

    // Verify that a fill pattern returned by isFilledAt(r,c) in the
    // shape actually matches the given layout string
    public static void verify_shape_fill(String layout, Shape shape) {
        int row = 0, col = 0, max_row = 0, max_col = 0;
        for (int i = 0; i < layout.length(); i++) {
            char c = layout.charAt(i);
            if (c == '\n') {
                max_row++;
                max_col = col;
                row++;
                col = 0;
                continue;
            }
            boolean expect = c != empty;
            boolean actual = shape.isFilledAt(row, col);
            if (expect != actual) {
                failFmt("\nFill pattern does not match:\nExpect filledAt(%s,%s): %s\nActual filledAt(%s,%s): %s\nLayout:\n%s\n%s\n",
                        row, col, expect, row, col, actual, layout, shape);
            }
            col++;
        }
        assertEquals("Height wrong", max_row, shape.getHeight());
        assertEquals("Width wrong", max_col, shape.getWidth());
    }

    public static void verify_rotations(String[] layouts, Shape shape) {
        Rotation[] rots = {Rotation.CW0, Rotation.CW90, Rotation.CW180, Rotation.CW270};
        for (int i = 0; i < rots.length; i++) {
            Rotation rot = rots[i];
            String layout = layouts[i];
            assertEquals(rot, shape.getRotation());
            verify_shape_fill(layout, shape);
            shape.rotateCW();
        }
        assertEquals(rots[0], shape.getRotation());
        verify_shape_fill(layouts[0], shape);
    }

    // Verify that a fill pattern returned by isFilledAt(r,c) in the
    // shape actually matches the given layout string
    public static void verify_space_fill(String layout, Space space) {
        int row = 0, col = 0, max_row = 0, max_col = 0;
        for (int i = 0; i < layout.length(); i++) {
            char c = layout.charAt(i);
            if (c == '\n') {
                max_row++;
                max_col = col;
                row++;
                col = 0;
                continue;
            }
            boolean expect = c != empty;
            boolean actual = space.isFilledAt(row, col);
            if (expect != actual) {
                failFmt("\nFill pattern does not match:\nExpect filledAt(%s,%s): %s\nActual filledAt(%s,%s): %s\nLayout:\n%s\n%s\n",
                        row, col, expect, row, col, actual, layout, space);
            }
            col++;
        }
        assertEquals("Height wrong", max_row, space.getHeight());
        assertEquals("Width wrong", max_col, space.getWidth());
    }

    // Convert a string to 2D array of characters
    public static char[][] string2char2D(String s) {
        String lines[] = s.split("\n");
        char c[][] = new char[lines.length][lines[0].length()];
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                c[i][j] = lines[i].charAt(j);
            }
        }
        return c;
    }

    // Check if a shape is in a space at the given space coordinate
    public static boolean shapeInSpaceAt(char space[][],
                                         char shape[][],
                                         int spR, int spC,
                                         boolean markBlocks, boolean blockOK[][]) {
        for (int shR = 0; shR < shape.length; shR++) {
            for (int shC = 0; shC < shape[shR].length; shC++) {
                char shChar = shape[shR][shC];
                int plR = spR + shR, plC = spC + shC;
                if (shChar != empty) {
                    if (plR >= space.length || plC >= space[plR].length) {
                        return false;       // Out of bounds
                    }
                    char spChar = space[plR][plC];
                    if (shChar != spChar) {
                        return false;       // Mismatch
                    }
                    // Match, mark if markBlocks is true
                    if (markBlocks) {
                        blockOK[plR][plC] = true;
                    }
                }
            }
        }
        // All matched
        return true;
    }

    // Verify a shape is in the space based on string searching; does
    // not do rotations
    public static boolean shapeIsInSpace(char space[][], String shapeString, boolean blockOK[][]) {
        char shape[][] = string2char2D(shapeString);
        for (int spR = 0; spR < space.length; spR++) {
            for (int spC = 0; spC < space[spR].length; spC++) {
                boolean result = shapeInSpaceAt(space, shape, spR, spC, false, null);
                if (result == true) {       // Shape found
                    // Redo computation marking each block the shape occupies as
                    // ok in blockOK
                    shapeInSpaceAt(space, shape, spR, spC, true, blockOK);

                    return true;
                }
            }
        }
        // Shape not found
        return false;
    }

    // Verify that all shapes are present in the given fit represented
    // as a string. The 2D array of shapeStrings contains one row for
    // each shape and each column represents a rotation of the shape as
    // a string. Raises failures if the fit has modified the original
    // filled blocks or if a shape is not present in the spec with a
    // valid rotation
    public static void verifyFit(String fitSpaceString,
                                 String emptySpaceString,
                                 String shapeStrings[][]) {

        char orig[][] = string2char2D(emptySpaceString);
        char space[][] = string2char2D(fitSpaceString);
        boolean blockOK[][] = new boolean[orig.length][orig[0].length];

        // Verify dimensions (shallowly)
        if (orig.length != space.length || orig[0].length != space[0].length) {
            String msg =
                    String.format("\nOriginal and Fit Space have different dimensions\nOriginal:%s\nFit:\n%s",
                            emptySpaceString, fitSpaceString);
            fail(msg);
        }

        // Verify that the pattern of filled blocks has not changed
        for (int i = 0; i < orig.length; i++) {
            for (int j = 0; j < orig[i].length; j++) {
                if (orig[i][j] == filled) {
                    if (space[i][j] != filled) { // Missing fill where it should be
                        String msg =
                                String.format("\nFilled block at (%d,%d) changed\nOriginal:%s\nFit:\n%s",
                                        i, j, emptySpaceString, fitSpaceString);
                        fail(msg);
                    }
                    blockOK[i][j] = true;   // Solution Correctly filled
                }
            }
        }

        // Verify every expected shape appears in the space
        for (String[] rotShapes : shapeStrings) {
            boolean found = false;
            for (String shapeString : rotShapes) {
                found |= shapeIsInSpace(space, shapeString, blockOK);
            }
            if (!found) {
                String rotationsStr = Arrays.toString(rotShapes).replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(", ", "\n");
                failFmt("\nShape not found in space\n%s\nShape Rotations:\n%s\n",
                        fitSpaceString, rotationsStr);
            }
        }

        // Verify that each block that is not marked OK is empty in the
        // solution; otherwise it contains garbage that is not accounted
        // for in any other way
        for (int i = 0; i < space.length; i++) {
            for (int j = 0; j < space[i].length; j++) {
                if (space[i][j] != empty && !blockOK[i][j]) {
                    failFmt("\nBlock (%d,%d) contains junk belonging to no shape or permanent fill\n%s",
                            i, j, fitSpaceString);
                }
            }
        }
    }

    public static String shape2dStr(String[][] as) {
        StringBuilder sb = new StringBuilder();
        for (String[] a : as) {
            sb.append("SHAPE (all rotations)\n");
            for (String s : a) {
                sb.append(s);
                sb.append('\n');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void failFmt(String fmt, Object... args) {
        fail(String.format(fmt, args));
    }

    public static void performSearchForFit(String sampleFit,
                                           String spaceLayout,
                                           String[]... shapeStrings) {
        // Verify that the sample fit passes
        try {
            if (sampleFit != null) {
                verifyFit(sampleFit, spaceLayout, shapeStrings);
            }
        } catch (Throwable e) {
            failFmt("\nBroken test: sampleFit was not verified\nsampleFit:\n%s\nspaceLayout:\n%s\nShape rotations:\n%s",
                    sampleFit, spaceLayout, shape2dStr(shapeStrings));
        }

        // Construct the list of hsapes to place
        Space space = FitIt.makeSpace(spaceLayout);
        String emptySpaceString = space.fitString();
        ArrayList<Shape> shapes = new ArrayList<Shape>();
        for (String[] shapeString : shapeStrings) {
            String shapeLayout = shapeString[0];
            char displayChar = empty;
            int ci = 0;
            while (displayChar == empty) {
                displayChar = shapeLayout.charAt(ci);
                ci++;
            }
            Shape shape = FitIt.makeShape(shapeLayout, displayChar);
            shapes.add(shape);
        }
        // Calculate student solution
        Space fitSpace = FitIt.searchForFit(space, shapes);
        boolean actualPossible = fitSpace != null;
        boolean expectPossible = sampleFit != null;

        if (actualPossible) {         // Produced a result, check it
            verifyFit(fitSpace.fitString(), emptySpaceString, shapeStrings);

            // Did someone find solution we didn't find?
            if (!expectPossible) {
                failFmt("%s\n%s\n%s\n",
                        "CONGRATULATIONS!\nYou found a fit which the instructors didn't. This is a bug in the test cases. Please report this to the instructors",
                        spaceLayout,
                        fitSpace);
            }
        } else if (expectPossible) {    // Didn't produce a result but should have
            failFmt("Returned null for fit but at least one fit exists.\nSample:\n%s\nOriginal Space:\n%s\nShapes:\n%s",
                    sampleFit, spaceLayout, shapes);
        }

    }

    // Read a whole file
    private static String slurp(String fname) throws Exception {
        return new Scanner(new File(fname), "UTF-8").useDelimiter("\\Z").next();
    }

    // Append strings as columns using space as the divider
    public static String appendColumns2(String all[]) {
        return appendColumns2(all, " ");
    }

    // Create a side-by-side diff of two strings compared line by line
    public static String simpleDiff2(String x, String y) {
        String xs[] = x.split("\n");
        String ys[] = y.split("\n");
        String sep = "      ";
        String dif = " **** ";
        StringBuilder sb = new StringBuilder();

        int maxWidth = 0;
        for (String s : xs) {
            maxWidth = s.length() > maxWidth ? s.length() : maxWidth;
        }
        for (String s : ys) {
            maxWidth = s.length() > maxWidth ? s.length() : maxWidth;
        }
        // Max width format
        String fmt = String.format("%%-%ds", maxWidth);

        // Construct the side-by-side diff
        for (int i = 0; i < xs.length || i < ys.length; i++) {
            if (i < xs.length && i < ys.length) { // both exist, compare
                sb.append(String.format(fmt, xs[i]));
                String mid = xs[i].equals(ys[i]) ? sep : dif;
                sb.append(mid);
                sb.append(String.format(fmt, ys[i]));
                sb.append("\n");
            } else if (i < xs.length) {     // only x left
                sb.append(String.format(fmt, xs[i]));
                sb.append(dif);
                sb.append(String.format(fmt, ""));
                sb.append("\n");
            } else if (i < ys.length) {     // only y left
                sb.append(String.format(fmt, ""));
                sb.append(dif);
                sb.append(String.format(fmt, ys[i]));
                sb.append("\n");
            } else {
                throw new RuntimeException("Something fishy's going on here...");
            }
        }
        return sb.toString();
    }

    // Append string as columns using the provided divider between lines
    public static String appendColumns2(String all[], String divider) {
        String allCols[][] = new String[all.length][];
        int widths[] = new int[all.length];
        int rowCounts[] = new int[all.length];
        for (int col = 0; col < all.length; col++) {
            widths[col] = 1;            // Can't have %0s formats
            allCols[col] = all[col].split("\n");
            for (int row = 0; row < allCols[col].length; row++) {
                int len = allCols[col][row].length();
                widths[col] = len > widths[col] ? len : widths[col];
            }
        }
        String formats[] = new String[all.length];
        int maxRow = 0;
        for (int col = 0; col < all.length; col++) {
            String div = col < all.length - 1 ? divider : "\n";
            formats[col] = String.format("%%-%ds%s", widths[col], div);
            maxRow = maxRow < allCols[col].length ? allCols[col].length : maxRow;
        }
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < all.length; col++) {
                String fill = "";
                if (row < allCols[col].length) {
                    fill = allCols[col][row];
                }
                sb.append(String.format(formats[col], fill));
            }
        }
        return sb.toString();
    }

    @Test(timeout = 1000, expected = FitItException.class)
    public void bad_shape1() {
        String layout =
                "b.\n" +
                        "b.\n" +
                        "b.\n" +
                        "";
        Shape shape = FitIt.makeShape(layout, getDisplayChar(layout));
    }

    @Test(timeout = 1000, expected = FitItException.class)
    public void bad_shape2() {
        String layout =
                ".b\n" +
                        ".b\n" +
                        "";
        Shape shape = FitIt.makeShape(layout, getDisplayChar(layout));
    }

    @Test(timeout = 1000, expected = FitItException.class)
    public void bad_shape3() {
        String layout =
                "bbbbb\n" +
                        "bb..b\n" +
                        ".....\n" +
                        "";
        Shape shape = FitIt.makeShape(layout, getDisplayChar(layout));
    }

    @Test(timeout = 1000, expected = FitItException.class)
    public void bad_shape4() {
        String layout =
                ".....\n" +
                        "bbbbb\n" +
                        "bb..b\n" +
                        "bb..b\n" +
                        "";
        Shape shape = FitIt.makeShape(layout, getDisplayChar(layout));
    }

    @Test(timeout = 1000)
    public void make_shape1() {
        String layout =
                "bb\n" +
                        "b.\n" +
                        "b.\n" +
                        "";
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        assertEquals(dc, shape.getDisplayChar());
    }

    @Test(timeout = 1000)
    public void make_shape2() {
        String layout =
                "bbbbb\n" +
                        "bb..b\n" +
                        "bb..b\n" +
                        "";
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        assertEquals(dc, shape.getDisplayChar());
    }

    @Test(timeout = 1000)
    public void make_shape3() {
        String layout =
                "b....\n" +
                        ".....\n" +
                        "....b\n" +
                        "";
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        assertEquals(dc, shape.getDisplayChar());
    }

    @Test(timeout = 1000)
    public void shape_toString1() {
        String layout =
                "b....\n" +
                        ".....\n" +
                        "....b\n" +
                        "";
        String expect =
                "SHAPE b\n" +
                        "height: 3; width: 5; rotation: CW0\n" +
                        layout;
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        String actual = shape.toString();
        assertEquals(expect, actual);
    }

    @Test(timeout = 1000)
    public void shape_toString2_rot() {
        String layout =
                "b....\n" +
                        ".....\n" +
                        "....b\n" +
                        "";
        String expect =
                "SHAPE b\n" +
                        "height: 3; width: 5; rotation: CW180\n" +
                        layout;
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        shape.rotateCW();
        shape.rotateCW();
        String actual = shape.toString();
        assertEquals(expect, actual);
    }

    @Test(timeout = 1000)
    public void shape_toString3_rot() {
        String layout =
                "b....\n" +
                        ".....\n" +
                        "....b\n" +
                        "";
        String expect =
                "SHAPE b\n" +
                        "height: 5; width: 3; rotation: CW90\n" +
                        "..b\n" +
                        "...\n" +
                        "...\n" +
                        "...\n" +
                        "b..\n" +
                        "";
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        shape.rotateCW();
        String actual = shape.toString();
        assertEquals(expect, actual);
    }

    @Test(timeout = 1000)
    public void shape_toString4() {
        String layout =
                "rr\n" +
                        ".r\n" +
                        ".r\n" +
                        "";
        String expect =
                "SHAPE r\n" +
                        "height: 3; width: 2; rotation: CW0\n" +
                        layout;
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        String actual = shape.toString();
        assertEquals(expect, actual);
    }

    @Test(timeout = 1000)
    public void shape_toString5_rot() {
        String layout =
                "rr\n" +
                        ".r\n" +
                        ".r\n" +
                        "";
        String expect =
                "SHAPE r\n" +
                        "height: 2; width: 3; rotation: CW270\n" +
                        "rrr\n" +
                        "r..\n" +
                        "";
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        shape.rotateCW();
        shape.rotateCW();
        shape.rotateCW();
        String actual = shape.toString();
        assertEquals(expect, actual);
    }

    @Test(timeout = 1000)
    public void shape_toString6() {
        String layout =
                "dddddddddd\n" +
                        "......d...\n" +
                        "......dd..\n" +
                        "";
        String expect =
                "SHAPE d\n" +
                        "height: 3; width: 10; rotation: CW0\n" +
                        "dddddddddd\n" +
                        "......d...\n" +
                        "......dd..\n" +
                        "";
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        String actual = shape.toString();
        assertEquals(expect, actual);
    }

    @Test(timeout = 1000)
    public void shape_toString7_rot() {
        String layout =
                "dddddddddd\n" +
                        "......d...\n" +
                        "......dd..\n" +
                        "";
        String expect =
                "SHAPE d\n" +
                        "height: 10; width: 3; rotation: CW90\n" +
                        "..d\n" +
                        "..d\n" +
                        "..d\n" +
                        "..d\n" +
                        "..d\n" +
                        "..d\n" +
                        "ddd\n" +
                        "d.d\n" +
                        "..d\n" +
                        "..d\n" +
                        "";
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        shape.rotateCW();
        String actual = shape.toString();
        assertEquals(expect, actual);
    }

    @Test(timeout = 1000)
    public void shape_displayChar1() {
        String layout =
                "b....\n" +
                        ".....\n" +
                        "....b\n" +
                        "";
        String expect =
                "SHAPE x\n" +
                        "height: 3; width: 5; rotation: CW0\n" +
                        "x....\n" +
                        ".....\n" +
                        "....x\n" +
                        "";
        char newDC = 'x';
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        shape.setDisplayChar(newDC);
        assertEquals(newDC, shape.getDisplayChar());
        String actual = shape.toString();
        assertEquals(expect, actual);
    }

    @Test(timeout = 1000)
    public void shape_displayChar2_rot() {
        String layout =
                "b....\n" +
                        ".....\n" +
                        "....b\n" +
                        "";
        String expect =
                "SHAPE z\n" +
                        "height: 5; width: 3; rotation: CW90\n" +
                        "..z\n" +
                        "...\n" +
                        "...\n" +
                        "...\n" +
                        "z..\n" +
                        "";
        char newDC = 'z';
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        shape.setDisplayChar(newDC);
        shape.rotateCW();
        assertEquals(newDC, shape.getDisplayChar());
        String actual = shape.toString();
        assertEquals(expect, actual);
    }

    @Test(timeout = 1000)
    public void shape_displayChar3_rot() {
        String layout =
                "b....\n" +
                        ".....\n" +
                        "....b\n" +
                        "";
        String expect =
                "SHAPE z\n" +
                        "height: 5; width: 3; rotation: CW90\n" +
                        "..z\n" +
                        "...\n" +
                        "...\n" +
                        "...\n" +
                        "z..\n" +
                        "";
        char newDC = 'z';
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        shape.rotateCW();
        shape.setDisplayChar(newDC);
        assertEquals(newDC, shape.getDisplayChar());
        String actual = shape.toString();
        assertEquals(expect, actual);
    }

    @Test(timeout = 1000)
    public void shape_displayChar4() {
        String layout =
                "dddddddddd\n" +
                        "......d...\n" +
                        "......dd..\n" +
                        "";
        String expect =
                "SHAPE @\n" +
                        "height: 3; width: 10; rotation: CW0\n" +
                        "@@@@@@@@@@\n" +
                        "......@...\n" +
                        "......@@..\n" +
                        "";
        char newDC = '@';
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        shape.setDisplayChar(newDC);
        assertEquals(newDC, shape.getDisplayChar());
        String actual = shape.toString();
        assertEquals(expect, actual);
    }

    @Test(timeout = 1000)
    public void shape_displayChar5_rot() {
        String layout =
                "dddddddddd\n" +
                        "......d...\n" +
                        "......dd..\n" +
                        "";
        String expect =
                "SHAPE @\n" +
                        "height: 10; width: 3; rotation: CW270\n" +
                        "@..\n" +
                        "@..\n" +
                        "@.@\n" +
                        "@@@\n" +
                        "@..\n" +
                        "@..\n" +
                        "@..\n" +
                        "@..\n" +
                        "@..\n" +
                        "@..\n" +
                        "";
        char newDC = '@';
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        shape.rotateCW();
        shape.setDisplayChar('R');
        shape.rotateCW();
        shape.setDisplayChar('5');
        shape.rotateCW();
        shape.setDisplayChar(newDC);
        assertEquals(newDC, shape.getDisplayChar());
        String actual = shape.toString();
        assertEquals(expect, actual);
    }

    @Test(timeout = 1000)
    public void shape_basic_fill1() {
        String layout =
                "bb\n" +
                        "b.\n" +
                        "b.\n" +
                        "";
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        verify_shape_fill(layout, shape);
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Space tests

    @Test(timeout = 1000)
    public void shape_basic_fill2() {
        String layout =
                "x\n" +
                        "x\n" +
                        "";
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        verify_shape_fill(layout, shape);
    }

    @Test(timeout = 1000)
    public void shape_basic_fill3() {
        String layout =
                "c..c\n" +
                        "....\n" +
                        "c..c\n" +
                        "";
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        verify_shape_fill(layout, shape);
    }

    @Test(timeout = 1000)
    public void shape_basic_fill4() {
        String layout =
                "..hh\n" +
                        "hhh.\n" +
                        "..hh\n" +
                        "";
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        verify_shape_fill(layout, shape);
    }

    @Test(timeout = 1000)
    public void shape_basic_fill5() {
        String layout =
                "..aa\n" +
                        "..aa\n" +
                        "...a\n" +
                        "...a\n" +
                        "...a\n" +
                        "...a\n" +
                        "...a\n" +
                        "aaaa\n" +
                        "aaaa\n" +
                        "...a\n" +
                        "...a\n" +
                        "...a\n" +
                        "...a\n" +
                        "...a\n" +
                        "...a\n" +
                        "...a\n" +
                        "...a\n" +
                        "";
        char dc = getDisplayChar(layout);
        Shape shape = FitIt.makeShape(layout, dc);
        verify_shape_fill(layout, shape);
    }

    @Test(timeout = 1000)
    public void shape_rotations1() {
        String[] layouts =
                {"mmm\n" +
                        "",
                        "m\n" +
                                "m\n" +
                                "m\n" +
                                "",
                        "mmm\n" +
                                "",
                        "m\n" +
                                "m\n" +
                                "m\n" +
                                "",
                };
        char dc = getDisplayChar(layouts[0]);
        Shape shape = FitIt.makeShape(layouts[0], dc);
        verify_rotations(layouts, shape);
    }

    @Test(timeout = 1000)
    public void shape_rotations2() {
        String[] layouts =
                {"pp.\n" +
                        "..p\n" +
                        "",
                        ".p\n" +
                                ".p\n" +
                                "p.\n" +
                                "",
                        "p..\n" +
                                ".pp\n" +
                                "",
                        ".p\n" +
                                "p.\n" +
                                "p.\n" +
                                "",
                };
        char dc = getDisplayChar(layouts[0]);
        Shape shape = FitIt.makeShape(layouts[0], dc);
        verify_rotations(layouts, shape);
    }

    @Test(timeout = 1000)
    public void shape_rotations3() {
        String[] layouts =
                {"rr\n" +
                        ".r\n" +
                        ".r\n" +
                        "",
                        "..r\n" +
                                "rrr\n" +
                                "",
                        "r.\n" +
                                "r.\n" +
                                "rr\n" +
                                "",
                        "rrr\n" +
                                "r..\n" +
                                "",
                };
        char dc = getDisplayChar(layouts[0]);
        Shape shape = FitIt.makeShape(layouts[0], dc);
        verify_rotations(layouts, shape);
    }

    @Test(timeout = 1000)
    public void shape_rotations4() {
        String[] layouts =
                {"c.c\n" +
                        "...\n" +
                        "...\n" +
                        "c.c\n" +
                        "",
                        "c..c\n" +
                                "....\n" +
                                "c..c\n" +
                                "",
                        "c.c\n" +
                                "...\n" +
                                "...\n" +
                                "c.c\n" +
                                "",
                        "c..c\n" +
                                "....\n" +
                                "c..c\n" +
                                "",
                };
        char dc = getDisplayChar(layouts[0]);
        Shape shape = FitIt.makeShape(layouts[0], dc);
        verify_rotations(layouts, shape);
    }

    @Test(timeout = 1000)
    public void shape_rotations5() {
        String[] layouts =
                {"dddddddddd\n" +
                        "......d...\n" +
                        "......dd..\n" +
                        "",
                        "..d\n" +
                                "..d\n" +
                                "..d\n" +
                                "..d\n" +
                                "..d\n" +
                                "..d\n" +
                                "ddd\n" +
                                "d.d\n" +
                                "..d\n" +
                                "..d\n" +
                                "",
                        "..dd......\n" +
                                "...d......\n" +
                                "dddddddddd\n" +
                                "",
                        "d..\n" +
                                "d..\n" +
                                "d.d\n" +
                                "ddd\n" +
                                "d..\n" +
                                "d..\n" +
                                "d..\n" +
                                "d..\n" +
                                "d..\n" +
                                "d..\n" +
                                "",
                };
        char dc = getDisplayChar(layouts[0]);
        Shape shape = FitIt.makeShape(layouts[0], dc);
        verify_rotations(layouts, shape);
    }

    @Test(timeout = 1000)
    public void shape_rotations6() {
        String[] layouts =
                {".jjjjjjjjjj....\n" +
                        "jj.............\n" +
                        "jjjjjjjjj......\n" +
                        "........jjj....\n" +
                        "...........jj..\n" +
                        "............jjj\n" +
                        "...........jj..\n" +
                        "",
                        "....jj.\n" +
                                "....jjj\n" +
                                "....j.j\n" +
                                "....j.j\n" +
                                "....j.j\n" +
                                "....j.j\n" +
                                "....j.j\n" +
                                "....j.j\n" +
                                "...jj.j\n" +
                                "...j..j\n" +
                                "...j..j\n" +
                                "j.j....\n" +
                                "jjj....\n" +
                                ".j.....\n" +
                                ".j.....\n" +
                                "",
                        "..jj...........\n" +
                                "jjj............\n" +
                                "..jj...........\n" +
                                "....jjj........\n" +
                                "......jjjjjjjjj\n" +
                                ".............jj\n" +
                                "....jjjjjjjjjj.\n" +
                                "",
                        ".....j.\n" +
                                ".....j.\n" +
                                "....jjj\n" +
                                "....j.j\n" +
                                "j..j...\n" +
                                "j..j...\n" +
                                "j.jj...\n" +
                                "j.j....\n" +
                                "j.j....\n" +
                                "j.j....\n" +
                                "j.j....\n" +
                                "j.j....\n" +
                                "j.j....\n" +
                                "jjj....\n" +
                                ".jj....\n" +
                                "",
                };
        char dc = getDisplayChar(layouts[0]);
        Shape shape = FitIt.makeShape(layouts[0], dc);
        verify_rotations(layouts, shape);
    }

    @Test(timeout = 1000)
    public void shape_rotations7() {
        String[] layouts =
                {"........l...\n" +
                        "........ll..\n" +
                        "ll.......ll.\n" +
                        "ll........l.\n" +
                        "ll........l.\n" +
                        "llllllllllll\n" +
                        "llllllllllll\n" +
                        "llllllllllll\n" +
                        "ll..........\n" +
                        "ll..........\n" +
                        "ll..........\n" +
                        "",
                        "lllllllll..\n" +
                                "lllllllll..\n" +
                                "...lll.....\n" +
                                "...lll.....\n" +
                                "...lll.....\n" +
                                "...lll.....\n" +
                                "...lll.....\n" +
                                "...lll.....\n" +
                                "...lll...ll\n" +
                                "...lll..ll.\n" +
                                "...llllll..\n" +
                                "...lll.....\n" +
                                "",
                        "..........ll\n" +
                                "..........ll\n" +
                                "..........ll\n" +
                                "llllllllllll\n" +
                                "llllllllllll\n" +
                                "llllllllllll\n" +
                                ".l........ll\n" +
                                ".l........ll\n" +
                                ".ll.......ll\n" +
                                "..ll........\n" +
                                "...l........\n" +
                                "",
                        ".....lll...\n" +
                                "..llllll...\n" +
                                ".ll..lll...\n" +
                                "ll...lll...\n" +
                                ".....lll...\n" +
                                ".....lll...\n" +
                                ".....lll...\n" +
                                ".....lll...\n" +
                                ".....lll...\n" +
                                ".....lll...\n" +
                                "..lllllllll\n" +
                                "..lllllllll\n" +
                                "",
                };
        char dc = getDisplayChar(layouts[0]);
        Shape shape = FitIt.makeShape(layouts[0], dc);
        verify_rotations(layouts, shape);
    }

    @Test(timeout = 1000)
    public void shape_rotations8() {
        String[] layouts =
                {"22.......222\n" +
                        "222......222\n" +
                        "2222......22\n" +
                        "2222......22\n" +
                        "22.22.....22\n" +
                        "22.222....22\n" +
                        "22..222...22\n" +
                        "22...22...22\n" +
                        "22....22..22\n" +
                        "22.....2..22\n" +
                        "22.....22222\n" +
                        "22......2222\n" +
                        "22.......222\n" +
                        "",
                        "2222222222222\n" +
                                "2222222222222\n" +
                                ".........222.\n" +
                                ".......2222..\n" +
                                "......222....\n" +
                                ".....222.....\n" +
                                "....222......\n" +
                                "..222........\n" +
                                ".22..........\n" +
                                "222........22\n" +
                                "2222222222222\n" +
                                "2222222222222\n" +
                                "",
                        "222.......22\n" +
                                "2222......22\n" +
                                "22222.....22\n" +
                                "22..2.....22\n" +
                                "22..22....22\n" +
                                "22...22...22\n" +
                                "22...222..22\n" +
                                "22....222.22\n" +
                                "22.....22.22\n" +
                                "22......2222\n" +
                                "22......2222\n" +
                                "222......222\n" +
                                "222.......22\n" +
                                "",
                        "2222222222222\n" +
                                "2222222222222\n" +
                                "22........222\n" +
                                "..........22.\n" +
                                "........222..\n" +
                                "......222....\n" +
                                ".....222.....\n" +
                                "....222......\n" +
                                "..2222.......\n" +
                                ".222.........\n" +
                                "2222222222222\n" +
                                "2222222222222\n" +
                                "",
                };
        char dc = getDisplayChar(layouts[0]);
        Shape shape = FitIt.makeShape(layouts[0], dc);
        verify_rotations(layouts, shape);
    }

    @Test(timeout = 1000)
    public void make_space1() {
        String layout =
                "...\n" +
                        "...\n" +
                        "";
        int height = 2, width = 3;
        Space space = FitIt.makeSpace(layout);
        assertEquals(height, space.getHeight());
        assertEquals(width, space.getWidth());
    }

    @Test(timeout = 1000)
    public void make_space2() {
        String layout =
                "...\n" +
                        "...\n" +
                        "...\n" +
                        "...\n" +
                        "...\n" +
                        "";
        int height = 5, width = 3;
        Space space = FitIt.makeSpace(layout);
        assertEquals(height, space.getHeight());
        assertEquals(width, space.getWidth());
    }

    @Test(timeout = 1000)
    public void make_space3() {
        String layout =
                "||..\n" +
                        ".|..\n" +
                        ".||.\n" +
                        ".|..\n" +
                        ".|..\n" +
                        "";
        int height = 5, width = 4;
        Space space = FitIt.makeSpace(layout);
        assertEquals(height, space.getHeight());
        assertEquals(width, space.getWidth());
    }

    @Test(timeout = 1000)
    public void space_fill1() {
        String layout =
                "...\n" +
                        "...\n" +
                        "";
        Space space = FitIt.makeSpace(layout);
        verify_space_fill(layout, space);
    }

    @Test(timeout = 1000)
    public void space_fill2() {
        String layout =
                "||..\n" +
                        ".|..\n" +
                        ".||.\n" +
                        ".|..\n" +
                        ".|..\n" +
                        "";
        Space space = FitIt.makeSpace(layout);
        verify_space_fill(layout, space);
    }

    @Test(timeout = 1000)
    public void space_fill3() {
        String layout =
                "||||||\n" +
                        "||||||\n" +
                        "||||||\n" +
                        "||||||\n" +
                        "||||||\n" +
                        "";
        Space space = FitIt.makeSpace(layout);
        verify_space_fill(layout, space);
    }

    @Test(timeout = 1000)
    public void space_fill4() {
        String layout =
                "|....|\n" +
                        "......\n" +
                        "..||..\n" +
                        "......\n" +
                        "|....|\n" +
                        "";
        Space space = FitIt.makeSpace(layout);
        verify_space_fill(layout, space);
    }

    @Test(timeout = 1000)
    public void space_fits1() {
        String shLayout =
                "aaa\n" +
                        "";
        int row = 0, col = 0;
        String spLayout =
                "|....|\n" +
                        "......\n" +
                        "..||..\n" +
                        "......\n" +
                        "|....|\n" +
                        "";
        boolean expect = false;
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        boolean actual = space.shapeFitsAt(row, col, shape);
        if (actual != expect) {
            failFmt("\nshapeFitsAt() problem\nExpect: %s\nActual: %s\nrow: %s; col %s\n\n%s\n%s",
                    expect, actual, row, col, shape, space);
        }
    }

    @Test(timeout = 1000)
    public void space_fits2() {
        String shLayout =
                "aaa\n" +
                        "";
        int row = 0, col = 1;
        String spLayout =
                "|....|\n" +
                        "......\n" +
                        "..||..\n" +
                        "......\n" +
                        "|....|\n" +
                        "";
        boolean expect = true;
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        boolean actual = space.shapeFitsAt(row, col, shape);
        if (actual != expect) {
            failFmt("\nshapeFitsAt() problem\nExpect: %s\nActual: %s\nrow: %s; col %s\n\n%s\n%s",
                    expect, actual, row, col, shape, space);
        }
    }

    @Test(timeout = 1000)
    public void space_fits3() {
        String shLayout =
                "aaa\n" +
                        "";
        int row = 1, col = 1;
        String spLayout =
                "....\n" +
                        "....\n" +
                        "";
        boolean expect = true;
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        boolean actual = space.shapeFitsAt(row, col, shape);
        if (actual != expect) {
            failFmt("\nshapeFitsAt() problem\nExpect: %s\nActual: %s\nrow: %s; col %s\n\n%s\n%s",
                    expect, actual, row, col, shape, space);
        }
    }

    @Test(timeout = 1000)
    public void space_fits4() {
        String shLayout =
                "aaa\n" +
                        "";
        int row = 1, col = 2;
        String spLayout =
                "....\n" +
                        "....\n" +
                        "";
        boolean expect = false;
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        boolean actual = space.shapeFitsAt(row, col, shape);
        if (actual != expect) {
            failFmt("\nshapeFitsAt() problem\nExpect: %s\nActual: %s\nrow: %s; col %s\n\n%s\n%s",
                    expect, actual, row, col, shape, space);
        }
    }

    @Test(timeout = 1000)
    public void space_fits5() {
        String shLayout =
                "aaa\n" +
                        "";
        int row = 0, col = 0;
        String spLayout =
                "..|.\n" +
                        "....\n" +
                        "";
        boolean expect = false;
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        boolean actual = space.shapeFitsAt(row, col, shape);
        if (actual != expect) {
            failFmt("\nshapeFitsAt() problem\nExpect: %s\nActual: %s\nrow: %s; col %s\n\n%s\n%s",
                    expect, actual, row, col, shape, space);
        }
    }

    @Test(timeout = 1000)
    public void space_fits6() {
        String shLayout =
                "aaa\n" +
                        "";
        int row = 0, col = 0;
        String spLayout =
                ".|..\n" +
                        "....\n" +
                        "";
        boolean expect = false;
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        boolean actual = space.shapeFitsAt(row, col, shape);
        if (actual != expect) {
            failFmt("\nshapeFitsAt() problem\nExpect: %s\nActual: %s\nrow: %s; col %s\n\n%s\n%s",
                    expect, actual, row, col, shape, space);
        }
    }

    @Test(timeout = 1000)
    public void space_fits7() {
        String shLayout =
                "aaa\n" +
                        "";
        int row = 0, col = 0;
        String spLayout =
                "|...\n" +
                        "....\n" +
                        "";
        boolean expect = false;
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        boolean actual = space.shapeFitsAt(row, col, shape);
        if (actual != expect) {
            failFmt("\nshapeFitsAt() problem\nExpect: %s\nActual: %s\nrow: %s; col %s\n\n%s\n%s",
                    expect, actual, row, col, shape, space);
        }
    }

    @Test(timeout = 1000)
    public void space_fits8() {
        String shLayout =
                "aaa\n" +
                        "aaa\n" +
                        "";
        int row = 0, col = 0;
        String spLayout =
                "...\n" +
                        "..|\n" +
                        "";
        boolean expect = false;
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        boolean actual = space.shapeFitsAt(row, col, shape);
        if (actual != expect) {
            failFmt("\nshapeFitsAt() problem\nExpect: %s\nActual: %s\nrow: %s; col %s\n\n%s\n%s",
                    expect, actual, row, col, shape, space);
        }
    }

    @Test(timeout = 1000)
    public void space_fits9() {
        String shLayout =
                "aaa\n" +
                        "aa.\n" +
                        "";
        int row = 0, col = 0;
        String spLayout =
                "...\n" +
                        "..|\n" +
                        "";
        boolean expect = true;
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        boolean actual = space.shapeFitsAt(row, col, shape);
        if (actual != expect) {
            failFmt("\nshapeFitsAt() problem\nExpect: %s\nActual: %s\nrow: %s; col %s\n\n%s\n%s",
                    expect, actual, row, col, shape, space);
        }
    }

    @Test(timeout = 1000)
    public void space_fits10() {
        String shLayout =
                "aaa\n" +
                        "aa.\n" +
                        "";
        int row = 4, col = 3;
        String spLayout =
                "......\n" +
                        "......\n" +
                        "......\n" +
                        "......\n" +
                        "......\n" +
                        ".....|\n" +
                        "";
        boolean expect = true;
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        boolean actual = space.shapeFitsAt(row, col, shape);
        if (actual != expect) {
            failFmt("\nshapeFitsAt() problem\nExpect: %s\nActual: %s\nrow: %s; col %s\n\n%s\n%s",
                    expect, actual, row, col, shape, space);
        }
    }

    @Test(timeout = 1000)
    public void space_fits11() {
        String shLayout =
                "a.a\n" +
                        "aa.\n" +
                        "";
        int row = 4, col = 3;
        String spLayout =
                "......\n" +
                        "..|...\n" +
                        "......\n" +
                        ".|....\n" +
                        "....|.\n" +
                        ".....|\n" +
                        "";
        boolean expect = true;
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        boolean actual = space.shapeFitsAt(row, col, shape);
        if (actual != expect) {
            failFmt("\nshapeFitsAt() problem\nExpect: %s\nActual: %s\nrow: %s; col %s\n\n%s\n%s",
                    expect, actual, row, col, shape, space);
        }
    }

    @Test(timeout = 1000)
    public void space_fits12() {
        String shLayout =
                "a.a\n" +
                        "aa.\n" +
                        "";
        int row = 1, col = 2;
        String spLayout =
                "......\n" +
                        "...|..\n" +
                        "......\n" +
                        ".|....\n" +
                        "....|.\n" +
                        ".....|\n" +
                        "";
        boolean expect = true;
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        boolean actual = space.shapeFitsAt(row, col, shape);
        if (actual != expect) {
            failFmt("\nshapeFitsAt() problem\nExpect: %s\nActual: %s\nrow: %s; col %s\n\n%s\n%s",
                    expect, actual, row, col, shape, space);
        }
    }

    @Test(timeout = 1000)
    public void space_fits13() {
        String shLayout =
                "a.a\n" +
                        "aa.\n" +
                        "";
        int row = 1, col = 3;
        String spLayout =
                "......\n" +
                        "...|..\n" +
                        "......\n" +
                        ".|....\n" +
                        "....|.\n" +
                        ".....|\n" +
                        "";
        boolean expect = false;
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        boolean actual = space.shapeFitsAt(row, col, shape);
        if (actual != expect) {
            failFmt("\nshapeFitsAt() problem\nExpect: %s\nActual: %s\nrow: %s; col %s\n\n%s\n%s",
                    expect, actual, row, col, shape, space);
        }
    }

    @Test(timeout = 1000)
    public void space_fits14() {
        String shLayout =
                "a.a\n" +
                        "aa.\n" +
                        "";
        int row = 1, col = 3;
        String spLayout =
                "......\n" +
                        "...|..\n" +
                        "......\n" +
                        ".|....\n" +
                        "....|.\n" +
                        ".....|\n" +
                        "";
        boolean expect = false;
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        boolean actual = space.shapeFitsAt(row, col, shape);
        if (actual != expect) {
            failFmt("\nshapeFitsAt() problem\nExpect: %s\nActual: %s\nrow: %s; col %s\n\n%s\n%s",
                    expect, actual, row, col, shape, space);
        }
    }

    @Test(timeout = 1000, expected = FitItException.class)
    public void space_place_fail1() {
        String shLayout =
                "aaa";
        int row = 0, col = 0;
        String spLayout =
                "|....|\n" +
                        "......\n" +
                        "..||..\n" +
                        "......\n" +
                        "|....|\n" +
                        "";
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        space.placeShapeAt(row, col, shape);
    }

    @Test(timeout = 1000, expected = FitItException.class)
    public void space_place_fail2() {
        String shLayout =
                "aaa\n" +
                        "";
        int row = 1, col = 2;
        String spLayout =
                "....\n" +
                        "....\n" +
                        "";
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        space.placeShapeAt(row, col, shape);
    }

    @Test(timeout = 1000, expected = FitItException.class)
    public void space_place_fail3() {
        String shLayout =
                "a.a\n" +
                        "aa.\n" +
                        "";
        int row = 1, col = 3;
        String spLayout =
                "......\n" +
                        "...|..\n" +
                        "......\n" +
                        ".|....\n" +
                        "....|.\n" +
                        ".....|\n" +
                        "";
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        space.placeShapeAt(row, col, shape);
    }

    @Test(timeout = 1000, expected = FitItException.class)
    public void space_place_fail4() {
        String shLayout =
                "a.a\n" +
                        "aa.\n" +
                        "";
        int row = 1, col = 3;
        String spLayout =
                "......\n" +
                        "...|..\n" +
                        "......\n" +
                        ".|....\n" +
                        "....|.\n" +
                        ".....|\n" +
                        "";
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        space.placeShapeAt(row, col, shape);
    }

    @Test(timeout = 1000)
    public void space_place_succeed1() {
        String shLayout =
                "aaa\n" +
                        "";
        int row = 0, col = 0;
        String spLayout =
                "....\n" +
                        "....\n" +
                        "";
        String expect =
                "aaa.\n" +
                        "....\n" +
                        "";
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        space.placeShapeAt(row, col, shape);
        String actual = space.fitString();
        if (!actual.equals(expect)) {
            failFmt("\nfitString() does not match expected\nplaceAt() row: %s; col: %s\n%s\nSpace Layout:\n%s\nExpect:\n%s\nActual:\n%s",
                    row, col, shape.toString(), spLayout, expect, actual);
        }
    }

    @Test(timeout = 1000)
    public void space_place_succeed2() {
        String shLayout =
                "aaa\n" +
                        "";
        int row = 1, col = 1;
        String spLayout =
                "....\n" +
                        "....\n" +
                        "";
        String expect =
                "....\n" +
                        ".aaa\n" +
                        "";
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        space.placeShapeAt(row, col, shape);
        String actual = space.fitString();
        if (!actual.equals(expect)) {
            failFmt("\nfitString() does not match expected\nplaceAt() row: %s; col: %s\n%s\nSpace Layout:\n%s\nExpect:\n%s\nActual:\n%s",
                    row, col, shape.toString(), spLayout, expect, actual);
        }
    }

    @Test(timeout = 1000)
    public void space_place_succeed3() {
        String shLayout =
                "aaa\n" +
                        "";
        int row = 1, col = 0;
        String spLayout =
                ".|..\n" +
                        "....\n" +
                        "";
        String expect =
                ".|..\n" +
                        "aaa.\n" +
                        "";
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        space.placeShapeAt(row, col, shape);
        String actual = space.fitString();
        if (!actual.equals(expect)) {
            failFmt("\nfitString() does not match expected\nplaceAt() row: %s; col: %s\n%s\nSpace Layout:\n%s\nExpect:\n%s\nActual:\n%s",
                    row, col, shape.toString(), spLayout, expect, actual);
        }
    }

    @Test(timeout = 1000)
    public void space_place_succeed4() {
        String shLayout =
                ".aa\n" +
                        "aaa\n" +
                        "";
        int row = 0, col = 1;
        String spLayout =
                ".|..\n" +
                        "....\n" +
                        "";
        String expect =
                ".|aa\n" +
                        ".aaa\n" +
                        "";
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        space.placeShapeAt(row, col, shape);
        String actual = space.fitString();
        if (!actual.equals(expect)) {
            failFmt("\nfitString() does not match expected\nplaceAt() row: %s; col: %s\n%s\nSpace Layout:\n%s\nExpect:\n%s\nActual:\n%s",
                    row, col, shape.toString(), spLayout, expect, actual);
        }
    }

    @Test(timeout = 1000)
    public void space_place_succeed5() {
        String shLayout =
                "aaa\n" +
                        "aa.\n" +
                        "";
        int row = 4, col = 3;
        String spLayout =
                "......\n" +
                        "......\n" +
                        "......\n" +
                        "......\n" +
                        "......\n" +
                        ".....|\n" +
                        "";
        String expect =
                "......\n" +
                        "......\n" +
                        "......\n" +
                        "......\n" +
                        "...aaa\n" +
                        "...aa|\n" +
                        "";
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        space.placeShapeAt(row, col, shape);
        String actual = space.fitString();
        if (!actual.equals(expect)) {
            failFmt("\nfitString() does not match expected\nplaceAt() row: %s; col: %s\n%s\nSpace Layout:\n%s\nExpect:\n%s\nActual:\n%s",
                    row, col, shape.toString(), spLayout, expect, actual);
        }
    }

    @Test(timeout = 1000)
    public void space_place_succeed6() {
        String shLayout =
                "a.a\n" +
                        "aa.\n" +
                        "";
        int row = 1, col = 2;
        String spLayout =
                "......\n" +
                        "...|..\n" +
                        "......\n" +
                        ".|....\n" +
                        "....|.\n" +
                        ".....|\n" +
                        "";
        String expect =
                "......\n" +
                        "..a|a.\n" +
                        "..aa..\n" +
                        ".|....\n" +
                        "....|.\n" +
                        ".....|\n" +
                        "";
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        space.placeShapeAt(row, col, shape);
        String actual = space.fitString();
        if (!actual.equals(expect)) {
            failFmt("\nfitString() does not match expected\nplaceAt() row: %s; col: %s\n%s\nSpace Layout:\n%s\nExpect:\n%s\nActual:\n%s",
                    row, col, shape.toString(), spLayout, expect, actual);
        }
    }

    @Test(timeout = 1000)
    public void space_place_succeed7() {
        String shLayout =
                "a.a\n" +
                        "aa.\n" +
                        "a.a\n" +
                        "aa.\n" +
                        "";
        int row = 1, col = 2;
        String spLayout =
                "......\n" +
                        "...|..\n" +
                        "......\n" +
                        ".|....\n" +
                        "....|.\n" +
                        ".....|\n" +
                        "";
        String expect =
                "......\n" +
                        "..a|a.\n" +
                        "..aa..\n" +
                        ".|a.a.\n" +
                        "..aa|.\n" +
                        ".....|\n" +
                        "";
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        space.placeShapeAt(row, col, shape);
        String actual = space.fitString();
        if (!actual.equals(expect)) {
            failFmt("\nfitString() does not match expected\nplaceAt() row: %s; col: %s\n%s\nSpace Layout:\n%s\nExpect:\n%s\nActual:\n%s",
                    row, col, shape.toString(), spLayout, expect, actual);
        }
    }

    @Test(timeout = 1000)
    public void space_place_succeed8_rot() {
        String shLayout =
                "rrr\n" +
                        "r..\n" +
                        "";
        int row = 0, col = 0, rot = 1;
        String spLayout =
                "..\n" +
                        "|.\n" +
                        "|.\n" +
                        "";
        String expect =
                "rr\n" +
                        "|r\n" +
                        "|r\n" +
                        "";
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        for (int i = 0; i < rot; i++) {
            shape.rotateCW();
        }
        space.placeShapeAt(row, col, shape);
        String actual = space.fitString();
        if (!actual.equals(expect)) {
            failFmt("\nfitString() does not match expected\nplaceAt() row: %s; col: %s\n%s\nSpace Layout:\n%s\nExpect:\n%s\nActual:\n%s",
                    row, col, shape.toString(), spLayout, expect, actual);
        }
    }

    @Test(timeout = 1000)
    public void space_place_succeed9_rot() {
        String shLayout =
                "rrr\n" +
                        "r..\n" +
                        "";
        int row = 2, col = 2, rot = 2;
        String spLayout =
                ".....\n" +
                        "...||\n" +
                        ".....\n" +
                        ".....\n" +
                        "";
        String expect =
                ".....\n" +
                        "...||\n" +
                        "....r\n" +
                        "..rrr\n" +
                        "";
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        for (int i = 0; i < rot; i++) {
            shape.rotateCW();
        }
        space.placeShapeAt(row, col, shape);
        String actual = space.fitString();
        if (!actual.equals(expect)) {
            failFmt("\nfitString() does not match expected\nplaceAt() row: %s; col: %s\n%s\nSpace Layout:\n%s\nExpect:\n%s\nActual:\n%s",
                    row, col, shape.toString(), spLayout, expect, actual);
        }
    }

    @Test(timeout = 1000)
    public void space_place_succeed10_rot() {
        String shLayout =
                "rrr\n" +
                        "r..\n" +
                        "";
        int row = 1, col = 2, rot = 3;
        String spLayout =
                ".....\n" +
                        "...||\n" +
                        ".....\n" +
                        ".....\n" +
                        "";
        String expect =
                ".....\n" +
                        "..r||\n" +
                        "..r..\n" +
                        "..rr.\n" +
                        "";
        char dc = getDisplayChar(shLayout);
        Space space = FitIt.makeSpace(spLayout);
        Shape shape = FitIt.makeShape(shLayout, dc);
        for (int i = 0; i < rot; i++) {
            shape.rotateCW();
        }
        space.placeShapeAt(row, col, shape);
        String actual = space.fitString();
        if (!actual.equals(expect)) {
            failFmt("\nfitString() does not match expected\nplaceAt() row: %s; col: %s\n%s\nSpace Layout:\n%s\nExpect:\n%s\nActual:\n%s",
                    row, col, shape.toString(), spLayout, expect, actual);
        }
    }

    @Test(timeout = 1000, expected = FitItException.class)
    public void space_shapes_colide1() {
        ShapePlace[] shapes = new ShapePlace[]{
                new ShapePlace("bb\n" +
                        "bb\n" +
                        "", 0, 2, 0),
                new ShapePlace("aaa\n" +
                        "aaa\n" +
                        "", 0, 0, 0),
        };
        String spLayout =
                ".....\n" +
                        ".....\n" +
                        "";
        Space space = FitIt.makeSpace(spLayout);
        for (ShapePlace sp : shapes) {
            space.placeShapeAt(sp.row, sp.col, sp.shape);
        }
    }

    @Test(timeout = 1000, expected = FitItException.class)
    public void space_shapes_colide2() {
        ShapePlace[] shapes = new ShapePlace[]{
                new ShapePlace("bb\n" +
                        "bb\n" +
                        "", 0, 1, 0),
                new ShapePlace("aaa\n" +
                        "aaa\n" +
                        "", 1, 1, 0),
        };
        String spLayout =
                ".......\n" +
                        ".......\n" +
                        ".......\n" +
                        "";
        Space space = FitIt.makeSpace(spLayout);
        for (ShapePlace sp : shapes) {
            space.placeShapeAt(sp.row, sp.col, sp.shape);
        }
    }

    @Test(timeout = 1000, expected = FitItException.class)
    public void space_shapes_colide3() {
        ShapePlace[] shapes = new ShapePlace[]{
                new ShapePlace("aaa\n" +
                        "aaa\n" +
                        "", 0, 1, 0),
                new ShapePlace("bb\n" +
                        "bb\n" +
                        "", 1, 2, 0),
        };
        String spLayout =
                ".......\n" +
                        ".......\n" +
                        ".......\n" +
                        "";
        Space space = FitIt.makeSpace(spLayout);
        for (ShapePlace sp : shapes) {
            space.placeShapeAt(sp.row, sp.col, sp.shape);
        }
    }

    @Test(timeout = 1000, expected = FitItException.class)
    public void space_shapes_colide4() {
        ShapePlace[] shapes = new ShapePlace[]{
                new ShapePlace("aaa\n" +
                        "aaa\n" +
                        "aaa\n" +
                        "", 0, 1, 0),
                new ShapePlace("b\n" +
                        "", 1, 2, 0),
        };
        String spLayout =
                ".......\n" +
                        ".......\n" +
                        ".......\n" +
                        "";
        Space space = FitIt.makeSpace(spLayout);
        for (ShapePlace sp : shapes) {
            space.placeShapeAt(sp.row, sp.col, sp.shape);
        }
    }

    @Test(timeout = 1000, expected = FitItException.class)
    public void space_shapes_colide5() {
        ShapePlace[] shapes = new ShapePlace[]{
                new ShapePlace("aaa\n" +
                        "a..\n" +
                        "a..\n" +
                        "", 0, 0, 0),
                new ShapePlace(".b\n" +
                        "bb\n" +
                        "", 1, 1, 0),
                new ShapePlace("c\n" +
                        "c\n" +
                        "c\n" +
                        "", 0, 1, 0),
        };
        String spLayout =
                ".......\n" +
                        ".......\n" +
                        ".......\n" +
                        "";
        Space space = FitIt.makeSpace(spLayout);
        for (ShapePlace sp : shapes) {
            space.placeShapeAt(sp.row, sp.col, sp.shape);
        }
    }

    @Test(timeout = 1000, expected = FitItException.class)
    public void space_shapes_colide6() {
        ShapePlace[] shapes = new ShapePlace[]{
                new ShapePlace("aaa\n" +
                        "a..\n" +
                        "a..\n" +
                        "", 0, 0, 0),
                new ShapePlace("b\n" +
                        "b\n" +
                        "", 1, 2, 0),
                new ShapePlace("c\n" +
                        "c\n" +
                        "", 0, 1, 1),
        };
        String spLayout =
                ".......\n" +
                        ".......\n" +
                        ".......\n" +
                        "";
        Space space = FitIt.makeSpace(spLayout);
        for (ShapePlace sp : shapes) {
            space.placeShapeAt(sp.row, sp.col, sp.shape);
        }
    }

    @Test(timeout = 1000)
    public void space_mutiple_place1() {
        ShapePlace[] shapes = new ShapePlace[]{
                new ShapePlace("bb\n" +
                        "bb\n" +
                        "", 0, 3, 0),
                new ShapePlace("aaa\n" +
                        "aaa\n" +
                        "", 0, 0, 0),
        };
        String spLayout =
                ".....\n" +
                        ".....\n" +
                        "";
        String expect =
                "aaabb\n" +
                        "aaabb\n" +
                        "";
        Space space = FitIt.makeSpace(spLayout);
        for (ShapePlace sp : shapes) {
            space.placeShapeAt(sp.row, sp.col, sp.shape);
        }
        String actual = space.fitString();
        if (!actual.equals(expect)) {
            failFmt("\nfitString() does not match expected\nShapes:\n%s\nSpace Layout:\n%s\nExpect:\n%s\nActual:\n%s",
                    Arrays.toString(shapes), spLayout, expect, actual);
        }
    }

    @Test(timeout = 1000)
    public void space_mutiple_place2() {
        ShapePlace[] shapes = new ShapePlace[]{
                new ShapePlace("bb\n" +
                        "bb\n" +
                        "", 3, 0, 0),
                new ShapePlace("aaa\n" +
                        "aaa\n" +
                        "", 0, 0, 1),
        };
        String spLayout =
                "..\n" +
                        "..\n" +
                        "..\n" +
                        "..\n" +
                        "..\n" +
                        "";
        String expect =
                "aa\n" +
                        "aa\n" +
                        "aa\n" +
                        "bb\n" +
                        "bb\n" +
                        "";
        Space space = FitIt.makeSpace(spLayout);
        for (ShapePlace sp : shapes) {
            space.placeShapeAt(sp.row, sp.col, sp.shape);
        }
        String actual = space.fitString();
        if (!actual.equals(expect)) {
            failFmt("\nfitString() does not match expected\nShapes:\n%s\nSpace Layout:\n%s\nExpect:\n%s\nActual:\n%s",
                    Arrays.toString(shapes), spLayout, expect, actual);
        }
    }

    @Test(timeout = 1000)
    public void space_mutiple_place3() {
        ShapePlace[] shapes = new ShapePlace[]{
                new ShapePlace("rrr\n" +
                        "r..\n" +
                        "", 1, 2, 3),
                new ShapePlace("a.a\n" +
                        "aa.\n" +
                        "", 0, 0, 0),
        };
        String spLayout =
                ".....\n" +
                        "...||\n" +
                        ".....\n" +
                        ".....\n" +
                        "";
        String expect =
                "a.a..\n" +
                        "aar||\n" +
                        "..r..\n" +
                        "..rr.\n" +
                        "";
        Space space = FitIt.makeSpace(spLayout);
        for (ShapePlace sp : shapes) {
            space.placeShapeAt(sp.row, sp.col, sp.shape);
        }
        String actual = space.fitString();
        if (!actual.equals(expect)) {
            failFmt("\nfitString() does not match expected\nShapes:\n%s\nSpace Layout:\n%s\nExpect:\n%s\nActual:\n%s",
                    Arrays.toString(shapes), spLayout, expect, actual);
        }
    }

    @Test(timeout = 1000)
    public void space_mutiple_place4() {
        ShapePlace[] shapes = new ShapePlace[]{
                new ShapePlace("rrr\n" +
                        "r..\n" +
                        "", 0, 2, 3),
                new ShapePlace("a.a\n" +
                        "aa.\n" +
                        "", 0, 0, 1),
                new ShapePlace("xxxxx\n" +
                        "x...x\n" +
                        "", 2, 0, 2),
        };
        String spLayout =
                ".....\n" +
                        "...||\n" +
                        ".....\n" +
                        ".....\n" +
                        "";
        String expect =
                "aar..\n" +
                        "a.r||\n" +
                        "xarrx\n" +
                        "xxxxx\n" +
                        "";
        Space space = FitIt.makeSpace(spLayout);
        for (ShapePlace sp : shapes) {
            space.placeShapeAt(sp.row, sp.col, sp.shape);
        }
        String actual = space.fitString();
        if (!actual.equals(expect)) {
            failFmt("\nfitString() does not match expected\nShapes:\n%s\nSpace Layout:\n%s\nExpect:\n%s\nActual:\n%s",
                    Arrays.toString(shapes), spLayout, expect, actual);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Search for fit test utilities

    @Test(timeout = 1000)
    public void space_mutiple_place5() {
        ShapePlace[] shapes = new ShapePlace[]{
                new ShapePlace(".aa\n" +
                        "aaa\n" +
                        "", 0, 2, 3),
                new ShapePlace("xxxxx\n" +
                        "x.x..\n" +
                        "x..x.\n" +
                        "x...x\n" +
                        "", 0, 0, 2),
                new ShapePlace("r.\n" +
                        ".r\n" +
                        "", 0, 0, 1),
        };
        String spLayout =
                ".....\n" +
                        ".....\n" +
                        "||...\n" +
                        ".....\n" +
                        "";
        String expect =
                "xraax\n" +
                        "rxaax\n" +
                        "||xax\n" +
                        "xxxxx\n" +
                        "";
        Space space = FitIt.makeSpace(spLayout);
        for (ShapePlace sp : shapes) {
            space.placeShapeAt(sp.row, sp.col, sp.shape);
        }
        String actual = space.fitString();
        if (!actual.equals(expect)) {
            failFmt("\nfitString() does not match expected\nShapes:\n%s\nSpace Layout:\n%s\nExpect:\n%s\nActual:\n%s",
                    Arrays.toString(shapes), spLayout, expect, actual);
        }
    }

    @Test(timeout = 1000)
    public void space_mutiple_place6() {
        ShapePlace[] shapes = new ShapePlace[]{
                new ShapePlace("vvv\n" +
                        "v..\n" +
                        "v..\n" +
                        "", 2, 0, 2),
                new ShapePlace("sss\n" +
                        "ss.\n" +
                        "", 0, 0, 0),
                new ShapePlace("a\n" +
                        "a\n" +
                        "a\n" +
                        "a\n" +
                        "", 1, 2, 1),
                new ShapePlace("ll\n" +
                        "", 2, 4, 0),
                new ShapePlace("bb\n" +
                        "b.\n" +
                        "", 3, 4, 4),
        };
        String spLayout =
                "...|..\n" +
                        "......\n" +
                        "||.|..\n" +
                        "...|..\n" +
                        "...|.|\n" +
                        "";
        String expect =
                "sss|..\n" +
                        "ssaaaa\n" +
                        "||v|ll\n" +
                        "..v|bb\n" +
                        "vvv|b|\n" +
                        "";
        Space space = FitIt.makeSpace(spLayout);
        for (ShapePlace sp : shapes) {
            space.placeShapeAt(sp.row, sp.col, sp.shape);
        }
        String actual = space.fitString();
        if (!actual.equals(expect)) {
            failFmt("\nfitString() does not match expected\nShapes:\n%s\nSpace Layout:\n%s\nExpect:\n%s\nActual:\n%s",
                    Arrays.toString(shapes), spLayout, expect, actual);
        }
    }

    @Test(timeout = 1000)
    public void space_placedShapeInfo1() {

        ShapePlace[] shapes = new ShapePlace[]{
                new ShapePlace("bb\n" +
                        "bb\n" +
                        "", 0, 3, 0),
                new ShapePlace("aaa\n" +
                        "aaa\n" +
                        "", 0, 0, 0),
        };
        String spLayout =
                ".....\n" +
                        ".....\n" +
                        "";
        String expect =
                "2 shapes placed\n" +
                        "Shape at (0,0)\n" +
                        "SHAPE a\n" +
                        "height: 2; width: 3; rotation: CW0\n" +
                        "aaa\n" +
                        "aaa\n" +
                        "\n" +
                        "Shape at (0,3)\n" +
                        "SHAPE b\n" +
                        "height: 2; width: 2; rotation: CW0\n" +
                        "bb\n" +
                        "bb\n" +
                        "\n" +
                        "";
        Space space = FitIt.makeSpace(spLayout);
        for (ShapePlace sp : shapes) {
            space.placeShapeAt(sp.row, sp.col, sp.shape);
        }
        String actual = space.placedShapeInfo();
        String msg =
                String.format("\nplacedShapeString() does not match expected, CHECK semicolons and ending newline\nEXPECT:\n%s\nACTUAL:\n%s\nShapes:\n%s\nSpace Layout:\n%s",
                        expect, actual, Arrays.toString(shapes), spLayout);
        assertEquals(msg, expect, actual);
        if (space.placedShapeCount() != shapes.length) {
            failFmt("\nplacedShapeCount() wrong\nExpect: %s\nActual: %s\nShapes:\n%s",
                    shapes.length, space.placedShapeCount(), Arrays.toString(shapes));
        }
    }

    @Test(timeout = 1000)
    public void space_placedShapeInfo2() {
        ShapePlace[] shapes = new ShapePlace[]{
                new ShapePlace("bb\n" +
                        "bb\n" +
                        "", 3, 0, 0),
                new ShapePlace("aaa\n" +
                        "aaa\n" +
                        "", 0, 0, 1),
        };
        String spLayout =
                "..\n" +
                        "..\n" +
                        "..\n" +
                        "..\n" +
                        "..\n" +
                        "";
        String expect =
                "2 shapes placed\n" +
                        "Shape at (0,0)\n" +
                        "SHAPE a\n" +
                        "height: 3; width: 2; rotation: CW90\n" +
                        "aa\n" +
                        "aa\n" +
                        "aa\n" +
                        "\n" +
                        "Shape at (3,0)\n" +
                        "SHAPE b\n" +
                        "height: 2; width: 2; rotation: CW0\n" +
                        "bb\n" +
                        "bb\n" +
                        "\n" +
                        "";
        Space space = FitIt.makeSpace(spLayout);
        for (ShapePlace sp : shapes) {
            space.placeShapeAt(sp.row, sp.col, sp.shape);
        }
        String actual = space.placedShapeInfo();
        String msg =
                String.format("\nplacedShapeString() does not match expected, CHECK semicolons and ending newline\nEXPECT:\n%s\nACTUAL:\n%s\nShapes:\n%s\nSpace Layout:\n%s",
                        expect, actual, Arrays.toString(shapes), spLayout);
        assertEquals(msg, expect, actual);
        if (space.placedShapeCount() != shapes.length) {
            failFmt("\nplacedShapeCount() wrong\nExpect: %s\nActual: %s\nShapes:\n%s",
                    shapes.length, space.placedShapeCount(), Arrays.toString(shapes));
        }
    }

    @Test(timeout = 1000)
    public void space_placedShapeInfo3() {
        ShapePlace[] shapes = new ShapePlace[]{
                new ShapePlace("rrr\n" +
                        "r..\n" +
                        "", 1, 2, 3),
                new ShapePlace("a.a\n" +
                        "aa.\n" +
                        "", 0, 0, 0),
        };
        String spLayout =
                ".....\n" +
                        "...||\n" +
                        ".....\n" +
                        ".....\n" +
                        "";
        String expect =
                "2 shapes placed\n" +
                        "Shape at (0,0)\n" +
                        "SHAPE a\n" +
                        "height: 2; width: 3; rotation: CW0\n" +
                        "a.a\n" +
                        "aa.\n" +
                        "\n" +
                        "Shape at (1,2)\n" +
                        "SHAPE r\n" +
                        "height: 3; width: 2; rotation: CW270\n" +
                        "r.\n" +
                        "r.\n" +
                        "rr\n" +
                        "\n" +
                        "";
        Space space = FitIt.makeSpace(spLayout);
        for (ShapePlace sp : shapes) {
            space.placeShapeAt(sp.row, sp.col, sp.shape);
        }
        String actual = space.placedShapeInfo();
        String msg =
                String.format("\nplacedShapeString() does not match expected, CHECK semicolons and ending newline\nEXPECT:\n%s\nACTUAL:\n%s\nShapes:\n%s\nSpace Layout:\n%s",
                        expect, actual, Arrays.toString(shapes), spLayout);
        assertEquals(msg, expect, actual);
        if (space.placedShapeCount() != shapes.length) {
            failFmt("\nplacedShapeCount() wrong\nExpect: %s\nActual: %s\nShapes:\n%s",
                    shapes.length, space.placedShapeCount(), Arrays.toString(shapes));
        }
    }

    @Test(timeout = 1000)
    public void space_placedShapeInfo4() {
        ShapePlace[] shapes = new ShapePlace[]{
                new ShapePlace("vvv\n" +
                        "v..\n" +
                        "v..\n" +
                        "", 2, 0, 2),
                new ShapePlace("sss\n" +
                        "ss.\n" +
                        "", 0, 0, 0),
                new ShapePlace("a\n" +
                        "a\n" +
                        "a\n" +
                        "a\n" +
                        "", 1, 2, 1),
                new ShapePlace("ll\n" +
                        "", 2, 4, 0),
                new ShapePlace("bb\n" +
                        "b.\n" +
                        "", 3, 4, 4),
        };
        String spLayout =
                "...|..\n" +
                        "......\n" +
                        "||.|..\n" +
                        "...|..\n" +
                        "...|.|\n" +
                        "";
        String expect =
                "5 shapes placed\n" +
                        "Shape at (1,2)\n" +
                        "SHAPE a\n" +
                        "height: 1; width: 4; rotation: CW90\n" +
                        "aaaa\n" +
                        "\n" +
                        "Shape at (3,4)\n" +
                        "SHAPE b\n" +
                        "height: 2; width: 2; rotation: CW0\n" +
                        "bb\n" +
                        "b.\n" +
                        "\n" +
                        "Shape at (2,4)\n" +
                        "SHAPE l\n" +
                        "height: 1; width: 2; rotation: CW0\n" +
                        "ll\n" +
                        "\n" +
                        "Shape at (0,0)\n" +
                        "SHAPE s\n" +
                        "height: 2; width: 3; rotation: CW0\n" +
                        "sss\n" +
                        "ss.\n" +
                        "\n" +
                        "Shape at (2,0)\n" +
                        "SHAPE v\n" +
                        "height: 3; width: 3; rotation: CW180\n" +
                        "..v\n" +
                        "..v\n" +
                        "vvv\n" +
                        "\n" +
                        "";
        Space space = FitIt.makeSpace(spLayout);
        for (ShapePlace sp : shapes) {
            space.placeShapeAt(sp.row, sp.col, sp.shape);
        }
        String actual = space.placedShapeInfo();
        String msg =
                String.format("\nplacedShapeString() does not match expected, CHECK semicolons and ending newline\nEXPECT:\n%s\nACTUAL:\n%s\nShapes:\n%s\nSpace Layout:\n%s",
                        expect, actual, Arrays.toString(shapes), spLayout);
        assertEquals(msg, expect, actual);
        if (space.placedShapeCount() != shapes.length) {
            failFmt("\nplacedShapeCount() wrong\nExpect: %s\nActual: %s\nShapes:\n%s",
                    shapes.length, space.placedShapeCount(), Arrays.toString(shapes));
        }
    }

    @Test(timeout = 1000)
    public void space_remove1() {
        char[] remove = {'b'};
        ShapePlace[] shapes = new ShapePlace[]{
                new ShapePlace("bb\n" +
                        "bb\n" +
                        "", 0, 3, 0),
                new ShapePlace("aaa\n" +
                        "aaa\n" +
                        "", 0, 0, 0),
        };
        String spLayout =
                ".....\n" +
                        ".....\n" +
                        "";
        String expect =
                "SPACE:\n" +
                        "height: 2 width: 5\n" +
                        "aaa..\n" +
                        "aaa..\n" +
                        "\n" +
                        "1 shapes placed\n" +
                        "Shape at (0,0)\n" +
                        "SHAPE a\n" +
                        "height: 2; width: 3; rotation: CW0\n" +
                        "aaa\n" +
                        "aaa\n" +
                        "\n" +
                        "";
        Space space = FitIt.makeSpace(spLayout);
        for (ShapePlace sp : shapes) {
            space.placeShapeAt(sp.row, sp.col, sp.shape);
        }
        for (char dc : remove) {
            space.removeShapeByDisplayChar(dc);
        }
        String actual = space.toString();
        if (!actual.equals(expect)) {
            failFmt("\ntoString() does not match expected\nEXPECT:\n%s\nACTUAL:\n%s\nRemoved: %s\nShapes:\n%s\nSpace Layout:\n%s",
                    expect, actual, Arrays.toString(remove), Arrays.toString(shapes), spLayout);
        }
    }


    ///////////////////////////////////////////////////////////
    // Impossible searchForFit()

    @Test(timeout = 1000)
    public void space_remove2() {
        char[] remove = {'a'};
        ShapePlace[] shapes = new ShapePlace[]{
                new ShapePlace("rrr\n" +
                        "r..\n" +
                        "", 1, 2, 3),
                new ShapePlace("a.a\n" +
                        "aa.\n" +
                        "", 0, 0, 0),
        };
        String spLayout =
                ".....\n" +
                        "...||\n" +
                        ".....\n" +
                        ".....\n" +
                        "";
        String expect =
                "SPACE:\n" +
                        "height: 4 width: 5\n" +
                        ".....\n" +
                        "..r||\n" +
                        "..r..\n" +
                        "..rr.\n" +
                        "\n" +
                        "1 shapes placed\n" +
                        "Shape at (1,2)\n" +
                        "SHAPE r\n" +
                        "height: 3; width: 2; rotation: CW270\n" +
                        "r.\n" +
                        "r.\n" +
                        "rr\n" +
                        "\n" +
                        "";
        Space space = FitIt.makeSpace(spLayout);
        for (ShapePlace sp : shapes) {
            space.placeShapeAt(sp.row, sp.col, sp.shape);
        }
        for (char dc : remove) {
            space.removeShapeByDisplayChar(dc);
        }
        String actual = space.toString();
        if (!actual.equals(expect)) {
            failFmt("\ntoString() does not match expected\nEXPECT:\n%s\nACTUAL:\n%s\nRemoved: %s\nShapes:\n%s\nSpace Layout:\n%s",
                    expect, actual, Arrays.toString(remove), Arrays.toString(shapes), spLayout);
        }
    }

    @Test(timeout = 1000)
    public void space_remove3() {
        char[] remove = {'b', 'l'};
        ShapePlace[] shapes = new ShapePlace[]{
                new ShapePlace("vvv\n" +
                        "v..\n" +
                        "v..\n" +
                        "", 2, 0, 2),
                new ShapePlace("sss\n" +
                        "ss.\n" +
                        "", 0, 0, 0),
                new ShapePlace("a\n" +
                        "a\n" +
                        "a\n" +
                        "a\n" +
                        "", 1, 2, 1),
                new ShapePlace("ll\n" +
                        "", 2, 4, 0),
                new ShapePlace("bb\n" +
                        "b.\n" +
                        "", 3, 4, 4),
        };
        String spLayout =
                "...|..\n" +
                        "......\n" +
                        "||.|..\n" +
                        "...|..\n" +
                        "...|.|\n" +
                        "";
        String expect =
                "SPACE:\n" +
                        "height: 5 width: 6\n" +
                        "sss|..\n" +
                        "ssaaaa\n" +
                        "||v|..\n" +
                        "..v|..\n" +
                        "vvv|.|\n" +
                        "\n" +
                        "3 shapes placed\n" +
                        "Shape at (1,2)\n" +
                        "SHAPE a\n" +
                        "height: 1; width: 4; rotation: CW90\n" +
                        "aaaa\n" +
                        "\n" +
                        "Shape at (0,0)\n" +
                        "SHAPE s\n" +
                        "height: 2; width: 3; rotation: CW0\n" +
                        "sss\n" +
                        "ss.\n" +
                        "\n" +
                        "Shape at (2,0)\n" +
                        "SHAPE v\n" +
                        "height: 3; width: 3; rotation: CW180\n" +
                        "..v\n" +
                        "..v\n" +
                        "vvv\n" +
                        "\n" +
                        "";
        Space space = FitIt.makeSpace(spLayout);
        for (ShapePlace sp : shapes) {
            space.placeShapeAt(sp.row, sp.col, sp.shape);
        }
        for (char dc : remove) {
            space.removeShapeByDisplayChar(dc);
        }
        String actual = space.toString();
        if (!actual.equals(expect)) {
            failFmt("\ntoString() does not match expected\nEXPECT:\n%s\nACTUAL:\n%s\nRemoved: %s\nShapes:\n%s\nSpace Layout:\n%s",
                    expect, actual, Arrays.toString(remove), Arrays.toString(shapes), spLayout);
        }
    }

    @Test(timeout = 1000)
    public void space_comprehensive1() {
        Shape t = FitIt.makeShape("tttt\n" +
                ".ttt\n" +
                "", 't');
        Shape f = FitIt.makeShape("ff\n" +
                "", 'f');
        Shape c = FitIt.makeShape("cc.\n" +
                "ccc\n" +
                "", 'c');
        Shape a = FitIt.makeShape("a..\n" +
                "aaa\n" +
                "", 'a');
        Shape b = FitIt.makeShape("bb\n" +
                "bb\n" +
                "bb\n" +
                "", 'b');
        Space space = FitIt.makeSpace(
                "...||...\n" +
                        "....||..\n" +
                        ".|||....\n" +
                        "...|....\n" +
                        "");

        String expect;

        space.placeShapeAt(0, 0, c);
        b.rotateCW();
        space.placeShapeAt(2, 5, b);
        space.placeShapeAt(2, 0, a);
        space.placeShapeAt(1, 6, f);

        expect =
                "SPACE:\n" +
                        "height: 4 width: 8\n" +
                        "cc.||...\n" +
                        "ccc.||ff\n" +
                        "a|||.bbb\n" +
                        "aaa|.bbb\n" +
                        "\n" +
                        "4 shapes placed\n" +
                        "Shape at (2,0)\n" +
                        "SHAPE a\n" +
                        "height: 2; width: 3; rotation: CW0\n" +
                        "a..\n" +
                        "aaa\n" +
                        "\n" +
                        "Shape at (2,5)\n" +
                        "SHAPE b\n" +
                        "height: 2; width: 3; rotation: CW90\n" +
                        "bbb\n" +
                        "bbb\n" +
                        "\n" +
                        "Shape at (0,0)\n" +
                        "SHAPE c\n" +
                        "height: 2; width: 3; rotation: CW0\n" +
                        "cc.\n" +
                        "ccc\n" +
                        "\n" +
                        "Shape at (1,6)\n" +
                        "SHAPE f\n" +
                        "height: 1; width: 2; rotation: CW0\n" +
                        "ff\n" +
                        "\n" +
                        "";
        assertEquals(String.format("\nEXPECT:\n%s\nACTUAL:\n%s\n", expect, space.toString()), expect, space.toString());

        assertFalse(space.shapeFitsAt(0, 0, t));
        assertFalse(space.shapeFitsAt(2, 4, t));
        space.removeShapeByDisplayChar('c');

        expect =
                "SPACE:\n" +
                        "height: 4 width: 8\n" +
                        "...||...\n" +
                        "....||ff\n" +
                        "a|||.bbb\n" +
                        "aaa|.bbb\n" +
                        "\n" +
                        "3 shapes placed\n" +
                        "Shape at (2,0)\n" +
                        "SHAPE a\n" +
                        "height: 2; width: 3; rotation: CW0\n" +
                        "a..\n" +
                        "aaa\n" +
                        "\n" +
                        "Shape at (2,5)\n" +
                        "SHAPE b\n" +
                        "height: 2; width: 3; rotation: CW90\n" +
                        "bbb\n" +
                        "bbb\n" +
                        "\n" +
                        "Shape at (1,6)\n" +
                        "SHAPE f\n" +
                        "height: 1; width: 2; rotation: CW0\n" +
                        "ff\n" +
                        "\n" +
                        "";
        assertEquals(String.format("\nEXPECT:\n%s\nACTUAL:\n%s\n", expect, space.toString()), expect, space.toString());

        t.rotateCW();
        t.rotateCW();
        space.placeShapeAt(0, 0, t);

        expect =
                "SPACE:\n" +
                        "height: 4 width: 8\n" +
                        "ttt||...\n" +
                        "tttt||ff\n" +
                        "a|||.bbb\n" +
                        "aaa|.bbb\n" +
                        "\n" +
                        "4 shapes placed\n" +
                        "Shape at (2,0)\n" +
                        "SHAPE a\n" +
                        "height: 2; width: 3; rotation: CW0\n" +
                        "a..\n" +
                        "aaa\n" +
                        "\n" +
                        "Shape at (2,5)\n" +
                        "SHAPE b\n" +
                        "height: 2; width: 3; rotation: CW90\n" +
                        "bbb\n" +
                        "bbb\n" +
                        "\n" +
                        "Shape at (1,6)\n" +
                        "SHAPE f\n" +
                        "height: 1; width: 2; rotation: CW0\n" +
                        "ff\n" +
                        "\n" +
                        "Shape at (0,0)\n" +
                        "SHAPE t\n" +
                        "height: 2; width: 4; rotation: CW180\n" +
                        "ttt.\n" +
                        "tttt\n" +
                        "\n" +
                        "";
        assertEquals(String.format("\nEXPECT:\n%s\nACTUAL:\n%s\n", expect, space.toString()), expect, space.toString());

        space.removeShapeByDisplayChar('f');
        c.rotateCW();
        c.rotateCW();
        space.placeShapeAt(0, 5, c);

        expect =
                "SPACE:\n" +
                        "height: 4 width: 8\n" +
                        "ttt||ccc\n" +
                        "tttt||cc\n" +
                        "a|||.bbb\n" +
                        "aaa|.bbb\n" +
                        "\n" +
                        "4 shapes placed\n" +
                        "Shape at (2,0)\n" +
                        "SHAPE a\n" +
                        "height: 2; width: 3; rotation: CW0\n" +
                        "a..\n" +
                        "aaa\n" +
                        "\n" +
                        "Shape at (2,5)\n" +
                        "SHAPE b\n" +
                        "height: 2; width: 3; rotation: CW90\n" +
                        "bbb\n" +
                        "bbb\n" +
                        "\n" +
                        "Shape at (0,5)\n" +
                        "SHAPE c\n" +
                        "height: 2; width: 3; rotation: CW180\n" +
                        "ccc\n" +
                        ".cc\n" +
                        "\n" +
                        "Shape at (0,0)\n" +
                        "SHAPE t\n" +
                        "height: 2; width: 4; rotation: CW180\n" +
                        "ttt.\n" +
                        "tttt\n" +
                        "\n" +
                        "";
        assertEquals(String.format("\nEXPECT:\n%s\nACTUAL:\n%s\n", expect, space.toString()), expect, space.toString());

        f.rotateCW();
        f.rotateCW();
        f.rotateCW();
        space.placeShapeAt(2, 4, f);

        expect =
                "SPACE:\n" +
                        "height: 4 width: 8\n" +
                        "ttt||ccc\n" +
                        "tttt||cc\n" +
                        "a|||fbbb\n" +
                        "aaa|fbbb\n" +
                        "\n" +
                        "5 shapes placed\n" +
                        "Shape at (2,0)\n" +
                        "SHAPE a\n" +
                        "height: 2; width: 3; rotation: CW0\n" +
                        "a..\n" +
                        "aaa\n" +
                        "\n" +
                        "Shape at (2,5)\n" +
                        "SHAPE b\n" +
                        "height: 2; width: 3; rotation: CW90\n" +
                        "bbb\n" +
                        "bbb\n" +
                        "\n" +
                        "Shape at (0,5)\n" +
                        "SHAPE c\n" +
                        "height: 2; width: 3; rotation: CW180\n" +
                        "ccc\n" +
                        ".cc\n" +
                        "\n" +
                        "Shape at (2,4)\n" +
                        "SHAPE f\n" +
                        "height: 2; width: 1; rotation: CW270\n" +
                        "f\n" +
                        "f\n" +
                        "\n" +
                        "Shape at (0,0)\n" +
                        "SHAPE t\n" +
                        "height: 2; width: 4; rotation: CW180\n" +
                        "ttt.\n" +
                        "tttt\n" +
                        "\n" +
                        "";
        assertEquals(String.format("\nEXPECT:\n%s\nACTUAL:\n%s\n", expect, space.toString()), expect, space.toString());
    }

    ///////////////////////////////////////////////////////////
    // Auto-generated tests for searcForFit()

    @Test(timeout = 3000)
    public void impossible_sff_1() {
        String solution = null;
        String space =
                "..\n" +
                        "..\n" +
                        ".|\n" +
                        "";
        String[][] shapes =
                {
                        {"aaa\n" +
                                "",
                                "a\n" +
                                        "a\n" +
                                        "a\n" +
                                        "",
                                "aaa\n" +
                                        "",
                                "a\n" +
                                        "a\n" +
                                        "a\n" +
                                        "",
                        },
                        {"b\n" +
                                "b\n" +
                                "b\n" +
                                "",
                                "bbb\n" +
                                        "",
                                "b\n" +
                                        "b\n" +
                                        "b\n" +
                                        "",
                                "bbb\n" +
                                        "",
                        },
                };
        performSearchForFit(solution, space, shapes);
    }

    @Test(timeout = 3000)
    public void impossible_sff_2() {
        String solution = null;
        String space =
                "....\n" +
                        "....\n" +
                        ".|..\n" +
                        "";
        String[][] shapes =
                {
                        {"b.\n" +
                                "bb\n" +
                                "bb\n" +
                                "",
                                "bbb\n" +
                                        "bb.\n" +
                                        "",
                                "bb\n" +
                                        "bb\n" +
                                        ".b\n" +
                                        "",
                                ".bb\n" +
                                        "bbb\n" +
                                        "",
                        },
                        {"aaa\n" +
                                "aaa\n" +
                                "",
                                "aa\n" +
                                        "aa\n" +
                                        "aa\n" +
                                        "",
                                "aaa\n" +
                                        "aaa\n" +
                                        "",
                                "aa\n" +
                                        "aa\n" +
                                        "aa\n" +
                                        "",
                        },
                };
        performSearchForFit(solution, space, shapes);
    }

    @Test(timeout = 3000)
    public void impossible_sff_3() {
        String solution = null;
        String space =
                "....\n" +
                        "....\n" +
                        ".|..\n" +
                        ".||.\n" +
                        "....\n" +
                        "";
        String[][] shapes =
                {
                        {"aa\n" +
                                "aa\n" +
                                "aa\n" +
                                "",
                                "aaa\n" +
                                        "aaa\n" +
                                        "",
                                "aa\n" +
                                        "aa\n" +
                                        "aa\n" +
                                        "",
                                "aaa\n" +
                                        "aaa\n" +
                                        "",
                        },
                        {".bb\n" +
                                "bbb\n" +
                                "",
                                "b.\n" +
                                        "bb\n" +
                                        "bb\n" +
                                        "",
                                "bbb\n" +
                                        "bb.\n" +
                                        "",
                                "bb\n" +
                                        "bb\n" +
                                        ".b\n" +
                                        "",
                        },
                        {"c.\n" +
                                "cc\n" +
                                "c.\n" +
                                "",
                                "ccc\n" +
                                        ".c.\n" +
                                        "",
                                ".c\n" +
                                        "cc\n" +
                                        ".c\n" +
                                        "",
                                ".c.\n" +
                                        "ccc\n" +
                                        "",
                        },
                };
        performSearchForFit(solution, space, shapes);
    }

    @Test(timeout = 3000)
    public void auto_sff_1() {
        String solution =
                "abc\n" +
                        "";
        String space =
                "...\n" +
                        "";
        String[][] shapes =
                {
                        {"c\n" +
                                "",
                                "c\n" +
                                        "",
                                "c\n" +
                                        "",
                                "c\n" +
                                        "",
                        },
                        {"b\n" +
                                "",
                                "b\n" +
                                        "",
                                "b\n" +
                                        "",
                                "b\n" +
                                        "",
                        },
                        {"a\n" +
                                "",
                                "a\n" +
                                        "",
                                "a\n" +
                                        "",
                                "a\n" +
                                        "",
                        },
                };
        performSearchForFit(solution, space, shapes);
    }

    @Test(timeout = 3000)
    public void auto_sff_2() {
        String solution =
                "abc\n" +
                        "aa|\n" +
                        "";
        String space =
                "...\n" +
                        "..|\n" +
                        "";
        String[][] shapes =
                {
                        {"aa\n" +
                                "a.\n" +
                                "",
                                "aa\n" +
                                        ".a\n" +
                                        "",
                                ".a\n" +
                                        "aa\n" +
                                        "",
                                "a.\n" +
                                        "aa\n" +
                                        "",
                        },
                        {"b\n" +
                                "",
                                "b\n" +
                                        "",
                                "b\n" +
                                        "",
                                "b\n" +
                                        "",
                        },
                        {"c\n" +
                                "",
                                "c\n" +
                                        "",
                                "c\n" +
                                        "",
                                "c\n" +
                                        "",
                        },
                };
        performSearchForFit(solution, space, shapes);
    }

    @Test(timeout = 3000)
    public void auto_sff_3() {
        String solution =
                "abcc\n" +
                        "aa|c\n" +
                        "dddd\n" +
                        "";
        String space =
                "....\n" +
                        "..|.\n" +
                        "....\n" +
                        "";
        String[][] shapes =
                {
                        {"cc\n" +
                                ".c\n" +
                                "",
                                ".c\n" +
                                        "cc\n" +
                                        "",
                                "c.\n" +
                                        "cc\n" +
                                        "",
                                "cc\n" +
                                        "c.\n" +
                                        "",
                        },
                        {"a.\n" +
                                "aa\n" +
                                "",
                                "aa\n" +
                                        "a.\n" +
                                        "",
                                "aa\n" +
                                        ".a\n" +
                                        "",
                                ".a\n" +
                                        "aa\n" +
                                        "",
                        },
                        {"b\n" +
                                "",
                                "b\n" +
                                        "",
                                "b\n" +
                                        "",
                                "b\n" +
                                        "",
                        },
                        {"dddd\n" +
                                "",
                                "d\n" +
                                        "d\n" +
                                        "d\n" +
                                        "d\n" +
                                        "",
                                "dddd\n" +
                                        "",
                                "d\n" +
                                        "d\n" +
                                        "d\n" +
                                        "d\n" +
                                        "",
                        },
                };
        performSearchForFit(solution, space, shapes);
    }

    @Test(timeout = 3000)
    public void auto_sff_4() {
        String solution =
                "aaccbb.\n" +
                        "aacccbb\n" +
                        ".dddbb.\n" +
                        "";
        String space =
                ".......\n" +
                        ".......\n" +
                        ".......\n" +
                        "";
        String[][] shapes =
                {
                        {"b.b\n" +
                                "bbb\n" +
                                ".b.\n" +
                                "",
                                ".bb\n" +
                                        "bb.\n" +
                                        ".bb\n" +
                                        "",
                                ".b.\n" +
                                        "bbb\n" +
                                        "b.b\n" +
                                        "",
                                "bb.\n" +
                                        ".bb\n" +
                                        "bb.\n" +
                                        "",
                        },
                        {"aa\n" +
                                "aa\n" +
                                "",
                                "aa\n" +
                                        "aa\n" +
                                        "",
                                "aa\n" +
                                        "aa\n" +
                                        "",
                                "aa\n" +
                                        "aa\n" +
                                        "",
                        },
                        {"ccc\n" +
                                ".cc\n" +
                                "",
                                ".c\n" +
                                        "cc\n" +
                                        "cc\n" +
                                        "",
                                "cc.\n" +
                                        "ccc\n" +
                                        "",
                                "cc\n" +
                                        "cc\n" +
                                        "c.\n" +
                                        "",
                        },
                        {"ddd\n" +
                                "",
                                "d\n" +
                                        "d\n" +
                                        "d\n" +
                                        "",
                                "ddd\n" +
                                        "",
                                "d\n" +
                                        "d\n" +
                                        "d\n" +
                                        "",
                        },
                };
        performSearchForFit(solution, space, shapes);
    }

    @Test(timeout = 3000)
    public void auto_sff_5() {
        String solution =
                "ttt||ccc\n" +
                        "tttt||cc\n" +
                        "a|||bbbf\n" +
                        "aaa|bbbf\n" +
                        "";
        String space =
                "...||...\n" +
                        "....||..\n" +
                        ".|||....\n" +
                        "...|....\n" +
                        "";
        String[][] shapes =
                {
                        {"tttt\n" +
                                ".ttt\n" +
                                "",
                                ".t\n" +
                                        "tt\n" +
                                        "tt\n" +
                                        "tt\n" +
                                        "",
                                "ttt.\n" +
                                        "tttt\n" +
                                        "",
                                "tt\n" +
                                        "tt\n" +
                                        "tt\n" +
                                        "t.\n" +
                                        "",
                        },
                        {"ff\n" +
                                "",
                                "f\n" +
                                        "f\n" +
                                        "",
                                "ff\n" +
                                        "",
                                "f\n" +
                                        "f\n" +
                                        "",
                        },
                        {"cc.\n" +
                                "ccc\n" +
                                "",
                                "cc\n" +
                                        "cc\n" +
                                        "c.\n" +
                                        "",
                                "ccc\n" +
                                        ".cc\n" +
                                        "",
                                ".c\n" +
                                        "cc\n" +
                                        "cc\n" +
                                        "",
                        },
                        {"a..\n" +
                                "aaa\n" +
                                "",
                                "aa\n" +
                                        "a.\n" +
                                        "a.\n" +
                                        "",
                                "aaa\n" +
                                        "..a\n" +
                                        "",
                                ".a\n" +
                                        ".a\n" +
                                        "aa\n" +
                                        "",
                        },
                        {"bb\n" +
                                "bb\n" +
                                "bb\n" +
                                "",
                                "bbb\n" +
                                        "bbb\n" +
                                        "",
                                "bb\n" +
                                        "bb\n" +
                                        "bb\n" +
                                        "",
                                "bbb\n" +
                                        "bbb\n" +
                                        "",
                        },
                };
        performSearchForFit(solution, space, shapes);
    }

    @Test(timeout = 3000)
    public void auto_sff_6() {
        String solution =
                "aadddffe\n" +
                        "aaddccc|\n" +
                        "bbbbc|||\n" +
                        "";
        String space =
                "........\n" +
                        ".......|\n" +
                        ".....|||\n" +
                        "";
        String[][] shapes =
                {
                        {"cc\n" +
                                ".c\n" +
                                ".c\n" +
                                "",
                                "..c\n" +
                                        "ccc\n" +
                                        "",
                                "c.\n" +
                                        "c.\n" +
                                        "cc\n" +
                                        "",
                                "ccc\n" +
                                        "c..\n" +
                                        "",
                        },
                        {"e\n" +
                                "",
                                "e\n" +
                                        "",
                                "e\n" +
                                        "",
                                "e\n" +
                                        "",
                        },
                        {"b\n" +
                                "b\n" +
                                "b\n" +
                                "b\n" +
                                "",
                                "bbbb\n" +
                                        "",
                                "b\n" +
                                        "b\n" +
                                        "b\n" +
                                        "b\n" +
                                        "",
                                "bbbb\n" +
                                        "",
                        },
                        {"d.\n" +
                                "dd\n" +
                                "dd\n" +
                                "",
                                "ddd\n" +
                                        "dd.\n" +
                                        "",
                                "dd\n" +
                                        "dd\n" +
                                        ".d\n" +
                                        "",
                                ".dd\n" +
                                        "ddd\n" +
                                        "",
                        },
                        {"aa\n" +
                                "aa\n" +
                                "",
                                "aa\n" +
                                        "aa\n" +
                                        "",
                                "aa\n" +
                                        "aa\n" +
                                        "",
                                "aa\n" +
                                        "aa\n" +
                                        "",
                        },
                        {"ff\n" +
                                "",
                                "f\n" +
                                        "f\n" +
                                        "",
                                "ff\n" +
                                        "",
                                "f\n" +
                                        "f\n" +
                                        "",
                        },
                };
        performSearchForFit(solution, space, shapes);
    }

    @Test(timeout = 3000)
    public void auto_sff_7() {
        String solution =
                "|tTTTTss\n" +
                        "tt|||ss|\n" +
                        "it|.|err\n" +
                        "iiieeerr\n" +
                        "";
        String space =
                "|.......\n" +
                        "..|||..|\n" +
                        "..|.|...\n" +
                        "........\n" +
                        "";
        String[][] shapes =
                {
                        {"ttt\n" +
                                ".t.\n" +
                                "",
                                ".t\n" +
                                        "tt\n" +
                                        ".t\n" +
                                        "",
                                ".t.\n" +
                                        "ttt\n" +
                                        "",
                                "t.\n" +
                                        "tt\n" +
                                        "t.\n" +
                                        "",
                        },
                        {"T\n" +
                                "T\n" +
                                "T\n" +
                                "T\n" +
                                "",
                                "TTTT\n" +
                                        "",
                                "T\n" +
                                        "T\n" +
                                        "T\n" +
                                        "T\n" +
                                        "",
                                "TTTT\n" +
                                        "",
                        },
                        {"eee\n" +
                                "e..\n" +
                                "",
                                "ee\n" +
                                        ".e\n" +
                                        ".e\n" +
                                        "",
                                "..e\n" +
                                        "eee\n" +
                                        "",
                                "e.\n" +
                                        "e.\n" +
                                        "ee\n" +
                                        "",
                        },
                        {"s.\n" +
                                "ss\n" +
                                ".s\n" +
                                "",
                                ".ss\n" +
                                        "ss.\n" +
                                        "",
                                "s.\n" +
                                        "ss\n" +
                                        ".s\n" +
                                        "",
                                ".ss\n" +
                                        "ss.\n" +
                                        "",
                        },
                        {".i\n" +
                                ".i\n" +
                                "ii\n" +
                                "",
                                "i..\n" +
                                        "iii\n" +
                                        "",
                                "ii\n" +
                                        "i.\n" +
                                        "i.\n" +
                                        "",
                                "iii\n" +
                                        "..i\n" +
                                        "",
                        },
                        {"rr\n" +
                                "rr\n" +
                                "",
                                "rr\n" +
                                        "rr\n" +
                                        "",
                                "rr\n" +
                                        "rr\n" +
                                        "",
                                "rr\n" +
                                        "rr\n" +
                                        "",
                        },
                };
        performSearchForFit(solution, space, shapes);
    }


    ////////////////////////////////////////////////////////////////////////////////
    // Tests for main method

    @Test(timeout = 3000)
    public void auto_sff_8() {
        String solution =
                "|TTTTeee\n" +
                        "rr|||es|\n" +
                        "rr|i|.ss\n" +
                        "...iii.s\n" +
                        "";
        String space =
                "|.......\n" +
                        "..|||..|\n" +
                        "..|.|...\n" +
                        "........\n" +
                        "";
        String[][] shapes =
                {
                        {"rr\n" +
                                "rr\n" +
                                "",
                                "rr\n" +
                                        "rr\n" +
                                        "",
                                "rr\n" +
                                        "rr\n" +
                                        "",
                                "rr\n" +
                                        "rr\n" +
                                        "",
                        },
                        {"ee\n" +
                                ".e\n" +
                                ".e\n" +
                                "",
                                "..e\n" +
                                        "eee\n" +
                                        "",
                                "e.\n" +
                                        "e.\n" +
                                        "ee\n" +
                                        "",
                                "eee\n" +
                                        "e..\n" +
                                        "",
                        },
                        {".ss\n" +
                                "ss.\n" +
                                "",
                                "s.\n" +
                                        "ss\n" +
                                        ".s\n" +
                                        "",
                                ".ss\n" +
                                        "ss.\n" +
                                        "",
                                "s.\n" +
                                        "ss\n" +
                                        ".s\n" +
                                        "",
                        },
                        {"i..\n" +
                                "iii\n" +
                                "",
                                "ii\n" +
                                        "i.\n" +
                                        "i.\n" +
                                        "",
                                "iii\n" +
                                        "..i\n" +
                                        "",
                                ".i\n" +
                                        ".i\n" +
                                        "ii\n" +
                                        "",
                        },
                        {"T\n" +
                                "T\n" +
                                "T\n" +
                                "T\n" +
                                "",
                                "TTTT\n" +
                                        "",
                                "T\n" +
                                        "T\n" +
                                        "T\n" +
                                        "T\n" +
                                        "",
                                "TTTT\n" +
                                        "",
                        },
                };
        performSearchForFit(solution, space, shapes);
    }

    @Test(timeout = 3000)
    public void auto_sff_9() {
        {
            String solution =
                    "|TTTTeee\n" +
                            "rr|||es|\n" +
                            "rr|i|.ss\n" +
                            "...iii.s\n" +
                            "";
            String space =
                    "|.......\n" +
                            "..|||..|\n" +
                            "..|.|...\n" +
                            "........\n" +
                            "";
            String[][] shapes =
                    {
                            {"rr\n" +
                                    "rr\n" +
                                    "",
                                    "rr\n" +
                                            "rr\n" +
                                            "",
                                    "rr\n" +
                                            "rr\n" +
                                            "",
                                    "rr\n" +
                                            "rr\n" +
                                            "",
                            },
                            {"ee\n" +
                                    ".e\n" +
                                    ".e\n" +
                                    "",
                                    "..e\n" +
                                            "eee\n" +
                                            "",
                                    "e.\n" +
                                            "e.\n" +
                                            "ee\n" +
                                            "",
                                    "eee\n" +
                                            "e..\n" +
                                            "",
                            },
                            {".ss\n" +
                                    "ss.\n" +
                                    "",
                                    "s.\n" +
                                            "ss\n" +
                                            ".s\n" +
                                            "",
                                    ".ss\n" +
                                            "ss.\n" +
                                            "",
                                    "s.\n" +
                                            "ss\n" +
                                            ".s\n" +
                                            "",
                            },
                            {"i..\n" +
                                    "iii\n" +
                                    "",
                                    "ii\n" +
                                            "i.\n" +
                                            "i.\n" +
                                            "",
                                    "iii\n" +
                                            "..i\n" +
                                            "",
                                    ".i\n" +
                                            ".i\n" +
                                            "ii\n" +
                                            "",
                            },
                            {"T\n" +
                                    "T\n" +
                                    "T\n" +
                                    "T\n" +
                                    "",
                                    "TTTT\n" +
                                            "",
                                    "T\n" +
                                            "T\n" +
                                            "T\n" +
                                            "T\n" +
                                            "",
                                    "TTTT\n" +
                                            "",
                            },
                    };
            performSearchForFit(solution, space, shapes);
        }
        String solution =
                "aabbb\n" +
                        "acc|b\n" +
                        "";
        String space =
                ".....\n" +
                        "...|.\n" +
                        "";
        String[][] shapes =
                {
                        {"cc\n" +
                                "",
                                "c\n" +
                                        "c\n" +
                                        "",
                                "cc\n" +
                                        "",
                                "c\n" +
                                        "c\n" +
                                        "",
                        },
                        {"bbb\n" +
                                "..b\n" +
                                "",
                                ".b\n" +
                                        ".b\n" +
                                        "bb\n" +
                                        "",
                                "b..\n" +
                                        "bbb\n" +
                                        "",
                                "bb\n" +
                                        "b.\n" +
                                        "b.\n" +
                                        "",
                        },
                        {"aa\n" +
                                "a.\n" +
                                "",
                                "aa\n" +
                                        ".a\n" +
                                        "",
                                ".a\n" +
                                        "aa\n" +
                                        "",
                                "a.\n" +
                                        "aa\n" +
                                        "",
                        },
                };
        performSearchForFit(solution, space, shapes);
    }

    @Test(timeout = 3000)
    public void auto_sff_10() {
        String solution =
                "aabbb\n" +
                        "acc|b\n" +
                        ".dd|.\n" +
                        "";
        String space =
                ".....\n" +
                        "...|.\n" +
                        "...|.\n" +
                        "";
        String[][] shapes =
                {
                        {"dd\n" +
                                "",
                                "d\n" +
                                        "d\n" +
                                        "",
                                "dd\n" +
                                        "",
                                "d\n" +
                                        "d\n" +
                                        "",
                        },
                        {"aa\n" +
                                ".a\n" +
                                "",
                                ".a\n" +
                                        "aa\n" +
                                        "",
                                "a.\n" +
                                        "aa\n" +
                                        "",
                                "aa\n" +
                                        "a.\n" +
                                        "",
                        },
                        {"c\n" +
                                "c\n" +
                                "",
                                "cc\n" +
                                        "",
                                "c\n" +
                                        "c\n" +
                                        "",
                                "cc\n" +
                                        "",
                        },
                        {"bbb\n" +
                                "..b\n" +
                                "",
                                ".b\n" +
                                        ".b\n" +
                                        "bb\n" +
                                        "",
                                "b..\n" +
                                        "bbb\n" +
                                        "",
                                "bb\n" +
                                        "b.\n" +
                                        "b.\n" +
                                        "",
                        },
                };
        performSearchForFit(solution, space, shapes);
    }

    @Test(timeout = 3000)
    public void test_main_impossible1() {
        test_main_files("impossible1", false);
    }

    @Test(timeout = 3000)
    public void test_main_impossible2() {
        test_main_files("impossible2", false);
    }

    @Test(timeout = 3000)
    public void test_main_impossible3() {
        test_main_files("impossible3", false);
    }

    @Test(timeout = 3000)
    public void test_main01() {
        test_main_files("test-input01", true);
    }

    @Test(timeout = 3000)
    public void test_main02() {
        test_main_files("test-input02", true);
    }

    @Test(timeout = 3000)
    public void test_main03() {
        test_main_files("test-input03", true);
    }

    @Test(timeout = 3000)
    public void test_main04() {
        test_main_files("test-input04", true);
    }

    @Test(timeout = 3000)
    public void test_main05() {
        test_main_files("test-input05", true);
    }

    @Test(timeout = 3000)
    public void test_main06() {
        test_main_files("test-input06", true);
    }

    @Test(timeout = 3000)
    public void test_main07() {
        test_main_files("test-input07", true);
    }


    ////////////////////////////////////////////////////////////////////////////////
    // File Utilities

    @Test(timeout = 3000)
    public void test_main08() {
        test_main_files("test-input08", true);
    }

    @Test(timeout = 3000)
    public void test_main09() {
        test_main_files("test-input09", true);
    }

    @Test(timeout = 3000)
    public void test_main10() {
        test_main_files("test-input10", true);
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Utilities to append columns of strings
    ////////////////////////////////////////////////////////////////////////////////

    // Ensure the test directory exists and is properly initiliazed
    public void ensureTestDirExists() {
        this.testDir = new File(testingDirectory);
        if (testDir.exists()) {
            testDir = testDir.getAbsoluteFile();
            return;
        }
        String msg =
                String.format("Could not locate the testing directory %s (%s)",
                        testingDirectory, testDir.getAbsolutePath());
        throw new RuntimeException(msg);
    }

    // Test whether main method output to a named file is what is
    // expected.
    public void test_main_files(String fileBase, boolean solutionExists) {
        try {
            ensureTestDirExists();
            File inputFile = new File(testDir, fileBase + ".txt");
            File actualFile = new File(testDir, fileBase + ".actual");
            File expectFile = new File(testDir, fileBase + ".expect");

            String fileS = inputFile.toString();
            String expectFileS = expectFile.toString();
            String actualFileS = actualFile.toString();

            // Run the main method
            FitIt.main(new String[]{fileS, actualFileS});

            String input = slurp(fileS);
            String expect = slurp(expectFileS);
            String actual = slurp(actualFileS);
            actual = actual.replaceAll("\r\n", "\n");

            boolean solutionFound = actual.contains("FOUND FIT");
            boolean noSolutionFound = actual.contains("NO FIT FOUND");


            if (solutionExists && (!solutionFound || noSolutionFound)) {
                failFmt("A solution exists but was not found\nInput File:\n%s\nSample Output:\n%s\nActual output:%s\n",
                        input, expect, actual);
            }
            if (!solutionExists && (solutionFound || !noSolutionFound)) {
                failFmt("No solution exists but one was reported\nInput File:\n%s\nSample Output:\n%s\nActual output:%s\n",
                        input, expect, actual);
            }

            // String outputComparison = simpleDiff2("EXPECT\n------\n"+expect,
            //                                       "ACTUAL\n------\n"+actual);
            // String msg =
            //   String.format("Test: %s\nActual output does not match expected output\n"+
            //                 "Input File:\n%s\nOutput:\n%s\n",
            //                 fileBase,input,outputComparison);
            // assertEquals(msg,expect,actual);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    class ShapePlace {
        public int row, col, rot;
        public Shape shape;

        public ShapePlace(String layout, int row, int col, int rot) {
            this.row = row;
            this.col = col;
            this.rot = rot;
            this.shape = FitIt.makeShape(layout, getDisplayChar(layout));
            for (int i = 0; i < rot; i++) {
                shape.rotateCW();
            }
        }

        public String toString() {
            return String.format("\n%sPlaced at %s %s\n", shape.toString(), row, col);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Honors section tests
    // Uncomment here to enablet the honors section tests

/*
  public static void verify_searchForAllFits(String spLayout, String [] shLayouts,
                                             String [] expectA){
    Space space = FitIt.makeSpace(spLayout);
    ArrayList<Shape> shapes = new ArrayList<Shape>();
    for(String shLayout : shLayouts){
      Shape shape = FitIt.makeShape(shLayout,getDisplayChar(shLayout));
      shapes.add(shape);
    }
    ArrayList<String> actual = new ArrayList<String>();
    FitIt.searchForAllFits(space,shapes,actual);
    Collections.sort(actual);
    Arrays.sort(expectA);
    List<String> expect = Arrays.asList(expectA);
    String expectS = "";
    for(String s : expect){
      expectS += s +"\n";
    }
    String actualS = "";
    for(String s : actual){
      actualS += s +"\n";
    }
    String msg =
      String.format("\nEXPECT         ACTUAL\n%s",simpleDiff2(expectS,actualS));

    assertEquals(msg,expect, actual);
  }

  @Test(timeout=3000) public void test_all_fits1(){
    String space =
      "........\n"+
      ".......|\n"+
      ".....|||\n"+
      "";
    String [] shapes = {
      "aa\n"+
      "aa\n"+
      "",
      "bbbb\n"+
      "",
      "c.\n"+
      "c.\n"+
      "cc\n"+
      "",      
      ".dd\n"+
      "ddd\n"+
      "",
      "e\n"+
      "",
      "f\n"+
      "f\n"+
      "",
    };
    String [] expect = {
      "aabbbbfe\n"+
      "aacdddf|\n"+
      "cccdd|||\n"+
      "",
      "aabbbbff\n"+
      "aacddde|\n"+
      "cccdd|||\n"+
      "",
      "aabbbbff\n"+
      "aaddccc|\n"+
      "edddc|||\n"+
      "",
      "aacccddd\n"+
      "aacefdd|\n"+
      "bbbbf|||\n"+
      "",
      "aacccddd\n"+
      "aacffdd|\n"+
      "bbbbe|||\n"+
      "",
      "aacccddd\n"+
      "aacffdd|\n"+
      "ebbbb|||\n"+
      "",
      "aadbbbbe\n"+
      "aaddccc|\n"+
      "ffddc|||\n"+
      "",
      "aaddbbbb\n"+
      "aaddccc|\n"+
      "effdc|||\n"+
      "",
      "aaddbbbb\n"+
      "aaddccc|\n"+
      "ffedc|||\n"+
      "",
      "aadddccc\n"+
      "aaddfce|\n"+
      "bbbbf|||\n"+
      "",
      "aadddeff\n"+
      "aaddccc|\n"+
      "bbbbc|||\n"+
      "",
      "aadddffe\n"+
      "aaddccc|\n"+
      "bbbbc|||\n"+
      "",
      "aadebbbb\n"+
      "aaddccc|\n"+
      "ffddc|||\n"+
      "",
      "aaeccddd\n"+
      "aaffcdd|\n"+
      "bbbbc|||\n"+
      "",
      "aaedbbbb\n"+
      "aacddff|\n"+
      "cccdd|||\n"+
      "",
      "aafccddd\n"+
      "aafecdd|\n"+
      "bbbbc|||\n"+
      "",
      "aafddccc\n"+
      "aafddce|\n"+
      "bbbbd|||\n"+
      "",
      "aaffbbbb\n"+
      "aacddde|\n"+
      "cccdd|||\n"+
      "",
      "aaffbbbb\n"+
      "aaddccc|\n"+
      "edddc|||\n"+
      "",
      "aaffcddd\n"+
      "aacccdd|\n"+
      "bbbbe|||\n"+
      "",
      "aaffcddd\n"+
      "aacccdd|\n"+
      "ebbbb|||\n"+
      "",
      "bbbbcddd\n"+
      "aacccdd|\n"+
      "aaeff|||\n"+
      "",
      "bbbbcddd\n"+
      "aacccdd|\n"+
      "aaffe|||\n"+
      "",
      "bbbbeddd\n"+
      "aacccdd|\n"+
      "aacff|||\n"+
      "",
      "bbbbeddd\n"+
      "aaffcdd|\n"+
      "aaccc|||\n"+
      "",
      "bbbbeddd\n"+
      "cccaadd|\n"+
      "cffaa|||\n"+
      "",
      "bbbbeddd\n"+
      "ffcaadd|\n"+
      "cccaa|||\n"+
      "",
      "caabbbbe\n"+
      "caaddff|\n"+
      "ccddd|||\n"+
      "",
      "caadbbbb\n"+
      "caaddff|\n"+
      "ccedd|||\n"+
      "",
      "caaebbbb\n"+
      "caaddff|\n"+
      "ccddd|||\n"+
      "",
      "cbbbbaae\n"+
      "cffddaa|\n"+
      "ccddd|||\n"+
      "",
      "cbbbbddd\n"+
      "ceaafdd|\n"+
      "ccaaf|||\n"+
      "",
      "cbbbbddd\n"+
      "cefaadd|\n"+
      "ccfaa|||\n"+
      "",
      "cbbbbddd\n"+
      "cffaadd|\n"+
      "cceaa|||\n"+
      "",
      "cccaaddd\n"+
      "cffaadd|\n"+
      "bbbbe|||\n"+
      "",
      "cccaaddd\n"+
      "cffaadd|\n"+
      "ebbbb|||\n"+
      "",
      "cccbbbbe\n"+
      "cddaaff|\n"+
      "dddaa|||\n"+
      "",
      "cccdbbbb\n"+
      "caaddff|\n"+
      "eaadd|||\n"+
      "",
      "cccddaae\n"+
      "cffddaa|\n"+
      "bbbbd|||\n"+
      "",
      "cccebbbb\n"+
      "cddaaff|\n"+
      "dddaa|||\n"+
      "",
      "cddbbbbe\n"+
      "cddaaff|\n"+
      "ccdaa|||\n"+
      "",
      "cdddbbbb\n"+
      "cddaaff|\n"+
      "cceaa|||\n"+
      "",
      "cddebbbb\n"+
      "cddaaff|\n"+
      "ccdaa|||\n"+
      "",
      "dbbbbaae\n"+
      "ddcccaa|\n"+
      "ddcff|||\n"+
      "",
      "dbbbbaae\n"+
      "ddffcaa|\n"+
      "ddccc|||\n"+
      "",
      "dbbbbccc\n"+
      "ddaafce|\n"+
      "ddaaf|||\n"+
      "",
      "dbbbbccc\n"+
      "ddfaace|\n"+
      "ddfaa|||\n"+
      "",
      "dbbbbeff\n"+
      "ddaaccc|\n"+
      "ddaac|||\n"+
      "",
      "dbbbbffe\n"+
      "ddaaccc|\n"+
      "ddaac|||\n"+
      "",
      "dccbbbbe\n"+
      "ddcaaff|\n"+
      "ddcaa|||\n"+
      "",
      "dccebbbb\n"+
      "ddcaaff|\n"+
      "ddcaa|||\n"+
      "",
      "ddaabbbb\n"+
      "ddaaccc|\n"+
      "edffc|||\n"+
      "",
      "ddaabbbb\n"+
      "ddaacff|\n"+
      "edccc|||\n"+
      "",
      "ddbbbbff\n"+
      "ddaaccc|\n"+
      "edaac|||\n"+
      "",
      "dddbbbbe\n"+
      "ddaaccc|\n"+
      "ffaac|||\n"+
      "",
      "dddbbbbe\n"+
      "ddcaaff|\n"+
      "cccaa|||\n"+
      "",
      "dddccaae\n"+
      "ddffcaa|\n"+
      "bbbbc|||\n"+
      "",
      "dddebbbb\n"+
      "ddaaccc|\n"+
      "ffaac|||\n"+
      "",
      "dddebbbb\n"+
      "ddcaaff|\n"+
      "cccaa|||\n"+
      "",
      "ddffbbbb\n"+
      "ddaaccc|\n"+
      "edaac|||\n"+
      "",
      "deaabbbb\n"+
      "ddaaccc|\n"+
      "ddffc|||\n"+
      "",
      "deaabbbb\n"+
      "ddaacff|\n"+
      "ddccc|||\n"+
      "",
      "debbbbff\n"+
      "ddaaccc|\n"+
      "ddaac|||\n"+
      "",
      "deffbbbb\n"+
      "ddaaccc|\n"+
      "ddaac|||\n"+
      "",
      "dffbbbbe\n"+
      "ddaaccc|\n"+
      "ddaac|||\n"+
      "",
      "dffebbbb\n"+
      "ddaaccc|\n"+
      "ddaac|||\n"+
      "",
      "ebbbbddd\n"+
      "aacccdd|\n"+
      "aacff|||\n"+
      "",
      "ebbbbddd\n"+
      "aaffcdd|\n"+
      "aaccc|||\n"+
      "",
      "ebbbbddd\n"+
      "cccaadd|\n"+
      "cffaa|||\n"+
      "",
      "ebbbbddd\n"+
      "ffcaadd|\n"+
      "cccaa|||\n"+
      "",
      "eccdbbbb\n"+
      "aacddff|\n"+
      "aacdd|||\n"+
      "",
      "effaaddd\n"+
      "cccaadd|\n"+
      "cbbbb|||\n"+
      "",
      "faaccddd\n"+
      "faaecdd|\n"+
      "bbbbc|||\n"+
      "",
      "faaddccc\n"+
      "faaddce|\n"+
      "bbbbd|||\n"+
      "",
      "fbbbbccc\n"+
      "fddaace|\n"+
      "dddaa|||\n"+
      "",
      "fbbbbddd\n"+
      "fecaadd|\n"+
      "cccaa|||\n"+
      "",
      "ffcaaddd\n"+
      "cccaadd|\n"+
      "bbbbe|||\n"+
      "",
      "ffcaaddd\n"+
      "cccaadd|\n"+
      "ebbbb|||\n"+
      "",
      "ffcddaae\n"+
      "cccddaa|\n"+
      "bbbbd|||\n"+
      "",
      "ffdbbbbe\n"+
      "aaddccc|\n"+
      "aaddc|||\n"+
      "",
      "ffddbbbb\n"+
      "aaddccc|\n"+
      "aaedc|||\n"+
      "",
      "ffdebbbb\n"+
      "aaddccc|\n"+
      "aaddc|||\n"+
      "",
      "ffeaaddd\n"+
      "cccaadd|\n"+
      "cbbbb|||\n"+
      "",
    };
    verify_searchForAllFits(space,shapes,expect);
  }
      
  @Test(timeout=3000) public void test_all_fits2(){
    String space =
      "|.......\n"+
      "..|||..|\n"+
      "..|.|...\n"+
      "........\n"+
      "";
    String [] shapes = {
      "TTTT\n"+
      "",
      "..e\n"+
      "eee\n"+
      "",
      ".t.\n"+
      "ttt\n"+
      "",
      "rr\n"+
      "rr\n"+
      "",
      "i..\n"+
      "iii\n"+
      "",
      ".ss\n"+
      "ss.\n"+
      "",
    };
    String [] expect = {
      "|eeeTTTT\n"+
      "se|||ii|\n"+
      "ss|t|irr\n"+
      ".stttirr\n"+
      "",
      "|tTTTTss\n"+
      "tt|||ss|\n"+
      "it|.|err\n"+
      "iiieeerr\n"+
      "",
    };
    verify_searchForAllFits(space,shapes,expect);
  }

  @Test(timeout=3000) public void test_all_fits3(){
    String space =
      "|.......\n"+
      "..|||..|\n"+
      "..|.|...\n"+
      "........\n"+
      "";
    String [] shapes = {
      "TTTT\n"+
      "",
      "..e\n"+
      "eee\n"+
      "",
      "rr\n"+
      "rr\n"+
      "",
      "i..\n"+
      "iii\n"+
      "",
      ".ss\n"+
      "ss.\n"+
      "",
    };
    String [] expect = {
      "|...TTTT\n"+
      "rr|||is|\n"+
      "rr|e|iss\n"+
      ".eeeii.s\n"+
      "",
      "|..TTTT.\n"+
      "rr|||is|\n"+
      "rr|e|iss\n"+
      ".eeeii.s\n"+
      "",
      "|.TTTT..\n"+
      "rr|||is|\n"+
      "rr|e|iss\n"+
      ".eeeii.s\n"+
      "",
      "|.TTTTss\n"+
      "..|||ss|\n"+
      "i.|.|err\n"+
      "iiieeerr\n"+
      "",
      "|.TTTTss\n"+
      ".e|||ss|\n"+
      ".e|i|.rr\n"+
      ".eeiiirr\n"+
      "",
      "|.TTTTss\n"+
      ".i|||ss|\n"+
      ".i|.|err\n"+
      "ii.eeerr\n"+
      "",
      "|.TTTTss\n"+
      "e.|||ss|\n"+
      "e.|i|.rr\n"+
      "ee.iiirr\n"+
      "",
      "|.TTTTss\n"+
      "ee|||ss|\n"+
      ".e|i|.rr\n"+
      ".e.iiirr\n"+
      "",
      "|.TTTTss\n"+
      "ii|||ss|\n"+
      "i.|.|err\n"+
      "i..eeerr\n"+
      "",
      "|.TTTTss\n"+
      "ii|||ss|\n"+
      "i.|e|.rr\n"+
      "ieee..rr\n"+
      "",
      "|.TTTTss\n"+
      "ii|||ss|\n"+
      "i.|e|rr.\n"+
      "ieee.rr.\n"+
      "",
      "|.TTTTss\n"+
      "rr|||ss|\n"+
      "rr|e|i..\n"+
      ".eee.iii\n"+
      "",
      "|.TTTTss\n"+
      "rr|||ss|\n"+
      "rr|e|iii\n"+
      ".eee...i\n"+
      "",
      "|TTTT...\n"+
      "rr|||is|\n"+
      "rr|e|iss\n"+
      ".eeeii.s\n"+
      "",
      "|TTTT.ss\n"+
      "..|||ss|\n"+
      "i.|.|err\n"+
      "iiieeerr\n"+
      "",
      "|TTTT.ss\n"+
      ".e|||ss|\n"+
      ".e|i|.rr\n"+
      ".eeiiirr\n"+
      "",
      "|TTTT.ss\n"+
      ".i|||ss|\n"+
      ".i|.|err\n"+
      "ii.eeerr\n"+
      "",
      "|TTTT.ss\n"+
      "e.|||ss|\n"+
      "e.|i|.rr\n"+
      "ee.iiirr\n"+
      "",
      "|TTTT.ss\n"+
      "ee|||ss|\n"+
      ".e|i|.rr\n"+
      ".e.iiirr\n"+
      "",
      "|TTTT.ss\n"+
      "ii|||ss|\n"+
      "i.|.|err\n"+
      "i..eeerr\n"+
      "",
      "|TTTT.ss\n"+
      "ii|||ss|\n"+
      "i.|e|.rr\n"+
      "ieee..rr\n"+
      "",
      "|TTTT.ss\n"+
      "ii|||ss|\n"+
      "i.|e|rr.\n"+
      "ieee.rr.\n"+
      "",
      "|TTTT.ss\n"+
      "rr|||ss|\n"+
      "rr|e|i..\n"+
      ".eee.iii\n"+
      "",
      "|TTTT.ss\n"+
      "rr|||ss|\n"+
      "rr|e|iii\n"+
      ".eee...i\n"+
      "",
      "|TTTTeee\n"+
      "..|||es|\n"+
      "rr|i|.ss\n"+
      "rr.iii.s\n"+
      "",
      "|TTTTeee\n"+
      "rr|||es|\n"+
      "rr|i|.ss\n"+
      "...iii.s\n"+
      "",
      "|TTTTeee\n"+
      "s.|||e.|\n"+
      "ss|i|.rr\n"+
      ".s.iiirr\n"+
      "",
      "|TTTTii.\n"+
      "rr|||i.|\n"+
      "rr|e|iss\n"+
      ".eee.ss.\n"+
      "",
      "|TTTTii.\n"+
      "rr|||is|\n"+
      "rr|e|iss\n"+
      ".eee...s\n"+
      "",
      "|TTTTrr.\n"+
      "ii|||rr|\n"+
      "i.|e|.ss\n"+
      "ieee.ss.\n"+
      "",
      "|TTTTrr.\n"+
      "ii|||rr|\n"+
      "i.|e|ss.\n"+
      "ieeess..\n"+
      "",
      "|TTTTs..\n"+
      "rr|||ss|\n"+
      "rr|e|is.\n"+
      ".eee.iii\n"+
      "",
      "|eee..ss\n"+
      ".e|||ss|\n"+
      "rr|.|iii\n"+
      "rr.TTTTi\n"+
      "",
      "|eee..ss\n"+
      ".e|||ss|\n"+
      "rr|.|iii\n"+
      "rrTTTT.i\n"+
      "",
      "|eee.ii.\n"+
      ".e|||is|\n"+
      "rr|.|iss\n"+
      "rr.TTTTs\n"+
      "",
      "|eee.ii.\n"+
      ".e|||is|\n"+
      "rr|.|iss\n"+
      "rrTTTT.s\n"+
      "",
      "|eee.ii.\n"+
      "se|||i.|\n"+
      "ss|.|irr\n"+
      ".sTTTTrr\n"+
      "",
      "|eee.rr.\n"+
      "se|||rr|\n"+
      "ss|.|iii\n"+
      ".s.TTTTi\n"+
      "",
      "|eee.rr.\n"+
      "se|||rr|\n"+
      "ss|.|iii\n"+
      ".sTTTT.i\n"+
      "",
      "|eeeTTTT\n"+
      ".e|||.s|\n"+
      "rr|i|.ss\n"+
      "rr.iii.s\n"+
      "",
      "|eeeTTTT\n"+
      ".e|||is|\n"+
      "rr|.|iss\n"+
      "rr..ii.s\n"+
      "",
      "|eeeTTTT\n"+
      ".e|||s.|\n"+
      "rr|i|ss.\n"+
      "rr.iiis.\n"+
      "",
      "|eeeTTTT\n"+
      "se|||..|\n"+
      "ss|i|.rr\n"+
      ".s.iiirr\n"+
      "",
      "|eeeTTTT\n"+
      "se|||i.|\n"+
      "ss|.|irr\n"+
      ".s..iirr\n"+
      "",
      "|eeeTTTT\n"+
      "se|||ii|\n"+
      "ss|.|irr\n"+
      ".s...irr\n"+
      "",
      "|eeeTTTT\n"+
      "se|||rr|\n"+
      "ss|i|rr.\n"+
      ".s.iii..\n"+
      "",
      "|eeeiii.\n"+
      ".e|||si|\n"+
      "rr|.|ss.\n"+
      "rrTTTTs.\n"+
      "",
      "|eeeiii.\n"+
      "se|||.i|\n"+
      "ss|.|.rr\n"+
      ".sTTTTrr\n"+
      "",
      "|iTTTTss\n"+
      ".i|||ss|\n"+
      "ii|.|err\n"+
      "...eeerr\n"+
      "",
      "|iTTTTss\n"+
      ".i|||ss|\n"+
      "ii|e|.rr\n"+
      ".eee..rr\n"+
      "",
      "|iTTTTss\n"+
      ".i|||ss|\n"+
      "ii|e|rr.\n"+
      ".eee.rr.\n"+
      "",
      "|ii...ss\n"+
      "ei|||ss|\n"+
      "ei|.|.rr\n"+
      "eeTTTTrr\n"+
      "",
      "|ss..eee\n"+
      "ss|||e.|\n"+
      "rr|.|iii\n"+
      "rr.TTTTi\n"+
      "",
      "|ss..eee\n"+
      "ss|||e.|\n"+
      "rr|.|iii\n"+
      "rrTTTT.i\n"+
      "",
      "|ss..ii.\n"+
      "ss|||ie|\n"+
      "rr|.|ie.\n"+
      "rrTTTTee\n"+
      "",
      "|ss.TTTT\n"+
      "ss|||..|\n"+
      "i.|.|err\n"+
      "iiieeerr\n"+
      "",
      "|ss.TTTT\n"+
      "ss|||.e|\n"+
      "rr|i|.e.\n"+
      "rr.iiiee\n"+
      "",
      "|ss.TTTT\n"+
      "ss|||ee|\n"+
      "rr|i|.e.\n"+
      "rr.iiie.\n"+
      "",
      "|ss.TTTT\n"+
      "ss|||i.|\n"+
      "..|e|irr\n"+
      ".eeeiirr\n"+
      "",
      "|ss.TTTT\n"+
      "ss|||ie|\n"+
      "rr|.|ie.\n"+
      "rr..iiee\n"+
      "",
      "|ss.TTTT\n"+
      "ss|||ii|\n"+
      "..|e|irr\n"+
      ".eee.irr\n"+
      "",
      "|ss.TTTT\n"+
      "ss|||rr|\n"+
      ".i|.|rre\n"+
      ".iii.eee\n"+
      "",
      "|ss.TTTT\n"+
      "ss|||rr|\n"+
      "i.|.|rre\n"+
      "iii..eee\n"+
      "",
      "|ss.eeT.\n"+
      "ss|||eT|\n"+
      "rr|i|eT.\n"+
      "rr.iiiT.\n"+
      "",
      "|ss.eeii\n"+
      "ss|||ei|\n"+
      "rr|.|ei.\n"+
      "rr..TTTT\n"+
      "",
      "|ss.eeii\n"+
      "ss|||ei|\n"+
      "rr|.|ei.\n"+
      "rr.TTTT.\n"+
      "",
      "|ss.eeii\n"+
      "ss|||ei|\n"+
      "rr|.|ei.\n"+
      "rrTTTT..\n"+
      "",
      "|ssTTTT.\n"+
      "ss|||..|\n"+
      "i.|.|err\n"+
      "iiieeerr\n"+
      "",
      "|ssTTTT.\n"+
      "ss|||.e|\n"+
      "rr|i|.e.\n"+
      "rr.iiiee\n"+
      "",
      "|ssTTTT.\n"+
      "ss|||ee|\n"+
      "rr|i|.e.\n"+
      "rr.iiie.\n"+
      "",
      "|ssTTTT.\n"+
      "ss|||i.|\n"+
      "..|e|irr\n"+
      ".eeeiirr\n"+
      "",
      "|ssTTTT.\n"+
      "ss|||ie|\n"+
      "rr|.|ie.\n"+
      "rr..iiee\n"+
      "",
      "|ssTTTT.\n"+
      "ss|||ii|\n"+
      "..|e|irr\n"+
      ".eee.irr\n"+
      "",
      "|ssTTTT.\n"+
      "ss|||rr|\n"+
      ".i|.|rre\n"+
      ".iii.eee\n"+
      "",
      "|ssTTTT.\n"+
      "ss|||rr|\n"+
      "i.|.|rre\n"+
      "iii..eee\n"+
      "",
      "|ssiii..\n"+
      "ss|||ie|\n"+
      "rr|.|.e.\n"+
      "rrTTTTee\n"+
      "",
      "|ssiiiT.\n"+
      "ss|||iT|\n"+
      "rr|.|eT.\n"+
      "rr.eeeT.\n"+
      "",
      "|ssiiie.\n"+
      "ss|||ie|\n"+
      "rr|.|.ee\n"+
      "rr..TTTT\n"+
      "",
      "|ssiiie.\n"+
      "ss|||ie|\n"+
      "rr|.|.ee\n"+
      "rr.TTTT.\n"+
      "",
      "|ssiiie.\n"+
      "ss|||ie|\n"+
      "rr|.|.ee\n"+
      "rrTTTT..\n"+
      "",
    };
    verify_searchForAllFits(space,shapes,expect);
  }

  @Test(timeout=3000) public void test_all_fits4(){
    String space =
      ".....\n"+
      "...|.\n"+
      "";
    String [] shapes = {
      "aa\n"+
      ".a\n"+
      "",
      "bbb\n"+
      "..b\n"+
      "",
      "c\n"+
      "c\n"+
      "",
    };
    String [] expect = {
      "aabbb\n"+
      "acc|b\n"+
      "",
      "bbbaa\n"+
      "ccb|a\n"+
      "",
      "bccaa\n"+
      "bbb|a\n"+
      "",
      "cabbb\n"+
      "caa|b\n"+
      "",
    };
    verify_searchForAllFits(space,shapes,expect);
  }

  @Test(timeout=3000) public void test_all_fits5(){
    String space =
      ".....\n"+
      "...|.\n"+
      "...|.\n"+
      "";
    String [] shapes = {
      "aa\n"+
      ".a\n"+
      "",
      "bbb\n"+
      "..b\n"+
      "",
      "c\n"+
      "c\n"+
      "",
      "dd\n"+
      "",
    };
    String [] expect = {
      "..acc\n"+
      "baa|d\n"+
      "bbb|d\n"+
      "",
      "..add\n"+
      "baa|c\n"+
      "bbb|c\n"+
      "",
      "..bcc\n"+
      "aab|d\n"+
      "abb|d\n"+
      "",
      "..bdd\n"+
      "aab|c\n"+
      "abb|c\n"+
      "",
      ".a.cc\n"+
      "baa|d\n"+
      "bbb|d\n"+
      "",
      ".a.dd\n"+
      "baa|c\n"+
      "bbb|c\n"+
      "",
      ".aacc\n"+
      "b.a|d\n"+
      "bbb|d\n"+
      "",
      ".aacc\n"+
      "ba.|d\n"+
      "bbb|d\n"+
      "",
      ".aadd\n"+
      "b.a|c\n"+
      "bbb|c\n"+
      "",
      ".aadd\n"+
      "ba.|c\n"+
      "bbb|c\n"+
      "",
      ".abb.\n"+
      "aab|c\n"+
      "ddb|c\n"+
      "",
      ".abb.\n"+
      "aab|d\n"+
      "ccb|d\n"+
      "",
      ".abbb\n"+
      "aac|b\n"+
      "ddc|.\n"+
      "",
      ".abbb\n"+
      "aad|b\n"+
      "ccd|.\n"+
      "",
      ".abbb\n"+
      "caa|b\n"+
      "cdd|.\n"+
      "",
      ".abbb\n"+
      "daa|b\n"+
      "dcc|.\n"+
      "",
      ".abbc\n"+
      "aab|c\n"+
      "ddb|.\n"+
      "",
      ".abbd\n"+
      "aab|d\n"+
      "ccb|.\n"+
      "",
      ".abcc\n"+
      "aab|d\n"+
      ".bb|d\n"+
      "",
      ".abdd\n"+
      "aab|c\n"+
      ".bb|c\n"+
      "",
      ".acc.\n"+
      "baa|d\n"+
      "bbb|d\n"+
      "",
      ".accd\n"+
      "baa|d\n"+
      "bbb|.\n"+
      "",
      ".add.\n"+
      "baa|c\n"+
      "bbb|c\n"+
      "",
      ".addc\n"+
      "baa|c\n"+
      "bbb|.\n"+
      "",
      ".bbaa\n"+
      "cbd|a\n"+
      "cbd|.\n"+
      "",
      ".bbaa\n"+
      "dbc|a\n"+
      "dbc|.\n"+
      "",
      ".caa.\n"+
      "bca|d\n"+
      "bbb|d\n"+
      "",
      ".caad\n"+
      "bca|d\n"+
      "bbb|.\n"+
      "",
      ".cbaa\n"+
      "dcb|a\n"+
      "dbb|.\n"+
      "",
      ".cbb.\n"+
      "acb|d\n"+
      "aab|d\n"+
      "",
      ".cbbb\n"+
      "acd|b\n"+
      "aad|.\n"+
      "",
      ".cbbb\n"+
      "dca|b\n"+
      "daa|.\n"+
      "",
      ".cbbd\n"+
      "acb|d\n"+
      "aab|.\n"+
      "",
      ".ccaa\n"+
      "bbb|a\n"+
      "ddb|.\n"+
      "",
      ".ccaa\n"+
      "bdd|a\n"+
      "bbb|.\n"+
      "",
      ".cdaa\n"+
      "bcd|a\n"+
      "bbb|.\n"+
      "",
      ".daa.\n"+
      "bda|c\n"+
      "bbb|c\n"+
      "",
      ".daac\n"+
      "bda|c\n"+
      "bbb|.\n"+
      "",
      ".dbaa\n"+
      "cdb|a\n"+
      "cbb|.\n"+
      "",
      ".dbb.\n"+
      "adb|c\n"+
      "aab|c\n"+
      "",
      ".dbbb\n"+
      "adc|b\n"+
      "aac|.\n"+
      "",
      ".dbbb\n"+
      "cda|b\n"+
      "caa|.\n"+
      "",
      ".dbbc\n"+
      "adb|c\n"+
      "aab|.\n"+
      "",
      ".dcaa\n"+
      "bdc|a\n"+
      "bbb|.\n"+
      "",
      ".ddaa\n"+
      "bbb|a\n"+
      "ccb|.\n"+
      "",
      ".ddaa\n"+
      "bcc|a\n"+
      "bbb|.\n"+
      "",
      "a.bb.\n"+
      "aab|c\n"+
      "ddb|c\n"+
      "",
      "a.bb.\n"+
      "aab|d\n"+
      "ccb|d\n"+
      "",
      "a.bbb\n"+
      "aac|b\n"+
      "ddc|.\n"+
      "",
      "a.bbb\n"+
      "aad|b\n"+
      "ccd|.\n"+
      "",
      "a.bbc\n"+
      "aab|c\n"+
      "ddb|.\n"+
      "",
      "a.bbd\n"+
      "aab|d\n"+
      "ccb|.\n"+
      "",
      "a.bcc\n"+
      "aab|d\n"+
      ".bb|d\n"+
      "",
      "a.bdd\n"+
      "aab|c\n"+
      ".bb|c\n"+
      "",
      "aa.cc\n"+
      "ba.|d\n"+
      "bbb|d\n"+
      "",
      "aa.dd\n"+
      "ba.|c\n"+
      "bbb|c\n"+
      "",
      "aab..\n"+
      "cab|d\n"+
      "cbb|d\n"+
      "",
      "aab..\n"+
      "dab|c\n"+
      "dbb|c\n"+
      "",
      "aab.c\n"+
      "dab|c\n"+
      "dbb|.\n"+
      "",
      "aab.d\n"+
      "cab|d\n"+
      "cbb|.\n"+
      "",
      "aabb.\n"+
      ".ab|c\n"+
      "ddb|c\n"+
      "",
      "aabb.\n"+
      ".ab|d\n"+
      "ccb|d\n"+
      "",
      "aabb.\n"+
      "a.b|c\n"+
      "ddb|c\n"+
      "",
      "aabb.\n"+
      "a.b|d\n"+
      "ccb|d\n"+
      "",
      "aabb.\n"+
      "acb|d\n"+
      ".cb|d\n"+
      "",
      "aabb.\n"+
      "adb|c\n"+
      ".db|c\n"+
      "",
      "aabb.\n"+
      "cab|d\n"+
      "c.b|d\n"+
      "",
      "aabb.\n"+
      "dab|c\n"+
      "d.b|c\n"+
      "",
      "aabbb\n"+
      ".ac|b\n"+
      "ddc|.\n"+
      "",
      "aabbb\n"+
      ".ad|b\n"+
      "ccd|.\n"+
      "",
      "aabbb\n"+
      "a.c|b\n"+
      "ddc|.\n"+
      "",
      "aabbb\n"+
      "a.d|b\n"+
      "ccd|.\n"+
      "",
      "aabbb\n"+
      "acc|b\n"+
      ".dd|.\n"+
      "",
      "aabbb\n"+
      "acc|b\n"+
      "dd.|.\n"+
      "",
      "aabbb\n"+
      "acd|b\n"+
      ".cd|.\n"+
      "",
      "aabbb\n"+
      "adc|b\n"+
      ".dc|.\n"+
      "",
      "aabbb\n"+
      "add|b\n"+
      ".cc|.\n"+
      "",
      "aabbb\n"+
      "add|b\n"+
      "cc.|.\n"+
      "",
      "aabbb\n"+
      "ca.|b\n"+
      "cdd|.\n"+
      "",
      "aabbb\n"+
      "cad|b\n"+
      "c.d|.\n"+
      "",
      "aabbb\n"+
      "da.|b\n"+
      "dcc|.\n"+
      "",
      "aabbb\n"+
      "dac|b\n"+
      "d.c|.\n"+
      "",
      "aabbc\n"+
      ".ab|c\n"+
      "ddb|.\n"+
      "",
      "aabbc\n"+
      "a.b|c\n"+
      "ddb|.\n"+
      "",
      "aabbc\n"+
      "adb|c\n"+
      ".db|.\n"+
      "",
      "aabbc\n"+
      "dab|c\n"+
      "d.b|.\n"+
      "",
      "aabbd\n"+
      ".ab|d\n"+
      "ccb|.\n"+
      "",
      "aabbd\n"+
      "a.b|d\n"+
      "ccb|.\n"+
      "",
      "aabbd\n"+
      "acb|d\n"+
      ".cb|.\n"+
      "",
      "aabbd\n"+
      "cab|d\n"+
      "c.b|.\n"+
      "",
      "aabcc\n"+
      ".ab|d\n"+
      ".bb|d\n"+
      "",
      "aabcc\n"+
      "a.b|d\n"+
      ".bb|d\n"+
      "",
      "aabcc\n"+
      "dab|.\n"+
      "dbb|.\n"+
      "",
      "aabdd\n"+
      ".ab|c\n"+
      ".bb|c\n"+
      "",
      "aabdd\n"+
      "a.b|c\n"+
      ".bb|c\n"+
      "",
      "aabdd\n"+
      "cab|.\n"+
      "cbb|.\n"+
      "",
      "aac..\n"+
      "bac|d\n"+
      "bbb|d\n"+
      "",
      "aac.d\n"+
      "bac|d\n"+
      "bbb|.\n"+
      "",
      "aacc.\n"+
      "ba.|d\n"+
      "bbb|d\n"+
      "",
      "aaccd\n"+
      "ba.|d\n"+
      "bbb|.\n"+
      "",
      "aacdd\n"+
      "bac|.\n"+
      "bbb|.\n"+
      "",
      "aad..\n"+
      "bad|c\n"+
      "bbb|c\n"+
      "",
      "aad.c\n"+
      "bad|c\n"+
      "bbb|.\n"+
      "",
      "aadcc\n"+
      "bad|.\n"+
      "bbb|.\n"+
      "",
      "aadd.\n"+
      "ba.|c\n"+
      "bbb|c\n"+
      "",
      "aaddc\n"+
      "ba.|c\n"+
      "bbb|.\n"+
      "",
      "bb.aa\n"+
      "bcc|a\n"+
      "bdd|.\n"+
      "",
      "bb.aa\n"+
      "bcd|a\n"+
      "bcd|.\n"+
      "",
      "bb.aa\n"+
      "bdc|a\n"+
      "bdc|.\n"+
      "",
      "bb.aa\n"+
      "bdd|a\n"+
      "bcc|.\n"+
      "",
      "bb.cc\n"+
      "b.a|d\n"+
      "baa|d\n"+
      "",
      "bb.cc\n"+
      "ba.|d\n"+
      "baa|d\n"+
      "",
      "bb.cc\n"+
      "baa|d\n"+
      "b.a|d\n"+
      "",
      "bb.cc\n"+
      "baa|d\n"+
      "ba.|d\n"+
      "",
      "bb.dd\n"+
      "b.a|c\n"+
      "baa|c\n"+
      "",
      "bb.dd\n"+
      "ba.|c\n"+
      "baa|c\n"+
      "",
      "bb.dd\n"+
      "baa|c\n"+
      "b.a|c\n"+
      "",
      "bb.dd\n"+
      "baa|c\n"+
      "ba.|c\n"+
      "",
      "bba..\n"+
      "baa|c\n"+
      "bdd|c\n"+
      "",
      "bba..\n"+
      "baa|d\n"+
      "bcc|d\n"+
      "",
      "bba.c\n"+
      "baa|c\n"+
      "bdd|.\n"+
      "",
      "bba.d\n"+
      "baa|d\n"+
      "bcc|.\n"+
      "",
      "bbaa.\n"+
      "b.a|c\n"+
      "bdd|c\n"+
      "",
      "bbaa.\n"+
      "b.a|d\n"+
      "bcc|d\n"+
      "",
      "bbaa.\n"+
      "bca|d\n"+
      "bc.|d\n"+
      "",
      "bbaa.\n"+
      "bda|c\n"+
      "bd.|c\n"+
      "",
      "bbaac\n"+
      "b.a|c\n"+
      "bdd|.\n"+
      "",
      "bbaac\n"+
      "bda|c\n"+
      "bd.|.\n"+
      "",
      "bbaad\n"+
      "b.a|d\n"+
      "bcc|.\n"+
      "",
      "bbaad\n"+
      "bca|d\n"+
      "bc.|.\n"+
      "",
      "bbacc\n"+
      "baa|.\n"+
      "bdd|.\n"+
      "",
      "bbacc\n"+
      "baa|d\n"+
      "b..|d\n"+
      "",
      "bbadd\n"+
      "baa|.\n"+
      "bcc|.\n"+
      "",
      "bbadd\n"+
      "baa|c\n"+
      "b..|c\n"+
      "",
      "bbb..\n"+
      "aab|c\n"+
      "add|c\n"+
      "",
      "bbb..\n"+
      "aab|d\n"+
      "acc|d\n"+
      "",
      "bbb..\n"+
      "cab|d\n"+
      "caa|d\n"+
      "",
      "bbb..\n"+
      "dab|c\n"+
      "daa|c\n"+
      "",
      "bbb.c\n"+
      "aab|c\n"+
      "add|.\n"+
      "",
      "bbb.c\n"+
      "dab|c\n"+
      "daa|.\n"+
      "",
      "bbb.d\n"+
      "aab|d\n"+
      "acc|.\n"+
      "",
      "bbb.d\n"+
      "cab|d\n"+
      "caa|.\n"+
      "",
      "bbbaa\n"+
      "c.b|a\n"+
      "cdd|.\n"+
      "",
      "bbbaa\n"+
      "ccb|a\n"+
      ".dd|.\n"+
      "",
      "bbbaa\n"+
      "ccb|a\n"+
      "dd.|.\n"+
      "",
      "bbbaa\n"+
      "cdb|a\n"+
      "cd.|.\n"+
      "",
      "bbbaa\n"+
      "d.b|a\n"+
      "dcc|.\n"+
      "",
      "bbbaa\n"+
      "dcb|a\n"+
      "dc.|.\n"+
      "",
      "bbbaa\n"+
      "ddb|a\n"+
      ".cc|.\n"+
      "",
      "bbbaa\n"+
      "ddb|a\n"+
      "cc.|.\n"+
      "",
      "bbbcc\n"+
      ".ab|d\n"+
      ".aa|d\n"+
      "",
      "bbbcc\n"+
      ".ab|d\n"+
      "aa.|d\n"+
      "",
      "bbbcc\n"+
      "a.b|d\n"+
      "aa.|d\n"+
      "",
      "bbbcc\n"+
      "aab|.\n"+
      "add|.\n"+
      "",
      "bbbcc\n"+
      "aab|d\n"+
      ".a.|d\n"+
      "",
      "bbbcc\n"+
      "aab|d\n"+
      "a..|d\n"+
      "",
      "bbbcc\n"+
      "dab|.\n"+
      "daa|.\n"+
      "",
      "bbbdd\n"+
      ".ab|c\n"+
      ".aa|c\n"+
      "",
      "bbbdd\n"+
      ".ab|c\n"+
      "aa.|c\n"+
      "",
      "bbbdd\n"+
      "a.b|c\n"+
      "aa.|c\n"+
      "",
      "bbbdd\n"+
      "aab|.\n"+
      "acc|.\n"+
      "",
      "bbbdd\n"+
      "aab|c\n"+
      ".a.|c\n"+
      "",
      "bbbdd\n"+
      "aab|c\n"+
      "a..|c\n"+
      "",
      "bbbdd\n"+
      "cab|.\n"+
      "caa|.\n"+
      "",
      "bbc..\n"+
      "bac|d\n"+
      "baa|d\n"+
      "",
      "bbc.d\n"+
      "bac|d\n"+
      "baa|.\n"+
      "",
      "bbcaa\n"+
      "b.c|a\n"+
      "bdd|.\n"+
      "",
      "bbcaa\n"+
      "bdc|a\n"+
      "bd.|.\n"+
      "",
      "bbcc.\n"+
      "b.a|d\n"+
      "baa|d\n"+
      "",
      "bbcc.\n"+
      "ba.|d\n"+
      "baa|d\n"+
      "",
      "bbcc.\n"+
      "baa|d\n"+
      "b.a|d\n"+
      "",
      "bbcc.\n"+
      "baa|d\n"+
      "ba.|d\n"+
      "",
      "bbccd\n"+
      "b.a|d\n"+
      "baa|.\n"+
      "",
      "bbccd\n"+
      "ba.|d\n"+
      "baa|.\n"+
      "",
      "bbccd\n"+
      "baa|d\n"+
      "b.a|.\n"+
      "",
      "bbccd\n"+
      "baa|d\n"+
      "ba.|.\n"+
      "",
      "bbcdd\n"+
      "bac|.\n"+
      "baa|.\n"+
      "",
      "bbd..\n"+
      "bad|c\n"+
      "baa|c\n"+
      "",
      "bbd.c\n"+
      "bad|c\n"+
      "baa|.\n"+
      "",
      "bbdaa\n"+
      "b.d|a\n"+
      "bcc|.\n"+
      "",
      "bbdaa\n"+
      "bcd|a\n"+
      "bc.|.\n"+
      "",
      "bbdcc\n"+
      "bad|.\n"+
      "baa|.\n"+
      "",
      "bbdd.\n"+
      "b.a|c\n"+
      "baa|c\n"+
      "",
      "bbdd.\n"+
      "ba.|c\n"+
      "baa|c\n"+
      "",
      "bbdd.\n"+
      "baa|c\n"+
      "b.a|c\n"+
      "",
      "bbdd.\n"+
      "baa|c\n"+
      "ba.|c\n"+
      "",
      "bbddc\n"+
      "b.a|c\n"+
      "baa|.\n"+
      "",
      "bbddc\n"+
      "ba.|c\n"+
      "baa|.\n"+
      "",
      "bbddc\n"+
      "baa|c\n"+
      "b.a|.\n"+
      "",
      "bbddc\n"+
      "baa|c\n"+
      "ba.|.\n"+
      "",
      "bccaa\n"+
      "bbb|a\n"+
      ".dd|.\n"+
      "",
      "bccaa\n"+
      "bbb|a\n"+
      "dd.|.\n"+
      "",
      "bddaa\n"+
      "bbb|a\n"+
      ".cc|.\n"+
      "",
      "bddaa\n"+
      "bbb|a\n"+
      "cc.|.\n"+
      "",
      "c.bb.\n"+
      "cab|d\n"+
      "aab|d\n"+
      "",
      "c.bbb\n"+
      "caa|b\n"+
      "dda|.\n"+
      "",
      "c.bbb\n"+
      "cad|b\n"+
      "aad|.\n"+
      "",
      "c.bbd\n"+
      "cab|d\n"+
      "aab|.\n"+
      "",
      "cabbb\n"+
      "caa|b\n"+
      ".dd|.\n"+
      "",
      "cabbb\n"+
      "caa|b\n"+
      "dd.|.\n"+
      "",
      "cb.aa\n"+
      "cbd|a\n"+
      "bbd|.\n"+
      "",
      "cbaa.\n"+
      "cba|d\n"+
      "bb.|d\n"+
      "",
      "cbaad\n"+
      "cba|d\n"+
      "bb.|.\n"+
      "",
      "cbbaa\n"+
      "cbd|a\n"+
      ".bd|.\n"+
      "",
      "cbdaa\n"+
      "cbd|a\n"+
      "bb.|.\n"+
      "",
      "cc.aa\n"+
      "bbb|a\n"+
      "ddb|.\n"+
      "",
      "cc.aa\n"+
      "bdd|a\n"+
      "bbb|.\n"+
      "",
      "cca..\n"+
      "baa|d\n"+
      "bbb|d\n"+
      "",
      "cca.d\n"+
      "baa|d\n"+
      "bbb|.\n"+
      "",
      "ccaa.\n"+
      "b.a|d\n"+
      "bbb|d\n"+
      "",
      "ccaad\n"+
      "b.a|d\n"+
      "bbb|.\n"+
      "",
      "ccadd\n"+
      "baa|.\n"+
      "bbb|.\n"+
      "",
      "ccb..\n"+
      "aab|d\n"+
      "abb|d\n"+
      "",
      "ccb.d\n"+
      "aab|d\n"+
      "abb|.\n"+
      "",
      "ccbaa\n"+
      "d.b|a\n"+
      "dbb|.\n"+
      "",
      "ccbaa\n"+
      "ddb|a\n"+
      ".bb|.\n"+
      "",
      "ccbb.\n"+
      ".ab|d\n"+
      "aab|d\n"+
      "",
      "ccbb.\n"+
      "a.b|d\n"+
      "aab|d\n"+
      "",
      "ccbb.\n"+
      "aab|d\n"+
      ".ab|d\n"+
      "",
      "ccbb.\n"+
      "aab|d\n"+
      "a.b|d\n"+
      "",
      "ccbbb\n"+
      ".aa|b\n"+
      "dda|.\n"+
      "",
      "ccbbb\n"+
      ".ad|b\n"+
      "aad|.\n"+
      "",
      "ccbbb\n"+
      "a.d|b\n"+
      "aad|.\n"+
      "",
      "ccbbb\n"+
      "aa.|b\n"+
      "add|.\n"+
      "",
      "ccbbb\n"+
      "aad|b\n"+
      ".ad|.\n"+
      "",
      "ccbbb\n"+
      "aad|b\n"+
      "a.d|.\n"+
      "",
      "ccbbb\n"+
      "add|b\n"+
      "aa.|.\n"+
      "",
      "ccbbb\n"+
      "d.a|b\n"+
      "daa|.\n"+
      "",
      "ccbbb\n"+
      "da.|b\n"+
      "daa|.\n"+
      "",
      "ccbbb\n"+
      "daa|b\n"+
      "d.a|.\n"+
      "",
      "ccbbb\n"+
      "daa|b\n"+
      "da.|.\n"+
      "",
      "ccbbb\n"+
      "dda|b\n"+
      ".aa|.\n"+
      "",
      "ccbbd\n"+
      ".ab|d\n"+
      "aab|.\n"+
      "",
      "ccbbd\n"+
      "a.b|d\n"+
      "aab|.\n"+
      "",
      "ccbbd\n"+
      "aab|d\n"+
      ".ab|.\n"+
      "",
      "ccbbd\n"+
      "aab|d\n"+
      "a.b|.\n"+
      "",
      "ccbdd\n"+
      "aab|.\n"+
      "abb|.\n"+
      "",
      "ccdaa\n"+
      "b.d|a\n"+
      "bbb|.\n"+
      "",
      "cdbaa\n"+
      "cdb|a\n"+
      ".bb|.\n"+
      "",
      "cdbbb\n"+
      "cda|b\n"+
      ".aa|.\n"+
      "",
      "d.bb.\n"+
      "dab|c\n"+
      "aab|c\n"+
      "",
      "d.bbb\n"+
      "daa|b\n"+
      "cca|.\n"+
      "",
      "d.bbb\n"+
      "dac|b\n"+
      "aac|.\n"+
      "",
      "d.bbc\n"+
      "dab|c\n"+
      "aab|.\n"+
      "",
      "dabbb\n"+
      "daa|b\n"+
      ".cc|.\n"+
      "",
      "dabbb\n"+
      "daa|b\n"+
      "cc.|.\n"+
      "",
      "db.aa\n"+
      "dbc|a\n"+
      "bbc|.\n"+
      "",
      "dbaa.\n"+
      "dba|c\n"+
      "bb.|c\n"+
      "",
      "dbaac\n"+
      "dba|c\n"+
      "bb.|.\n"+
      "",
      "dbbaa\n"+
      "dbc|a\n"+
      ".bc|.\n"+
      "",
      "dbcaa\n"+
      "dbc|a\n"+
      "bb.|.\n"+
      "",
      "dcbaa\n"+
      "dcb|a\n"+
      ".bb|.\n"+
      "",
      "dcbbb\n"+
      "dca|b\n"+
      ".aa|.\n"+
      "",
      "dd.aa\n"+
      "bbb|a\n"+
      "ccb|.\n"+
      "",
      "dd.aa\n"+
      "bcc|a\n"+
      "bbb|.\n"+
      "",
      "dda..\n"+
      "baa|c\n"+
      "bbb|c\n"+
      "",
      "dda.c\n"+
      "baa|c\n"+
      "bbb|.\n"+
      "",
      "ddaa.\n"+
      "b.a|c\n"+
      "bbb|c\n"+
      "",
      "ddaac\n"+
      "b.a|c\n"+
      "bbb|.\n"+
      "",
      "ddacc\n"+
      "baa|.\n"+
      "bbb|.\n"+
      "",
      "ddb..\n"+
      "aab|c\n"+
      "abb|c\n"+
      "",
      "ddb.c\n"+
      "aab|c\n"+
      "abb|.\n"+
      "",
      "ddbaa\n"+
      "c.b|a\n"+
      "cbb|.\n"+
      "",
      "ddbaa\n"+
      "ccb|a\n"+
      ".bb|.\n"+
      "",
      "ddbb.\n"+
      ".ab|c\n"+
      "aab|c\n"+
      "",
      "ddbb.\n"+
      "a.b|c\n"+
      "aab|c\n"+
      "",
      "ddbb.\n"+
      "aab|c\n"+
      ".ab|c\n"+
      "",
      "ddbb.\n"+
      "aab|c\n"+
      "a.b|c\n"+
      "",
      "ddbbb\n"+
      ".aa|b\n"+
      "cca|.\n"+
      "",
      "ddbbb\n"+
      ".ac|b\n"+
      "aac|.\n"+
      "",
      "ddbbb\n"+
      "a.c|b\n"+
      "aac|.\n"+
      "",
      "ddbbb\n"+
      "aa.|b\n"+
      "acc|.\n"+
      "",
      "ddbbb\n"+
      "aac|b\n"+
      ".ac|.\n"+
      "",
      "ddbbb\n"+
      "aac|b\n"+
      "a.c|.\n"+
      "",
      "ddbbb\n"+
      "acc|b\n"+
      "aa.|.\n"+
      "",
      "ddbbb\n"+
      "c.a|b\n"+
      "caa|.\n"+
      "",
      "ddbbb\n"+
      "ca.|b\n"+
      "caa|.\n"+
      "",
      "ddbbb\n"+
      "caa|b\n"+
      "c.a|.\n"+
      "",
      "ddbbb\n"+
      "caa|b\n"+
      "ca.|.\n"+
      "",
      "ddbbb\n"+
      "cca|b\n"+
      ".aa|.\n"+
      "",
      "ddbbc\n"+
      ".ab|c\n"+
      "aab|.\n"+
      "",
      "ddbbc\n"+
      "a.b|c\n"+
      "aab|.\n"+
      "",
      "ddbbc\n"+
      "aab|c\n"+
      ".ab|.\n"+
      "",
      "ddbbc\n"+
      "aab|c\n"+
      "a.b|.\n"+
      "",
      "ddbcc\n"+
      "aab|.\n"+
      "abb|.\n"+
      "",
      "ddcaa\n"+
      "b.c|a\n"+
      "bbb|.\n"+
      "",
    };
    verify_searchForAllFits(space,shapes,expect);
  }

  // Test whether main method output to a named file is what is
  // expected.
  public void test_main_all_fit(String fileBase) {
    try{
      ensureTestDirExists();
      File inputFile = new File(testDir,fileBase+".txt");
      File actualFile = new File(testDir,fileBase+".actual");
      File expectFile = new File(testDir,fileBase+".expect");
    
      String fileS = inputFile.toString();
      String expectFileS = expectFile.toString();
      String actualFileS = actualFile.toString();
    
      // Run the main method
      FitIt.main(new String[]{fileS, "all", actualFileS});
    
      String input = slurp(fileS);
      String expect = slurp(expectFileS);
      String actual = slurp(actualFileS);
      actual = actual.replaceAll("\r\n","\n");
    
      String outputComparison = simpleDiff2("EXPECT\n------\n"+expect,
                                            "ACTUAL\n------\n"+actual);
      String msg =
        String.format("Test: %s\nActual output does not match expected output\n"+
                      "Input File:\n%s\nOutput:\n%s\n",
                      fileBase,input,outputComparison);
      assertEquals(msg,expect,actual);
    }
    catch(Exception e){
      throw new RuntimeException(e);
    }
  }  

  // Honors section main
  @Test(timeout=5000) public void test_main_all_fit1(){ test_main_all_fit("problem-all1"); }
  @Test(timeout=5000) public void test_main_all_fit2(){ test_main_all_fit("problem-all2"); }
  @Test(timeout=5000) public void test_main_all_fit3(){ test_main_all_fit("problem-all3"); }
  @Test(timeout=5000) public void test_main_all_fit4(){ test_main_all_fit("problem-all4"); }
  @Test(timeout=5000) public void test_main_all_fit5(){ test_main_all_fit("problem-all5"); }

*/
    // End honors section tests
}
