/*
IN1010 Oblig 3, skrevet av Hallgrim Bratberg (hallgrib)
*/
interface Liste<T> extends Iterable<T>{
  public int stoerrelse();
  public void leggTil(int pos, T x); //setter inn uten å fjerne element, forlenger listen
  public void leggTil(T x);
  public void sett(int pos, T x); // setter inn og sletter evt eksisterende objekt på posisjon
  public T hent(int pos);
  public T fjern( int pos);
  public T fjern();
}
