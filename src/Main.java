import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.JFileChooser;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<String> list = new ArrayList<>();
        boolean running = true;
        boolean needsToBeSaved = false;
        String choice = "";

        while (running) {
            displayMenu(list);
            choice = SafeInput.getRegExString(in, "Choose an option", "[AaDdVvOoSsCcQq]".toUpperCase());

            if (choice.equals("A")) {
                addItem(list, in);
            } else if (choice.equals("D")) {
                deleteItem(list, in);
            } else if (choice.equals("V")) {
                viewList(list);
                
            } else if (choice.equals("O")) {
                if (!needsToBeSaved || confirmSaveIfNeeded(list, needsToBeSaved, in))
                {
                    list = openListFromFile(in);
                    needsToBeSaved = false;
                }
            } else if (choice.equals("S"))
            {
                saveListToFile(list, in);
                needsToBeSaved = false;
            }else if (choice.equals("C"))
            {
                clearList(list, in);
                needsToBeSaved = true;
            } else if (choice.equals("Q")) {
                if (!needsToBeSaved || confirmSaveIfNeeded(list, needsToBeSaved, in))
                {
                    running = false;
                }
            }

        }
    }

    private static void viewList(ArrayList<String> list) {
        if (list.isEmpty()) {
            System.out.println("The list is empty");
        } else {
            for (String item : list) {
                System.out.println(item);
            }
        }
    }

    private static void printNumberedList(ArrayList<String> list) {
        int index = 1;
        for (String item : list) {
            System.out.println(index++ + ". " + item);
        }
    }

    private static void displayMenu(ArrayList<String> list) {
        System.out.println("\n--- Menu ---");
        viewList(list);
        System.out.println("A - Add an item to the list");
        System.out.println("D - Delete an item from the list");
        System.out.println("V - View (i.e Display) the list");
        System.out.println("O - Open a list from a file");
        System.out.println("S - Save the current list to a file");
        System.out.println("C - Clear the list");
        System.out.println("Q - Quit the program");
    }

    private static void addItem(ArrayList<String> list, Scanner pipe) {
        String item = SafeInput.getNonZeroLenString(pipe, "Enter item to add to the list");
        list.add(item);
        System.out.println("The item has been added to the list!");
    }

    private static void deleteItem(ArrayList<String> list, Scanner pipe) {
        if (list.isEmpty()) {
            System.out.println("The list is already empty. There is nothing to delete!");
            return;
        }
        printNumberedList(list);
        int index = SafeInput.getRangedInt(pipe, "Enter the number of the item you wish to delete!", 1, list.size()) - 1;
        list.remove(index);
        System.out.println("The item has been removed");
    }
    private static void saveListToFile(ArrayList<String> list, Scanner scanner) {
        String filename = SafeInput.getNonZeroLenString(scanner, "Enter the filename to save the list");
        if (!filename.endsWith(".txt")) {
            filename += ".txt";
        }

        try (PrintWriter out = new PrintWriter(filename)) {
            for (String item : list) {
                out.println(item);
            }
            System.out.println("List saved to " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private static ArrayList<String> openListFromFile(Scanner scanner) {
        System.out.print("Enter the filename to open: ");
        String filename = scanner.nextLine();
        ArrayList<String> newList = new ArrayList<>();

        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                newList.add(fileScanner.nextLine());
            }
            System.out.println("List loaded from " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }

        return newList;
    }

    private static void clearList(ArrayList<String> list, Scanner pipe)
    {
        if (SafeInput.getYNConfirm(pipe, "Are you sure you want to clear the list"))
        {
            list.clear();
            System.out.println("The list has been cleared");
        }
    }
    private static boolean confirmSaveIfNeeded(ArrayList<String> list, boolean needsToBeSaved, Scanner pipe)
    {
        if (needsToBeSaved)
        {
            System.out.println("You have unsaved changes. Would you like to save? y/n");
            String response = pipe.nextLine().toLowerCase();
            if (response.equals("y"));
            {
                saveListToFile(list, pipe);
                return true;
            }
        }return !needsToBeSaved;
    }

}