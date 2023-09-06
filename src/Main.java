import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Character> lettersToBeReplacedByNumbers = new LinkedList<>(Arrays.asList('I', 'E', 'A', 'S', 'B'));
        List<Character> numbersToReplaceLetters = new LinkedList<>(Arrays.asList('1', '3', '4', '5', '8'));

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
                        .map(s -> {
                            char secondLetter = s.charAt(1);
                            //lettersToBeReplacedByNumbers.contains(s.charAt(1)))
                            StringBuilder stringBuilder = new StringBuilder(s);
                            if(s.length() == 7 && lettersToBeReplacedByNumbers.contains(secondLetter)) {
                                stringBuilder.setCharAt(1, numbersToReplaceLetters.get(lettersToBeReplacedByNumbers.indexOf(secondLetter)));
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