IN1010 2018, LABYRINT KONKURRANSE


Skrevet av Hallgrim Bratberg (hallgrib)


Hei!

Jeg har laget en variant av oblig 5 for å kjappest mulig finne utveiene fra
labyrintene. Etter innlesing av fil lages det en modell av den underliggende
grafen ved en enkel gjennomgang av labyrinten uten rekursjon. For å finne en utvei
gjøres det rekursivt søk i denne grafen, dermed blir dette en mye enklere og kjappere prosess.

Hver grafkant(edge) har en liste med tilknyttede ruter som brukes for å generere
utvei-streng.

Utveien-strengene legges i UtveiSortertListe, en subklasse av Lenkeliste fra tidligere oblig.
De legges inn sortert etter lengde (antall ruter).

Main metoden ligger i LabyrintCrusher.
