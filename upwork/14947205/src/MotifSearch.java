import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MotifSearch {

    public static void main(String[] args) throws Exception {
        try {
            Configuration conf = new Configuration();

            if (args.length != 2) {
                System.err.println("Usage: ModifSearch <input> <output>");
                System.exit(2);
            }

            Job job = new Job(conf, "ModifSearch");
            job.setJarByClass(MotifSearch.class);
            job.setMapperClass(MotifMapper.class);

            job.setCombinerClass(MotifReducer.class);
            job.setReducerClass(MotifReducer.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

            // set the input/output of MotifSearch
            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
