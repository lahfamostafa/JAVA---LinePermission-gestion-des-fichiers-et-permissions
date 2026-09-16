package ma.youcode.lineperm.service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
// import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ma.youcode.lineperm.model.Fichier;

public class FileService {

    private final Path saveFoder = Path.of("src/main/java/ma/youcode/lineperm/files");
    Map <String,Fichier> fileMap = new HashMap<>();
    private final Path dataFile = Path.of("src/main/java/ma/youcode/lineperm/Files.txt");

    public FileService() {
        loadFile();
    }

    public void lsCommande(){
        for(Fichier f:fileMap.values()){
            System.out.printf("%-12s %-18s %s%n",f.getPersmission(),f.getName(),f.getProprietaire());
        }
    }
    
    public void touchCommande(String FileName , String propr){
        try {
            Path filePath = saveFoder.resolve(FileName);
            if (Files.exists(filePath)) {
                System.out.println("Le fichier " + FileName + " existe deja !");
                return;
            }
            FileWriter fw = new FileWriter(dataFile.toFile(),true);
            Fichier fichier = new Fichier(propr, FileName);
            fw.write(fichier.getName()+","+fichier.getProprietaire()+","
                    +fichier.isOwnerRead()+","+fichier.isOwnerWrite()+","+fichier.isOwnerDelete()+","
                    +fichier.isAutherRead()+","+fichier.isAutherWrite()+","+fichier.isAutherDelete()
                +"\n");
            fw.close();
            loadFile();
            Files.createDirectories(saveFoder);
            Files.createFile(filePath);
            System.out.println("Fichier " + FileName + " cree avec succes.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadFile(){
        try {
            List<String> lignes = Files.readAllLines(dataFile);
            for(String ligne :lignes){
                String[] line = ligne.split(",");
                String FileName = line[0];
                String Owner = line[1];
                boolean OW = Boolean.parseBoolean(line[2]);
                boolean OR = Boolean.parseBoolean(line[3]);
                boolean OD = Boolean.parseBoolean(line[4]);
                boolean AW = Boolean.parseBoolean(line[5]);
                boolean AR = Boolean.parseBoolean(line[6]);
                boolean AD = Boolean.parseBoolean(line[7]);
                Fichier fichier =new Fichier(Owner, FileName, OW, OR, OD, AW, AR, AD);
                fileMap.put(FileName, fichier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasAcces(String file, String perm ,String user, String newPerm){
        if(user == null){
            System.out.println("Tu dois se connecter d'abord");return false;
        }
        for(Fichier f:fileMap.values()){
            if(f.getName().equals(file)){
                if(perm.equals("w")){
                    if (f.isAutherWrite() || (user.equals(f.getProprietaire()))) {
                        nanoCommande(file);
                        return true;
                    }
                    System.out.println("vous n'avez pas la permission d'ecrire dans : "+file);
                }else if(perm.equals("r")){
                    if (f.isAutherRead() || (user.equals(f.getProprietaire()))) {
                        catCommande(file);
                        return true;
                    }
                    System.out.println("vous n'avez pas la permission de lire : "+file);
                }else if(perm.equals("c")){
                    if (user.equals(f.getProprietaire())) {
                        chmodCommande(file,newPerm);
                        return true;
                    }
                    System.out.println("vous n'avez pas la permission de modifier les permssions : "+file);
                }else if(perm.equals("d")){
                    if (f.isAutherDelete() || (user.equals(f.getProprietaire()))) {
                        System.out.println("vous avez la permission de supprimer  : "+file);
                        return true;
                    }
                    System.out.println("vous n'avez pas la permission de supprimer : "+file);
                }return false;
            }
        }
        System.out.println("le fichier introuvable");
        return false;
    }

    public void catCommande(String file){
        Path folderFiles = saveFoder.resolve(file);
        try {
            List<String> lines = Files.readAllLines(folderFiles);
            if(lines.size() == 0){
                System.out.println("(fichier vide)");
                return ;
            }
            for(String f:lines){
                System.out.println(f);
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    public void nanoCommande(String file){
        Path folderFiles = saveFoder.resolve(file);
        System.out.println("=== Contenu actuel de " + file + " ===");
        System.out.println("--- Entrez votre texte (tapez 'EOF' sur une nouvelle ligne pour enregistrer) ---");
        catCommande(file);
        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        while (true) {
            String ligne = scanner.nextLine();
            if(ligne.contains("EOF"))break ;
            sb.append(ligne).append(System.lineSeparator());
        }
        try {
            Files.write(folderFiles, sb.toString().getBytes(StandardCharsets.UTF_8),StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chmodCommande(String file, String newPerm){
        Fichier fichier = fileMap.get(file);

        if (fichier == null) {
            System.out.println("Fichier introuvable : " + file);
            return;
        }

        String ancientPerm = fichier.getPersmission();

        if (newPerm.startsWith("-")) {
            if(newPerm.contains("w")) fichier.setAutherWrite(false);
            if(newPerm.contains("r")) fichier.setAutherRead(false);
            if(newPerm.contains("d")) fichier.setAutherDelete(false);
        }else{
            if(newPerm.contains("w")) fichier.setAutherWrite(true);
            if(newPerm.contains("r")) fichier.setAutherRead(true);
            if(newPerm.contains("d")) fichier.setAutherDelete(true);
        }
        
        System.out.println(file + " : " + ancientPerm + "  ->  " + fichier.getPersmission());
        try {
            List<String> lignes = new ArrayList<>();
            for(Fichier f:fileMap.values()){
                String ligne = f.getName() + "," +
                           f.getProprietaire() + "," +
                           f.isOwnerRead() + "," +
                           f.isOwnerWrite() + "," +
                           f.isOwnerDelete() + "," +
                           f.isAutherRead() + "," +
                           f.isAutherWrite() + "," +
                           f.isAutherDelete();
                lignes.add(ligne);
            }
            Files.write(dataFile, lignes);
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise a jour de Files.txt : " +e.getStackTrace());
        }
    }
}