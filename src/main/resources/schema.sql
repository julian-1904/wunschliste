DROP TABLE IF EXISTS benutzer;
DROP TABLE IF EXISTS wunschliste;
DROP TABLE IF EXISTS wunschliste_mitglied;
DROP TABLE IF EXISTS wunsch;
DROP TABLE IF EXISTS wunsch_reservierung;

CREATE TABLE IF NOT EXISTS benutzer (
                                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                                        vorname TEXT NOT NULL,
                                        nachname TEXT NOT NULL,
                                        email TEXT NOT NULL UNIQUE,
                                        passwort TEXT NOT NULL,
                                        CHECK (email LIKE '%@%.%')
);

CREATE TABLE IF NOT EXISTS wunschliste (
                                           id INTEGER PRIMARY KEY AUTOINCREMENT,
                                           titel TEXT NOT NULL,
                                           besitzer_id INTEGER NOT NULL,
                                           erstellt_am TEXT NOT NULL DEFAULT (datetime('now')),
                                           eventdate DATE,
                                           freigabe_token TEXT NOT NULL UNIQUE,
                                           FOREIGN KEY (besitzer_id) REFERENCES benutzer(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS wunschliste_mitglied (
                                                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                                                    liste_id INTEGER NOT NULL,
                                                    benutzer_id INTEGER NOT NULL,
                                                    rolle TEXT NOT NULL,
                                                    FOREIGN KEY (liste_id) REFERENCES wunschliste(id) ON DELETE CASCADE,
                                                    FOREIGN KEY (benutzer_id) REFERENCES benutzer(id) ON DELETE CASCADE,
                                                    UNIQUE (liste_id, benutzer_id),
                                                    CHECK (rolle IN ('BESITZER','ADMIN','MITGLIED'))
);

CREATE TABLE IF NOT EXISTS wunsch (
                                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                                      liste_id INTEGER NOT NULL,
                                      titel TEXT NOT NULL,
                                      url TEXT,
                                      bild_url TEXT,
                                      bild_dateiname TEXT,
                                      prioritaet TEXT NOT NULL,
                                      preis NUMERIC,
                                      beschreibung TEXT,
                                      erstellt_am TEXT NOT NULL DEFAULT (datetime('now')),
                                      FOREIGN KEY (liste_id) REFERENCES wunschliste(id) ON DELETE CASCADE,
                                      CHECK (prioritaet IN ('HOCH','MITTEL','NIEDRIG')),
                                      CHECK (preis IS NULL OR preis >= 0)
);

CREATE TABLE IF NOT EXISTS wunsch_reservierung (
                                                   id INTEGER PRIMARY KEY AUTOINCREMENT,
                                                   wunsch_id INTEGER NOT NULL,
                                                   reservierer_id INTEGER NOT NULL,
                                                   reserviert_am TEXT NOT NULL DEFAULT (datetime('now')),
                                                   aktiv INTEGER NOT NULL DEFAULT 1,
                                                   FOREIGN KEY (wunsch_id) REFERENCES wunsch(id) ON DELETE CASCADE,
                                                   FOREIGN KEY (reservierer_id) REFERENCES benutzer(id) ON DELETE CASCADE
);