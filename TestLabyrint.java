import java.io.*;

class TestLabyrint{

  public static void main(String[] args){
    String filnavn = args[0];
    boolean detaljer = (args.length > 1 && args[1].equals("detaljer"));
    Labyrint labyrint;

    try{
      labyrint = Labyrint.lesFraFil(new File(filnavn));
      System.out.println("\n\n" + labyrint.toString());
      Liste<String> utveier = labyrint.finnUtveiFra(0, 1);
      System.out.println("\nAntall utveier: " + utveier.stoerrelse());
      for(String utvei : utveier){ System.out.println(utvei); }
    }
    catch(FileNotFoundException e){
      System.out.println("Fant ikke fil!!");
    }


  }



}
