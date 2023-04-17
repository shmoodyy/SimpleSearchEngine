package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static Map<String, Integer> peopleIndex = new HashMap<>();
    static boolean exitChosen = false;

    public static void main(String[] args) {
        if (args[0].equals("--data")) {
            readData(args[1]);
        } else {
            createData();
        }
        while (!exitChosen) menu();
        System.out.print("Bye!");
    }

    public static void searchStrategy() {
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        String strategySelected = scanner.next().toUpperCase();
        scanner.nextLine();
        if (strategySelected.matches("ALL|ANY|NONE")) {
            System.out.println("Enter a name or email to search all suitable people.");
            String searchQuery = scanner.nextLine();
            List<String> results = new ArrayList<>();
            switch (strategySelected) {
                case "ALL"  -> results = searchALL(searchQuery);
                case "ANY"  -> results = searchANY(searchQuery);
                case "NONE" -> results = searchNONE(searchQuery);
            }
            if (results.size() != 0) {
                System.out.printf("\n%d persons found:\n", results.size());
                results.forEach(System.out::println);
            } else {
                System.out.println("\nNo matching people found.");
            }
        } else searchStrategy();
    }

    public static void readData(String fileName) {
        File dataFile = new File(fileName);
        try (Scanner fileScanner = new Scanner(dataFile)) {
            int lineNumber = 0;
            while (fileScanner.hasNextLine()) {
                String loadedData = fileScanner.nextLine();
                peopleIndex.put(loadedData, lineNumber++);
            }
        } catch (FileNotFoundException fNFE) {
            System.out.println("File not found.");
        }
    }

    public static void outputData() {
        System.out.println("=== List of people ===");
        peopleIndex.forEach((person, line) -> System.out.println(person));
    }

    public static List<String> searchNONE(String query) {
        List<String> searchResults = new ArrayList<>(peopleIndex.keySet());
        String[] queryArray = query.split("\\s+");
        for (String subQuery : queryArray) {
            for (String entry : peopleIndex.keySet()) {
                String[] words = entry.split("\\s+");
                for (String word : words) {
                    if (word.equalsIgnoreCase(subQuery)) {
                        searchResults.remove(entry);
                        break;
                    }
                }
            }
        }
        return searchResults;
    }


    public static List<String> searchANY(String query) {
        List<String> searchResults = new ArrayList<>();
        String[] queryArray = query.split("\\s+");
        for (String subQuery : queryArray) {
            for (String entry : peopleIndex.keySet()) {
                String[] words = entry.split("\\s+");
                for (String word : words) {
                    if (word.equalsIgnoreCase(subQuery)) {
                        searchResults.add(entry);
                    }
                }
            }
        }
        return searchResults;
    }

    public static List<String> searchALL(String query) {
        List<String> searchResults = new ArrayList<>();
        for (String entry : peopleIndex.keySet()) {
            String[] words = entry.split("\\s+");
            for (String word : words) {
                if (word.equalsIgnoreCase(query)) {
                    searchResults.add(entry);
                }
            }
        }
        return searchResults;
    }

    public static void createData() {
        System.out.println("Enter the number of people:");
        int numberOfPeople = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter all people:");
        for (int i = 0; i < numberOfPeople; i++) {
            peopleIndex.put(scanner.nextLine(), i);
        }
    }

    public static void menu() {
        System.out.println();
        System.out.println("""
                === Menu ===
                1. Search information.
                2. Print all data.
                0. Exit.""");
        int menuOption = scanner.nextInt();
        scanner.nextLine();
        System.out.println();
        switch (menuOption) {
            case 1 -> searchStrategy();
            case 2 -> outputData();
            case 0 -> exitChosen = true;
            default -> System.out.println("Incorrect option! Try again.");
        }
    }
}