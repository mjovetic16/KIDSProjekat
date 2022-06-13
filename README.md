# KIDSProjekat

### Parametri cvora
Cvor se u sistemu cuva kao [ServentInfo] objekat

Sadrzi IP adresu na kojoj se nalazi
Port na kome slusa
Listu komsijskih node-ova (U ovom slucaju to su svi ostali cvorovi u sistemu)
ID cvora

##### Parametri koje cvor cuva
U konfiguracionom fajlu:
-  navodi se lista poslova ([Job])  koji mogu da se startuju

U memoriji cvora:
- Cuva se aktivni posao ([ActiveJob])
- Thread koji izvrsava algoritam


### Ukljucivanje node-a u sistem
Sistem je kompletan graf node-ova

- Node kontaktira bootstrap node, (definisan u servent properties file-u, sadrzi ip i port na kome se nalazi), koristeci hail poruku sa svojim ip i port-om npr. [ Hail  192.0.6.0 1000]
- Bootstap node odgovara porukom koja sadrzi ip i port node-a koji je vec u sistemu
- Node zatim kontaktira zadati node [NEW NODE] porukom koja sadrzi [ServentInfo] objekat node-a koji se ukljucuje u sistem
- Kontaktirani node ukoliko prihvati zahtev zapisuje ServentInfo koji je dobio u listu komsija i odgovara [WELCOME] porukom koja sadrzi listu svih komsija u [NewNodeData] objektu
- Kada node koje se ukljucuje dobije odgovor zapisuje dobijene podatke i salje [UPDATE] poruku svim novim komsijama, update poruka sadrzi podatke o ukljucujucem node-u
- Svi node-ovi koje dobiju update poruku zapisuju novi node u listu komsija
- Ukljucivanje je tog trenutka zavrseno


### Komande node-a
**- status |X [id]|** - Prikazuje stanje svih započetih izračunavanja

**- start |X|** - Započinje izračunavanje za zadati posao X

**- result [X [id]]** - Prikazuje rezultate za završeno izračunavanje za posao X, Eksportuje PNG sliku rezultata

**-stop |X|** - Zaustavlja izračunavanje za posao X. .

**-quit** - Uredno gašenje čvora. Ispisivanje dela posla na kome je cvor radio


#### Status komanda
Status komanda pokrece slanje  [STATUS]  poruke svim komsijama u slucaju da nije naveden kontretan id, u suprotnom salje status poruku samo node-u sa zadatim id.

Kada stignu svi odgovori ispisuje se status (Ime posla, fractalni id node-a, broj tacaka) npr.
|Status|: triangle | 01 | 100

#### Start komanda
Salje [JOB_REQUEST] poruku svim komsijama
Ako stigne dovoljno poruka sa true odogovorom zapocinje se podela posla i salje se [JOB RESPONSE] , job response moze biti true ili false.
- False response dopusta node-u da prihvati neki drugi posao, koji stigne posle ili u toku postavljanja job requesta na primajuci node
- True response se salje nakon dodele posla, on salje [ActiveJob] objekat, i pri primanju node zapocinje zadati posao

Ako request dodje u toku postavljanja drugog posla, ceka se na odgovor prvog i na osnovu toga se daje odgovor

#### Result komanda
Node salje [RESULT] poruku koja sadrzi response tipa - [RESULT_REQUEST] svim komsijama,
result request respone sadrzi ime posla za koji se trazi rezultat.
Node koji prima result request poruku ukoliko ima aktivan job koji se trazi odgovara sa result porukom koja sadrzi response tipa [RESULT_RESPONSE] , result response sadrzi [Result] objekat sa listom svih tacaka i brojem iteracija algoritma, i ActiveJob objekat koji sadrzi listu svih nodova koji rade na zadatom poslu.
Node kada dobije response zapisuje tu listu i ceka odgovore od ostalih clanova te liste.
Kada dobije sve odgovore ispisuje rezultat u png sa imenom zadatog posla.
Ukoliko pri result request-u node nema taj aktivan posao, ne odgovora na poruku.

**Stop komanda**
Za zadati posao salje [STOP] poruku svim komsijama
Kada node primi stop poruku ukoliko ima aktivan zadati posao, gasi ga i resetuje sve potrebne parametre

