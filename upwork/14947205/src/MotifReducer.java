import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by dma on 4/6/15.
 */

public class MotifReducer extends Reducer<Text, Text, Text, Text> {

    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        MotifInSeq res = new MotifInSeq();

        int minDis = Integer.MAX_VALUE;

        for (Text m : values) {
            if (m == null)
                continue;
            MotifInSeq tmp = MotifInSeq.build(m.toString());
            if (tmp.getScore() < minDis) {
                res = new MotifInSeq(tmp);
            }
        }

        StringBuilder tup = new StringBuilder();
        tup.append(res.getCandidate()).append("\t");
        tup.append(res.getSeqId()).append("\t");
        tup.append(res.getScore()).append("\t");
        tup.append(res.getStartIndex());

        context.write(key, new Text(tup.toString()));

    }

}
