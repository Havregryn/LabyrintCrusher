/**
  Instanser av klassen Labyrint representerer en en labyrint som
  består av en 2-dimensjonal matrise av sorte og hvite ruter.
*/
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.time.Instant;
import java.time.Duration;

class Labyrint{
  private boolean dt = true; // Visertest - detaljer;

  //TEST:
  private GrafKant testKant, testKant2, testkant3;

  private int antallKolonner, antallRader;
  private Rute[][] ruter;  // [kolonne][rad]
  private ArrayList<GrafKant> grafKantene;
  private UtveiSortertListe<String> utveier;
  private boolean detaljer;
  private int gaaAntall = 0; // Teller antall ganger gaa kalles på.
  private Instant foer, etter;
  private long intervall; // Måler tiden en prosess tar i ns.

  // privat konstruktør som kalles fra lesFraFil:
  private Labyrint(Rute[][] ruter, int antallKolonner, int antallRader){
    this.ruter = ruter;
    this.antallKolonner = antallKolonner;
    this.antallRader = antallRader;
    grafKantene = new ArrayList<GrafKant>(antallKolonner * antallRader / 5);
  }

  // Statisk fabrikk-metode som leser fra fil og kaller opp konstruktør:
  public static Labyrint lesFraFil(File fil) throws FileNotFoundException{
    Scanner inn = new Scanner(fil);
    // Leser inn antall rader/kolonner:
    int antallRader = inn.nextInt();
    int antallKolonner = inn.nextInt();
    // Oppretter Rute-array:
    Rute[][] ruter = new Rute[antallKolonner][antallRader];
    // Leser inn rutene:
    inn.nextLine();
    for(int r = 0; r < antallRader; r++){
      String linje = inn.nextLine();
      for(int k = 0; k < antallKolonner; k++){
        char ruteTegn = linje.charAt(k);
        // Legger inn hvit, sort eller aapning rute i array:
        if(ruteTegn =='.'){
          if(r == 0 || k == 0 || r == antallRader - 1 || k == antallKolonner - 1){
            ruter[k][r] = new Aapning(k, r);
          }
          else{ ruter[k][r] = new HvitRute(k, r); }
        }
        else{
          ruter[k][r] = new SortRute(k, r);
        }
      }
    }
    // Kaller opp konstruktør:
    Labyrint nyLabyrint = new Labyrint(ruter, antallKolonner, antallRader);
    // Setter labyrint-ref og nabo-ref´s i Rute instanser i array:
    nyLabyrint.settRuterLabyrintRef();
    nyLabyrint.settRuterNaboRef();
    return nyLabyrint;
  }

  // Metode som setter Labyrint - ref i alle Rute instanser i array:
  private void settRuterLabyrintRef(){
    for(int k = 0; k < antallKolonner; k++){
      for(int r = 0; r < antallRader; r++){
        ruter[k][r].settLabyrint(this);
      }
    }
  }

  // Metode som setter nabo-ref´s i alle Rute instanser i array:
  private void settRuterNaboRef(){
    for(int k = 0; k < antallKolonner; k++){
      for(int r = 0; r < antallRader; r++){
        Rute nordNabo = null;
        Rute ostNabo = null;
        Rute sorNabo = null;
        Rute vestNabo = null;
        if(r > 0){ nordNabo = ruter[k][r - 1]; }
        if(k < antallKolonner - 1){ ostNabo = ruter[k + 1][r]; }
        if(r < antallRader - 1){ sorNabo = ruter[k][r + 1]; }
        if(k > 0){ vestNabo = ruter[k - 1][r]; }
        ruter[k][r].settNaboer(nordNabo, ostNabo, sorNabo, vestNabo);
      }
    }
  }

  public void leggTilUtvei(String utvei){
    utveier.leggTilUtvei(utvei);
  }

  public void settDetaljer(boolean detaljer){ this.detaljer = detaljer; }

  public boolean hentDetaljer(){ return detaljer; }

  public void leggTilGaaAntall(){ gaaAntall++; }

  public int hentGaaAntall(){ return gaaAntall; }

  public double hentTid(){ return intervall; }



  @Override
  public String toString(){
    String utskrift = "";
    for(int r = 0; r < antallRader; r++){
      for(int k = 0; k < antallKolonner; k++){
        utskrift += ruter[k][r].tilTegn();
      }
      utskrift += "\n";
    }
    return utskrift;
  }

