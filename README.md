# BancaGV

Progetto TPSIT Quarantena

## Funzionamento:

Sono presenti 3 "programmi" indipendenti all'interno del progetto intero.

#### Server:

Il ***Server***, come suggerisce il nome, ha il compito di gestire la connessione con i client e di "dialogare" con il ***DB***, che nel nostro caso è gestito tramite semplici file di testo.

I client presenti sono di due diversi tipi: ***Customer*** e ***Operator***.

Di seguito verranno spiegati i ruoli concettuali dei due client sopra citati, successivamente verrà descritto il funzionamento tecnico delle signole parti.

#### Customer:

Questa classe gestisce il cliente nel senso stretto del termine, cioè il privato cittadino che si rivolge ad una banca (idealizzata dal ***Server*** come struttura centralizzata).

Per prima cosa è stata prevista la possibilità di *Creare* o *Accedere* al proprio account o profilo bancario. NB- Il profilo, se così lo vogliamo chiamare, è <u>diverso</u> dal Conto Corrente, entità che gestisce i *soldi* che un *cliente* affida alla banca attraverso il *profilo*.

Successivamente se decidiamo di creare un nuovo *profilo* verrà visualizzata una finestra che ci esorta a contattare un'*operatore* per collegare al nostro nuovo *profilo* un *conto corrente*. Nel caso in cui avessimo già creato un *profilo* e lo avessimo già collegato ad un *conto corrente*,  verremo direzionati alla *HomePage* dell'applicazione lato ***Customer***.

Questa pagina ci permetterà di compiere 2 azioni: *Prelevare* e *Depositare*. NB- E' ovvio che sia impossibile depositare e prelevare denaro contante attraverso un'applicazione software ma la nostra è più una simulazione del processo che, con un pò di immaginazione, può avvenire all'interno degli sportelli ATM.

Entrambe le azioni necessitano che nel *TextField* soprastante sia inserito un valore numerico accettato, cioè conforme alla notazione decimale americana (120.96 e non 120,96). Nel momento in cui viene premuto il tasto corrispondente all'azione desiderata verrà contattato il ***Server*** per completare la transazione e aggiornare il valore del conto sul ***DB***.

Il fulcro dell'esercitazione era garantire l'accesso e l'utilizzo concorrente di un c/c (conto corrente) da parte di più ***Customer*** simultaneamente. Questo è un test che possiamo fare banalmente aprento due volte la finestra relativa al ***Customer***. NB- Successivamente, nella descrizione tecnica, avremo modo di analizzare come i singoli ***Customers*** siano capaci di eseguire le transazioni con il ***Server*** ma soprattutto come tutti i ***Customer*** online riescano ad aggiornare in tempo reale il saldo del c/c.

#### Operator:

Questa classe, come ho già accennato, si occupa di gestire i collegamenti tra *Profilo* e *ContoCorrente*. Analogamente questo applicativo è pensato per essere usato dagli impiegati della banca che intendono modificare il profilo dei clienti.

La schermata iniziale, nonchè l'unica schermata presente lato ***Operator***, ci permette di: creare un nuovo c/c e di collegarlo ad un ***Customer***, rimuovere il collegamento di un c/c di un ***Customer*** oppure di collegare un c/c <u>esistente</u> ad un ***Customer***.

Le azioni che riguardano rimuovere o aggiungere un c/c esistente sono abbastanza facili da interpretare guardando la finestra, basterà inserire il nome utente del *profilo* e sotto il codice del c/c da aggiungere, nel caso della rimozione può essere lasciato vuoto; per la creazione di un <u>nuovo</u> c/c dovremo inserire il nome utente del *profilo* a cui verrà collegato e dovremo lasciare <u>vuoto</u> il campo relativo al codice, successivamente premere il *Button* per aggiungere.

Ovviamente l'***Operator*** contatterà il server per completare le operazioni.

NB- Successivamente nell'analisi tecnica parleremo di come abbiamo gestito il problema della rimozione del c/c da un utente online.

#### Database:

Come già sottolineato, il ***DB*** è stato gestito tramite *file di testo*.

Nella cartella del progetto è presente una sotto-cartella chiamata *database* che a sua volta contiente *users* e *bankaccounts* (conti corrente).

Ogni *profilo* disporrà di un *file* (il nome del file sarà <nomeutente>.txt) contenente 3 campi nella prima riga separati da uno spazio: *nome utente* (String), *password* (String), *codice c/c* (String). NB- Se ad un profilo non è collegato nessun *conto corrente* il codice sarà "*null*".

