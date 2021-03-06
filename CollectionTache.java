package todoList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;


public class CollectionTache
{
  private ArrayList<Tache> codex;
  private ArrayList<Tache> archive;
  private ArrayList<String> categories;

  public CollectionTache(){
    codex=new ArrayList<Tache>();
    start_taches();

    archive=new ArrayList<Tache>();
    start_archives();

    categories=new ArrayList<String>();
    start_categories();
  }

  //Rempli codex de nos tâches sérializées
  public void start_taches(){
    String filePath = "./todoList/taches";
    File file = new File(filePath);

    if(file.exists()){
      System.out.println("Initialisation de nos tâches...");
      try{
        Scanner scanner=new Scanner(new File(filePath));
        while(scanner.hasNextLine()){
          String title = scanner.nextLine();
          String line = scanner.nextLine();
          StringTokenizer st = new StringTokenizer(line);

          String dd=st.nextToken();
          String df=st.nextToken();
          String cat=st.nextToken();

          SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
          try{
            if(st.hasMoreTokens()){
              String avance=st.nextToken();
              codex.add(new TacheLongCours(formatter.parse(dd), formatter.parse(df), title, cat, Integer.parseInt(avance)));
            }
            else{
              codex.add(new TachePonctuelle(formatter.parse(dd), formatter.parse(df), title, cat));
            }
          }catch(ParseException err){
            err.printStackTrace();
          }
        }
        scanner.close();
      }catch(FileNotFoundException fnf){
        fnf.printStackTrace();
      }
    }
  }

  public void start_categories(){
    int size=codex.size();
    for(int i=0;i<size;++i){
        ajouter(getCategorie(i));
    }
  }

  public void close_Tache(){
    File file = new File ("./todoList/taches");
    if(file.exists()){
      file.delete();
    }
    try{
      if(file.createNewFile()){
        FileWriter writer = new FileWriter(file);
        int size=codex.size();
        for(int i=0; i<size; ++i){
          String s=codex.get(i).toWrite();
          writer.write(s);
          writer.flush();
        }
        writer.close();
      }
      else{
        System.out.println ("Création du fichier echouée");
      }
    }catch(IOException e){
      System.out.println ("Erreur " + e.getMessage());
    }
  }

  public void close_Archive()
  {
    File file = new File ("./todoList/taches_archivees");
    if(file.exists()){
      file.delete();
      try{
        if(file.createNewFile()){
          try{
            FileWriter writer = new FileWriter(file);
            int size=archive.size();
            for(int i=0; i<size; ++i){
              String s=archive.get(i).toWrite();
              writer.write(s);
              writer.flush();
            }
            writer.close();
          } catch (IOException e){
            System.out.println ("Erreur " + e.getMessage());
          }
        }
      }catch(IOException e){
        System.out.println ("Erreur " + e.getMessage());
      }
    }
  }

  public int nbTaches(){
	   return codex.size();
  }
  public Tache getTache(int i){
	   return codex.get(i);
  }
  public String getTitre(int i){
	   return codex.get(i).get_titre();
  }
  public String getCategorie(int i){
	   return codex.get(i).get_categorie();
  }
  public Date getDebut(int i){
	   return codex.get(i).get_debut();
  }
  public Date getEcheance(int i){
	   return codex.get(i).get_echeance();
  }
  public int getAvancement(int i){
    return codex.get(i).get_avancement();
  }

  public void ajout(Tache t){
    if(!codex.contains(t)){
      codex.add(t);
      ajouter(t.get_categorie());
    }
  }
  public void ajout(ArrayList<Tache> t){
    for(int i=0;i<t.size();++i){
        ajout(t.get(i));
    }
  }
  public void retrait(int n){
    int nb = codex.size();
    if(n<=nb){
      codex.remove(codex.get(n));
    }
  }

  public void ajouter(String s){
    if(!categories.contains(s))
      categories.add(s);
  }
  public void modifier(String old_cat, String s)
  {
    if(categories.contains(old_cat)){
      int size=codex.size();
      for(int i=0; i<size; ++i){
        if(getCategorie(i).equals(old_cat)){
          codex.get(i).set_categorie(s);
        }
      }
      categories.remove(old_cat);
      categories.add(s);
    }
  }

  public int nbArchive(){
     return archive.size();
  }
  public Tache getArchive(int i){
     return archive.get(i);
  }
  public void archiver_Tache(int i){
    if(!archive.contains(getTache(i))){
      archive.add(getTache(i));
      retrait(i);
    }
  }
  public boolean isLineaire(int i){
    return codex.get(i).isLineaire();
  }


  //rempli archive des tâches échues non archivées du codex
  public void start_archives(){
    String filePath = "./todoList/taches_archivees";
    File file = new File(filePath);
    if(file.exists()){
      System.out.println("Initialisation de nos tâches archivées...");
      try{
        Scanner scanner=new Scanner(new File(filePath));
        while(scanner.hasNextLine()){
          String title = scanner.nextLine();
          String line = scanner.nextLine();
          StringTokenizer st = new StringTokenizer(line);

          String dd=st.nextToken();
          String df=st.nextToken();
          String cat=st.nextToken();

          SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
          try{
            if(st.hasMoreTokens()){
              String avance=st.nextToken();
              archive.add(new TacheLongCours(formatter.parse(dd), formatter.parse(df), title, cat, Integer.parseInt(avance)));
            }
            else{
              archive.add(new TachePonctuelle(formatter.parse(dd), formatter.parse(df), title, cat));
            }
          }catch(ParseException err){
            err.printStackTrace();
          }
        }
        scanner.close();
      }catch(FileNotFoundException fnf){
        fnf.printStackTrace();
      }
    }
  }
  /*
  public void afficheCodex()
  {
    int size=codex.size();
    System.out.println(size+" tâche(s) restante(s) :");
    int i=0;
    while(i<size){
      System.out.println(codex.get(i).toString());
      ++i;
    }
  }
  public void afficheCategories()
  {
    String sum = "Les différentes catégories sont : [";
    for(String s : categories) {
    	    sum+=s+", ";
    }
    sum+="]";
    System.out.println(sum);
  }
  */

  public void tri_echeance(){
    System.out.println("On trie...");
    Collections.sort(codex, new DateComparator());
  }
}


class DateComparator implements Comparator<Tache> {
  public int compare(Tache t1, Tache t2) {
	   Date t1d = t1.get_echeance();
     Date t2d = t2.get_echeance();
     if (t1d.after(t2d)) {
       return 1;
     } else if (t1d.before(t2d)) {
       return -1;
     } else {
       return 0;
     }
   }
 }
