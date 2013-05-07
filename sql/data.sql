
-- Base
SET search_path = base, pg_catalog;

INSERT INTO ufficio (id, descrizione, sportello, mittenteodestinatario, attribuzione) VALUES (1, 'Ufficio protocollo', true, true, false);
INSERT INTO ufficio (id, descrizione, sportello, mittenteodestinatario, attribuzione) VALUES (2, 'Ufficio commercio', false, true, true);
INSERT INTO ufficio (id, descrizione, sportello, mittenteodestinatario, attribuzione) VALUES (3, 'Ufficio informativo', true, true, true);
INSERT INTO ufficio (id, descrizione, sportello, mittenteodestinatario, attribuzione) VALUES (4, 'Ufficio edilizia', true, true, true);
SELECT setval('base.ufficio_id_seq', 5, true);

INSERT INTO utente (id, amministratore, attributoreprotocollo, email, login, sigla, istruttorepratiche, modellatorepratiche, nome, operatoreanagrafiche, operatorepratiche, operatoreprotocollo, password, superutente, supervisoreanagrafiche, supervisorepratiche, supervisoreprotocollo, ricercatoreprotocollo) VALUES (1, true, false, NULL, 'admin', 'ADM', false, false, 'Utente amministrativo', false, false, false, '956b329eb8028e15ac00279623f2ef76', false, false, false, false, false);
INSERT INTO utente (id, amministratore, attributoreprotocollo, email, login, sigla, istruttorepratiche, modellatorepratiche, nome, operatoreanagrafiche, operatorepratiche, operatoreprotocollo, password, superutente, supervisoreanagrafiche, supervisorepratiche, supervisoreprotocollo, ricercatoreprotocollo) VALUES (2, false, false, NULL, 'mario', 'M.S.', true, false, 'Mario', true, true, true, '1b3231655cebb7a1f783eddf27d254ca', false, false, false, false, false);
INSERT INTO utente (id, amministratore, attributoreprotocollo, email, login, sigla, istruttorepratiche, modellatorepratiche, nome, operatoreanagrafiche, operatorepratiche, operatoreprotocollo, password, superutente, supervisoreanagrafiche, supervisorepratiche, supervisoreprotocollo, ricercatoreprotocollo) VALUES (3, false, false, NULL, 'luigi', 'L.B.', false, false, 'Luigi', true, true, true, '1b3231655cebb7a1f783eddf27d254ca', false, false, false, false, false);
INSERT INTO utente (id, amministratore, attributoreprotocollo, email, login, sigla, istruttorepratiche, modellatorepratiche, nome, operatoreanagrafiche, operatorepratiche, operatoreprotocollo, password, superutente, supervisoreanagrafiche, supervisorepratiche, supervisoreprotocollo, ricercatoreprotocollo) VALUES (4, false, false, NULL, 'sindaco', 'F.S.', false, false, 'Franco il sindaco', false, false, false, '1b3231655cebb7a1f783eddf27d254ca', false, false, false, false, false);
INSERT INTO utente (id, amministratore, attributoreprotocollo, email, login, sigla, istruttorepratiche, modellatorepratiche, nome, operatoreanagrafiche, operatorepratiche, operatoreprotocollo, password, superutente, supervisoreanagrafiche, supervisorepratiche, supervisoreprotocollo, ricercatoreprotocollo) VALUES (5, false, false, NULL, 'segretario', 'B.S.', false, false, 'Beppe il segretario', false, false, false, '1b3231655cebb7a1f783eddf27d254ca', false, false, false, false, false);
SELECT setval('base.utente_id_seq', 6, true);

INSERT INTO ufficioutente (id, riservato, ricerca, visualizza, daiperletto, modificapratica, inseriscepratica, consolida, responsabile, procedimenti, ufficio, utente) VALUES (1, false, true, true, false, false, false, false, false, false, 1, 3);
INSERT INTO ufficioutente (id, riservato, ricerca, visualizza, daiperletto, modificapratica, inseriscepratica, consolida, responsabile, procedimenti, ufficio, utente) VALUES (2, false, true, false, true, true, true, false, false, false, 3, 2);
INSERT INTO ufficioutente (id, riservato, ricerca, visualizza, daiperletto, modificapratica, inseriscepratica, consolida, responsabile, procedimenti, ufficio, utente) VALUES (3, false, true, false, false, false, false, false, false, false, 4, 2);
SELECT setval('base.ufficioutente_id_seq', 4, true);

