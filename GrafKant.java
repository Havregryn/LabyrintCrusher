import java.util.ArrayList;

public class GrafKant{
  private Rute fraNode, tilNode;
  private ArrayList<Rute> ruterPaaDenneVeien; //inkl fra/til
  private Rute[] noder;
  private int antallNoder;
  private boolean erSortert = false; // til test av sortering


  public GrafKant(){
    ruterPaaDenneVeien = new ArrayList<Rute>(30);
    noder = new Rute[2];
    antallNoder = 0;
  }

  public void leggTilRute(Rute r){
    ruterPaaDenneVeien.add(r);
  }
  public ArrayList<Rute> hentRuter(){ return ruterPaaDenneVeien; }

  public void leggTilNode(Rute r){
    noder[antallNoder++] = r;
    if(antallNoder == 2){
      erSortert = true;
      ruterPaaDenneVeien = sorterOgMerkRuter(); }
  }

  public boolean sortert(){return erSortert; }

  public Rute hentNode(int n){ return noder[n]; }

  public Rute hentNodeMenIkke(Rute r){
    if(noder[0] != r){ return noder[0]; } else{ return noder[1]; }
  }

  public int hentAntallNoder(){ return antallNoder; }

  public boolean erKomplett(){ return (antallNoder == 2); }

  public int hentAntallRuter(){ return ruterPaaDenneVeien.size(); }

  private ArrayList<Rute> sorterOgMerkRuter(){
    for(Rute r : ruterPaaDenneVeien){
    }
    // Legger rutene i rekkefølge:
    ArrayList<Rute> ruterSortert = new ArrayList<Rute>(ruterPaaDenneVeien.size());
    Rute her = noder[0];
    ruterSortert.add(her);
    //System.out.println("Sortert legger inn: " + her.toString());
    ruterPaaDenneVeien.remove(noder[0]);
    boolean funnet;
    int i;
    while(ruterPaaDenneVeien.size() > 0){
      // finn rute som er nabo,dvs (kol = kol, abs(rad - rad) = 1) ELLER
      //  (rad = rad OG abs(kol - kol) = 1)
      funnet = false;
      i = 0;
      while(!funnet){
        Rute r = ruterPaaDenneVeien.get(i);
        if((r.hentKol() == her.hentKol() && Math.abs(r.hentRad() - her.hentRad()) == 1) ||
           (r.hentRad() == her.hentRad() && Math.abs(r.hentKol() - her.hentKol()) == 1)){
          // Har funnet neste rute:
          her = ruterPaaDenneVeien.get(i);
          //System.out.println("Sortert legger inn: " + her.toString());
          ruterSortert.add(ruterPaaDenneVeien.remove(i--));
          funnet = true;
        }
        i++;
      }
    }
    for(Rute r : ruterSortert){ r.leggTilGrafKant(this); }
    return ruterSortert;
  }

  @Override
  public String toString(){
    StringBuilder sb = new StringBuilder(100);
    //sb.append("!");
    for(int i = 0; i < ruterPaaDenneVeien.size(); i++){
      Rute r = ruterPaaDenneVeien.get(i);
      sb.append(r.toString());
      if(i < (ruterPaaDenneVeien.size() - 1)){ sb.append("-->"); }
    }
    //sb.append("!");
    return sb.toString();
  }

  public String toString(Rute fra, boolean retning){
    //Returnerer steng med ruter i listen, UNNTATT SISTE RUTE!
    StringBuilder sb = new StringBuilder(100);
    int fraIndx = ruterPaaDenneVeien.indexOf(fra);
    if(retning){
      for(int i = fraIndx; i < ruterPaaDenneVeien.size() - 1; i++){
        sb.append(ruterPaaDenneVeien.get(i));
        sb.append("-->");
      }
    }
    else{
      for(int i = fraIndx; i > 0; i--){
        sb.append(ruterPaaDenneVeien.get(i));
        sb.append("-->");
      }
    }
    return sb.toString();
  }

  public String toString(Rute til){
    StringBuilder sb = new StringBuilder(100);
    if(til == noder[1]){
      for(int i = 0; i < ruterPaaDenneVeien.size() - 1; i++){
        sb.append(ruterPaaDenneVeien.get(i));
        sb.append("-->");
      }
    }
    else{
      for(int i = ruterPaaDenneVeien.size() - 1; i > 0; i--){
        sb.append(ruterPaaDenneVeien.get(i));
        sb.append("-->");
      }
    }
    return sb.toString();
  }

  public String hentDetaljer(){
    String s = "Grafkant med " + antallNoder + " noder :";
    for(int i = 0; i < antallNoder; i++){
      s += (noder[i].hentDetaljer() + " ");
    }
    s += (" Antall ruter: " + ruterPaaDenneVeien.size());
    return s;
  }

}
