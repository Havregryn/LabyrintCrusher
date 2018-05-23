import java.util.ArrayList;

class Aapning extends HvitRute{
  public Aapning(int kolonne, int rad){
    super(kolonne, rad);
  }

  @Override
  public char tilTegn(){
    return '.';
  }

  @Override
  protected void grafGaa(GrafKant komFraGK, String veien){
    veien += this.toString();
    //System.out.println("Er i Aapning!");
    labyrint.leggTilUtvei(veien);
  }
}
