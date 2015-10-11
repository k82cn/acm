import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by dma on 4/6/15.
 */
public class MotifInSeq implements Writable {

    public static final String DATA_SPLITOR = "\t";
    private int score, seqId, startIndex;
    private String candidate;
    private String key;

    public MotifInSeq() {
    }

    public MotifInSeq(int score, int seqId, int startIndex, String candidate) {
        this.score = score;
        this.seqId = seqId;
        this.startIndex = startIndex;
        this.candidate = candidate;
    }

    public MotifInSeq(MotifInSeq seq) {
        this.score = seq.getScore();
        this.seqId = seq.getSeqId();
        this.startIndex = seq.getStartIndex();
        this.candidate = seq.getCandidate();
    }

    public static MotifInSeq build(String m) {
        String[] data = m.split(DATA_SPLITOR);
        MotifInSeq res = new MotifInSeq();
        res.setCandidate(data[0]);
        res.setSeqId(Integer.parseInt(data[1]));
        res.setScore(Integer.parseInt(data[2]));
        res.setStartIndex(Integer.parseInt(data[3]));
        return res;
    }

    @Override
    public String toString() {
        return this.candidate + DATA_SPLITOR +
                this.seqId + DATA_SPLITOR +
                this.score + DATA_SPLITOR +
                this.startIndex;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(score);
        dataOutput.writeInt(seqId);
        dataOutput.writeInt(startIndex);
        dataOutput.writeUTF(candidate);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        score = dataInput.readInt();
        seqId = dataInput.readInt();
        startIndex = dataInput.readInt();
        candidate = dataInput.readUTF();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
