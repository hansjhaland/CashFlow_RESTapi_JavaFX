[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2021/gr2151/gr2151/-/tree/master/cash-flow)

# Group gr2151 repository 
Kodelageret inneholder mappene **cash-flow** med selve kodingsprosjektet og **docs** med dokumentasjon tilknyttet de forskjellige gruppeinnleveringene. 

## cash-flow-prosjektet

Domenelogikk og json-hådtering ligger i **core-modulen**, i henholdsvis core- og json-mappene. 

I **core** ligger javaklassene 
- **AbstractAccount**, **CheckingAccount**, **SavingsAccount**, **BSUAccount** for logikk som omhandler kontoer
- **User** for logikk som omhandler brukeren
- **Transaction** for kode som omhandler transaksjoner

I **json** ligger javaklassene
- **AccountSerializer** som tar seg av serialisering av forskjellige typer konto-objekter
- **UserSerializer** som tar seg av serialisering av bruker-objekter
- **TransactionSerializer** som tar seg av serialisering av transaksjon-objekter
- **AccountDeserializer** om tar seg av deserialisering av forskjellige typer konto-objekter
- **UserDeserializer** som tar seg av deserialisering av bruker-objekter
- **TransactionDeserializer** som tar seg av serialisering av transaksjon-objekter
- **CashFlowModule** som håndterer serialisering- og deserialiseringsklassene
- **CashFlowPersistence** håndterre fillagring og fillesing


Klasser og filer som omhandler GUI ligger i **ui-modulen**
- **CashFlowApp** som kjører applikasjonen
- **CashFlow.FXML** som spesifiserer hvordan forsiden til applikasjonen skal se ut. Ligger i **resources-mappen**
- **CashFlowController** som forbinder brukergrensesnittet med domenelogikken og persistens
- **Details.FXML** som spesifiserer hvordan neste side til applikasjonen skal se ut. Ligger også i **resources-mappen**
- **DetailsController** neste side som forbinder brukergrensesnittet med domenelogikken og persistens


**MÅ OPPDATERES**

## Testing

- Testklasser ligger i tilhørende modul sin **test-mappe**.
- Vi har lagt til rapportering av testdekkningsgrad med **Jacoco**.
- For å gjennomføre tester og få rapporten må man kjøre kommandoen **mvn test jacoco:report**
- Etter dette vil rapporten ligge i hver modul sin **target/site/jacoco/index.html**
- Tester kan også kjøres uten jacoco med **mvn test**


# Hvordan kjøre appen
 - Først må man gå inn i rot-mappen til prosjektet (.../gr2151/cash-flow).
 - Deretter må man kjøre kommandoen **mvn install**
 - Kjøre kommandoen **cd ui** for å komme inn i ui-mappen som skal kjøres (.../gr2151/cash-flow/ui).
 - Kjøre kommandoen **mvn javafx:run**
