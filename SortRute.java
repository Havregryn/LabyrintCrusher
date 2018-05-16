class SortRute extends Rute{
  public SortRute(int kolonne, int rad){
    super(kolonne, rad);
  }

  @Override
  public char tilTegn(){
    return '#';
  }
}
