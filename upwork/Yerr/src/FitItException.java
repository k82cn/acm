// Catch-all exception for Shapes and Spaces so that explicit
// exception throwing can be checked
public class FitItException extends RuntimeException {
    public FitItException(String msg) {
        super(msg);
    }
}
