// A Space is a rectangular grid of empty and filled blocks. On being
// initialized, some blocks of a space are permanently filled.
// Instances of Shape may be placed in the space so long as they are
// in bounds and do not conflict with any filled blocks. Space classes
// provide mechanisms to check whether shapes fit at a location in the
// space, place shapes at a location, remove shapes, and display the
// present contents of the space.
public interface Space{

  // Return the number of rows of the space
  public int getHeight();

  // Return the number of columns of the space
  public int getWidth();

  // Return true if the space has a filled block at the given row/col
  // position and false if the block is empty. A block may be filled
  // if it is permanently filled or if a shape has been placed in a
  // position such that the space's block is filled by the shape's block
  // If the position is out of bounds, return true as there is implicit
  // fill all around the space. DO NOT THROW EXCEPTIONS from this
  // method.
  public boolean isFilledAt(int row, int col);

  // Determine if the given shape may be placed at the indicated
  // row/col position.  The row,col indicates where the upper left
  // corner [block (0,0)] of the shape would be placed.  A shape would
  // not fit if one of its filled blocks would conflict with an
  // existing filled block in the space or would be out of bounds in
  // the space.
  public boolean shapeFitsAt(int row, int col, Shape shape);

  // Place the given shape at the given row/col position.  The row,col
  // indicates where the upper left corner [block (0,0)] of the shape
  // would be placed.  The shape should be removable and when
  // displaying info on the placed shapes, they must be shown in order
  // of their display characters (shape A before B before C...).
  // There may be more than one shape in a space with the same display
  // character.  If the shape would not fit at the specified row/col
  // position, raise a FitItException with an informative message.
  public void placeShapeAt(int row, int col, Shape shape);

  // Remove the shape with the display character indicated by dc from this
  // space. Update all internal state so that blocks in the space that
  // were filled by the removed shape are emptied.
  public void removeShapeByDisplayChar(char dc);

  // Return how many shapes have been placed in this space.
  public int placedShapeCount();

  // Return a string representing the space and the shapes that have
  // been fit into it. The following character conventions must be
  // used.
  //  | : vertical bar for permanently filled blocks
  //  . : period for empty blocks
  // displaychar : any space filled by a shape uses its display char
  //
  // Example:
  // aa.....e
  // aacddd.|
  // cccdd|||
  public String fitString();

  // Give a listing of all the placed shapes. This should start with
  // the number of placed shapes, show the position of each shape, and
  // use the toString() of each shape.  Shapes must be reported in
  // sorted order based on their display character (shape A before B
  // before C...).
  //
  // Example:
  //
  // 3 shapes placed
  // Shape at (0,0)
  // SHAPE a
  // height: 2 width: 2 rotation: CW90
  // aa
  // aa
  // 
  // Shape at (0,2)
  // SHAPE b
  // height: 1 width: 4 rotation: CW180
  // bbbb
  //
  // Shape at (1,0)
  // SHAPE c
  // height: 2 width: 3 rotation: CW270
  // ..c
  // ccc
  public String placedShapeInfo();

  // Print a verbose string representation of the space. Start with
  // the string SPACE: then follow with the fitString() of the space
  // and finally the placedShapeInfo() string.
  //
  // Example:
  // 
  // SPACE:
  // height: 3 width: 8
  // aabbbbfe
  // aacdddf|
  // cccdd|||
  // 
  // 6 shapes placed
  // Shape at (0,0)
  // SHAPE a
  // height: 2 width: 2 rotation: CW90
  // aa
  // aa
  // 
  // Shape at (0,2)
  // SHAPE b
  // height: 1 width: 4 rotation: CW180
  // bbbb
  // ...
  public String toString();


}
