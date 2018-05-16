import java.util.ArrayList;

public class GrafKant{
  private Rute fraNode, tilNode;
  private ArrayList<Rute> ruterPaaDenneVeien; //inkl fra/til
  private Rute[] noder;
  private int antallNoder;


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
    if(antallNoder == 2){ sorterRuter(); }
  }

  public Rute hentNode(int n){ return noder[n]; }

  public int hentAntallNoder(){ return antallNoder; }

  public boolean erKomplett(){ return (antallNoder == 2); }

  public String hentDetaljer(){
    String s = "Grafkant med " + antallNoder + " noder :";
    for(int i = 0; i < antallNoder; i++){
      s += (noder[i].hentDetaljer() + " ");

    }
    s += (" Antall ruter: " + ruterPaaDenneVeien.size());
    return s;
  }

  public int hentAntallRuter(){ return ruterPaaDenneVeien.size(); }

  private void sorterRuter(){
    // Legger rutene i rekkefølge:
    ArrayList<Rute> ruterSortert = new ArrayList<Rute>(ruterPaaDenneVeien.size());
    Rute her = noder[0];
    ruterSortert.add(her);
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
          ruterSortert.add(ruterPaaDenneVeien.remove(i));
          funnet = true;
        }
        i++;
      }
    }
    ruterPaaDenneVeien = ruterSortert;
  }

  @Override
  public String toString(){
    StringBuilder sb = new StringBuilder(100);
    for(int i = 0; i < ruterPaaDenneVeien.size(); i++){
      Rute r = ruterPaaDenneVeien.get(i);
      sb.append(r.toString());
      if(i < (ruterPaaDenneVeien.size() - 1)){ sb.append("-->"); }
    }
    return sb.toString();
  }

}
