package ma.youcode.lineperm.service;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
// import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
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
            System.out.printf("%-12s %-18s %s%n",f.Persmission(),f.getName(),f.getProprietaire());
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

    public boolean hasAcces(String file, String perm ,String user){
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
        Path foldesFiles = Path.of("src/main/java/ma/youcode/lineperm/files/"+file);
        try {
            List<String> lines = Files.readAllLines(foldesFiles);
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
        Path foldesFiles = Path.of("src/main/java/ma/youcode/lineperm/files/"+file);
        System.out.println("=== Contenu actuel de " + file + " ===");
        System.out.println("--- Entrez votre texte (tapez 'EOF' sur une nouvelle ligne pour enregistrer) ---");
        catCommande(file);
        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        while (true) {
            String ligne = scanner.nextLine();
            if(ligne.contains("EOF"))break ;
            sb.append(ligne);
            sb.append(System.lineSeparator());
        }
    }
}