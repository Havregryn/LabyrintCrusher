class UtveiSortertListe<T> extends Lenkeliste<T>{

  public void leggTilUtvei(T utvei){
    if(stoerrelse() == 0){ leggTil(utvei); return; }
    String utveiStr = utvei.toString();
    int nyUtveiLengde = 0, gmlUtveiLengde = 0;
    for(char c : utveiStr.toCharArray()){ if(c == '('){ nyUtveiLengde++; }}
    for(int i = 0; i < stoerrelse(); i++){
      String gmlUtveiStr = hent(i).toString();
      gmlUtveiLengde = 0;
      for(char c : gmlUtveiStr.toCharArray()){ if(c == '('){ gmlUtveiLengde++; }}
      if(nyUtveiLengde < gmlUtveiLengde){ leggTil(i, utvei); return; }
    }
    leggTil(utvei);
  }
}