Ogni *conto corrente* invece disporrà di un file, con la stessa nomenclatura usata per i *profili* (<codice cc>.txt), contenente 2 campi nella prima riga separati da uno spazio: *codice cc* (String), *saldo* (Double). 

NB- Il motivo per cui abbiamo deciso di creare un file per ogni utente/conto verrà discusso della sezione tecnica.

## Sezione tecnica:

In ordine parleremo di ***Server***, ***Customer***, ***Operator*** e ***DB***.

#### Server:

Abbiamo deciso di optare per un *server* multi-thread, le connessioni stabilite sfruttano il protocollo TCP.

Il ***Server***  starà in ascolto nel main thread sulla  WelcomePort, nel nostro caso la 6000. Una volta che è stata accettata la connessione con il client verrà generata la nota coppia di socket server-client, la socket del client verrà salvata in memoria e verrà passata come parametro al *ClientThread*, classe che gestisce la connessione con un unico client. La classe *ClientThread*, quando inizializzata, istanzierà i buffer di input e output necessari per la comunicazione in entrambe le direzioni con il client stesso.

I messaggi inviati utilizzeranno un protocollo ben preciso: <operazione>|<arg 1>|<arg 2>|<arg 3>|<arg 4>|...

Quando il *Server*/*Client* riceverà il messaggio gli basterà separare il messaggio utilizzando come carattere '|' (la funzione che si occupa di splittare i messaggi è *User.split(String data)* del modulo *com.bancagv.utils*) e successivamente fare un confronto con il primo elemento della lista.

Per ogni operazione abbiamo previsto un metodo della classe *ClientThread*.

Per il ***DB*** abbiamo utilizzato una classe, *FileHandler*; questa classe dà la possibilità di ottenere i buffer di lettura/scrittura sul file in mutua esclusione tra tutti i thread presenti lato ***Server***, inoltre tiene traccia degli utenti online che sono collegati ad uno specifico c/c, questa lista viene utilizzata per aggiornare in tempo reale il saldo del  conto corrente lato ***Customer***.

I Thread che gestiscono la connessione con i client vengono chiusi nel momento in cui i client chiudono l'applicazione.

#### Customer:

Questa classe interagisce con il server in 3 sezione diverse: *Login/Registrazione*, *Prelievo/Deposito* e infine durante l'*Update del saldo*.

Nelle prime due sezioni banalmente verrà contattato il ***Server*** tramite parole chiave apposite come "login" e "signin" riconosciute dal ricevente per eseguire i controlli del caso, come la semplice verifica che la password immessa sia corretta.

Successivamente vengono gestite in maniera analoga le azioni di Deposito e Prelievo.

La parte interessante è quella di aggiornare in tempo reale il saldo del c/c. Abbiamo previsto un Thread (classe *Updater*) che sta in ascolto sul Buffer d'ingresso, questo Thread viene avviato solo quando il login è stato effettuato con successo ed è collegato effettivamente un c/c con il *profilo* in questione; i valori che può ricevere sono 2: "disconnect" oppure "<saldo>" (il secondo sotto forma di notazione decimale).

Il primo viene ricevuto nel caso in cui un operatore abbia disconnesso il c/c di un *utente* che era online nella *HomePage* lato ***Customer***; in questo caso viene immediatamente interrotta la connessione con il server, chiusa la finestra Home e aperta una nuova finestra che esorta a contattare un ***Operator***. 

Nel secondo caso viene semplicemente aggiornato il testo di un *Label* con il saldo appena ricevuto.

#### Operator:

Riguardo questa classe non c'è molto da dire in quanto si occupa solo di mandare messaggi al ***Server*** e non richiede una tecnica particolare per farlo.

#### Database:

Riguardo al *DB* invece dobbiamo notare il perchè dei singoli file.

Abbiamo deciso di gestire così il DB in quanto crediamo che ogni volta, per controllare la passwod di un utente o il codice del suo conto, leggere da un unico file sia troppo dispendioso in quanto a tempo, inoltre in questo caso avremmo dovuto concedere l'accesso in mutua esclusione ogni volta che un utente faceva qualcosa, questo scalato a migliaia di utenti (poco probabile ma potenzialmente possibile) sarebbe risultato in un tempo di attesa enorme nell'accesso al file per una qualsiasi, pur banale, query.

Siamo comunque coscienti del fatto che i "veri" database, come MySQL, lavorano in questo modo (con una singola tabella).

## Conslusioni:

Questo progetto è stato molto interessante e ci ha permesso di sperimentare con Socket, Thread e Semafori più di quanto non avremmo fatto normalmente.

Per suggerimenti, domande o precisazioni sul programma non esitate a contattarmi su GitHub o per email: l.bartolini02@gmail.com .
