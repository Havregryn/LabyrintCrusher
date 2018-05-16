import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class LabyrintCrusher{
    public static void main(String[] args) {
        String filnavn = null;

        if (args.length > 0) {
            filnavn = args[0];
        } else {
        System.out.print("Oppgi filnavn: ");
        Scanner inn = new Scanner(System.in);
        filnavn = inn.next();
        }

        // 2.argument 'detaljer' gir detaljert utskrift av forløpet i programmet:
        boolean detaljer = (args.length > 1 && args[1].toLowerCase().equals("detaljer"));

        File fil = new File(filnavn);
        Labyrint l = null;
        try {
            l = Labyrint.lesFraFil(fil);
        } catch (FileNotFoundException e) {
            System.out.printf("FEIL: Kunne ikke lese fra '%s'\n", filnavn);
            System.exit(1);
        }

        l.settDetaljer(detaljer);
        if(detaljer){
          System.out.println("\n\nLABYRINT " + filnavn + ":\n\n" + l.toString() + "\n\n");
        }

        // Finner alle grafKanter i Labyrinten:
        l.finnGrafKanter();

        // les start-koordinater fra standard input
        Scanner inn = new Scanner(System.in);
        System.out.println("Skriv inn koordinater <kolonne> <rad> ('a' for aa avslutte)");
        String[] ord = inn.nextLine().split(" ");
        while (!ord[0].equals("a")) {

            try {
                int startKol = Integer.parseInt(ord[0]);
                int startRad = Integer.parseInt(ord[1]);

                Liste<String> utveier = l.finnUtveiFra(startKol, startRad);
                if(detaljer){ System.out.println("\n"); }
                int minsteLengde = 0;
                String korteste = "";
                if (utveier.stoerrelse() != 0) {
                    for (String s : utveier) {
                      System.out.println("\nUtvei med " + antallRuter(s) + " ruter:");
                        System.out.println(s);
                        // Finner korteste vei:
                        if(minsteLengde == 0 || s.length() < minsteLengde ){
                          minsteLengde = s.length();
                          korteste = s;
                        }
                    }
                    if(detaljer){ System.out.println("\nGaa metoden ble utført " + l.hentGaaAntall() + " ganger."); }
                    System.out.println("\nDet ble funnet " + utveier.stoerrelse() + " mulige utveier.\n" +
                                       "Korteste utvei med " + antallRuter(korteste) +" ruter er:\n" + korteste);
                } else {
                    System.out.println("Ingen utveier.");
                }
                System.out.println();
            } catch (NumberFormatException e) {
                System.out.println("Ugyldig input!");
            } catch (ArrayIndexOutOfBoundsException e){
              System.out.println("FEIL VERDI(ER)!");
            }

            System.out.println("Skriv inn nye koordinater ('a' for aa avslutte)");
            ord = inn.nextLine().split(" ");
        }
    }

    // Metode som beregner antall ruter i en utvei - streng:
    private static int antallRuter(String s){
      int teller = 0;
      for(char c : s.toCharArray()){ if(c == '('){ teller++; }}
      return teller;
    }
}