-- Anagrafiche
SET search_path = anagrafiche, pg_catalog;

INSERT INTO alboprofessionale (id, descrizione) VALUES (1, 'AGRONOMO');
INSERT INTO alboprofessionale (id, descrizione) VALUES (2, 'ARCHITETTO');
INSERT INTO alboprofessionale (id, descrizione) VALUES (3, 'GEOMETRA');
INSERT INTO alboprofessionale (id, descrizione) VALUES (4, 'INGEGNERE');
INSERT INTO alboprofessionale (id, descrizione) VALUES (5, 'PERITO');
INSERT INTO alboprofessionale (id, descrizione) VALUES (6, 'GEOLOGO');
SELECT setval('anagrafiche.alboprofessionale_id_seq', 7, true);

INSERT INTO gruppo (id, descrizione, persona, azienda, ente) VALUES (1, 'FORNITORE SOFTWARE', false, true, false);
INSERT INTO gruppo (id, descrizione, persona, azienda, ente) VALUES (2, 'CONSULENZA INFORMATICA', false, true, false);
INSERT INTO gruppo (id, descrizione, persona, azienda, ente) VALUES (3, 'DIPENDENTE COMUNALE', true, false, false);
INSERT INTO gruppo (id, descrizione, persona, azienda, ente) VALUES (4, 'COMUNITA'' ALTO GARDA E LEDRO', false, false, true);
INSERT INTO gruppo (id, descrizione, persona, azienda, ente) VALUES (5, 'COMUNITA'' VALLE DEI LAGHI', false, false, true);
INSERT INTO gruppo (id, descrizione, persona, azienda, ente) VALUES (6, 'COLLABORATORE', true, true, false);
SELECT setval('anagrafiche.gruppo_id_seq', 7, true);

INSERT INTO relazione (id, descrizione, inversa, psx, asx, esx, pdx, adx, edx) VALUES (1, 'è in relazione con', 'è in relazione con', true, true, true, true, true, true);
INSERT INTO relazione (id, descrizione, inversa, psx, asx, esx, pdx, adx, edx) VALUES (2, 'è titolare di', 'è la ditta di', true, false, false, false, true, false);
INSERT INTO relazione (id, descrizione, inversa, psx, asx, esx, pdx, adx, edx) VALUES (3, 'è dipendente di', 'è datore di lavoro di', true, false, false, false, true, true);
SELECT setval('anagrafiche.relazione_id_seq', 4, true);

INSERT INTO soggetto (id, codicefiscale, cognome, denominazione, nick, nome, ragionesociale, sessosoggetto, tipo, titolosoggetto) VALUES (1, NULL, 'Lattisi', NULL, NULL, 'Tiziano', NULL, 'M', 'PERSONA', NULL);
INSERT INTO soggetto (id, codicefiscale, cognome, denominazione, nick, nome, ragionesociale, sessosoggetto, tipo, titolosoggetto) VALUES (2, NULL, NULL, NULL, NULL, NULL, 'AXIA STUDIO', NULL, 'AZIENDA', NULL);
INSERT INTO soggetto (id, codicefiscale, cognome, denominazione, nick, nome, ragionesociale, sessosoggetto, tipo, titolosoggetto) VALUES (3, NULL, NULL, 'Comune di Riva del Garda', NULL, NULL, NULL, NULL, 'ENTE', NULL);
SELECT setval('anagrafiche.soggetto_id_seq', 4, true);

INSERT INTO relazionesoggetto (id, soggetto, relazionato, relazione) VALUES (1, 1, 2, 2);
SELECT setval('anagrafiche.relazionesoggetto_id_seq', 4, true);

-- Pratiche
SET search_path = pratiche, pg_catalog;

