import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Set<Character> lettersToBeReplacedByNumbers = new HashSet<>(Arrays.asList('I', 'E', 'A', 'S', 'B'));

        Scanner scanner = new Scanner(System.in);

        Path filePath;
        while (true) {
            System.out.println("Enter file path: ");

            filePath = Path.of(scanner.nextLine());

            if (!Files.exists(filePath)) {
                System.out.println("No file found under provided path.");
                break;
            }

            try {
                List<String> fileLines = Files.readAllLines(filePath);

                List<String> modifiedFileLines = fileLines.stream()
                        .filter(s -> s.length() >= 3 && s.length() <= 5 || s.length() == 7)
                        .map(s -> Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]|\\p{M}", "").toUpperCase())
                        .collect(Collectors.toList());

                Files.write(filePath, modifiedFileLines, StandardOpenOption.TRUNCATE_EXISTING);

                System.out.println("File modified successfully.");
                break;

            } catch (IOException e) {
                System.out.println("An error occured while modifying the file.");
            }
        }
        scanner.close();
    }
}