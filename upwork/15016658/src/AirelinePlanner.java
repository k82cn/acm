/**
 * Created by dma on 5/3/15.
 */
public interface AirelinePlanner {
    void generateOpenSet();

    boolean isFinished();

    void applyNextStep();

    Path getPath();
}
