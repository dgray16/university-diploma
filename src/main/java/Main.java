import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.RandomAccessFile;


/**
 *
 */
public class Main {

    public static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        FileProcessor fileProcessor = new FileProcessor();
        RandomAccessFile file = fileProcessor.generateFile();
    }

}
