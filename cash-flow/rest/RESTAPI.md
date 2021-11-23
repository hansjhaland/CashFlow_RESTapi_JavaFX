# Intro

Kort beskrivelse av RESTAPI-et. Merk at til forskjell fra Jackson-serialisering/deserialisering så er det ikke laget et eget nivå for transaksjoner i RESTAPI-et. RESTAPI-et bruker heller ikke POST-forespørsler.

**Merk:** Prosjektet benytter seg av en lagringsfil som sørger for at applikasjonen har en standardbruker hver gang serveren startes på nytt. Altså vil applikasjonen resettes hver gang man stopper og starter serveren på nytt.

# Forespørsler

## GET

### getUser()
Returnerer en User med en HTTP-GET forespørsler. Applikasjonen opererer bare med én User og vil derfor alltid returnere denne. 

**End point URI:** `http://localhost:8999/user/`

### getAccount()
Returnerer en Account med et gitt kontonummer **accountNumber** med en HTTP-GET forespørsler.

**End point URI:** `http://localhost:8999/user/{accountNumber}`


## PUT

### putAccount()
Sender en HTTP-PUT forespørsel og legger til eller oppdaterer en gitt account. 


## DELETE

### deleteAccount()
Sender en HTTP-DELETE forespørsel og sletter en gitt account.

