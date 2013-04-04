
-- Base
SET search_path = base, pg_catalog;

INSERT INTO ufficio (id, descrizione) VALUES (1, 'Ufficio protocollo');
INSERT INTO ufficio (id, descrizione) VALUES (2, 'Ufficio commercio');
INSERT INTO ufficio (id, descrizione) VALUES (3, 'Ufficio informativo');
INSERT INTO ufficio (id, descrizione) VALUES (4, 'Ufficio edilizia');
INSERT INTO ufficio (id, descrizione) VALUES (5, 'Ufficio protocollo');
SELECT setval('base.ufficio_id_seq', 6, true);

INSERT INTO utente (id, amministratore, attributoreprotocollo, email, login, sigla, modellatorepratiche, nome, operatoreanagrafiche, operatorepratiche, operatoreprotocollo, password, superutente, supervisoreanagrafiche, supervisorepratiche, supervisoreprotocollo, ricercatoreprotocollo) VALUES (1, true, false, NULL, 'admin', 'ADM', false, 'Utente amministrativo', false, false, false, '956b329eb8028e15ac00279623f2ef76', false, false, false, false, false);
INSERT INTO utente (id, amministratore, attributoreprotocollo, email, login, sigla, modellatorepratiche, nome, operatoreanagrafiche, operatorepratiche, operatoreprotocollo, password, superutente, supervisoreanagrafiche, supervisorepratiche, supervisoreprotocollo, ricercatoreprotocollo) VALUES (2, false, false, NULL, 'mario', 'M.S.', false, 'Mario', true, true, true, '1b3231655cebb7a1f783eddf27d254ca', false, false, false, false, false);
INSERT INTO utente (id, amministratore, attributoreprotocollo, email, login, sigla, modellatorepratiche, nome, operatoreanagrafiche, operatorepratiche, operatoreprotocollo, password, superutente, supervisoreanagrafiche, supervisorepratiche, supervisoreprotocollo, ricercatoreprotocollo) VALUES (3, false, false, NULL, 'luigi', 'L.B.', false, 'Luigi', true, true, true, '1b3231655cebb7a1f783eddf27d254ca', false, false, false, false, false);
INSERT INTO utente (id, amministratore, attributoreprotocollo, email, login, sigla, modellatorepratiche, nome, operatoreanagrafiche, operatorepratiche, operatoreprotocollo, password, superutente, supervisoreanagrafiche, supervisorepratiche, supervisoreprotocollo, ricercatoreprotocollo) VALUES (4, false, false, NULL, 'sindaco', 'F.S.', false, 'Franco il sindaco', false, false, false, '1b3231655cebb7a1f783eddf27d254ca', false, false, false, false, false);
INSERT INTO utente (id, amministratore, attributoreprotocollo, email, login, sigla, modellatorepratiche, nome, operatoreanagrafiche, operatorepratiche, operatoreprotocollo, password, superutente, supervisoreanagrafiche, supervisorepratiche, supervisoreprotocollo, ricercatoreprotocollo) VALUES (5, false, false, NULL, 'segretario', 'B.S.', false, 'Beppe il segretario', false, false, false, '1b3231655cebb7a1f783eddf27d254ca', false, false, false, false, false);
SELECT setval('base.utente_id_seq', 6, true);

INSERT INTO ufficioutente (id, privato, ricerca, visualizza, ufficio, utente) VALUES (1, NULL, true, true, 1, 3);
INSERT INTO ufficioutente (id, privato, ricerca, visualizza, ufficio, utente) VALUES (2, NULL, true, NULL, 3, 2);
INSERT INTO ufficioutente (id, privato, ricerca, visualizza, ufficio, utente) VALUES (3, NULL, true, NULL, 4, 2);
INSERT INTO ufficioutente (id, privato, ricerca, visualizza, ufficio, utente) VALUES (4, NULL, true, NULL, 5, 2);
SELECT setval('base.ufficioutente_id_seq', 5, true);

-- Anagrafiche
SET search_path = anagrafiche, pg_catalog;

INSERT INTO soggetto (id, codicefiscale, cognome, denominazione, nick, nome, ragionesociale, sessosoggetto, tiposoggetto, titolosoggetto) VALUES (1, NULL, 'Lattisi', NULL, NULL, 'Tiziano', NULL, 'M', 'PERSONA', NULL);
SELECT setval('anagrafiche.soggetto_id_seq', 2, true);

-- Pratiche
SET search_path = pratiche, pg_catalog;

