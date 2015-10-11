import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dma on 4/6/15.
 */
public class MotifMapper extends Mapper<Object, Text, Text, Text> {

    public static final int MOTIF_LEN = 8;

    public static final char[] DNA = {'a', 'c', 'g', 't'};

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

        String dnaSeq = value.toString();

        ArrayList<String> allPerm = this.getPermutation(MOTIF_LEN);

        for (int i = 0; i < allPerm.size(); i++) {
            String motif = allPerm.get(i);
            context.write(new Text(motif), new Text(minDistance(motif, dnaSeq, i)));
        }
    }

    private ArrayList<String> getPermutation(int len) {

        ArrayList<String> res = new ArrayList<String>();

        for (long i = 0; i < Math.pow(DNA.length, len); i++) {
            res.add(itos(i, len));
        }

        return res;
    }

    private String itos(long i, int len) {
        StringBuilder res = new StringBuilder();
        for (int j = 0; j < len; j++) {
            long t = i;
            t = t & 3;
            res.append(DNA[(int) t]);
            i = i >> 2;
        }

        return res.toString();
    }

    //return concatinated string in format minDistance,bestMatching,index
    private String minDistance(String motif, String input, int seq) {
        String temp = input.substring(0, motif.length());

        int minDistance = getDistance(motif, temp);
        String bestMatching = temp;
        int index = 0;

        for (int i = 1; i <= input.length() - motif.length(); i++) {

            temp = input.substring(i, motif.length() + i);
            if (getDistance(motif, temp) < minDistance) {
                minDistance = getDistance(motif, temp);
                bestMatching = temp;
                index = i;
            }
        }

        return new MotifInSeq(minDistance, seq, index, bestMatching).toString();
    }

    //Get distance or count of difference between characters of two strings
    private int getDistance(String first, String second) {
        int distance = 0;
        for (int i = 0; i < first.length(); i++) {
            if (first.charAt(i) != second.charAt(i)) {
                distance++;
            }
        }

        return distance;

    }

}
