import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class WordCount {
    private static final String WANTED_WORD_KEY = "WANTED_WORD_KEY";
    private static final String WANTED_WORD_KEY_SPLITOR = ",";


    public static void main(String[] args) throws Exception {
        try {
            Configuration conf = new Configuration();

            if (args.length < 3) {
                System.err.println("Usage: wordcount <input_dir> <output_file> <words...>");
                System.exit(2);
            }

            //wanted word, only one word case

            if (args.length >= 3) {
                String[] wantedWords = new String[args.length - 2];
                for (int i = 0; i < wantedWords.length; i++) {
                    wantedWords[i] = args[i + 2];
                }
                String words = String.join(WANTED_WORD_KEY_SPLITOR, wantedWords);
                conf.set(WANTED_WORD_KEY, words);
            }

            Job job = new Job(conf, "WordCount");
            job.setJarByClass(WordCount.class);
            job.setMapperClass(TokenizerMapper.class);
            job.setCombinerClass(IntSumReducer.class);
            job.setReducerClass(IntSumReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            ArrayList<Path> inputPaths = getInputPathList(args[0], conf);

            for (Path p : inputPaths)
                FileInputFormat.addInputPath(job, p);

            job.setNumReduceTasks(inputPaths.size() / 2);

            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    private static ArrayList<Path> getInputPathList(String input, Configuration conf) throws IOException {
        ArrayList<Path> res = new ArrayList<Path>();
        Path inputPath = new Path(input);
        FileSystem fs = inputPath.getFileSystem(conf);
        FileStatus[] statuses = fs.listStatus(inputPath);
        System.out.println("The input file directory is: " + input);

        for (FileStatus status : statuses) {
            if (!status.isDir())
                res.add(status.getPath());
        }

        return res;
    }

    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);

        private Text word = new Text();
        private String[] wantedWords;

        @Override
        public void setup(Context context
        ) throws IOException, InterruptedException {
            String words = context.getConfiguration().get(WANTED_WORD_KEY, null);
            if (words != null) {
                wantedWords = words.split(WANTED_WORD_KEY_SPLITOR);
            }
        }

        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                String token = itr.nextToken();
                boolean isWantedWord = false;

                if (wantedWords != null) {
                    for (String w : wantedWords) {
                        if (token.equals(w)) {
                            isWantedWord = true;
                            break;
                        }
                    }
                }

                if (!isWantedWord) continue;

                word.set(token);
                context.write(word, one);
            }
        }
    }

    public static class IntSumReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }
}
