
-- Base
SET search_path = base, pg_catalog;

INSERT INTO ufficio (id, descrizione) VALUES (1, 'Ufficio protocollo');
INSERT INTO ufficio (id, descrizione) VALUES (2, 'Ufficio commercio');
INSERT INTO ufficio (id, descrizione) VALUES (3, 'Ufficio informativo');
INSERT INTO ufficio (id, descrizione) VALUES (4, 'Ufficio edilizia');
INSERT INTO ufficio (id, descrizione) VALUES (5, 'Ufficio protocollo');
SELECT setval('base.ufficio_id_seq', 6, true);

INSERT INTO utente (id, amministratore, attributoreprotocollo, email, login, modellatorepratiche, nome, operatoreanagrafiche, operatorepratiche, operatoreprotocollo, password, superutente, supervisoreanagrafiche, supervisorepratiche, supervisoreprotocollo) VALUES (1, true, false, NULL, 'admin', false, NULL, false, false, false, '956b329eb8028e15ac00279623f2ef76', false, false, false, false);
INSERT INTO utente (id, amministratore, attributoreprotocollo, email, login, modellatorepratiche, nome, operatoreanagrafiche, operatorepratiche, operatoreprotocollo, password, superutente, supervisoreanagrafiche, supervisorepratiche, supervisoreprotocollo) VALUES (2, false, false, NULL, 'mario', false, NULL, true, true, true, '1b3231655cebb7a1f783eddf27d254ca', false, false, false, false);
INSERT INTO utente (id, amministratore, attributoreprotocollo, email, login, modellatorepratiche, nome, operatoreanagrafiche, operatorepratiche, operatoreprotocollo, password, superutente, supervisoreanagrafiche, supervisorepratiche, supervisoreprotocollo) VALUES (3, false, false, NULL, 'luigi', false, NULL, false, false, false, '1fc1e18f4e180479fc5225d791f80a57', false, false, false, false);
SELECT setval('base.utente_id_seq', 4, true);

INSERT INTO ufficioutente (id, privato, ricerca, visualizza, ufficio, utente) VALUES (1, NULL, true, true, 1, 3);
INSERT INTO ufficioutente (id, privato, ricerca, visualizza, ufficio, utente) VALUES (2, NULL, true, NULL, 3, 2);
INSERT INTO ufficioutente (id, privato, ricerca, visualizza, ufficio, utente) VALUES (3, NULL, true, NULL, 4, 2);
INSERT INTO ufficioutente (id, privato, ricerca, visualizza, ufficio, utente) VALUES (4, NULL, true, NULL, 5, 2);
SELECT setval('base.ufficioutente_id_seq', 5, true);

-- Anagrafiche
SET search_path = anagrafiche, pg_catalog;

INSERT INTO soggetto (id, codicefiscale, cognome, denominazione, nick, nome, ragionesociale, sessosoggetto, tipologiasoggetto, titolosoggetto) VALUES (1, NULL, 'Lattisi', NULL, NULL, 'Tiziano', NULL, 'M', 'PERSONA', NULL);
SELECT setval('anagrafiche.soggetto_id_seq', 2, true);

-- Pratiche
SET search_path = pratiche, pg_catalog;

INSERT INTO pratica (id, anno, datapratica, descrizione, idpratica, note, attribuzione, gestione, ubicazione, dettaglioubicazione) VALUES (1, 2012, '2012-12-10', 'Pratica demo', '201200000001', NULL, 3, 4, 3, 'scaffale in alto');
SELECT setval('pratiche.pratica_id_seq', 2, true);

-- Protocollo
SET search_path = protocollo, pg_catalog;

INSERT INTO protocollo (id, convalidaattribuzioni, convalidaprotocollo, anno, annullamentorichiesto, annullato, corrispostoostornato, dataprotocollo, datariferimentomittente, iddocumento, note, oggetto, richiederisposta, riferimentomittente, riservato, spedito, tipo, tiporiferimentomittente, sportello) VALUES (1, false, false, 2012, false, false, false, '2012-12-10', NULL, '201200000001', 'Note del protocollo', 'Oggetto del protocollo', true, NULL, false, false, 'ENTRATA', NULL, 3);
INSERT INTO protocollo (id, convalidaattribuzioni, convalidaprotocollo, anno, annullamentorichiesto, annullato, corrispostoostornato, dataprotocollo, datariferimentomittente, iddocumento, note, oggetto, richiederisposta, riferimentomittente, riservato, spedito, tipo, tiporiferimentomittente, sportello) VALUES (2, false, false, 2012, false, false, false, '2012-12-10', NULL, '201200000002', 'Note del protocollo2', 'Oggetto del protocollo2', false, NULL, false, false, 'USCITA', NULL, 3);
SELECT setval('protocollo.protocollo_id_seq', 3, true);

INSERT INTO attribuzione (id, letto, principale, protocollo, ufficio) VALUES (1, false, NULL, '201200000001', 2);
INSERT INTO attribuzione (id, letto, principale, protocollo, ufficio) VALUES (2, false, NULL, '201200000001', 1);
SELECT setval('protocollo.attribuzione_id_seq', 3, true);

INSERT INTO praticaprotocollo (id, titolo, pratica, protocollo) VALUES (1, NULL, 1, 1);
SELECT setval('protocollo.praticaprotocollo_id_seq', 2, true);

INSERT INTO soggettoprotocollo (id, conoscenza, corrispondenza, notifica, titolo, protocollo, soggetto) VALUES (1, false, false, false, 'TECNICO', '201200000001', 1);
INSERT INTO soggettoprotocollo (id, conoscenza, corrispondenza, notifica, titolo, protocollo, soggetto) VALUES (2, false, false, false, 'TECNICO', '201200000002', 1);
SELECT setval('protocollo.soggettoprotocollo_id_seq', 3, true);

INSERT INTO ufficioprotocollo (id, protocollo, ufficio) VALUES (1, '201200000001', 2);
INSERT INTO ufficioprotocollo (id, protocollo, ufficio) VALUES (2, '201200000002', 1);
SELECT setval('protocollo.ufficioprotocollo_id_seq', 3, true);
