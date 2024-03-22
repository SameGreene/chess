package ui;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        // Print out welcome screen
        System.out.println("Welcome to CS 240 Chess\nType 'help' to get started");

        // STATES
        // 0 - PRE-LOGIN
        // 1 - POST-LOGIN
        // 2 - GAMEPLAY
        int userState = 0;

        // User input Scanner
        Scanner userInput = new Scanner(System.in);

        // TEXT COLORS
        String defaultColor = "\u001B[0m";
        String blueColor = "\u001B[34m";

        while (true) {
            switch (userState) {
                // Not logged in
                case 0:
                    String preInput = userInput.nextLine();
                    String[] splitPreInput = preInput.split("\\s+");
                    // register
                    if (splitPreInput[0].equals("register") && splitPreInput.length == 4) {
                        String username = splitPreInput[1];
                        String password = splitPreInput[2];
                        String email = splitPreInput[3];

                        // TODO - Handle register request

                        // Successful. Print success message and move to post state
                        System.out.println("Logged in as " + blueColor + username + defaultColor);
                        System.out.println("Type 'help' for a list of available options.");
                        userState = 1;
                    }
                    // login
                    else if (splitPreInput[0].equals("login") && splitPreInput.length == 3) {
                        String username = splitPreInput[1];
                        String password = splitPreInput[2];
                        System.out.println(username);
                        System.out.println(password);

                        // TODO - Handle login request

                        // Successful. Print success message and move to post state
                        System.out.println("Logged in as " + blueColor + username + defaultColor);
                        System.out.println("Type 'help' for a list of available options.");
                        userState = 1;
                    }
                    // quit
                    else if (splitPreInput[0].equals("quit")) {
                        System.out.println("Exiting... Goodbye!");
                        return;
                    }
                    // help
                    else if (splitPreInput[0].equals("help")) {
                        System.out.println("register <USERNAME> <PASSWORD> <EMAIL>" + blueColor + " - Register a new user" + defaultColor);
                        System.out.println("login <USERNAME> <PASSWORD>" + blueColor + " - Login with existing credentials" + defaultColor);
                        System.out.println("quit" + blueColor + " - Exit the game" + defaultColor);
                        System.out.println("help" + blueColor + " - List available commands" + defaultColor);
                    }
                    // error handling
                    else {
                        System.out.println("ERROR: Unknown command or incorrect syntax. Please type 'help' for a list of commands");
                    }
                    break;

                // Logged in
                case 1:
                    String postInput = userInput.nextLine();
                    String[] splitPostInput = postInput.split("\\s+");
                    // create
                    if (splitPostInput[0].equals("create") && splitPostInput.length == 2) {
                        String gameName = splitPostInput[1];
                    }
                    // list
                    else if (splitPostInput[0].equals("list")) {
                        // TODO - Handle list request
                    }
                    // join
                    else if (splitPostInput[0].equals("join") && splitPostInput.length == 3) {
                        // TODO - Handle join request
                        String gameID = splitPostInput[1];
                        String teamColor = splitPostInput[2];
                    }
                    // observe
                    else if (splitPostInput[0].equals("observe") && splitPostInput.length == 2) {
                        // TODO - Handle observe request
                        String gameID = splitPostInput[1];
                    }
                    // logout
                    else if (splitPostInput[0].equals("logout")) {
                        // TODO - Handle logout request

                        // Successful. Print success message and move to pre stage
                        System.out.println("Successfully logged out.");
                        System.out.println("Type 'help' to get started.");
                        userState = 0;
                    }
                    // quit
                    else if (splitPostInput[0].equals("quit")) {
                        System.out.println("Exiting... Goodbye!");
                        return;
                    }
                    // help
                    else if (splitPostInput[0].equals("help")) {
                        System.out.println("create <GAME_NAME>" + blueColor + " - Create a game" + defaultColor);
                        System.out.println("list" + blueColor + " - Show available games" + defaultColor);
                        System.out.println("join <ID> [WHITE|BLACK|<empty>]" + blueColor + " - Join game by ID" + defaultColor);
                        System.out.println("observe <ID>" + blueColor + " - Watch a game" + defaultColor);
                        System.out.println("logout" + blueColor + " - Logout" + defaultColor);
                        System.out.println("quit" + blueColor + " - Exit the game" + defaultColor);
                        System.out.println("help" + blueColor + " - List available commands" + defaultColor);
                    }
                    // error handling
                    else {
                        System.out.println("ERROR: Unknown command or incorrect syntax. Please type 'help' for a list of commands");
                    }
                    break;

                // In a game
                case 2:
                    break;

                // Error encountered. Exit the program
                default:
                    System.out.println("Unknown state error occured. Exiting...");
                    return;
            }
        }
    }
}
