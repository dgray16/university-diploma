
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;


public class FileProcessor {

    /**
     * Method returns list of ordered files by number of file in name
     */
    @SneakyThrows
    public List<File> getFiles(String path) {
        File directory = new File(Main.class.getResource(path).toURI());
        return Arrays.stream(directory.listFiles())
                .sorted((obj1, obj2) -> {
                    Integer value1 = parseInt(obj1.getName().split(".txt")[0]);
                    Integer value2 = parseInt(obj2.getName().split(".txt")[0]);
                    return value1.compareTo(value2);
                })
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public File getFile(String path) {
        return new File(Main.class.getResource(path).toURI());
    }

    /**
     * Method returns selected file by filename
     */
    @SneakyThrows
    public List<File> getTextFilesWithName(String path, Integer number) {
        File directory = new File(Main.class.getResource(path).toURI());
        return Arrays.stream(directory.listFiles())
                .filter(file -> parseInt(file.getName().split(".txt")[0]) == number)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public String getText(File file) {
        List<String> lines = Files.readAllLines(file.toPath());
        return lines.parallelStream().map(String::trim).collect(Collectors.joining(" "));
    }

}