INSERT INTO tipopratica (id, codice, descrizione, tipopadre) VALUES (1, 'DET', 'Determine', NULL);
INSERT INTO tipopratica (id, codice, descrizione, tipopadre) VALUES (2, 'GES', 'Ramo GES', NULL);
INSERT INTO tipopratica (id, codice, descrizione, tipopadre) VALUES (3, 'DETRS', 'Determina del responsabile del servizio', 1);
SELECT setval('pratiche.tipopratica_id_seq', 4, true);

INSERT INTO pratica (id, anno, datapratica, descrizione, idpratica, codiceinterno, note, attribuzione, gestione, ubicazione, dettaglioubicazione, tipopratica)
  VALUES (1, 2012, '2012-12-10', 'Pratica demo', '2012000001', 'DETDS2012000001', NULL, 3, 4, 3, 'scaffale in alto', 3);
SELECT setval('pratiche.pratica_id_seq', 2, true);

-- Protocollo
SET search_path = protocollo, pg_catalog;

INSERT INTO protocollo (id, convalidaattribuzioni, convalidaprotocollo, anno, annullamentorichiesto, annullato, corrispostoostornato, dataprotocollo, datariferimentomittente, iddocumento, note, oggetto, richiederisposta, riferimentomittente, riservato, spedito, tipo, tiporiferimentomittente, sportello) VALUES (1, false, false, 2012, false, false, false, '2012-12-10', NULL, '201200000001', 'Note del protocollo', 'Oggetto del protocollo', true, NULL, false, false, 'ENTRATA', NULL, 3);
INSERT INTO protocollo (id, convalidaattribuzioni, convalidaprotocollo, anno, annullamentorichiesto, annullato, corrispostoostornato, dataprotocollo, datariferimentomittente, iddocumento, note, oggetto, richiederisposta, riferimentomittente, riservato, spedito, tipo, tiporiferimentomittente, sportello) VALUES (2, false, false, 2012, false, false, false, '2012-12-10', NULL, '201200000002', 'Note del protocollo2', 'Oggetto del protocollo2', false, NULL, false, false, 'USCITA', NULL, 3);
SELECT setval('protocollo.protocollo_id_seq', 3, true);

INSERT INTO attribuzione (id, letto, principale, protocollo, ufficio, evidenza) VALUES (1, false, NULL, '201200000001', 2, 'E');
INSERT INTO attribuzione (id, letto, principale, protocollo, ufficio, evidenza) VALUES (2, false, NULL, '201200000001', 1, 'N');
SELECT setval('protocollo.attribuzione_id_seq', 3, true);

INSERT INTO praticaprotocollo (id, titolo, pratica, protocollo) VALUES (1, NULL, 1, 1);
SELECT setval('protocollo.praticaprotocollo_id_seq', 2, true);

INSERT INTO soggettoprotocollo (id, conoscenza, corrispondenza, notifica, titolo, protocollo, soggetto) VALUES (1, false, false, false, 'TECNICO', '201200000001', 1);
INSERT INTO soggettoprotocollo (id, conoscenza, corrispondenza, notifica, titolo, protocollo, soggetto) VALUES (2, false, false, false, 'TECNICO', '201200000002', 1);
SELECT setval('protocollo.soggettoprotocollo_id_seq', 3, true);

INSERT INTO ufficioprotocollo (id, protocollo, ufficio) VALUES (1, '201200000001', 2);
INSERT INTO ufficioprotocollo (id, protocollo, ufficio) VALUES (2, '201200000002', 1);
SELECT setval('protocollo.ufficioprotocollo_id_seq', 3, true);


-- Finanziaria
SET search_path = finanziaria, pg_catalog;

INSERT INTO servizio (id, descrizione, ufficio) VALUES (1, 'Segreteria generale, personale e organizzazione', 1);
SELECT setval('finanziaria.servizio_id_seq', 2, true);

INSERT INTO capitolo (id, descrizione) VALUES (1, 'Descrizione del capitolo');
SELECT setval('finanziaria.capitolo_id_seq', 2, true);


-- Procedimenti
SET search_path = procedimenti, pg_catalog;

INSERT INTO procedimento (id, descrizione) VALUES (1, 'Unico procedimento di prova');
SELECT setval('procedimenti.procedimento_id_seq', 2, true);

