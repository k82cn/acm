/**
 * Created by dma on 4/26/15.
 */

import java.util.*;
import java.util.Map.Entry;

public class SpaceImpl implements Space {

    // The height of Space
    private int height;
    // The width of Space
    private int width;
    // The two-dimensional array of space
    private char[][] data;
    // The map to store information of placed shapes
    private Map<Character, List<String>> placedShape = new TreeMap<Character, List<String>>();

    /*
     * The constructor of SpaceImpl, init the class members
     */
    public SpaceImpl(char[][] data) throws FitItException {
        this.data = data;
        height = data.length;
        width = data[0].length;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public boolean isFilledAt(int row, int col) {
        if (row >= height || col >= width || row < 0 || col < 0)
            return true;

        return data[row][col] != FitIt.FILLED_BLOCK;
    }

    /*
     * Check whether a shape can be placed at row,col in space
     */
    @Override
    public boolean shapeFitsAt(int row, int col, Shape shape) {
        for (int i = 0; i < shape.getHeight(); i++)
            for (int j = 0; j < shape.getWidth(); j++) {
                // if the point is out of Space, then return false
                if (i + row >= height || j + col >= width)
                    return false;
                // if both Shape & Space are filled, then conflict
                if (shape.isFilledAt(i, j) && this.isFilledAt(i + row, j + col))
                    return false;
            }
        return true;
    }

    /*
     * Place the shape in Space at row, col
     */
    @Override
    public void placeShapeAt(int row, int col, Shape shape) {
        if (!shapeFitsAt(row, col, shape))
            throw new FitItException("Can not fit shape in space.");

        // replace space with display char in shape
        for (int i = 0; i < shape.getHeight(); i++) {
            for (int j = 0; j < shape.getWidth(); j++) {
                if (shape.isFilledAt(i, j)) {
                    if (this.isFilledAt(i + row, j + col))
                        throw new IllegalArgumentException();
                    data[i + row][j + col] = shape.getDisplayChar();
                }
            }
        }

        char dc = shape.getDisplayChar();

        if (!placedShape.containsKey(dc))
            placedShape.put(dc, new ArrayList<String>());
        placedShape.get(dc).add("Shape at (" + row + "," + col + ")\n" + shape.toString());
    }

    /*
     * remove shapes from Space by display char
     */
    @Override
    public void removeShapeByDisplayChar(char dc) {

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (data[i][j] == dc)
                    data[i][j] = FitIt.FILLED_BLOCK;

        placedShape.remove(dc);
    }

    /*
     * return the count of placed shapes
     */
    @Override
    public int placedShapeCount() {
        int res = 0;

        for (Entry<Character, List<String>> entry : this.placedShape.entrySet()) {
            List<String> val = entry.getValue();
            if (val != null)
                res += val.size();
        }
        return res;
    }

    /*
     * return the information of Space by String
     */
    @Override
    public String fitString() {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                res.append(data[i][j]);
            }
            res.append("\n");
        }
        return res.toString();
    }

    /*
     * return the information of placed shapes in space
     */
    @Override
    public String placedShapeInfo() {
        StringBuilder res = new StringBuilder(this.placedShapeCount() + " shapes placed\n");
        for (Entry<Character, List<String>> entry : this.placedShape.entrySet()) {
            for (String ss : entry.getValue())
                res.append(ss).append("\n");
        }
        return res.toString();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("SPACE:\n");
        sb.append("height: ").append(height).append(" width: ").append(width).append("\n");
        sb.append(this.fitString()).append("\n");

        sb.append(this.placedShapeInfo());

        return sb.toString();
    }
}
