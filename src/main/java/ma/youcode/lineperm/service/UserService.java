package ma.youcode.lineperm.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

// import ma.youcode.lineperm.ui.ConsoleApp;

public class UserService {

    public boolean actif = true;
    Scanner scanner = new Scanner(System.in);

    public String utilisateurConnecte = null;

    public String lireLigne(){
        if(utilisateurConnecte == null){
            System.out.print("lineperm> ");
        }else System.out.print(utilisateurConnecte + "@ligneperm> ");
        return scanner.nextLine();
    }

    public void login(){
        if(utilisateurConnecte != null){
            System.out.println("Vous aves deja connecte !");
            return ;
        }
        
        System.out.print("Login : ");
        String user = scanner.nextLine();
        System.out.print("password : ");
        String password = scanner.nextLine();
        
        if(verifierAuthentification(user,password)){
            System.out.println("Vous ete conncte ");
            utilisateurConnecte = user;
        }
        else
            System.out.println("Mot de pass ou user incorect !");
    }

    public void logout(){
        System.out.println("Vous ete deconnecte.");
        utilisateurConnecte = null;
    }
    
    public void signup(){
        if(utilisateurConnecte != null){
            System.out.println("Vous aves deja connecte !");
            return ;
        }

        System.out.print("Login : ");
        String user = scanner.nextLine();
        System.out.print("password : ");
        String password = scanner.nextLine();

        if(verifierAuthentification(user,password)){
            System.out.println("user ou mot de passe deja exist");
            return;
        }

        if(ajouterText(user,password)){
            if(verifierAuthentification(user,password)){
                System.out.println("inscription avec succes");
                utilisateurConnecte = user;
            }
        }
    }

    public void exit(){
        System.out.println("Au revoir.");
        utilisateurConnecte = null;
        actif = false ;
    }

    public static boolean verifierAuthentification(String loginSaisi, String mdpSaisi){
        try(BufferedReader br = new BufferedReader(new FileReader("src/main/java/ma/youcode/lineperm/Users.txt"))){
            String line;
            while((line = br.readLine()) != null){
                String[] identifiants = line.split(":");
                if(identifiants.length == 2){
                    String loginFichier = identifiants[0].trim();
                    String mdpFichier = identifiants[1].trim();
                    if(loginFichier.equals(loginSaisi) && mdpFichier.equals(mdpSaisi)){
                        return true;
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean ajouterText(String loginSaisi, String mdpSaisi){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/java/ma/youcode/lineperm/Users.txt" , true))){
            bw.write(loginSaisi + " : " + mdpSaisi);
            bw.newLine();
            return true;
        }catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }
}