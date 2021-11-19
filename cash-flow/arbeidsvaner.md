# Arbeidsvaner

### Møter 
Vi har faste møter tirsdager 12.15 - 14.00 og torsdager 10.15 - 12.00. Disse møtene er obligatoriske for alle medlemene på gruppa og det må gis beskjed om fravær i god tid før. Vanligvis pleier gruppa å jobbe mer enn oppgitt for å levere det beste produktet.

### Parprogrammering
Vi bruker parprogrammering på møtene, og når vi skal gjøre ting som vi må ha en felles enighet om. For å fullføre alle arbeidsoppgavene før en innlevering fordeles arbeidet på programmeringsparene. Dersom et programmeringspar ikke mener det er hensiktsmessig å bruke parprogrammering på en del av arbeidsoppgavene, gjøres den delen av et enkeltmedlem for å spare tid. Det kan for eksempel være å legge til en liten funksjon eller fikse noe i koden. Som student er det i praksis umulig å få tid til å gjøre alle arbeidsoppgavene med parprogrammering, og fremdeles rekke alt som skal gjøres. 


# Arbeidsflyt

### Branching
Kodingsoppgaver utformes som Issues av gruppemedlemme i fellesskap. De fleste issues har en egen branch siden de gjør betydelige endringer på koden. Noen issuse kan gjøres direkte i master dersom det er små endringer og gruppemedlemmene er enige i at risikoen er lav for å ødelegge noe. Vi har erfart at branching kan føre til mindre en produktiv utviklingsprosess. Vi burde kanskje heller bruk trunk-based arbeidsflyt, særlig i starten av prosjektarbeidet. 

### Mergeing
Mergeing av brancher inn i master skjer i hovedsak under møtene. Det at mergingen skjer i fellesskap er kanskje dårlig reflektert i GitLab historikken da noen merges er gjennomført uten godkjenning fra andre medlemmer. Disse har i praksis blitt gjennomført i fellesskap og med samlet enighet rundt koden som skal merges. 
Hvis merging har skjedd utenom møtene har dette vært grunnet at noen andre trengte koden i sin branch. Branchene som ble merget på denne måten skulle kanskje ikke blitt opprettet i første omgang. 


# Kodekvalitet
### Plugins
Prosjektet bruker checkstyle og spotbugs for å øke kodekvalitet og jacoco for informasjon om testdekningsgrad.

### Parprogrammering
Gjennom møtene ser vi gjennom hverandres kode og kommer med innspill. Gruppemeldemene har ansvar for å holde seg oppdatert på prsojektkoden. 

### Testing
Klasser testes etter logikken er skrevet.

### Formatering av navn i prosjektet
Prosjektet er satt til at både koden og alle navn på filene skal være på engelsk. Formateringen av navn skrives slik at alle ordene er sammenhengende med stor forbokstav på hvert ord. Dette ble bestemt på første møte med gruppen.


# Git-konvensjoner
### Issues og Branches
Issues navngis utifra hvilken oppgave som skal løses. Utifra issues lager vi branches som får sitt eget nummer (etterfølgende tall fra forrige branch nummer), der navnet på den branchen er korrosponderende med issuen.
Hver brukerhistorie har fått sitt eget nummer og utifra dem har vi laget UserStory-label. På den måten dersom det er en issue som bygger seg på en brukerhistorie, vil den få samme Userstory-label som brukerhistorien sin.

I tillegg labler vi hver issue med en milestone der hver milestone er en innlevering.
Vi bruker også andre labler som hvilke type oppgave issuen er, som bug, design, feature, refactoring og test.
Hvordan vi skiller en issue fra hverandre når det kommer til hvilke som skal bli gjennomført først, er ved å lable dem med hvilken type priority de er, om de er 1 eller 2.

### Commit meldinger
Commit meldinger skrives på norsk og skal gi en kort sammendrag av det som har blitt implementert, løst eller fikset på i branchen. Skal være oversiktelig for de andre på gruppen og seg selv. Commitmeldingene skal være på norsk. Ble bestemt på første møte med gruppen.

### Merge request
Dersom man er ferdig med en branch og har fungerende testere, skal man lage en merge request til branchen. Når vi møtes går alle på gruppen over koden og trykker på "Approve"-knappen dersom de sier seg enig i løsningen. Dersom de ikke er fornøyde skal dette kommenteres i merge requesten og tas opp med resten av gruppen. Eventuelle endringer kan oppstå da.

### Issue Boards
Vi bruker issue boards til å ha en fin og ryddig oversikt av alle issues som er aktive og hvordan gruppen ligger an. På møtene går vi i tillegg over dette boardet og ser hvordan vi ligger an i forhold til innleveringen og hva som på oppdateres dersom det ikke er gjort.

### Code review
Tatt i bruk code review i Release 3, der vi i en merge request kommenterer kodekvaliten til prosjektet i den branchen, eventuelt om noe kunne ha vært bedre eller om hva som er bra. Før Release 3 ble code reviewen gjort muntlig på møtene grunnet selve mergingen av branchene også ble gjort sammen med alle på gruppen.
