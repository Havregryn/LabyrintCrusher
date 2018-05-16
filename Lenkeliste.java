/*
  IN1010 Oblig 3, skrevet av Hallgrim Bratberg (hallgrib)
*/
import java.util.Iterator;

class Lenkeliste<T> implements Liste<T>{

  private int antallNoder = 0;
  protected Node hode = null;
  protected Node hale = null;

  //Metode som oppretter en LenkelisteIterator instans:
  public LenkelisteIterator<T> iterator(){
    return new LenkelisteIterator<T>(this);
  }

  // Metode som returnerer antall noder i listen:
  @Override
  public int stoerrelse(){
    return antallNoder;
  }

  //Metode som setter inn node på angitt posisjon:
  @Override
  public void leggTil(int pos, T x){
    if(pos < 0 || pos > antallNoder){ throw new UgyldigListeIndeks(pos); }
    Node nodePaaIndeks = hode;
    Node forrigeNode = null;
    //Itererer gjennom listen til angitt posisjon:
    for(int i = 0; i < pos; i++){
      forrigeNode = nodePaaIndeks;
      nodePaaIndeks = nodePaaIndeks.neste;
    }
    // Setter inn ny node og endrer alle aktuelle referanser:
    Node nyNode = new Node(x);
    if(pos > 0){ forrigeNode.neste = nyNode; }
    else{ hode = nyNode; }
    if(pos == antallNoder){ hale = nyNode; }
    else{ nyNode.neste = nodePaaIndeks; }

    antallNoder++;
  }

  // Metode som legger et objekt/node til slutten (halen) av listen:
  @Override
  public void leggTil(T x){
    leggTil((antallNoder), x);
  }

  // Metode som setter inn på angitt posisjon og sletter noden som er der fra før:
  @Override
  public void sett(int pos, T x){
    if(antallNoder == 0){ throw new UgyldigListeIndeks(-1); }
    else if(pos < 0 || pos > (antallNoder - 1)){ throw new UgyldigListeIndeks(pos); }
    Node nodePaaIndeks = hode;
    Node forrigeNode = null;
    //Itererer gjennom listen til angitt posisjon:
    for(int i = 0; i < pos; i++){
      forrigeNode = nodePaaIndeks;
      nodePaaIndeks = nodePaaIndeks.neste;
    }
    // Setter inn ny node og endrer alle aktuelle referanser:
    Node nyNode = new Node(x);
    if(pos > 0){ forrigeNode.neste = nyNode; }
    else{ hode = nyNode; }
    if(pos == (antallNoder - 1)){ hale = nyNode; }
    else{ nyNode.neste = nodePaaIndeks.neste; }
  }

  @Override
  public T hent(int pos){
    // Henter ut objekt lagret i node på posisjon:
    if(pos < 0 || pos >= antallNoder){ throw new UgyldigListeIndeks(pos); }
    Node nodePaaIndeks = hode;
    for( int i = 0; i < pos; i++){
      nodePaaIndeks = nodePaaIndeks.neste;
    }
    return nodePaaIndeks.ting;
  }

  // Metode som returnerer objekt lagret i node på posisjon og deretter
  // fjerner denne noden:
  @Override
  public T fjern( int pos){
    if(antallNoder == 0){ throw new UgyldigListeIndeks(-1); }
    if(pos < 0 || pos >= antallNoder){ throw new UgyldigListeIndeks(pos); }
    Node nodePaaIndeks = hode;
    Node forrigeNode = null;
    // Itererer fram til gjeldende posisjon:
    for( int i = 0; i < pos; i++){
      forrigeNode = nodePaaIndeks;
      nodePaaIndeks = nodePaaIndeks.neste;
    }
    // Endrer aktuelle referanser slik at nodePaaIndeks blir "utestengt":
    if(pos > 0){ forrigeNode.neste = nodePaaIndeks.neste; }
    else{ hode = nodePaaIndeks.neste; }
    if(nodePaaIndeks == hale){ hale = forrigeNode; }

    antallNoder--;
    return nodePaaIndeks.ting;
  }

  // Metode som fjerner "hode - noden":
  @Override
  public T fjern(){
    return(fjern(0));
  }

 // Node klasse for å kunne opprette node-instanser:
  protected class Node{
    T ting;
    Node neste;

    Node(T t){
      ting = t;
      neste = null;
    }
  }


  public class LenkelisteIterator<T> implements Iterator<T>{
    private Lenkeliste<T> lenkeListe;
    private int indeks = 0;

    LenkelisteIterator( Lenkeliste<T> l){
      lenkeListe = l;
    }

    public boolean hasNext(){
      return indeks < lenkeListe.stoerrelse();
    }

    public T next(){
      return lenkeListe.hent(indeks++);
    }

  }


}
