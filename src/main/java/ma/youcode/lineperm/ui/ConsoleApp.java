package ma.youcode.lineperm.ui;

import java.util.Scanner;

import ma.youcode.lineperm.service.FileService;
import ma.youcode.lineperm.service.UserService;

public class ConsoleApp {
    Scanner scanner = new Scanner(System.in);

    public boolean actif = true;
    public String utilisateurConnecte = null;

    private UserService us = new UserService();
    private FileService fs = new FileService();

    public void demmarer() {
        System.out.println("========================================================");
        System.out.println("        LinePerm ? gestion de fichiers & droits ");
        System.out.println("========================================================");
        System.out.println("Non connecte . Commandes : signup  |  login  |  help  |  exit  |  logout\n");
        while (actif) {
            String line = lireLigne();
            traiter(line);
        }
    }

    public String lireLigne() {
        if (utilisateurConnecte == null) {
            System.out.print("lineperm> ");

        } else {
            System.out.print(utilisateurConnecte + "@ligneperm> ");
        }
        return scanner.nextLine();
    }

    public void traiter(String ligne) {
        String netoyee = ligne.trim();
        if (netoyee.isEmpty()) {
            return;
        }
        String[] commandes = ligne.split("\\s+");

        switch (commandes[0].trim()) {
            case "signup":
                login();
                break;
            case "login":
                signup();
                break;
            case "help":
                System.out.println("help");
                break;
            case "logout":
                logout();
                break;
            case "exit":
                exit();
                break;
            case "ls":
                if (utilisateurConnecte == null) {
                    System.out.println("Tu dois se connecter d'abord");
                    break;
                }
                fs.lsCommande();
                break;
            case "chmod":
                if (utilisateurConnecte == null) {
                    System.out.println("Tu dois se connecter d'abord");
                    break;
                }
                if (commandes.length < 2) {
                    System.out.println("vous devez entrer le fihcier");
                    break;
                }
                fs.chmodCommande(commandes[2].trim(), utilisateurConnecte, commandes[1].trim());
                break;
            case "cat":
                if (utilisateurConnecte == null) {
                    System.out.println("Tu dois se connecter d'abord");
                    break;
                }
                if (commandes.length < 2) {
                    System.out.println("vous devez entrer le fihcier");
                    break;
                }
                fs.catCommande(commandes[1].trim(), utilisateurConnecte);
                break;
            case "nano":
                if (utilisateurConnecte == null) {
                    System.out.println("Tu dois se connecter d'abord");
                    break;
                }
                if (commandes.length < 2) {
                    System.out.println("vous devez entrer le fihcier");
                    break;
                }
                fs.nanoCommande(commandes[1].trim(), utilisateurConnecte);
                break;
            case "rm":
                if (utilisateurConnecte == null) {
                    System.out.println("Tu dois se connecter d'abord");
                    break;
                }
                if (commandes.length < 2) {
                    System.out.println("vous devez entrer le fihcier");
                    break;
                }
                fs.rmCommande(commandes[1].trim(), utilisateurConnecte);
                break;
            case "touch":
                if (utilisateurConnecte == null) {
                    System.out.println("Tu dois se connecter d'abord");
                    break;
                }
                if (commandes.length < 2) {
                    System.out.println("vous devez entrer le fihcier");
                    break;
                }
                if (utilisateurConnecte == null) {
                    System.out.println("Tu dois se connecter d'abord");
                    break;
                }
                fs.touchCommande(commandes[1].trim(), utilisateurConnecte);
                break;
            default:
                System.out.println("commande not found");
                break;
        }
    }

    public void exit() {
        System.out.println("Au revoir.");
        actif = false;
    }

    public void signup() {
        if (utilisateurConnecte != null) {
            System.out.println("Vous aves deja connecte !");
            return;
        }

        System.out.print("Login : ");
        String user = scanner.nextLine();
        System.out.print("password : ");
        String password = scanner.nextLine();

        if (us.login(user, password)) {
            utilisateurConnecte = user;
        }
        return;
    }

    public void login() {
        if (utilisateurConnecte != null) {
            System.out.println("Vous aves deja connecte !");
            return;
        }

        System.out.print("Login : ");
        String user = scanner.nextLine();
        System.out.print("password : ");
        String password = scanner.nextLine();

        if (us.signup(user, password)) {
            utilisateurConnecte = user;
        }
        return;
    }

    public void logout() {
        System.out.println("Vous ete deconnecte.");
        utilisateurConnecte = null;
    }

}