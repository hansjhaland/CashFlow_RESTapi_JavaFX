[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2021/gr2151/gr2151/-/tree/master/cash-flow)

# Group gr2151 repository 

# cash-flow-prosjektet

Prosjektet ligger i mappen "cash-flow". 

Vi har domenelogikk og json-hådtering plassert i core-modulen, i henholdsvis core- og json-mappene. 
I core ligger javaklassene AbstractAccount, CheckingAccount, SavingsAccount, BSUAccount og User. SavingsAccount og BSUAccount skal - implementeres i fremtiden.
I json har vi serialiserings- og deserialiseringsklasser for CheckingAccount og User, samt en wrapper-klasse for disse og CashFlowPersistence for å håndtere persistens.

Klasser og filer som omhandler GUI ligger i ui-modulen.
App-klassen og kontrollerklassen finnes under ui-mappen.
FXML-filen finnes under resources-mappen.

# Testing

Til Gruppeinnlevering 1 har vi bare skrevet ferdig tester for json-klassene. Disse testklassene ligger i core-modulens test-mappe under json-mappen.
Grunnen til at vi enda ikke har testet mer er at vi tolket oppgaven slik at vi bare trengte en test for å kjøre "mvn test". 
Vi skal implementere tester for resten av klassene til neste release.

# Hvordan kjøre appen
 - Først må man gå inn i rot-mappen til prosjektet (.../gr2151/cash-flow).
 - Deretter må man kjøre mvn install.
 - Kjøre kommandoen "cd ui" for å komme inn i ui-mappen som skal kjøres (.../gr2151/cash-flow/ui).
 - Kjøre kommandoen "mvn javafx:run".
