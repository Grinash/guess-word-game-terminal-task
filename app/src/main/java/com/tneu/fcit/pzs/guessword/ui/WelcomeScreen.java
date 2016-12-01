package com.tneu.fcit.pzs.guessword.ui;

import com.tneu.fcit.pzs.guessword.model.User;
import com.tneu.fcit.pzs.guessword.service.UserService;
import com.tneu.fcit.pzs.guessword.service.UserServiceImpl;
import com.tneu.fcit.pzs.guessword.utils.Utils;
import com.tneu.fcit.pzs.guessword.view.GameViewImpl;

import java.util.*;

/**
 * Created by yp on 02.11.16.
 */
public class WelcomeScreen {

    private final UserService userService = new UserServiceImpl();

    public void showWelcome() {
        System.out.println("Welcome! Please [l]ogin, [r]egister or [s]how best results");
        String line = Utils.SCANNER.nextLine();
        if (line.equalsIgnoreCase("l")) {
            onLogin();
        } else if (line.equalsIgnoreCase("r")) {
            onRegister();
        } else if (line.equalsIgnoreCase("s")) {
            onShow();
        }
    }

    private void onShow() {
        System.out.println("Records table of users");

        ArrayList allUser = (ArrayList) userService.all();

        allUser.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                int a1 = ((User) o1).getScore();
                int a2 = ((User) o1).getScore();
                return a1 < a2 ? 1 : (a1 > a2 ? -1 : 0);
            }
        });

        for (Object user : allUser) {
            System.out.println(((User) user).getNick() + "\t" + ((User) user).getScore());
        }
    }

    private void onRegister() {
        System.out.println("Registration is started");
        String nick;
        while (true) {
            nick = promptForNick();
            if (userService.all().get(nick) != null) {
                System.err.printf("The nick %s is busy (", nick);
            } else {
                break;
            }
        }
        String pass = promptForPass();
        User user = new User(nick, pass);
        userService.save(user);
        startGameForUser(user);
    }

    private static void startGameForUser(User user) {
        System.out.println("Hello, " + user.getNick() + "!");
        new GameViewImpl(user).gameLoop();
    }

    private void onLogin() {
        System.out.println("Login started");
        User user;
        while (true) {
            String nick = promptForNick();
            String pass = promptForPass();
            user = userService.check(nick, pass);
            if (user == null) {
                System.err.println("Wrong nick or password");
            } else {
                break;
            }
        }

        System.out.println("Do you want to change your data, [y]es or no");
        String line = Utils.SCANNER.nextLine();
        if (line.equalsIgnoreCase("y"))
            onChange(user);

        startGameForUser(user);
    }

    private void onChange(User user) {
        System.out.println("What you want to edit password: y");
        if (Utils.SCANNER.nextLine().toLowerCase().equals("Y"))
            user.setPassword(promptForPass());

        System.out.println("What you want to edit name: y");
        if (Utils.SCANNER.nextLine().toLowerCase().equals("Y"))
            user.setName(promptForName());

        System.out.println("What you want to edit surname: y");
        if (Utils.SCANNER.nextLine().toLowerCase().equals("Y"))
            user.setSurname(promptForSurname());

        System.out.println("What you want to edit sex: y");
        if (Utils.SCANNER.nextLine().toLowerCase().equals("Y"))
            user.setSex(promptForSex());

        System.out.println("What you want to edit birth: y");
        if (Utils.SCANNER.nextLine().toLowerCase().equals("Y"))
            user.setBirthYear(promptForBirth());

        userService.save(user);
    }

    private static String promptForPass() {
        System.out.println("Enter your pass, please");
        return Utils.SCANNER.nextLine();
    }

    private static String promptForNick() {
        System.out.println("Enter your nick, please");
        return Utils.SCANNER.nextLine();
    }

    private String promptForName() {
        System.out.println("Enter your name, please");
        return Utils.SCANNER.nextLine();
    }

    private String promptForSurname() {
        System.out.println("Enter your surname, please");
        return Utils.SCANNER.nextLine();
    }

    private boolean promptForSex() {
        boolean sex;
        while (true) {
            System.out.println("Enter your sex (woman or man), please");
            String Sex = Utils.SCANNER.nextLine();
            if (Sex.toLowerCase().equals("woman")) {
                sex = false;
                break;
            } else if (Sex.toLowerCase().equals("man")) {
                sex = true;
                break;
            } else
                System.out.println("Please enter 'woman' or 'man'");
        }
        return sex;
    }

    private int promptForBirth() {
        int year;
        while (true) {
            System.out.println("Enter your birth year, please");
            try {
                year = Utils.SCANNER.nextInt();
                break;
            } catch (Exception e) {
                System.out.println("Please enter real year");
            }
        }
        return year;
    }
}
