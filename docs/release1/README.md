# Realease 1

For å komme inn til prosjektet går man inn i mappen "cash-flow". Vi har core-logikk plassert i mappen "core", der vi har opprettet klassene AbstractAccount.java, BsuAccount.java, CheckingAccount.java, SavingsAccount.java, og User.java. Flere av disse klassene er enda ikke ferdig implementert, og skal jobbes videre med neste iterasjon. For utseendet av appen ligger koden i "ui"-mappen fxml filen er ferdig og samsvarer med CashFlowController.java som også er ferdig implementert. Kode for testing finner man i src/test/java/json og heter CashFlowModuleTest.java.

# Hva som er gjort

Vi har i denne iterasjonen implementert domenelogikk i form av klassene AbstractAccount.java, BsuAccount.java, CheckingAccount.java, SavingsAccount.java, og User.java. AbstractAccount har foreløpig ingen abstrakte metoder, men det er viktig at denne er abstrakt slik at man ikke kan lage instanser av denne. BsuAccount, CheckingAccount og SavingsAccount arver fra AbstractAccount da de har mange like metoder. Hensikten med User-klassen er at et User-objekt skal kunne ha en liste med bankkontoer og en brukerID slik at vi kan holde kontroll på hvilke kontonummer som er tatt og gjøre det mulig å overføre mellom kontoer i fremtiden. Vi har implementert lagring for disse klassene ved bruk av jackson-biblioteket og json-filer. 

I det foreløpige brukergrensesnittet er det mulig å opprette kontoer som lagres til et autogenerert User-objekt. Man kan velge navn og saldo på kontoen ved opprettelse. Kontonummeret genereres automatisk og er et tall på 4 siffer. Når man oppretter en konto lagres disse kontoene automatisk i en json-fil som lagres på brukerens hoved-directory (user.home). Neste gang man åpner appen vil kontoene man har opprettet være der. FOreløpig er det en del logikk i controlleren som skal delegeres til domenelogikken, men dette fikses i neste iterasjon.

Til Gruppeinnlevering 1 har vi bare skrevet ferdig tester for json-klassene. Disse testklassene ligger i core-modulens test-mappe under json-mappen.
Grunnen til at vi enda ikke har testet mer er at vi tolket oppgaven slik at vi bare trengte en test for å kjøre "mvn test", altså om "mvn test" i det hele tatt fungerte. Vi skal implementere tester for resten av klassene til neste release.
