package ma.youcode.lineperm.ui;

import java.util.Scanner;

import ma.youcode.lineperm.service.UserService;

public class ConsoleApp{
    Scanner scanner = new Scanner(System.in);

    private UserService us = new UserService();

    public void demmarer(){
            System.out.println("========================================================");
            System.out.println("        LinePerm ? gestion de fichiers & droits ");
            System.out.println("========================================================");
            System.out.println("Non connecte . Commandes : signup  |  login  |  help  |  exit  |  logout\n");
        while(us.actif){
            String line = us.lireLigne();
            traiter(line);
        }
    }

    public void traiter(String ligne){
        String netoyee = ligne.trim();
        if(netoyee.isEmpty()){
            return ;
        }
        String[] mots = ligne.split("\\s+");
        String commande = mots[0].toLowerCase();
        // System.out.println(commande);
        switch(commande){
            case "signup": 
                us.signup();
                break;
            case "login": 
                us.login();
                break;
            case "help": 
                System.out.println("help");break;
            case "logout": 
                us.logout();
                break;
            case "exit": 
                us.exit();
                break;
            default :
                System.out.println("commande not found");break;
        }
    }

    
}