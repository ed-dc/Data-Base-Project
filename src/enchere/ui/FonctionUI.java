package enchere.ui;

import enchere.dao.*;
import enchere.env.*;

import java.util.List;
import java.util.Scanner;

public class FonctionUI {

    private static Scanner scanner = null;

    public static String nextErrorMessage = null;
    public static String nextSuccessMessage = null;

    private static int lireChoixUser() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }

        return Integer.parseInt(scanner.nextLine().trim());
    }

    private static String lireStringUser() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        return scanner.nextLine().trim();
    }

    public static void quitterApplication() {
        System.out.println("Au revoir !");
        scanner.close();
        DatabaseConnection.closeConnection();
        System.exit(0);
    }

    public static boolean questionYesNo(String titre, String question) {
        Integer isMontante = -2;
        while (isMontante == -2 || isMontante == -1) {
            String choix = FonctionUI.displayQuestion(
                    titre,
                    question,
                    isMontante == -1 ? "Veuillez répondre par oui ou non" : "");
            if (choix.equalsIgnoreCase("oui")
                    || choix.equalsIgnoreCase("o")
                    || choix.equalsIgnoreCase("y")
                    || choix.equalsIgnoreCase("yes")) {
                return true;
            } else if (choix.equalsIgnoreCase("non")
                    || choix.equalsIgnoreCase("n")
                    || choix.equalsIgnoreCase("no")) {
                return false;
            } else {
                isMontante = -1;
            }
        }
        return true;
    }

    public static Integer intQuestion(String titre, String question, String errorMessage) {
        Integer res = null;
        boolean premier = true;
        while (res == null) {
            String choix = FonctionUI.displayQuestion(titre, question, premier ? "" : errorMessage);
            try {
                res = Integer.parseInt(choix);
            } catch (Exception e) {
                res = null;
                premier = false;
            }
        }
        return res;
    }

    public static String displayQuestion(String title, String question, String errorMessage) {
        int minWidth = 58;
        int padding = 2;
        int widthTerminal = Global.terminal();

        int titleWidth = title.length() + 6; // "<<<=== " + " ===>>>"
        int tableWidth = Math.max(minWidth, titleWidth + padding * 2);

        String horizontalLine = "╔" + "═".repeat(tableWidth) + "╗";
        String bottomLine = "╚" + "═".repeat(tableWidth) + "╝";

        String titleLine = "║" + centerText("<<<=== " + title + " ===>>>", tableWidth) + "║";

        int spaces = (widthTerminal - horizontalLine.length());

        Global.enchereAscii();

        // System.out.println(enchereAscii());

        System.out.println(" ".repeat(Math.max(0, spaces)) + horizontalLine);
        System.out.println(" ".repeat(Math.max(0, spaces)) + titleLine);

        System.out.println(" ".repeat(Math.max(0, spaces)) + bottomLine);

        if (errorMessage != "") {
            displayMessage(errorMessage, Global.RED);
        }

        System.out.print(
                " ".repeat(Math.max(0, spaces))
                        + "==> "
                        + Global.BOLD
                        + question
                        + Global.RESET
                        + "  : ");
        String res = lireStringUser();
        System.out.println("\n".repeat(2));
        return res;
    }

    private static int displayMenue(
            String title, String description, List<String> choices, String Error) {
        int minWidth = 58;
        int padding = 2;
        int widthTerminal = Global.terminal();

        int titleWidth = title.length() + 6; // "<<<=== " + " ===>>>"
        int maxChoiceWidth = choices.stream()
                .mapToInt(choice -> choice.length() + 4)
                .max()
                .orElse(0); // "N. " + choix
        int tableWidth = Math.max(minWidth, Math.max(titleWidth, maxChoiceWidth) + padding * 2);

        String horizontalLine = "╔" + "═".repeat(tableWidth) + "╗";
        String middleLine = "╠" + "═".repeat(tableWidth) + "╣";
        String bottomLine = "╚" + "═".repeat(tableWidth) + "╝";

        int spaces = (widthTerminal - horizontalLine.length());

        String titleLine = "║" + centerText("<<<=== " + title + " ===>>>", tableWidth) + "║";

        String[] descriptionLines = wrapText(description, tableWidth - padding * 2).stream()
                .map(line -> "║" + centerText(line, tableWidth) + "║")
                .toArray(String[]::new);

        String innerHorizontalLine = "╔" + "═".repeat(tableWidth - padding * 2) + "╗";
        String innerBottomLine = "╚" + "═".repeat(tableWidth - padding * 2) + "╝";

        StringBuilder choiceLines = new StringBuilder();
        for (int i = 0; i < choices.size(); i++) {
            int numero = i + 1 == choices.size() ? 0 : i + 1;
            String choice = numero + ". " + choices.get(i);
            choiceLines
                    .append(" ".repeat(Math.max(0, spaces)))
                    .append("║ ║  ")
                    .append(choice)
                    .append(" ".repeat(Math.max(0, tableWidth - choice.length() - 6)))
                    .append("║ ║\n");
        }

        Global.enchereAscii();

        // System.out.println(enchereAscii());

        if (Error != "") {
            displayMessage(Error, Global.RED);
        }
        if (nextErrorMessage != null) {
            displayMessage(nextErrorMessage, Global.RED);
            nextErrorMessage = null;
        }
        if (nextSuccessMessage != null) {
            displayMessage(nextSuccessMessage, Global.GREEN);
            nextSuccessMessage = null;
        }

        System.out.println(" ".repeat(Math.max(0, spaces)) + horizontalLine);
        System.out.println(" ".repeat(Math.max(0, spaces)) + titleLine);
        System.out.println(" ".repeat(Math.max(0, spaces)) + middleLine);

        for (String line : descriptionLines) {
            System.out.println(" ".repeat(Math.max(0, spaces)) + line);
        }

        System.out.println(" ".repeat(Math.max(0, spaces)) + middleLine);
        System.out.println(" ".repeat(Math.max(0, spaces)) + "║ " + innerHorizontalLine + " ║");
        System.out.println(" ".repeat(Math.max(0, spaces)) + choiceLines.toString().trim());
        System.out.println(" ".repeat(Math.max(0, spaces)) + "║ " + innerBottomLine + " ║");
        System.out.println(" ".repeat(Math.max(0, spaces)) + bottomLine);

        System.out.println("\n\n");
        return spaces;
    }

    public static int displayMenueChoixMultiple(
            String title, String description, List<String> choices, String Error) {

        int spaces = displayMenue(title, description, choices, Error);

        System.out.print(" ".repeat(Math.max(0, spaces)) + ("==> Votre choix " + ": "));
        int res = lireChoixUser();
        System.out.println("\n".repeat(2));
        return res;
    }

    public static void displayMenueAffichage(
            String title, String description, List<String> choices, String Error) {

        int spaces = displayMenue(title, description, choices, Error);
        System.out.print(" ".repeat(Math.max(0, spaces)) + "==> Tapez sur entrée pour continuer");
        lireStringUser();
        System.out.println("\n".repeat(2));
    }

    public static void displayMessage(String message, String color) {
        int minWidth = 58;
        int padding = 2;
        int widthTerminal = Global.terminal();

        int titleWidth = message.length() + 12; // "<<<=== " + " ===>>>"
        int tableWidth = Math.max(minWidth, titleWidth + padding * 2);

        String horizontalLine = color + "╔" + "═".repeat(tableWidth) + "╗";
        String bottomLine = "╚" + "═".repeat(tableWidth) + "╝" + Global.RESET;
        String titleLine = "║" + color + centerText("<<<=== " + message + " ===>>>", tableWidth) + "║";

        int spaces = (widthTerminal - (horizontalLine.length() - color.length()));

        System.out.println(" ".repeat(Math.max(0, spaces)) + horizontalLine);
        System.out.println(" ".repeat(Math.max(0, spaces)) + titleLine);
        System.out.println(" ".repeat(Math.max(0, spaces)) + bottomLine);
    }

    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding))
                + text
                + " ".repeat(Math.max(0, width - text.length() - padding));
    }

    private static List<String> wrapText(String text, int width) {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        List<String> lines = new java.util.ArrayList<>();

        for (String word : words) {
            if (line.length() + word.length() + 1 > width) {
                lines.add(line.toString().trim());
                line = new StringBuilder();
            }
            line.append(word).append(" ");
        }
        if (line.length() > 0) {
            lines.add(line.toString().trim());
        }

        return lines;
    }
}
