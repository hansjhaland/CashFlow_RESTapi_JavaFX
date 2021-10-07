# Intro
Dette er en beskrivelse av hvordan appen skal se ut til Gruppeinnlevering 2. 
Hovedfunksjonaliteten som legges til i denne releasen er:
- Kontotype: Brukeren skal kunne opprette forskjellige typer kontoer
- Kontodetaljer: Overføringshistorikken til en valgt konto
- Overføring: Mlighet til å overføre mellom kontoer
For å få til dette trengs det en ekstra side for detaljer og overføringer, og tilpassinger på forsiden
---

# Designdokumentasjon
## Forside
![Bilde av forside](img/forside.png "Forside")
1. Inntastingsfelt for å skrive inn navn på kontoen.
2. Inntastingsfelt for å skrive inn startbeløp som skal settes inn ved opprettelse av kontoen. Det på forhånd fylt ut med “0.0”.
3. Knapp for å opprette kontoen. Kontoinformasjonen lagres og vises i oversiktsfeltet (6).
4. Tekstfelt for å gi brukeren feedback.
5. Tekstfelt for å gi brukeren feilmeldinger.
6. Oversiktsfelt som viser kontonavn, kontonummer og disponibelt beløp. Feltet har scrollefunksjon. Senere skal en bruker kunne trykke på en konto for å få mer detaljert informasjon, som for eksempel kontohistorikk. 
7. Nedtreksmeny for valg av kontotype til konto som skal opprettes
8. Knapp for å komme til siden for kontodetaljer og overføring

## Detaljer og overføringer
![Bilde av detaljer- og overføringsside](img/detaljerOverforinger.png "Detaljer og overføringer")

9. Nedtreksmeny for valg av hvilken konto sine kontodetaljer man vil se
10. Oversikt over kontohistorikken til valgt konto
11. Overføringer skjer fra kontoen som er valgt i 9. og til kontoen som velges i denne nedtreksmenyen. Beløpet skrives inn i inntastingsfeltet. Overføringen gjennomføres ved å trykke på knappen. Dette vil legge til et "uttak" i kontohistorikken til "fra-kontoen" og et "innsetting" i kontohistorikken til "til-kontoen" 
