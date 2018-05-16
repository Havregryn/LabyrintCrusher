/**
  En Rute instans representerer et felt i labyrinten.
*/
abstract class Rute{

  private int kolonne, rad;
  private Labyrint labyrint;
  private Rute nord = null;
  private Rute ost = null;
  private Rute sor = null;
  private Rute vest = null;

  public Rute(int kolonne, int rad){
    this.kolonne = kolonne;
    this.rad = rad;
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

  public int hentKol(){ return kolonne; }

  public int hentRad(){ return rad; }

  public abstract char tilTegn();

  public String hentDetaljer(){
    String s = "Rute (" + kolonne + ", " + rad + ")";
    return s;
  }

  @Override
  public String toString(){
    StringBuilder sb = new StringBuilder(10);
    sb.append("(");
    sb.append(kolonne);
    sb.append(", ");
    sb.append(rad);
    sb.append(")");
    return sb.toString();
  }
}
