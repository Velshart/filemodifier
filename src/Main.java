import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        Map<Character, Character> lettersToBeReplacedByNumbers = new HashMap<>();

        lettersToBeReplacedByNumbers.put('i', '1');
        lettersToBeReplacedByNumbers.put('e', '3');
        lettersToBeReplacedByNumbers.put('a', '4');
        lettersToBeReplacedByNumbers.put('s', '5');
        lettersToBeReplacedByNumbers.put('b', '8');

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
                fileLines.removeIf(word -> word.length() == 7 && !lettersToBeReplacedByNumbers.containsKey(word.charAt(1)));

                List<String> modifiedFileLines = fileLines.stream()
                        .map(word -> {
                            if(word.length() == 7) {

                                char secondLetter = word.charAt(1);
                                StringBuilder stringBuilder = new StringBuilder(word);

                                if(lettersToBeReplacedByNumbers.containsKey(secondLetter)) {
                                    stringBuilder.setCharAt(1, lettersToBeReplacedByNumbers.get(secondLetter));
                                    return stringBuilder.toString();
                                }
                            }
                            return word;
                        })
                        .map(word -> Normalizer.normalize(word, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]|\\p{M}", "").toUpperCase())
                        .map(word -> {
                            if(word.length() == 7) {
                                return word.substring(0, 2) + " " + word.substring(2);
                            }
                            return word;
                        })
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