import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FitIt {

    public static final char FILLED_BLOCK = '.';

    public static final int READ_SPACE = 1;
    public static final int READ_SHAPE = 2;
    public static final int NONE = 0;

    // Factory methd to produce shapes. Call a constructor for one of
    // your own classes here. The layout given is be in the format
    //
    // ..e
    // eee
    //
    // Newlines (\n) show breaks in the rows of the Shape.  The period
    // (.) is used to represent empty blocks. Any other character is a
    // filled block even if it does not match the given displayChar.
    // The Shape returned by this method should have the display
    // character displayChar even if that character does not appear in
    // the layout string.  If the shape contains any empty border sides,
    // throw a SpaceShapeException with an informative message.
    public static Shape makeShape(String layout, char displayChar) {
        char[][] data = genDataByLayout(layout);

        int height = data.length;
        int width = data[0].length;


        // check empty border for each direction: north, south, west, east
        boolean emptyBorder = true;

        for (int j = 0; j < width; j++) {
            if (data[0][j] != FitIt.FILLED_BLOCK)
                emptyBorder = false;
        }

        if (emptyBorder) throw new FitItException("The north border of Space is empty");

        emptyBorder = true;

        for (int j = 0; j < width; j++) {
            if (data[height - 1][j] != FitIt.FILLED_BLOCK)
                emptyBorder = false;
        }
        if (emptyBorder) throw new FitItException("The south border of Space is empty");


        emptyBorder = true;

        for (int j = 0; j < height; j++) {
            if (data[j][0] != FitIt.FILLED_BLOCK)
                emptyBorder = false;
        }
        if (emptyBorder) throw new FitItException("The west border of Space is empty");


        emptyBorder = true;

        for (int j = 0; j < height; j++) {
            if (data[j][width - 1] != FitIt.FILLED_BLOCK)
                emptyBorder = false;
        }
        if (emptyBorder) throw new FitItException("The east border of Space is empty");

        // return Shape implementing
        ShapeImpl res = new ShapeImpl(data);

        res.setDisplayChar(displayChar);

        return res;
    }

    // Factory method to produce instances of space. Call a constructor
    // for one of your own classes here. The layout parameter will be in
    // the format
    //
    // |.......
    // ..|||..|
    // ..|.|...
    // ........
    //
    // where vertical bars are filled blocks and periods are empty blocks.
    public static Space makeSpace(String layout) {

        if (layout == null) {
            throw new FitItException("Layout of Space is null.");
        }
        char[][] data = genDataByLayout(layout);

        return new SpaceImpl(data);
    }

    // Translate layout string into two-demission array
    private static char[][] genDataByLayout(String layout) {
        String[] ds = layout.split("\n");

        if (ds.length == 0) throw new FitItException("Layout is empty.");

        int height = ds.length;
        int width = ds[0].length();

        char[][] data = new char[height][width];

        for (int i = 0; i < height; i++) {
            String d = ds[i];
            if (d.length() != width)
                throw new FitItException("The Layout is invalid: different width.");
            System.arraycopy(d.toCharArray(), 0, data[i], 0, width);
        }
        return data;
    }

    // Search for a fit of the given shapes in the given space. Return
    // null if no fit exists. If a fit is found, return a space with all
    // shapes placed in it. It is very useful for this method to be
    // recursive.
    public static Space searchForFit(Space space, List<Shape> unplaced) {

        Space res = null;

        // if no unplaced shapes, then we found a solution.
        if (unplaced.isEmpty()) {
            return space;
        }

        Shape shape = unplaced.remove(0);

        for (int i = 0; i < space.getHeight(); i++)
            for (int j = 0; j < space.getWidth(); j++) {
                // Try to place shape into space for each position

                for (Rotation r : Rotation.values()) {
                    // Try to place shape into Space for each rotation
                    if (space.shapeFitsAt(i, j, shape)) {
                        space.placeShapeAt(i, j, shape);
                        res = searchForFit(space, unplaced);
                        if (res != null)
                            return res;
                        // roll-back the status of placedShape
                        space.removeShapeByDisplayChar(shape.getDisplayChar());
                    }
                    shape.rotateCW();
                }
            }
        // add the shape back for next searching
        unplaced.add(0, shape);
        return res;
    }

    // Read an input file which contains a fitting problem in it. The
    // input file is the zeroth command line argument. The file format
    // contains records for SPACE and SHAPE. There should only be one
    // SPACE per file but potentially many SHAPEs.  SHAPE is followed by
    // a display character for the shape.  SPACE and SHAPE records
    // continue until a black line.  Any line that does not start with
    // SPACE or SHAPE should be ignored.  The main method should read
    // this file, initialize spaces and shapes using the methods
    // makeShape() and makeSpace().  It should then execute
    // searchforFit(space,shapes) and report the results as either
    //
    // NO FIT FOUND
    //
    // or
    //
    // FIT FOUND
    //   then call the toString() method of space
    public static void main(String args[]) throws Exception {

        Space space;
        List<Shape> shapes = new ArrayList<Shape>();

        if (args.length < 1) {
            System.out.println("Usage: FitIt input_file [output_file]");
            return;
        }

        space = buildSpaceAndShapes(args[0], shapes);

        Space res = searchForFit(space, shapes);

        printSpace(args, res);
    }

    // Print the information of Space; if there're two input parameters, the second one is output path;
    // otherwise, the information will print into stdout
    private static void printSpace(String[] args, Space res) throws IOException {

        OutputStream out = System.out;

        if (args.length == 2)
        {
            out = new FileOutputStream(new File(args[1]));
        }

        if (res == null) {
            out.write("NO FIT FOUND\n".getBytes());
        } else {
            out.write("FOUND FIT\n".getBytes());
            out.write(res.toString().getBytes());
        }
    }

    // Read data from input file, and build Space and Shapes according to the input; if ignore invalid line
    //
    private static Space buildSpaceAndShapes(String arg, List<Shape> shapes) throws IOException {
        File file = new File(arg);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        int status = 0;
        char dc = '.';
        Space space = null;
        StringBuffer sb = new StringBuffer();
        while ((line = br.readLine()) != null) {
            if (line.equals("SPACE")) {
                status = READ_SPACE;
                continue;
            }

            if (line.startsWith("SHAPE ")) {
                status = READ_SHAPE;
                dc = line.charAt(6);
                continue;
            }

            // if it's empty line, the build Space or Shape from the input
            if (line.isEmpty()) {
                switch (status) {
                    case READ_SPACE:
                        space = makeSpace(sb.toString());
                        break;
                    case READ_SHAPE:
                        shapes.add(makeShape(sb.toString(), dc));
                        break;
                }
                status = NONE;
                sb = new StringBuffer();
            } else {
                sb.append(line).append("\n");
            }
        }

        // close the input stream
        br.close();
        fr.close();
        return space;
    }
}


