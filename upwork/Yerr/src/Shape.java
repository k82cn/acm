// A shape is a rectangular grid of filled and empty blocks. They can
// be rotated clockwise (CW) in increments of 90 degrees and track
// their rotation using the Rotation enumeration. Shapes can be
// displayed in text terminals and have a display character which
// dictates how they are shown.
public interface Shape{

  // Return the number of rows of the shape
  public int getHeight();

  // Return the number of colulmns of the shape
  public int getWidth();

  // When drawing this shape in a text terminal, the character
  // returned by displayChar will be used
  public char getDisplayChar();

  // Set how the shpae will be displayed in text terminals
  public void setDisplayChar(char c);

  // Rotate this shape clockwise by 90 degrees. Adjust all internal
  // state required to achieve the rotation including height and width
  public void rotateCW();

  // Return an value from the Rotations enumeration indicating how
  // much the shape has been rotated from its original position.
  public Rotation getRotation();

  // Return true if the shape has a filled block at the given row/col
  // position and false if the block is empty. If the position is out
  // of bounds, raise a FitItException with an informative message.
  public boolean isFilledAt(int row, int col);

  // Create a string representation of the shape. The format should
  // follow this convention:
  //
  // SHAPE c
  // height: 2 width: 3 rotation: CW270
  // ..c
  // ccc
  public String toString();
}