  // Oppretter ArrayList med alle kanter (edges) i graf.
  // Ruter i kryss og Åpninger blir noder.
  public void finnGrafKanter(){
    foer = Instant.now();
    GrafKant[] forrigeRadKanter = new GrafKant[antallKolonner];
    GrafKant denneKant = null;
    Rute venstre;
    Rute hoyre;
    Rute nesteRad;
    Rute forrigeRad;
    Rute denne;
    boolean venstreHvit = false, hoyreHvit = false, forrigeRHvit = false, nesteRHvit = false;

    for(int rad = 0; rad < antallRader; rad++){
      for(int kol = 0; kol < antallKolonner; kol++){
        denne = ruter[kol][rad];

        // Setter nabo-ruter:
        if(kol > 0){ venstre = ruter[kol - 1][rad]; }else{ venstre = null; }
        if(kol < (antallKolonner -1)){ hoyre = ruter[kol + 1][rad]; } else{ hoyre = null; }
        if(rad > 0){ forrigeRad = ruter[kol][rad - 1]; }else{ forrigeRad = null; }
        if(rad < (antallRader - 1)){ nesteRad = ruter[kol][rad + 1]; } else{ nesteRad = null; }

        if(denne instanceof HvitRute || denne instanceof Aapning){

          //Sjekker om ensom, vei, tre veis-kryss eller 4-veis kryss:
          int hvitNaboTeller = 0;
          venstreHvit = false;
          hoyreHvit = false;
          forrigeRHvit = false;
          nesteRHvit = false;
          if(venstre instanceof HvitRute || venstre instanceof Aapning){
            hvitNaboTeller++;
            venstreHvit = true; }
          if(hoyre instanceof HvitRute || hoyre instanceof Aapning){
            hvitNaboTeller++;
            hoyreHvit = true; }
          if(forrigeRad instanceof HvitRute || forrigeRad instanceof Aapning){
            hvitNaboTeller++;
            forrigeRHvit = true; }
          if(nesteRad instanceof HvitRute || nesteRad instanceof Aapning){
            hvitNaboTeller++;
            nesteRHvit = true; }

          if(hvitNaboTeller == 0){ // Ruten er en ensom øy:
            denneKant = new GrafKant();
            denneKant.leggTilRute(denne);
            denneKant.leggTilNode(denne);
            denneKant.leggTilNode(denne);
            grafKantene.add(denneKant);
            denneKant = null;
          }
          else if(hvitNaboTeller == 1){ // Ruten er en ende:
            if(venstreHvit){
              denneKant.leggTilRute(denne);
              denneKant.leggTilNode(denne);
              if(denneKant.erKomplett()){grafKantene.add(denneKant); }
              denneKant = null;
            }
            else if(hoyreHvit){
              denneKant = new GrafKant();
              denneKant.leggTilNode(denne);
              denneKant.leggTilRute(denne);
            }
            else if(forrigeRHvit){
              GrafKant frk = forrigeRadKanter[kol];
              frk.leggTilRute(denne);
              frk.leggTilNode(denne);
              if(frk.erKomplett()){ grafKantene.add(frk); }
              forrigeRadKanter[kol] = null;
            }
            else if(nesteRHvit){
              GrafKant gk = new GrafKant();
              gk.leggTilRute(denne);
              gk.leggTilNode(denne);
              forrigeRadKanter[kol] = gk;
            }


          }
          else if(hvitNaboTeller == 2){ // Ruten er en vei:
            if(venstreHvit && hoyreHvit){
              denneKant.leggTilRute(denne);
            }
            else if(forrigeRHvit && nesteRHvit){
              forrigeRadKanter[kol].leggTilRute(denne);
            }
            else if(venstreHvit && forrigeRHvit){
              denneKant.leggTilRute(denne);
              GrafKant mergetGK = mergeGrafKanter(forrigeRadKanter[kol], denneKant);
              ArrayList<Rute> ruter = mergetGK.hentRuter();
              for(Rute r: ruter){
                if((r.hentSorNabo() instanceof HvitRute || r.hentSorNabo() instanceof Aapning) &&
                    !mergetGK.erKomplett() && r != mergetGK.hentNode(0)){
                  if((r.hentKol() < kol && r.hentRad() == rad) ||
                     (r.hentKol() > kol && r.hentRad() == rad - 1)){
                       forrigeRadKanter[r.hentKol()] = mergetGK;
                     }
                }
              }
              if(mergetGK.erKomplett()){ grafKantene.add(mergetGK); }

              denneKant = null;
              forrigeRadKanter[kol] = null;
            }
            else if(venstreHvit && nesteRHvit){
              denneKant.leggTilRute(denne);
              forrigeRadKanter[kol] = denneKant;
              denneKant = null;
            }
            else if(hoyreHvit && forrigeRHvit){
              denneKant = forrigeRadKanter[kol];
              denneKant.leggTilRute(denne);
              forrigeRadKanter[kol] = null;
            }
            else if(hoyreHvit && nesteRHvit){
              denneKant = new GrafKant();
              denneKant.leggTilRute(denne);
              forrigeRadKanter[kol] = denneKant;
            }

          }
          else if(hvitNaboTeller == 3){ // Ruten er et 3-veis kryss:
            if(!forrigeRHvit){
              denneKant.leggTilRute(denne);
              denneKant.leggTilNode(denne);
              if(denneKant.erKomplett()){ grafKantene.add(denneKant); }

              denneKant = new GrafKant();
              denneKant.leggTilRute(denne);
              denneKant.leggTilNode(denne);

              forrigeRadKanter[kol] = new GrafKant();
              forrigeRadKanter[kol].leggTilRute(denne);
              forrigeRadKanter[kol].leggTilNode(denne);
            }
            else if(!nesteRHvit){
              denneKant.leggTilRute(denne);
              denneKant.leggTilNode(denne);
              if(denneKant.erKomplett()){ grafKantene.add(denneKant); }

              forrigeRadKanter[kol].leggTilRute(denne);
              forrigeRadKanter[kol].leggTilNode(denne);
              if(forrigeRadKanter[kol].erKomplett()){ grafKantene.add(forrigeRadKanter[kol]); }

              denneKant = new GrafKant();
              denneKant.leggTilRute(denne);
              denneKant.leggTilNode(denne);
            }
            else if(!hoyreHvit){
              denneKant.leggTilRute(denne);
              denneKant.leggTilNode(denne);
              if(denneKant.erKomplett()){ grafKantene.add(denneKant); }
              denneKant = null;

              forrigeRadKanter[kol].leggTilRute(denne);
              forrigeRadKanter[kol].leggTilNode(denne);
              if(forrigeRadKanter[kol].erKomplett()){ grafKantene.add(forrigeRadKanter[kol]); }

              forrigeRadKanter[kol] = new GrafKant();
              forrigeRadKanter[kol].leggTilRute(denne);
              forrigeRadKanter[kol].leggTilNode(denne);
            }
            else if(!venstreHvit){
              denneKant = new GrafKant();
              denneKant.leggTilRute(denne);
              denneKant.leggTilNode(denne);

              forrigeRadKanter[kol].leggTilRute(denne);
              forrigeRadKanter[kol].leggTilNode(denne);
              if(forrigeRadKanter[kol].erKomplett()){ grafKantene.add(forrigeRadKanter[kol]); }

              forrigeRadKanter[kol] = new GrafKant();
              forrigeRadKanter[kol].leggTilRute(denne);
              forrigeRadKanter[kol].leggTilNode(denne);
            }
          }
          else{ // Ruten er et 4-veis kryss:
            denneKant.leggTilRute(denne);
            denneKant.leggTilNode(denne);
            if(denneKant.erKomplett()){ grafKantene.add(denneKant); }

            denneKant = new GrafKant();
            denneKant.leggTilRute(denne);
            denneKant.leggTilNode(denne);

            forrigeRadKanter[kol].leggTilRute(denne);
            forrigeRadKanter[kol].leggTilNode(denne);
            if(forrigeRadKanter[kol].erKomplett()){ grafKantene.add(forrigeRadKanter[kol]); }

            forrigeRadKanter[kol] = new GrafKant();
            forrigeRadKanter[kol].leggTilRute(denne);
            forrigeRadKanter[kol].leggTilNode(denne);
          }
        } // SLUTT DENNE = HVIT
      } // for Kol
    } // for Rad

    etter = Instant.now();

    intervall = Duration.between(foer, etter).toNanos();
    vis("Det tok " + (intervall/1000000.0) + " ms å opprette grafen.");


  } // metode finnGrafKanter

  public GrafKant mergeGrafKanter(GrafKant vinner, GrafKant taper ){

    ArrayList<Rute> taperRuter= taper.hentRuter();
    for(Rute r : taperRuter){ vinner.leggTilRute(r); }
    if(taper.hentAntallNoder() == 1){
      vinner.leggTilNode(taper.hentNode(0));

    }
    //vis("Merget : " + vinner.toString());
    return vinner;
  }

  public Liste<String> finnUtveiFra(int kol, int rad)throws ArrayIndexOutOfBoundsException{
    foer = Instant.now();
    utveier = new UtveiSortertListe<String>();
    ruter[kol][rad].grafFinnUtvei();
    etter = Instant.now();
    intervall = Duration.between(foer, etter).toNanos();
    return utveier;
  }

  private void vis(String s){ System.out.println(s); }


}
