INSERT INTO benutzer (id, vorname, nachname, email, passwort)
VALUES
    (1, 'Fred', 'Friedman', 'Fred.Friedman@dbg.de', '8418a59a686d05fd4936e5161deff693cb50319d'),
    (2, 'Nelson', 'Naluj', 'Nelson.Naluj@dbg.de', 'ea9b46e18a101e4cd8c17206447692ffd68200d9'),
    (3, 'Dilara', 'Arsalan', 'Dilara.Arsalan@dbg.de', 'b33e3e002d21544db1718f8f5ed25b105eaa6f9d');

INSERT INTO wunschliste (id, titel, besitzer_id, eventdate, freigabe_token)
VALUES
    (1, 'Meine Geburtstagswünsche', 1, '2026-03-09', 'fred-liste-token-123'),
    (2, 'Meine Technik-Wünsche', 2, '2026-07-15', 'nelson-liste-token-456'),
    (3, 'Meine Wohnideen', 3, '2026-12-01', 'dilara-liste-token-789');

INSERT INTO wunschliste_mitglied (liste_id, benutzer_id, rolle)
VALUES
    (1, 2, 'MITGLIED'),
    (1, 3, 'MITGLIED'),
    (2, 1, 'MITGLIED'),
    (2, 3, 'MITGLIED'),
    (3, 1, 'MITGLIED'),
    (3, 2, 'MITGLIED');

INSERT INTO wunsch (id, liste_id, titel, url, bild_url, prioritaet, preis, beschreibung, erstellt_am)
VALUES
    (
        1,
        1,
        'Digital Kamera',
        'https://www.amazon.de',
        'https://images.unsplash.com/photo-1516035069371-29a1b244cc32',
        'HOCH',
        29.99,
        'Eine kompakte Digitalkamera für unterwegs, um besondere Momente im Urlaub und bei Familienfeiern festzuhalten.',
        '2026-03-07 18:00:00'
    ),
    (
        2,
        1,
        'Schöne Duftkerze',
        'https://www.amazon.de',
        'https://images.unsplash.com/photo-1603006905003-be475563bc59',
        'MITTEL',
        14.99,
        'Eine gemütliche Duftkerze für mein Zimmer.',
        '2026-03-07 18:05:00'
    ),
    (
        3,
        2,
        'Bluetooth Kopfhörer',
        'https://www.amazon.de',
        'https://images.unsplash.com/photo-1505740420928-5e560c06d30e',
        'MITTEL',
        37.99,
        'Kabellose Bluetooth-Kopfhörer mit gutem Klang, langer Akkulaufzeit und angenehmem Tragekomfort.',
        '2026-03-07 18:10:00'
    ),
    (
        4,
        2,
        'Gaming Maus',
        'https://www.amazon.de',
        'https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7',
        'HOCH',
        134.11,
        'Präzise Gaming-Maus mit einstellbarer DPI und RGB-Beleuchtung.',
        '2026-03-07 18:15:00'
    ),
    (
        5,
        3,
        'Stehlampe',
        'https://www.amazon.de',
        'https://images.unsplash.com/photo-1540932239986-30128078f3c5',
        'HOCH',
        59.90,
        'Moderne Stehlampe für das Wohnzimmer.',
        '2026-03-07 18:20:00'
    ),
    (
        6,
        3,
        'Kuscheldecke',
        'https://www.amazon.de',
        'https://images.unsplash.com/photo-1515377905703-c4788e51af15',
        'NIEDRIG',
        24.50,
        'Weiche Kuscheldecke für gemütliche Abende.',
        '2026-03-07 18:25:00'
    );

INSERT INTO wunsch_reservierung (wunsch_id, reservierer_id, reserviert_am, aktiv)
VALUES
    (1, 2, '2026-03-08 08:00:00', 1),
    (3, 1, '2026-03-08 08:10:00', 1),
    (5, 2, '2026-03-08 08:20:00', 1);