import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        Map<Character, Character> lettersToBeReplacedByNumbers2 = new HashMap<>();

        lettersToBeReplacedByNumbers2.put('i', '1');
        lettersToBeReplacedByNumbers2.put('e', '3');
        lettersToBeReplacedByNumbers2.put('a', '4');
        lettersToBeReplacedByNumbers2.put('s', '5');
        lettersToBeReplacedByNumbers2.put('b', '8');



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
                fileLines.removeIf(word -> word.length() < 3 || (word.length() > 5 && word.length() != 7));
                fileLines.removeIf(s -> s.length() == 7 && !lettersToBeReplacedByNumbers2.containsKey(s.charAt(1)));

                List<String> modifiedFileLines = fileLines.stream()
                        .map(s -> {
                            char secondLetter = s.charAt(1);
                            StringBuilder stringBuilder = new StringBuilder(s);
                            if (s.length() == 7 && lettersToBeReplacedByNumbers2.containsKey(secondLetter)) {
                                stringBuilder.setCharAt(1, lettersToBeReplacedByNumbers2.get(secondLetter));
                            }
                            return stringBuilder.toString();
                        })
                        .map(s -> Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]|\\p{M}", "").toUpperCase())
                        .collect(Collectors.toList());

                Files.write(filePath, modifiedFileLines, StandardOpenOption.TRUNCATE_EXISTING);

                System.out.println("File modified successfully.");
                break;

            } catch (IOException e) {
                System.out.println("An error occurred while modifying the file.");
            }
        }
        scanner.close();
    }
}