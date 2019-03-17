package dmcs.apiorimethod;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URL url = Main.class.getResource("/data.txt");
        Path path = Paths.get(url.toURI());
        Database database = Database.forFile(path);
        Apiori apiori = Apiori.ofDatabase(database);
        apiori.execute(4);
        apiori.getSets().forEach(set -> System.out.println(">" + set.stream().collect(Collectors.joining(","))));
    }
}

