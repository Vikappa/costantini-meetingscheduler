<h1 align="center">Booking Management System</h1>

<p align="center">
  <a href="https://imgshields.io"><img src="https://img.shields.io/badge/Java-Spring-007396?logo=java&logoColor=white" alt="Java Spring"></a>
  <a href="https://imgshields.io"><img src="https://img.shields.io/badge/Testing-JUnit5-25A162?logo=junit5&logoColor=white" alt="JUnit 5"></a>
  <a href="https://imgshields.io"><img src="https://img.shields.io/badge/Validation-Regex-FF4500?logo=regex&logoColor=white" alt="Regex"></a>
</p>

## 📜 Descrizione

Questo progetto implementa un sistema di gestione delle prenotazioni utilizzando **Java Spring**. Il sistema elabora batch di richieste di prenotazione per riunioni in una sala conferenze, assicurando che tutte le prenotazioni siano valide, non si sovrappongano e rientrino negli orari di apertura specificati.

## 🌟 Funzionalità

- **Gestione delle prenotazioni**: Verifica che una prenotazione sia digitata correttamente, non si sovrapponga con un'altra e rientri negli orari di apertura dell'ufficio.
- **Validazione e parsing**: Utilizza regex per la validazione e il parsing delle stringhe di input.

## 📦 Struttura del Progetto

- **BookingManager**: Classe Singleton che gestisce tutte le prenotazioni. Controlla che una prenotazione sia corretta, non si sovrapponga con altre e rientri negli orari di apertura dell'ufficio.
- **VariousUtilities**: Classe di servizio che contiene metodi di utilità per mantenere il codice pulito. Include metodi di validazione tramite regex e di controllo dei conflitti.
- **Schedule**: Classe che contiene vari formattatori e parser. Il costruttore si basa su stringhe digitate dagli utenti e validate tramite regex.

## 📝 Implementazione

### BookingManager

- **Descrizione**: Classe Singleton che contiene tutte le prenotazioni. Controlla che una prenotazione sia corretta, non si sovrapponga con altre e rientri negli orari di apertura dell'ufficio.
- **Metodi principali**:
  - `setWorkingHours(String line)`: Imposta gli orari di apertura dell'ufficio.
  - `addSchedule(String firstLine, String secondLine)`: Aggiunge una prenotazione alla lista delle prenotazioni.

### VariousUtilities

- **Descrizione**: Classe di servizio che contiene metodi di utilità per mantenere il codice pulito.
- **Metodi principali**:
  - `validateFirstLineStringFormat(String input)`: Valida il formato della prima linea di input.
  - `validateSecondLineStringFormat(String input)`: Valida il formato della seconda linea di input.
  - `validateWorkingHoursLine(String input)`: Valida il formato della linea degli orari di lavoro.
  - `checkScheduleConflicts(ArrayList<Schedule> schedules, Schedule schedule)`: Controlla se ci sono conflitti tra le prenotazioni.
  - `isScheduleInsideOfficeHours(LocalTime openingTime, LocalTime closingTime, Schedule schedule)`: Controlla se una prenotazione rientra negli orari di apertura dell'ufficio.

### Schedule

- **Descrizione**: Classe che contiene vari formattatori e parser. Il costruttore si basa su stringhe digitate dagli utenti e validate tramite regex.
- **Metodi principali**:
  - `getStartAt()`: Ritorna l'orario di inizio della prenotazione.
  - `getCalculatedEndAt()`: Ritorna l'orario di fine della prenotazione calcolato.

## 📂 package.json

- **dev**: Utilizzando `nodemon`, verifica che il file `src` sia stato modificato e in caso compila di nuovo l'output nella cartella `dist`, poi esegue i test e lancia il programma.
- **build**: Solo compilazione.
- **test**: Solo testing.
- **run-only**: Esegue solo il file `index` in `/dist`.

## 🧪 UnitTest

### Various Utilities Test

- **Descrizione**: Classe di test che garantisce che le regex e i metodi di controllo degli orari funzionino correttamente.

### Booking Manager Test

- **Descrizione**: Classe di test che garantisce che le prenotazioni vengano aggiunte o meno seguendo la logica delle attività.

## 🛠️ Tecnologie Utilizzate

- **Java Spring**: Per la configurazione del progetto e l'iniezione delle dipendenze.
- **Regex Expressions**: Per la validazione rigorosa delle stringhe di input.
- **Lambda Functions**: Per iterare sugli array, filtrare e stampare oggetti per ottenere l'output esatto richiesto.
- **JUnit 5**: Per il testing del progetto.
