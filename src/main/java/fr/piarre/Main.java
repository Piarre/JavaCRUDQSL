package fr.piarre;

import fr.piarre.Exceptions.Auth.UserNotFoundException;
import fr.piarre.Exceptions.Auth.WrongPasswordException;
import fr.piarre.Managers.DatabaseManager;
import fr.piarre.Managers.UserManager;
import fr.piarre.Models.LoginReturn;
import fr.piarre.Models.User;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws SQLException, UserNotFoundException, WrongPasswordException {
        Scanner scanner = new Scanner(System.in);

        DatabaseManager databaseManager = new DatabaseManager();
        Statement statement = databaseManager.connect();
        UserManager userManager = new UserManager(statement);

        User loggedUser = null;
        boolean isLogged = false;

        System.out.println("Welcome to the CRUD app!\n");

        System.out.println("You can now:");
        System.out.println("~ (login) Login");
        System.out.println("~ (register) Register");

        System.out.print("\nPlease enter your choice:");
        String choice = scanner.nextLine();

        switch (choice) {
            case "login":
                System.out.println();

                while (!isLogged) {
                    String email = null;
                    User user = null;

                    while (email == null) {
                        System.out.print("Please enter your email: ");
                        email = scanner.nextLine();
                        user = userManager.GetByEmail(email);
                    }

                    System.out.print("Please enter your password: ");
                    String password = scanner.nextLine();

                    LoginReturn loginReturn = userManager.Login(new User(email, password));

                    if (loginReturn == LoginReturn.WRONG_PASSWORD) {
                        System.out.println("\nWrong password. Please try again.");
                        password = null;
                        while (password == null) {
                            System.out.print("Please enter your password (" + email + "): ");
                            password = scanner.nextLine();
                            LoginReturn loginReturn2 = userManager.Login(new User(email, password));
                            if (loginReturn2 == LoginReturn.WRONG_PASSWORD) {
                                password = null;
                            } else {
                                System.out.println("\nLogin successful!\n");
                                isLogged = true;
                                loggedUser = user;
                            }
                        }
                    } else if (loginReturn == LoginReturn.USER_NOT_FOUND) {
                        System.out.println("\nUser not found. Please try again.");
                        main(null);
                    } else {
                        System.out.println("\nLogin successful!\n");
                        isLogged = true;
                        loggedUser = user;
                    }
                }
                loggedMenu(statement, userManager, loggedUser);
                main(null);
                break;
            case "register":
                RegisterMenu(scanner, userManager);
                main(null);
                break;
            default:
                System.out.println("Invalid choice. Please enter a valid option.");
                main(null);
                break;
        }

        databaseManager.close();
    }

    public static void loggedMenu(Statement statement, UserManager userManager, User loggedUser) throws UserNotFoundException, SQLException, WrongPasswordException {
        Scanner scanner = new Scanner(System.in);

        PrintMainMenu();
        String choice = scanner.nextLine();
        System.out.println();

        while (true) {
            switch (choice) {
                case "list":
                    userManager.GetAllUsers();
                    loggedMenu(statement, userManager, loggedUser);
                    break;
                case "search":
                    System.out.print("Choose a column to search by (id, name, surname, email) or exit: ");
                    String column = scanner.nextLine();
                    if (column.isEmpty() || column == null) {
                        System.out.println("Invalid column.");
                        break;
                    } else if (column.equals("exit")) {
                        break;
                    }
                    System.out.print("Enter the value to search: ");
                    String value = scanner.nextLine();
                    switch (column) {
                        case "id":
                            User foundedUser = null;
                            foundedUser = userManager.GetUserById(Integer.valueOf(value));
                            if (foundedUser != null) {
                                foundedUser.Print();
                            } else {
                                System.out.println("User not found.");
                            }
                            loggedMenu(statement, userManager, loggedUser);
                            break;
                        case "email":
                            ArrayList<User> foundedUser1 = userManager.GetUsersByEmail(value);

                            System.out.println("Id | Name | Surname | Email | Password | Actif | Age");
                            for (User user : foundedUser1) {
                                user.PrintValues();
                            }
                            loggedMenu(statement, userManager, loggedUser);
                            break;
                        case "name":
                            ArrayList<User> usersByName = userManager.GetUsersByName(value);

                            System.out.println("Id | Name | Surname | Email | Password | Actif | Age");
                            for (User user : usersByName) {
                                user.PrintValues();
                            }
                            loggedMenu(statement, userManager, loggedUser);
                            break;
                        case "surname":
                            ArrayList<User> usersBySurName = userManager.GetUsersByName(value);

                            System.out.println("Id | Name | Surname | Email | Password | Actif | Age");
                            for (User user : usersBySurName) {
                                user.PrintValues();
                            }
                            loggedMenu(statement, userManager, loggedUser);
                            break;
                        case "exit":
                            loggedMenu(statement, userManager, loggedUser);
                            break;
                        default:
                            System.out.println("Invalid column.");
                            break;
                    }
                    break;
                case "add":
                    String name = "";
                    String surname = "";
                    String email = "";
                    String password = "";
                    String actif = "";
                    Integer age = 0;

                    while (name == null || name.isEmpty()) {
                        System.out.print("Please enter the name: ");
                        name = scanner.nextLine();
                    }

                    while (surname == null || surname.isEmpty()) {
                        System.out.print("Please enter the surname: ");
                        surname = scanner.nextLine();
                    }

                    while (email == null || email.isEmpty()) {
                        System.out.print("Please enter the email: ");
                        email = scanner.nextLine();
                    }

                    while (password == null || password.isEmpty()) {
                        System.out.print("Please enter the password: ");
                        password = scanner.nextLine();
                    }

                    while (actif == null || actif.isEmpty()) {
                        System.out.print("Please enter the job: ");
                        actif = scanner.nextLine();
                    }

                    System.out.print("Please enter the age: ");
                    while (age == null || !scanner.hasNextInt()) {
                        if (!scanner.hasNextInt()) {
                            System.out.println("Age must be an integer.");
                            scanner.next();
                        } else {
                            System.out.println("Age must be greater than 0.");
                        }
                    }

                    User user = new User(name, surname, email, password, actif, age);

                    userManager.AddUser(user, loggedUser);
                    loggedMenu(statement, userManager, loggedUser);
                    break;
                case "select":
                    Boolean isId = false;

                    userManager.GetAllUsers();
                    System.out.print("Please enter the id of the user to select: ");
                    Integer selectedId = scanner.nextInt();

                    if (!userManager.UserExists(selectedId)) {
                        System.out.println("User not found.");
                        break;
                    }

                    isId = true;

                    PrintMainMenu(selectedId);

                    String choice2 = scanner.next();
                    System.out.println();

                    while (isId) {
                        if (choice2.equals("exit")) {
                            break;
                        }
                        switch (choice2) {
                            case "show":
                                User selectedUser = userManager.GetUserById(selectedId);
                                selectedUser.Print();
                                System.out.println();
                                PrintMainMenu(selectedId);

                                choice2 = scanner.nextLine();
                                break;
                            case "update":
                                User userToUpdate = userManager.GetUserById(selectedId);
                                String newName = "";
                                String newSurname = "";
                                String newEmail = "";
                                String newPassword = "";
                                String newActif = "";
                                Integer newAge = 0;

                                System.out.print("Please enter the name: ");
                                newName = scanner.nextLine();
                                if (!newName.isEmpty()) {
                                    userToUpdate.setName(newName);
                                }

                                System.out.print("Please enter the surname: ");
                                newSurname = scanner.nextLine();
                                if (!newSurname.isEmpty()) {
                                    userToUpdate.setSurname(newSurname);
                                }

                                System.out.print("Please enter the email: ");
                                newEmail = scanner.nextLine();
                                if (!newEmail.isEmpty()) {
                                    userToUpdate.setEmail(newEmail);
                                }

                                System.out.print("Please enter the password: ");
                                newPassword = scanner.nextLine();
                                if (!newPassword.isEmpty()) {
                                    userToUpdate.setPassword(newPassword);
                                }

                                System.out.print("Please enter the job: ");
                                newActif = scanner.nextLine();
                                if (!newActif.isEmpty()) {
                                    userToUpdate.setActif(newActif);
                                }

                                System.out.print("Please enter the age: ");
                                Integer age2 = null;

                                while (!scanner.hasNextInt()) {
                                    System.out.println("Age must be an integer.");
                                    scanner.next();
                                }

                                System.out.println("Please enter the age:");
                                age2 = scanner.nextInt();

                                userToUpdate.setAge(age2);

                                userManager.UpdateUser(userToUpdate);

                                PrintMainMenu();
                                if (scanner.hasNextLine()) {
                                    scanner.nextLine();
                                }
                                choice2 = scanner.nextLine();
                                break;
                            case "delete":
                                isId = false;
                                userManager.DeleteUser(selectedId);
                                selectedId = null;
                                choice2 = scanner.nextLine();
                                break;
                            case "quit":
                                isId = false;
                                selectedId = null;
                                loggedMenu(statement, userManager, loggedUser);

//                                PrintMainMenu();

                                break;
                            case "exit":
                                System.out.println("Exiting the CRUD app. Goodbye!");
                                System.exit(0);
                                break;
                            default:
                                System.out.print("Invalid choice. Please enter a valid option.");
                                choice2 = scanner.nextLine();
                                break;
                        }
                    }
                    break;
                case "logout":
                    System.out.println("You have been logged out.");
                    main(null);
                    break;
                case "exit":
                    System.out.println("Exiting the CRUD app. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.print("Invalid choice. Please enter a valid option.");
                    loggedMenu(statement, userManager, loggedUser);
                    break;
            }
        }
    }

    private static void RegisterMenu(Scanner scanner, UserManager userManager) {
        System.out.println("\nRegistering");

        System.out.print("Please enter your name: ");
        String name = scanner.nextLine();
        while (name == null || name.isEmpty()) {
            System.out.print("Please enter your name: ");
            name = scanner.nextLine();
        }

        System.out.print("Please enter your surname: ");
        String surname = scanner.nextLine();
        while (surname == null || surname.isEmpty()) {
            System.out.print("Please enter your surname: ");
            surname = scanner.nextLine();
        }

        System.out.print("Please enter your email: ");
        String email = scanner.nextLine();
        User emaiLExists = userManager.GetByEmail(email);
        if (emaiLExists != null) {
            System.out.println("Email already exists.");
            RegisterMenu(scanner, userManager);
        }
        while (email == null || email.isEmpty()) {
            System.out.print("Please enter your email: ");
            email = scanner.nextLine();
        }

        System.out.print("Please enter your password: ");
        String password = scanner.nextLine();
        while (password == null || password.isEmpty()) {
            System.out.print("Please enter your password: ");
            password = scanner.nextLine();
        }

        System.out.print("Please enter your job: ");
        String actif = scanner.nextLine();
        while (actif == null || actif.isEmpty()) {
            System.out.print("Please enter your job: ");
            actif = scanner.nextLine();
        }

        System.out.print("Please enter your age: ");
        Integer age = scanner.nextInt();
        while (age == null) {
            System.out.print("Please enter your age: ");
            age = scanner.nextInt();
        }

        User user = new User(name, surname, email, password, actif, age);
        userManager.AddUser(user, new User("SYSTEM"));
    }

    private static void PrintMainMenu() {
        System.out.println("You can now:");
        System.out.println("~ (list) List users");
        System.out.println("~ (search) Search user");
        System.out.println("~ (add) Add user");
        System.out.println("~ (select) Select user");
        System.out.println("~ (logout) Logout");
        System.out.println("~ (exit) Exit\n");

        System.out.print("Please enter your choice:");
    }

    private static void PrintMainMenu(Integer selectedId) {
        System.out.println("You can now:");
        System.out.println("~ (show) Show user with id " + selectedId);
        System.out.println("~ (update) Update user with id " + selectedId);
        System.out.println("~ (delete) Delete user with id " + selectedId);
        System.out.println("~ (quit) Quit selected user (" + selectedId + ")");
        System.out.println("~ (exit) Exit\n");

        System.out.print("Please enter your choice:");
    }
}