INSERT INTO tipopratica (id, codice, descrizione, tipopadre, formulacodifica, porzionenumeroda, porzionenumeroa) VALUES (1, 'DET', 'Determine', NULL, NULL, 0, 0);
INSERT INTO tipopratica (id, codice, descrizione, tipopadre, formulacodifica, porzionenumeroda, porzionenumeroa) VALUES (2, 'GES', 'Ramo GES', NULL, NULL, 0, 0);
INSERT INTO tipopratica (id, codice, descrizione, tipopadre, formulacodifica, porzionenumeroda, porzionenumeroa) VALUES (3, 'DETRS', 'Determina del responsabile del servizio', 1, '${s1}${s2}${anno}${n2,number,00000000}', 9, 17);
SELECT setval('pratiche.tipopratica_id_seq', 4, true);

INSERT INTO pratica (id, anno, datapratica, descrizione, idpratica, codiceinterno, note, attribuzione, gestione, ubicazione, dettaglioubicazione, tipo)
  VALUES (1, 2012, '2012-12-10', 'Pratica demo', '201200001', 'DETRS201200000001', NULL, 3, 4, 3, 'scaffale in alto', 3);
SELECT setval('pratiche.pratica_id_seq', 2, true);

-- Protocollo
SET search_path = protocollo, pg_catalog;

INSERT INTO titolo (id, descrizione, tipo) VALUES (1, 'PROPRIETARIO', 'PERSONA_INTERESSATA');
INSERT INTO titolo (id, descrizione, tipo) VALUES (2, 'CONSULENTE ESTERNO', 'TECNICO');
SELECT setval('protocollo.titolo_id_seq', 3, true);

INSERT INTO oggetto (id, descrizione) VALUES (1, 'ATTI RICEVUTI');
INSERT INTO oggetto (id, descrizione) VALUES (2, 'RICHIESTA GENERICA');
SELECT setval('protocollo.oggetto_id_seq', 3, true);

INSERT INTO protocollo (id, convalidaattribuzioni, convalidaprotocollo, anno, annullamentorichiesto, annullato, corrispostoostornato, dataprotocollo, datariferimentomittente, iddocumento, note, oggetto, richiederisposta, riferimentomittente, riservato, spedito, tipo, tiporiferimentomittente, sportello) VALUES (1, false, false, 2012, false, false, false, '2012-12-10', NULL, '201200000001', 'Note del protocollo', 'Oggetto del protocollo', true, NULL, false, false, 'ENTRATA', NULL, 3);
INSERT INTO protocollo (id, convalidaattribuzioni, convalidaprotocollo, anno, annullamentorichiesto, annullato, corrispostoostornato, dataprotocollo, datariferimentomittente, iddocumento, note, oggetto, richiederisposta, riferimentomittente, riservato, spedito, tipo, tiporiferimentomittente, sportello) VALUES (2, false, false, 2012, false, false, false, '2012-12-10', NULL, '201200000002', 'Note del protocollo2', 'Oggetto del protocollo2', false, NULL, false, false, 'USCITA', NULL, 3);
SELECT setval('protocollo.protocollo_id_seq', 3, true);

INSERT INTO attribuzione (id, letto, principale, protocollo, ufficio, evidenza) VALUES (1, false, true, '201200000001', 2, 'E');
INSERT INTO attribuzione (id, letto, principale, protocollo, ufficio, evidenza) VALUES (2, false, false, '201200000001', 1, 'N');
SELECT setval('protocollo.attribuzione_id_seq', 3, true);

INSERT INTO praticaprotocollo (id, oggetto, pratica, protocollo) VALUES (1, 2, '201200001', '201200000001');
SELECT setval('protocollo.praticaprotocollo_id_seq', 2, true);

INSERT INTO soggettoprotocollo (id, conoscenza, corrispondenza, notifica, titolo, protocollo, soggetto) VALUES (1, false, false, false, 1, '201200000001', 1);
INSERT INTO soggettoprotocollo (id, conoscenza, corrispondenza, notifica, titolo, protocollo, soggetto) VALUES (2, false, false, false, 2, '201200000002', 1);
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

INSERT INTO ufficiodetermina (id, determina, ufficio) VALUES (1, 1, 1);
SELECT setval('deliberedetermine.ufficiodetermina_id_seq', 2, true);