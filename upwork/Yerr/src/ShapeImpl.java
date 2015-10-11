/**
 * Created by dma on 4/26/15.
 */
public class ShapeImpl implements Shape {

    private int height;
    private int width;
    private char displayChar;
    private char[][] data;
    private Rotation rotation;

    public ShapeImpl() {
    }

    /*
     * The constructor of ShapeImpl, init all class members
     */
    public ShapeImpl(char[][] data) throws FitItException {
        this.data = data;
        rotation = Rotation.CW0;
        this.height = data.length;
        this.width = data[0].length;
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
    public char getDisplayChar() {
        return displayChar;
    }

    @Override
    public void setDisplayChar(char c) {
        this.displayChar = c;
        // also update displayChar in data array
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (data[i][j] != FitIt.FILLED_BLOCK)
                    data[i][j] = c;
    }

    /*
     * Update the shape by rotation: update the rotation and two-dimensional array
     */
    @Override
    public void rotateCW() {

        int nheight = this.width;
        int nwidth = this.height;

        char[][] newData = new char[nheight][nwidth];

        for (int i = 0; i < this.height; i++)
            for (int j = 0; j < this.width; j++) {
                newData[j][nwidth-1-i] = this.data[i][j];
            }

        // update new height & width
        this.data = newData;
        this.height = nheight;
        this.width = nwidth;
        rotation = rotation.next();
    }

    @Override
    public Rotation getRotation() {
        return rotation;
    }

    @Override
    public boolean isFilledAt(int row, int col) {
        if (row < 0 || col < 0 || row >= height || col >= width)
            throw new FitItException("row <" + row + "> and col <" + col + "> is out of bounds.");
        return data[row][col] != FitIt.FILLED_BLOCK;
    }

    @Override
    public Object clone() {
        ShapeImpl res = new ShapeImpl();

        res.height = this.height;
        res.width = this.width;
        res.rotation = this.rotation;
        res.displayChar = this.displayChar;
        res.data = new char[res.height][res.width];

        for (int i = 0; i < res.height; i++)
            for (int j = 0; j < res.width; j++)
                res.data[i][j] = data[i][j];

        return res;
    }

    @Override
    public String toString() {
        // SHAPE c
        // height: 2 width: 3 rotation: CW270
        // ..c
        // ccc

        StringBuilder sb = new StringBuilder();
        sb.append("SHAPE ").append(this.displayChar).append("\n");
        sb.append("height: ").append(height).append("; width: ").append(width).append("; rotation: ").append(rotation).append("\n");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sb.append(data[i][j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