INSERT INTO carica (id, descrizione, codicecarica) VALUES (1, 'Sindaco', 'SINDACO');
INSERT INTO carica (id, descrizione, codicecarica) VALUES (2, 'Vice Sindaco', 'VICE_SINDACO');
INSERT INTO carica (id, descrizione, codicecarica) VALUES (3, 'Segretario', 'SEGRETARIO');
INSERT INTO carica (id, descrizione, codicecarica) VALUES (4, 'Responsabile di servizio', 'RESPONSABILE_DI_SERVIZIO');
INSERT INTO carica (id, descrizione, codicecarica) VALUES (5, 'Assessore attività culturali', null);
INSERT INTO carica (id, descrizione, codicecarica) VALUES (6, 'Assessore attività sociali', null);
SELECT setval('procedimenti.carica_id_seq', 7, true);

INSERT INTO delega (id, carica, utente, ufficio, servizio, procedimento, inizio, fine, titolare, segretario, delegato, suassenza, delegante)
  VALUES (1, 4, 4, NULL, NULL, NULL, '2012-12-10', NULL, true, false, false, false, NULL);
INSERT INTO delega (id, carica, utente, ufficio, servizio, procedimento, inizio, fine, titolare, segretario, delegato, suassenza, delegante)
  VALUES (2, 5, 5, NULL, NULL, NULL, '2012-12-10', NULL, true, false, false, false, NULL);
INSERT INTO delega (id, carica, utente, ufficio, servizio, procedimento, inizio, fine, titolare, segretario, delegato, suassenza, delegante)
  VALUES (3, 4, 2, NULL, NULL, NULL, '2012-12-10', NULL, true, false, false, false, NULL);
SELECT setval('procedimenti.delega_id_seq', 4, true);

-- Sedute
SET search_path = sedute, pg_catalog;

INSERT INTO commissione (id, descrizione) VALUES (1, 'Commissione per la giunta');
INSERT INTO commissione (id, descrizione) VALUES (2, 'Commissione per il consiglio');
SELECT setval('sedute.commissione_id_seq', 3, true);

INSERT INTO caricacommissione (id, carica, commissione) VALUES (1, 1, 1);
INSERT INTO caricacommissione (id, carica, commissione) VALUES (2, 2, 1);
INSERT INTO caricacommissione (id, carica, commissione) VALUES (3, 3, 1);
INSERT INTO caricacommissione (id, carica, commissione) VALUES (4, 1, 2);
SELECT setval('sedute.caricacommissione_id_seq', 5, true);


INSERT INTO tiposeduta (id, descrizione, commissione, tipopratica) VALUES (1, 'Giunta comunale', 1, 1);
INSERT INTO tiposeduta (id, descrizione, commissione, tipopratica) VALUES (2, 'Consiglio comunale', 2, 1);
SELECT setval('sedute.tiposeduta_id_seq', 3, true);

INSERT INTO seduta (id, datacreazione, tiposeduta, dataoraconvocazione, faseseduta, statoseduta, inizioseduta, cambiostatoseduta, fineseduta) VALUES (1, '13/01/2013', 1, NULL, 'IN_GESTIONE', 'A', NULL, NULL, NULL);
SELECT setval('sedute.seduta_id_seq', 2, true);


-- Delibere e determine
SET search_path = deliberedetermine, pg_catalog;

INSERT INTO determina (id, idpratica, codiceinterno, oggetto, datapratica, dispesa, dientrata, diregolarizzazione, referentepolitico, ufficioresponsabile, nomeresponsabile, vistoresponsabile, datavistoresponsabile, titolarevistoresponsabile, segretariovistoresponsabile, delegatovistoresponsabile, utentevistoresponsabile, vistobilancio, datavistobilancio, titolarevistobilancio, segretariovistobilancio, delegatovistobilancio, utentevistobilancio, vistonegato, datavistonegato, titolarevistonegato, segretariovistonegato, delegatovistonegato, utentevistonegato, iddocumento)
  VALUES (1, '2012000001', 'DETRS2012000001', 'Determina di prova', '01/01/2012', false, false, false, NULL, NULL, NULL, false, NULL, false, false, false, NULL, false, NULL, false, false, false, NULL, false, NULL, false, false, false, NULL, NULL);
SELECT setval('deliberedetermine.determina_id_seq', 2, true);

INSERT INTO serviziodetermina (id, determina, servizio) VALUES (1, 1, 1);
SELECT setval('deliberedetermine.serviziodetermina_id_seq', 2, true);

INSERT INTO movimentodetermina (id, determina, importo) VALUES (1, 1, 10.00);
INSERT INTO movimentodetermina (id, determina, importo) VALUES (2, 1, 30.50);
SELECT setval('deliberedetermine.movimentodetermina_id_seq', 3, true);