#### Quit komanda
Zaustavlja node kada primi quit komandu, gasi sve aktivne procese i ispisuje deo fraktala na kome je radio u parts folder sa imenom posla i id-em servent-a (Za debug potrebe)


### Tipovi poruka

[[JOB_REQUEST]]:
- ServentInfo sender - Node koji salje poruku
- ServentInfo receiver - Node koji prima poruku
- ActiveJob activeJob - Svi podaci o poslu koji se startuje

[[JOB_RESPONSE]]:
- ServentInfo sender
- ServentInfo receiver
- Response response - Objekat koji sadrzi tip odgovora i aktivni posao
    - ResponseType JOB_RESPONSE / ResponseType ACCEPTED_OR_REJECTED_JOB_RESPONSE
    - data: ActiveJob

[[RESULT]]:
- ServentInfo sender
- ServentInfo receiver
- Response response
    - ResponseType RESULT_REQUEST / ResponseType RESULT RESPONSE
    - data: Result - Podaci o rezultatu izvrsavanja algoritma (broj iteracija, skup tacaka)

[[STATUS]]:
- ServentInfo sender
- ServentInfo receiver
- Response response
    - ResponseType STATUS_REQUEST / ResponseType STATUS RESPONSE
    - data : StatusData - Podaci o statusu posla (Broj iteracija, Ime posla, ID noda-a)

[[STOP]]:
- ServentInfo sender
- ServentInfo receiver
- Job job

[[NEW_NODE]]:
- ServentInfo sender
- ServentInfo receiver

[[WELCOME]]:
- ServentInfo sender
- ServentInfo receiver
- NewNodeData newNodeData - Podaci o novom node-u (ServentInfo i lista komsijskih node-ova)

[[UPDATE]]:
- ServentInfo sender
- ServentInfo receiver
- NewNodeData newNodeData


### Tipovi podataka


[ServentInfo]
- Integer id - id serventa (node-a)
- String ipAdress - ip adresa
- Integer listenerPort - port na kome se slusa
- List<Integer> Neighbors - lista komsijskih node-ova

[Node]
- ServentInfo info - podaci o node-u
- String ID - id node-a

[Dot]
- Integer x - x pozicija tacke
- Integer y - y pozicija tacke

[Job]
-  String name - Ime posla
- Integer n - Broj tacaka posla
- Double p - Rastojanje izmedju 2 tacke u algoritmi
- Integer w - Sirina konacne slike
- Integer h - Visina konacne slike
- HashMap <String, Dot> A - Skup svih pocetnih tacaka


[Section]
- Integer depth - Dubina u fraktalu
- HashMap<String, Dot> dots - Skup tacaka dela posla


[ActiveJob]
- Job job - Posao
- Node myNode - Podaci o cvoru
- Boolean active - Da li je posao zapoceo
- Boolean set - Da li se posao postavlja
- HashMap<String, Node> jobNodes - Skup ostalih node-ova koji rade na poslu
- Section section - Sekcija na kojoj radi node-a

[JobNodeData]
- String id - id
- List<Dot> dots - lista tacaka

[JobRequest]
- ActiveJob activeJob - Aktivni posao
- ServentInfo info - Podaci o cvoru

[StatusData]
- Long iterNumber - Broj dosadasnjih iteracija algoritma
- String fractalD - ID dela posla
- String jobName - Ime posla

[NewNodeData]
- Integer serventCount - Broj cvorova u sistemu
- List<ServentInfo> neigbors - Lista komsijskih node-ova

[ResponseType]
- JOB_RESPONSE - Odgovor prema starteru posla
-  ACCEPTED_OR_REJECTED_JOB_RESPONSE - Odgovor prema primaocu posla
-  RESULT_REQUEST - Zahtev za rezultat
-  RESULT_RESPONSE - Odgovor zahtevu za rezultat
-  STATUS_REQUEST - Zahtev za status
-  STATUS_RESPONSE - Odgovor zahtevu za status

[Response]
- ResponseType type - tip odgovora
- Boolean accepted - da li je odgovor prihvatajuci ili odbijajuci
- Object data - podaci uz odgovor
- Node sender - node koji je poslao odgovor
