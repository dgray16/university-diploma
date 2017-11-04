package utils;

import application.Main;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

public class FileProcessor {

    /**
     * Method returns list of ordered files by number of file in name
     */
    @SneakyThrows
    public List<File> getFiles(String path) {
        URL url = getClass().getResource(path);
        File directory = null;

        if (nonNull(url)) {
            directory = new File(url.toURI());
        }
        List<File> files = nonNull(directory)
                ? Arrays.stream(directory.listFiles())
                    .sorted((obj1, obj2) -> {
                        Integer value1 = parseInt(obj1.getName().split(".txt")[0]);
                        Integer value2 = parseInt(obj2.getName().split(".txt")[0]);
                        return value1.compareTo(value2);
                    })
                    .collect(Collectors.toList())
                : Collections.emptyList();

        if (files.isEmpty()) {
            Main.LOG.debug("No files were found.");
        }

        return files;
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

    public String getTextFromFile(File file) {
        List<String> lines = null;
        FileInputStream fileInputStream = null;

        try {
            lines = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            try {
                fileInputStream = new FileInputStream(file.getAbsolutePath());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, "utf-8"));
                lines = bufferedReader.lines().collect(Collectors.toList());
            } catch (UnsupportedEncodingException | FileNotFoundException e1) {
                Main.LOG.error(e1.getMessage());
            }
        } finally {
            if (nonNull(fileInputStream)) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    Main.LOG.error(e.getMessage());
                }
            }
        }

        lines = ofNullable(lines).orElse(Collections.emptyList());

        return lines.parallelStream().map(String::trim).collect(Collectors.joining(StringUtils.SPACE));
    }

    public String getTextFromBinaryFile(File file) {
        String fileChar;
        FileReader fileReader = null;

        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            Main.LOG.error(e.getMessage());
        }

        BufferedReader bufferReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();

        try {
            while(nonNull((fileChar = bufferReader.readLine()))){
                stringBuilder.append(fileChar);
            }
        } catch (IOException e) {
            Main.LOG.error(e.getMessage());
        } finally {
            try {
                bufferReader.close();
                fileReader.close();
            } catch (IOException e) {
                Main.LOG.error(e.getMessage());
            }
        }
        return stringBuilder.toString();
    }

    @SneakyThrows
    public byte[] getBytesFromBinaryFile(File file) {
        return IOUtils.toByteArray(new FileInputStream(file.getAbsoluteFile()));
    }


}
