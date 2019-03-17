package dmcs.apiorimethod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Database {

    public final List<List<String>> data;

    public static Database forFile(Path filePath) throws IOException {
        return new Database(filePath);
    }

    private Database(Path filePath) throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        data = Collections.unmodifiableList(
                lines.stream()
                        .map(line -> line.substring(1, line.length() - 1))
                        .map(line -> line.split(","))
                        .map(Arrays::asList)
                        .map(this::santize)
                        .peek(list -> list.sort(String::compareTo))
                        .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList))
        );

    }

   private List<String> santize(List<String> toSanitize) {
        return toSanitize.stream()
                .map(String::trim)
                .collect(Collectors.toList());
   }
}
