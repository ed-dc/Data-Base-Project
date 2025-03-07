package enchere.env;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Global {
    public static final String BLACK = "\033[0;30m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;33m";
    public static final String PURPLE = "\033[0;35m";
    public static final String LIGHT_BLUE = "\033[0;36m";
    public static final String WHITE = "\033[0;33m";

    public static final String RESET = "\033[0m";

    public static final String BOLD = "\033[0;1m";

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();
    }

    public static void enchereAscii()
    {
        clearScreen();

        String border = "╔" + "═".repeat(59) + "╗";
        int widthTerminal = terminal();
        int spaces = (widthTerminal - border.length());

        System.out.println(" ".repeat(Math.max(0, spaces)) + border);   
        System.out.println(Global.LIGHT_BLUE + " ".repeat(Math.max(0, spaces)) + "  ███████╗███╗   ██╗ ██████╗██╗  ██╗███████╗██████╗ ███████╗\n" + //
                        " ".repeat(Math.max(0, spaces)) + "  ██╔════╝████╗  ██║██╔════╝██║  ██║██╔════╝██╔══██╗██╔════╝\n" + //
                        " ".repeat(Math.max(0, spaces)) + "  █████╗  ██╔██╗ ██║██║     ███████║█████╗  ██████╔╝█████╗  \n" + //
                        " ".repeat(Math.max(0, spaces)) + "  ██╔══╝  ██║╚██╗██║██║     ██╔══██║██╔══╝  ██╔══██╗██╔══╝  \n" + //
                        " ".repeat(Math.max(0, spaces)) + "  ███████╗██║ ╚████║╚██████╗██║  ██║███████╗██║  ██║███████╗\n" + //
                        " ".repeat(Math.max(0, spaces)) + "  ╚══════╝╚═╝  ╚═══╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚══════╝"+
                        Global.RESET);
        System.out.println(" ".repeat(Math.max(0, spaces)) + "╚" + "═".repeat(59) + "╝");
        //System.out.println("\n".repeat(2));
    }

    public static int terminal() {
        try {
            // Obtenir la largeur du terminal avec "tput cols"
            ProcessBuilder processBuilder = new ProcessBuilder("tput", "cols");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int largeurTerminal = Integer.parseInt(reader.readLine());

            return largeurTerminal;

        } catch (Exception e) {
            System.err.println("Erreur lors de l'obtention de la largeur du terminal. Utilisez une largeur fixe.");
        }
        return 80;
    }
}
