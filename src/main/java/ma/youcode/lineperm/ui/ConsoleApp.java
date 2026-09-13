package ma.youcode.lineperm.ui;

import java.util.Scanner;

import ma.youcode.lineperm.service.FileService;
import ma.youcode.lineperm.service.UserService;

public class ConsoleApp{
    Scanner scanner = new Scanner(System.in);

    public boolean actif = true;
    public String utilisateurConnecte = null;

    private UserService us = new UserService();
    private FileService fs = new FileService();

    public void demmarer(){
            System.out.println("========================================================");
            System.out.println("        LinePerm ? gestion de fichiers & droits ");
            System.out.println("========================================================");
            System.out.println("Non connecte . Commandes : signup  |  login  |  help  |  exit  |  logout\n");
        while(actif){
            String line = lireLigne();
            traiter(line);
        }
    }

    public void traiter(String ligne){
        String netoyee = ligne.trim();
        if(netoyee.isEmpty()){
            return ;
        }
        String[] commandes = ligne.split("\\s+");

        switch(commandes[0].trim()){
            case "signup": 
            signupLogin(1);
                break;
            case "login": 
            signupLogin(0);
                break;
            case "help": 
                System.out.println("help");break;
            case "logout": 
            logout();
                break;
            case "exit": 
            exit();
                break;
            case "ls":
                if(utilisateurConnecte == null){
                    System.out.println("Tu dois se connecter d'abord");break;
                }
                fs.lsCommande();
                break;
            case "chmod":
                System.out.println("chmoud");
                break;
            case "cat":
                fs.hasAcces(commandes[1].trim(),"r",utilisateurConnecte);
                break;
            case "nano":
                fs.hasAcces(commandes[1].trim(),"w",utilisateurConnecte);
                break;
            case "rm":
                fs.hasAcces(commandes[1].trim(),"d",utilisateurConnecte);
                break;
            case "touch":
                if(utilisateurConnecte == null){
                    System.out.println("Tu dois se connecter d'abord");break;
                }
                fs.touchCommande(commandes[1].trim() , utilisateurConnecte);
                break;
            default :
            System.out.println("commande not found");break;
        }
    }

    public String lireLigne(){
        if(utilisateurConnecte == null){
            System.out.print("lineperm> ");

        }else{
            System.out.print(utilisateurConnecte + "@ligneperm> ");
        }
        return scanner.nextLine();
    }

    public void exit(){
        System.out.println("Au revoir.");
        actif = false ;
    }
    
    public void signupLogin(int i){
        if(utilisateurConnecte != null){
            System.out.println("Vous aves deja connecte !");
            return ;
        }
        
        System.out.print("Login : ");
        String user = scanner.nextLine();
        System.out.print("password : ");
        String password = scanner.nextLine();

        if(i == 0){
            if(us.login(user,password)){
                utilisateurConnecte = user;
            }
            return;
        }else{
            us.signup(user,password);
            utilisateurConnecte = user;
            return;
        }
    }

    public void logout(){
        System.out.println("Vous ete deconnecte.");
        utilisateurConnecte = null;
    }
    
}