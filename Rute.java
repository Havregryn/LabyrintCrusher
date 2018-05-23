/**
  En Rute instans representerer et felt i labyrinten.
*/
import java.util.ArrayList;

abstract class Rute{

  private int kolonne, rad;
  private String toStringString;
  protected Labyrint labyrint;
  private Rute nord = null;
  private Rute ost = null;
  private Rute sor = null;
  private Rute vest = null;

  private ArrayList<GrafKant> grafKanter;

  public Rute(int kolonne, int rad){
    this.kolonne = kolonne;
    this.rad = rad;
    grafKanter = new ArrayList<GrafKant>(4);
    StringBuilder sb = new StringBuilder(10);
    sb.append("(");
    sb.append(kolonne);
    sb.append(", ");
    sb.append(rad);
    sb.append(")");
    toStringString = sb.toString();
    }

  public void settLabyrint(Labyrint labyrint){
    this.labyrint = labyrint;
  }

  public void settNaboer(Rute nord, Rute ost, Rute sor, Rute vest){
    this.nord = nord;
    this.ost = ost;
    this.sor = sor;
    this. vest = vest;
  }

  public Rute hentSorNabo(){ return sor; }


  public void leggTilGrafKant(GrafKant gk){
    if(!grafKanter.contains(gk)){  grafKanter.add(gk); }
  }

  public ArrayList<GrafKant> hentGrafkanter(){ return grafKanter; }

  public int hentAntGrafKanter(){ return grafKanter.size(); }

  public void finnUtvei(){
    gaa(null, "");
  }


  private void gaa(Rute forrigeRute, String veiStreng){
    labyrint.leggTilGaaAntall();
    String ruteStreng = "(" + kolonne + ", " + rad + ")";
    boolean vaertHerFoer = veiStreng.contains(ruteStreng);

    veiStreng += ruteStreng;
    if(labyrint.hentDetaljer()){
      String rapport = "\nGaa er i rute: " + ruteStreng + " med tegn: " + tilTegn();

      if(nord != null){ rapport += "   Nordnabo: " + nord.tilTegn(); }
      else{ rapport += "   Nordnabo: 0"; }
      if(ost != null){ rapport += "   Østnabo: " + ost.tilTegn(); }
      else{ rapport += "   Østnabo: 0"; }
      if(sor != null){ rapport += "   Sørnabo: " + sor.tilTegn(); }
      else{ rapport += "   Sørnabo: 0"; }
      if(vest != null){ rapport += "   Vestnabo: " + vest.tilTegn(); }
      else{ rapport += "   Vestnabo: 0"; }
      if(vaertHerFoer){ rapport +="  OBS! SYKLISK!"; }
      System.out.print(rapport);
    }
    // Avslutningssjekk: Dersom felt er Sort, eller dersom feltet er i ytterkant
    //  så registreres ruten, deretter avsluttes rekursjonen:
    if(this.tilTegn() != '#'){
      if(nord == null || ost == null || sor == null || vest == null){
        labyrint.leggTilUtvei(veiStreng);
        if(labyrint.hentDetaljer()){ System.out.print(" AAPNING FUNNET OG REGISTRERT!"); }
      }
      if(!vaertHerFoer){
        veiStreng += "-->";
        // Rekusjerer til nabofelt dersom det finnes og dersom dette ikke er der vi kom ifra:
        if(nord != null && nord != forrigeRute){ nord.gaa(this, veiStreng); }
        if(ost != null && ost != forrigeRute){ ost.gaa(this, veiStreng); }
        if(sor != null && sor != forrigeRute){ sor.gaa(this, veiStreng); }
        if(vest != null && vest != forrigeRute){ vest.gaa(this, veiStreng); }
      }
    }

  }

  /**
    Metode som finner mulige utveier fra denne ruten og legger til i utveier i
    labyrint-instansen:
  */
  public void grafFinnUtvei(){
    // Bygger opp string til nærmeste node dersom this IKKE er en node:
    if(grafKanter.size() == 1){
      // Bygger vei-streng til nærmeste node den ene veien og kaller grafGaa:
      GrafKant gk = grafKanter.get(0);
      String veien1 = gk.toString(this, true);
      Rute node1 = gk.hentNode(1);
      node1.grafGaa(gk, veien1);
      // Bygger vei-streng til nærmeste node den andre veien og kaller grafGaa:
      String veien2 = gk.toString(this, false);
      Rute node2 = gk.hentNode(0);
      node2.grafGaa(gk, veien2);
    }
    else{
      // Rute klikket på er en node, gå rett til grafGaa!
        grafGaa(null, "");
    }
  }

  protected void grafGaa(GrafKant komFraGK, String veien){
    //vis("Gaa på node " + this.toString());
    int testAntall = 0;
    //Avslutning pga Åpning fanges opp i subklassen Aapning
    if(!veien.contains(this.toString())){
      for(GrafKant gk : grafKanter){
        String veiKopi = veien + "";
        if(gk != komFraGK){
          Rute nesteNode = gk.hentNodeMenIkke(this);
          veiKopi += gk.toString(nesteNode);
          nesteNode.grafGaa(gk, veiKopi);
          testAntall++;
        }
      }
    }
  }

  public int hentKol(){ return kolonne; }

  public int hentRad(){ return rad; }

  public abstract char tilTegn();

  public String hentDetaljer(){
    String s = "Rute (" + kolonne + ", " + rad + ")";
    return s;
  }

  @Override
  public String toString(){ return toStringString; }

  private void vis(String s){ System.out.println(s); }
}
