

-- Generale
SET search_path = generale, pg_catalog;

INSERT INTO costante (id, nome, descrizione, valore, tipocostante) VALUES (1, 'PRATICA_ANNULLATI', 'Pratica contenente i protocolli annullati', '1', 'LONG');
INSERT INTO costante (id, nome, descrizione, valore, tipocostante) VALUES (2, 'UFFICIO_ANNULLATI', 'Ufficio di attribuzione dei protocolli annullati', '1', 'LONG');
INSERT INTO costante (id, nome, descrizione, valore, tipocostante) VALUES (3, 'SOGGETTO_INTERNI', 'Il soggetto a cui collegare i protocolli interni, se non specificato diversamente.', '3', 'LONG');
INSERT INTO costante (id, nome, descrizione, valore, tipocostante) VALUES (4, 'CODICE_AMMINISTRAZIONE', 'Codice amministrazione.', 'r_h330', 'STRING');
INSERT INTO costante (id, nome, descrizione, valore, tipocostante) VALUES (5, 'CODICE_AOO', 'Codice area organizzativa omogenea.', 'RSERVIZI', 'STRING');
INSERT INTO costante (id, nome, descrizione, valore, tipocostante) VALUES (6, 'DENOMINAZIONE', 'Denominazione dell''amministrazione.', 'Comune di Riva del Garda', 'STRING');
INSERT INTO costante (id, nome, descrizione, valore, tipocostante) VALUES (7, 'TOPONIMO', 'Toponimo dell''indirizzo dell''amministrazione (es. via, piazza, etc).', 'piazza', 'STRING');
INSERT INTO costante (id, nome, descrizione, valore, tipocostante) VALUES (8, 'INDIRIZZO_POSTALE', 'Indirizzo postale.', 'III Novembre', 'STRING');
INSERT INTO costante (id, nome, descrizione, valore, tipocostante) VALUES (9, 'CIVICO', 'Numero civico.', '5', 'STRING');
INSERT INTO costante (id, nome, descrizione, valore, tipocostante) VALUES (10, 'CAP', 'Codice avviamento postale.', '38066', 'STRING');
INSERT INTO costante (id, nome, descrizione, valore, tipocostante) VALUES (11, 'COMUNE', 'Comune in cui risiede l''amministrazione.', 'Riva del Garda', 'STRING');
INSERT INTO costante (id, nome, descrizione, valore, tipocostante) VALUES (12, 'PROVINCIA', 'Sigla della provincia.', 'TN', 'STRING');
INSERT INTO costante (id, nome, descrizione, valore, tipocostante) VALUES (13, 'TELEFONO', 'Numero di telefono generale.', '0464 573888', 'STRING');
INSERT INTO costante (id, nome, descrizione, valore, tipocostante) VALUES (14, 'FAX', 'Numero di fax generale.', '0464 552410', 'STRING');
SELECT setval('generale.costante_id_seq', 15, true);

INSERT INTO etichetta (id, nome, device, descrizione, definizione, linguaggio, contesto) VALUES (1, 'ETICHETTA2D', 'Zebra_Technologies_ZTC_GK420t', 'Etichetta DataMatrix', '.\nS1\nb245,34,D,h6,"${iddocumento}"\nP1\n.\n', 'ZPL', 'com.axiastudio.suite.protocollo.entities.Protocollo');
INSERT INTO etichetta (id, nome, device, descrizione, definizione, linguaggio, contesto) VALUES (2, 'ETICHETTA2D2', 'Zebra_Technologies_ZTC_GK420t', 'Etichetta DataMatrix 2', '.\nS1\nb245,34,D,h6,"${iddocumento}"\nP1\n.\n', 'ZPL', 'com.axiastudio.suite.protocollo.entities.Protocollo');
SELECT setval('generale.etichetta_id_seq', 3, true);

-- Base
SET search_path = base, pg_catalog;

INSERT INTO giunta (id, numero, datanascita, datacessazione, note) VALUES (1, 3, '2008-06-01', '2012-05-31', 'Nome del Sindaco');
INSERT INTO giunta (id, numero, datanascita, datacessazione, note) VALUES (2, 4, '2012-06-01', NULL, 'Nome del Sindaco attuale');
SELECT setval('base.giunta_id_seq', 3, true);

INSERT INTO ufficio (id, descrizione, sportello, mittenteodestinatario, attribuzione) VALUES (1, 'Ufficio protocollo', true, true, false);
INSERT INTO ufficio (id, descrizione, sportello, mittenteodestinatario, attribuzione) VALUES (2, 'Ufficio commercio', false, true, true);
INSERT INTO ufficio (id, descrizione, sportello, mittenteodestinatario, attribuzione) VALUES (3, 'Ufficio informativo', true, true, true);
INSERT INTO ufficio (id, descrizione, sportello, mittenteodestinatario, attribuzione) VALUES (4, 'Ufficio edilizia', true, true, true);
INSERT INTO ufficio (id, descrizione, sportello, mittenteodestinatario, attribuzione) VALUES (5, 'Ufficio ragioneria e contabilità', true, true, true);
SELECT setval('base.ufficio_id_seq', 6, true);

INSERT INTO utente (id, amministratore, attributoreprotocollo, email, login, sigla, istruttorepratiche, modellatorepratiche, nome, operatoreanagrafiche, operatorepratiche, operatoreprotocollo, password, superutente, supervisoreanagrafiche, supervisorepratiche, supervisoreprotocollo, ricercatoreprotocollo)
  VALUES (1, true, false, NULL, 'admin', 'ADM', false, false, 'Utente amministrativo', false, false, false, '956b329eb8028e15ac00279623f2ef76', false, false, false, false, false);
INSERT INTO utente (id, amministratore, attributoreprotocollo, email, login, sigla, istruttorepratiche, modellatorepratiche, nome, operatoreanagrafiche, operatorepratiche, operatoreprotocollo, password, superutente, supervisoreanagrafiche, supervisorepratiche, supervisoreprotocollo, ricercatoreprotocollo)
  VALUES (2, false, false, NULL, 'mario', 'M.S.', true, false, 'Mario', true, true, true, '1b3231655cebb7a1f783eddf27d254ca', false, false, false, false, false);
INSERT INTO utente (id, amministratore, attributoreprotocollo, email, login, sigla, istruttorepratiche, modellatorepratiche, nome, operatoreanagrafiche, operatorepratiche, operatoreprotocollo, password, superutente, supervisoreanagrafiche, supervisorepratiche, supervisoreprotocollo, ricercatoreprotocollo)
  VALUES (3, false, true, NULL, 'luigi', 'L.B.', false, false, 'Luigi', true, true, true, '1b3231655cebb7a1f783eddf27d254ca', false, false, false, false, false);
INSERT INTO utente (id, amministratore, attributoreprotocollo, email, login, sigla, istruttorepratiche, modellatorepratiche, nome, operatoreanagrafiche, operatorepratiche, operatoreprotocollo, password, superutente, supervisoreanagrafiche, supervisorepratiche, supervisoreprotocollo, ricercatoreprotocollo)
  VALUES (4, false, false, NULL, 'sindaco', 'F.S.', false, false, 'Franco il sindaco', false, false, false, '1b3231655cebb7a1f783eddf27d254ca', false, false, false, false, false);
INSERT INTO utente (id, amministratore, attributoreprotocollo, email, login, sigla, istruttorepratiche, modellatorepratiche, nome, operatoreanagrafiche, operatorepratiche, operatoreprotocollo, password, superutente, supervisoreanagrafiche, supervisorepratiche, supervisoreprotocollo, ricercatoreprotocollo)
  VALUES (5, false, false, NULL, 'segretario', 'B.S.', false, false, 'Beppe il segretario', false, false, false, '1b3231655cebb7a1f783eddf27d254ca', false, false, false, false, false);
INSERT INTO utente (id, amministratore, attributoreprotocollo, email, login, sigla, istruttorepratiche, modellatorepratiche, nome, operatoreanagrafiche, operatorepratiche, operatoreprotocollo, password, superutente, supervisoreanagrafiche, supervisorepratiche, supervisoreprotocollo, ricercatoreprotocollo)
  VALUES (6, false, false, NULL, 'vice', 'G.V.S.', false, false, 'Gina il vice segretario', false, false, false, '1b3231655cebb7a1f783eddf27d254ca', false, false, false, false, false);
SELECT setval('base.utente_id_seq', 7, true);

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

INSERT INTO relazione (id, descrizione, inversa, asx, psx, esx, adx, pdx, edx) VALUES (1, 'è titolare di', 'è l''azienda di', false, true, false, true, false, false);
INSERT INTO relazione (id, descrizione, inversa, asx, psx, esx, adx, pdx, edx) VALUES (2, 'è dipendente di', 'ha tra i suoi dipendenti', false, true, false, true, false, true);
SELECT setval('anagrafiche.relazione_id_seq', 3, true);

INSERT INTO titolosoggetto (id, descrizione) VALUES (1, 'Sig.');
INSERT INTO titolosoggetto (id, descrizione) VALUES (2, 'Sig.ra');
INSERT INTO titolosoggetto (id, descrizione) VALUES (3, 'Dott.');
INSERT INTO titolosoggetto (id, descrizione) VALUES (4, 'Ing.');
SELECT setval('anagrafiche.titolosoggetto_id_seq', 5, true);

INSERT INTO soggetto (id, codicefiscale, cognome, denominazione, nick, nome, ragionesociale, sessosoggetto, tipo, titolosoggetto) VALUES (1, NULL, 'Lattisi', NULL, NULL, 'Tiziano', NULL, 'M', 'PERSONA', NULL);
INSERT INTO soggetto (id, codicefiscale, cognome, denominazione, nick, nome, ragionesociale, sessosoggetto, tipo, titolosoggetto) VALUES (2, NULL, NULL, NULL, NULL, NULL, 'AXIA STUDIO', NULL, 'AZIENDA', NULL);
INSERT INTO soggetto (id, codicefiscale, cognome, denominazione, nick, nome, ragionesociale, sessosoggetto, tipo, titolosoggetto) VALUES (3, NULL, NULL, 'COMUNE DI RIVA DEL GARDA', NULL, NULL, NULL, NULL, 'ENTE', NULL);
INSERT INTO soggetto (id, codicefiscale, cognome, denominazione, denominazione2, denominazione3, nick, nome, ragionesociale, sessosoggetto, tipo, titolosoggetto) VALUES (4, NULL, NULL, 'Provincia Autonoma di Trento', 'Direzione Generale della Provincia', 'Servizio supporto amministrativo e informatica', NULL, NULL, NULL, NULL, 'ENTE', NULL);
SELECT setval('anagrafiche.soggetto_id_seq', 5, true);

-- Riferimenti
INSERT INTO riferimento (id, descrizione, riferimento, soggetto, tipo) VALUES (1, 'Posta certificata', 'serv.supportoeinformatica@pec.provincia.tn.it', 4, 'PEC');
SELECT setval('anagrafiche.riferimento_id_seq', 2, true);

-- Le relazioni "demo" vengono inserite direttamente nella tabella reale
INSERT INTO relazionesoggetto (id, soggetto, relazione, relazionato) VALUES (1, 1, 1, 2);
SELECT setval('anagrafiche.relazionesoggetto_id_seq', 2, true);


-- Pratiche
SET search_path = pratiche, pg_catalog;

INSERT INTO fase (id, descrizione) VALUES (1, 'Istruttoria');
INSERT INTO fase (id, descrizione) VALUES (2, 'Approvazione del responsabile');
INSERT INTO fase (id, descrizione) VALUES (3, 'Preparazione e firma');
INSERT INTO fase (id, descrizione) VALUES (4, 'Visto di bilancio');
INSERT INTO fase (id, descrizione) VALUES (5, 'Completata');

INSERT INTO tipopratica (id, codice, descrizione, tipopadre, formulacodifica, lunghezzaprogressivo, progressivoanno, progressivogiunta) VALUES (1, 'DET', 'Determine', NULL, NULL, 0, false, false );
INSERT INTO tipopratica (id, codice, descrizione, tipopadre, formulacodifica, lunghezzaprogressivo, progressivoanno, progressivogiunta) VALUES (2, 'GES', 'Ramo GES', NULL, '${s1}${anno}${n1,number,00000}', 5, false, false);
INSERT INTO tipopratica (id, codice, descrizione, tipopadre, formulacodifica, lunghezzaprogressivo, progressivoanno, progressivogiunta) VALUES (3, 'DETRS', 'Determina del responsabile del servizio', 1, '${s2}${anno}${n2,number,00000}', 5, true, false);
SELECT setval('pratiche.tipopratica_id_seq', 4, true);

INSERT INTO pratica (id, anno, datapratica, descrizione, idpratica, codiceinterno, note, attribuzione, gestione, ubicazione, dettaglioubicazione, tipo)
  VALUES (1, 2012, '2012-12-10', 'Pratica dei protocolli annullati', '201200001', 'DETRS201200000001', NULL, 1, 1, 1, 'scaffale in alto', 2);
INSERT INTO pratica (id, anno, datapratica, descrizione, idpratica, codiceinterno, note, attribuzione, gestione, ubicazione, dettaglioubicazione, tipo)
  VALUES (2, 2012, '2012-12-10', 'Pratica demo', '201200002', 'DETRS201200000002', NULL, 3, 4, 3, 'scaffale in alto', 3);
INSERT INTO pratica (id, anno, datapratica, descrizione, idpratica, codiceinterno, note, attribuzione, gestione, ubicazione, dettaglioubicazione, tipo)
  VALUES (3, 2012, '2012-12-11', 'Pratica demo che integra', '201200003', 'DETRS201200000003', NULL, 4, 3, 1, 'scaffale in alto', 3);
SELECT setval('pratiche.pratica_id_seq', 4, true);

INSERT INTO dipendenza (id, descrizionedominante, descrizionedipendente) VALUES (1, 'sostituisce', 'è sostituita da');
INSERT INTO dipendenza (id, descrizionedominante, descrizionedipendente) VALUES (2, 'integra', 'è integrata da');
SELECT setval('pratiche.pratica_id_seq', 3, true);

INSERT INTO dipendenzapratica (id, praticadominante, praticadipendente, dipendenza) VALUES (1, '201200003', '201200002', 2);
SELECT setval('pratiche.dipendenzapratica_id_seq', 2, true);

-- Protocollo
SET search_path = protocollo, pg_catalog;

INSERT INTO titolo (id, descrizione, tipo) VALUES (1, 'PROPRIETARIO', 'PERSONA_INTERESSATA');
INSERT INTO titolo (id, descrizione, tipo) VALUES (2, 'CONSULENTE ESTERNO', 'TECNICO');
SELECT setval('protocollo.titolo_id_seq', 3, true);

INSERT INTO motivazioneannullamento (id, descrizione) VALUES (1, 'Errore materiale');
INSERT INTO motivazioneannullamento (id, descrizione) VALUES (2, 'Protocollo ritirato');
SELECT setval('protocollo.motivazioneannullamento_id_seq', 3, true);


INSERT INTO oggetto (id, descrizione) VALUES (1, 'ATTI RICEVUTI');
INSERT INTO oggetto (id, descrizione) VALUES (2, 'RICHIESTA GENERICA');
INSERT INTO oggetto (id, descrizione) VALUES (3, 'DETERMINAZIONE');
SELECT setval('protocollo.oggetto_id_seq', 4, true);

INSERT INTO protocollo (id, convalidaattribuzioni, convalidaprotocollo, anno, annullamentorichiesto, annullato, corrispostoostornato, dataprotocollo, datariferimentomittente, iddocumento, note, oggetto, richiederisposta, riferimentomittente, riservato, spedito, tipo, tiporiferimentomittente, sportello) VALUES (1, false, false, 2012, false, false, false, '2012-12-10', NULL, '201200000001', 'Note del protocollo', 'Oggetto del protocollo', true, NULL, false, false, 'ENTRATA', NULL, 3);
INSERT INTO protocollo (id, convalidaattribuzioni, convalidaprotocollo, anno, annullamentorichiesto, annullato, corrispostoostornato, dataprotocollo, datariferimentomittente, iddocumento, note, oggetto, richiederisposta, riferimentomittente, riservato, spedito, tipo, tiporiferimentomittente, sportello) VALUES (2, false, false, 2012, false, false, false, '2012-12-10', NULL, '201200000002', 'Note del protocollo2', 'Oggetto del protocollo2', false, NULL, false, false, 'USCITA', NULL, 3);
SELECT setval('protocollo.protocollo_id_seq', 3, true);

INSERT INTO attribuzione (id, letto, principale, protocollo, ufficio, evidenza) VALUES (1, false, true, '201200000001', 2, 'E');
INSERT INTO attribuzione (id, letto, principale, protocollo, ufficio, evidenza) VALUES (2, false, false, '201200000001', 1, 'N');
SELECT setval('protocollo.attribuzione_id_seq', 3, true);

INSERT INTO praticaprotocollo (id, oggetto, pratica, protocollo) VALUES (1, 2, '201200002', '201200000001');
SELECT setval('protocollo.praticaprotocollo_id_seq', 2, true);

INSERT INTO soggettoprotocollo (id, conoscenza, corrispondenza, notifica, titolo, protocollo, soggetto) VALUES (1, false, false, false, 1, '201200000001', 1);
INSERT INTO soggettoprotocollo (id, conoscenza, corrispondenza, notifica, titolo, protocollo, soggetto) VALUES (2, false, false, false, 2, '201200000002', 1);
SELECT setval('protocollo.soggettoprotocollo_id_seq', 3, true);

INSERT INTO ufficioprotocollo (id, protocollo, ufficio) VALUES (1, '201200000001', 2);
INSERT INTO ufficioprotocollo (id, protocollo, ufficio) VALUES (2, '201200000002', 1);
SELECT setval('protocollo.ufficioprotocollo_id_seq', 3, true);


-- Finanziaria
SET search_path = finanziaria, pg_catalog;

INSERT INTO servizio (id, descrizione, ufficio) VALUES (1, 'Segreteria generale, personale e organizzazione', 5);
SELECT setval('finanziaria.servizio_id_seq', 2, true);

INSERT INTO capitolo (id, descrizione) VALUES (1, 'Descrizione del capitolo');
SELECT setval('finanziaria.capitolo_id_seq', 2, true);


-- Procedimenti
SET search_path = procedimenti, pg_catalog;

INSERT INTO procedimento (id, descrizione, tipodettaglio) VALUES (1, 'Determina del responsabile di servizio', 'com.axiastudio.suite.deliberedetermine.entities.Determina');
INSERT INTO procedimento (id, descrizione, tipodettaglio) VALUES (2, 'Procedimento di gestione generico', 'exec open /Applications/TextEdit.app /Users/tiziano/Projects/Suite/${idpratica}.txt');
SELECT setval('procedimenti.procedimento_id_seq', 3, true);

-- fasi di procedimento
INSERT INTO faseprocedimento (id, procedimento, fase, progressivo, testo)
  VALUES (1, 1, 1, 0, 'Chiusura dell''istruttoria'); -- Istruttoria
INSERT INTO faseprocedimento (id, procedimento, fase, progressivo, testo)
  VALUES (2, 1, 2, 1, 'Approvazione del responsabile del servizio: verrà generato un protocollo e inserito nella pratica.'); -- Approvazione responsabile
INSERT INTO faseprocedimento (id, procedimento, fase, progressivo, testo)
  VALUES (3, 1, 3, 2, 'Preparazione dei documenti e firma: i documenti preparati verranno allegati al protocollo.'); -- Preparazione documenti e firma
INSERT INTO faseprocedimento (id, procedimento, fase, progressivo, testo)
  VALUES (4, 1, 4, 3, 'Visto del responsabile di bilancio'); -- Visto bilancio
INSERT INTO faseprocedimento (id, procedimento, fase, progressivo, testo)
  VALUES (5, 1, 5, 4, 'Completata'); -- Visto bilancio
SELECT setval('procedimenti.faseprocedimento_id_seq', 6, true);
-- update di confermata e rifiutata
UPDATE faseprocedimento SET confermabile=true, confermata=2, testoconfermata='Chiudi l''istruttoria' WHERE id=1;
UPDATE faseprocedimento SET confermabile=true, confermata=3, testoconfermata='Approva la determina',
  rifiutabile=true, rifiutata=1, testorifiutata='Rifiuta l''approvazione' WHERE id=2;
UPDATE faseprocedimento SET confermabile=true, confermata=4, testoconfermata='Conferma preparazione',
  rifiutabile=true, rifiutata=1, testorifiutata='Rifiuta la conferma' WHERE id=3;
UPDATE faseprocedimento SET confermabile=true, confermata=5, testoconfermata='Visto di bilancio',
  rifiutabile=true, rifiutata=1, testorifiutata='Rifiuta il visto' WHERE id=4;
-- closure
UPDATE faseprocedimento SET cariche='RESPONSABILE_DI_SERVIZIO,SEGRETARIO', azione='{ determina -> return true }' WHERE id=2;
UPDATE faseprocedimento SET cariche='RESPONSABILE_DI_BILANCIO', azione='{ determina -> return true }' WHERE id=4;

insert into ufficioprocedimento (id, ufficio, procedimento, principale) values (1, 3, 1, true);
insert into ufficioprocedimento (id, ufficio, procedimento, principale) values (2, 3, 2, true);
SELECT setval('procedimenti.ufficioprocedimento_id_seq', 3, true);

insert into utenteprocedimento (id, utente, ufficio, procedimento, responsabile, abilitato, abituale)
    values (1, 2, 3, 1, true, true, true);
SELECT setval('procedimenti.utenteprocedimento_id_seq', 2, true);

insert into tipopraticaprocedimento (id, tipopratica, procedimento) values (1, 3, 1);
insert into tipopraticaprocedimento (id, tipopratica, procedimento) values (2, 2, 2);
SELECT setval('procedimenti.tipopraticaprocedimento_id_seq', 3, true);

INSERT INTO carica (id, descrizione, codicecarica) VALUES (1, 'Sindaco', 'SINDACO');
INSERT INTO carica (id, descrizione, codicecarica) VALUES (2, 'Vice Sindaco', 'VICE_SINDACO');
INSERT INTO carica (id, descrizione, codicecarica) VALUES (3, 'Segretario', 'SEGRETARIO');
INSERT INTO carica (id, descrizione, codicecarica) VALUES (4, 'Vice segretario', 'VICE_SEGRETARIO');
INSERT INTO carica (id, descrizione, codicecarica) VALUES (5, 'Responsabile di servizio', 'RESPONSABILE_DI_SERVIZIO');
INSERT INTO carica (id, descrizione, codicecarica) VALUES (6, 'Responsabile della verifica delle attribuzioni del protocollo', 'RESPONSABILE_ATTRIBUZIONI');
INSERT INTO carica (id, descrizione, codicecarica) VALUES (7, 'Responsabile di bilancio', 'RESPONSABILE_DI_BILANCIO');
SELECT setval('procedimenti.carica_id_seq', 8, true);

-- sindaco, segretario, vicesegretario
INSERT INTO delega (id, carica, utente, ufficio, servizio, procedimento, inizio, fine, titolare, segretario, delegato, suassenza, delegante)
  VALUES (1, 1, 4, NULL, NULL, NULL, '2012-12-10', NULL, true, false, false, false, NULL);
INSERT INTO delega (id, carica, utente, ufficio, servizio, procedimento, inizio, fine, titolare, segretario, delegato, suassenza, delegante)
  VALUES (2, 3, 5, NULL, NULL, NULL, '2012-12-10', NULL, true, false, false, false, NULL);
INSERT INTO delega (id, carica, utente, ufficio, servizio, procedimento, inizio, fine, titolare, segretario, delegato, suassenza, delegante)
  VALUES (3, 4, 6, NULL, NULL, NULL, '2012-12-10', NULL, true, false, false, false, NULL);
-- responsabili di servizio
INSERT INTO delega (id, carica, utente, ufficio, servizio, procedimento, inizio, fine, titolare, segretario, delegato, suassenza, delegante)
  VALUES (4, 5, 2, NULL, NULL, NULL, '2012-12-10', NULL, true, false, false, false, NULL);
INSERT INTO delega (id, carica, utente, ufficio, servizio, procedimento, inizio, fine, titolare, segretario, delegato, suassenza, delegante)
  VALUES (5, 5, 3, NULL, NULL, NULL, '2012-12-10', NULL, true, false, false, false, NULL);
-- responsabili delle attribuzioni del protocollo
INSERT INTO delega (id, carica, utente, ufficio, servizio, procedimento, inizio, fine, titolare, segretario, delegato, suassenza, delegante)
  VALUES (6, 6, 5, NULL, NULL, NULL, '2012-12-10', NULL, true, false, false, false, NULL);
INSERT INTO delega (id, carica, utente, ufficio, servizio, procedimento, inizio, fine, titolare, segretario, delegato, suassenza, delegante)
  VALUES (7, 6, 6, NULL, NULL, NULL, '2012-12-10', NULL, false, false, true, false, 5);

SELECT setval('procedimenti.delega_id_seq', 8, true);

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

INSERT INTO determina (id, idpratica, codiceinterno, oggetto, dispesa, dientrata, diregolarizzazione, referentepolitico, protocollo)
  VALUES (1, '201200002', 'DETRS201200001', 'Determina di prova', false, false, false, NULL, NULL);
SELECT setval('deliberedetermine.determina_id_seq', 2, true);

INSERT INTO serviziodetermina (id, determina, servizio) VALUES (1, 1, 1);
SELECT setval('deliberedetermine.serviziodetermina_id_seq', 2, true);

INSERT INTO movimentodetermina (id, determina, importo) VALUES (1, 1, 10.00);
INSERT INTO movimentodetermina (id, determina, importo) VALUES (2, 1, 30.50);
SELECT setval('deliberedetermine.movimentodetermina_id_seq', 3, true);

INSERT INTO ufficiodetermina (id, determina, ufficio) VALUES (1, 1, 1);
SELECT setval('deliberedetermine.ufficiodetermina_id_seq', 2, true);


-- Modelli di documento
SET search_path = modelli, pg_catalog;

insert into modello (id, titolo, descrizione, uri)
  values (1, 'Comunicazione generica', 'Comunicazione generica in carta intestata', 'workspace://SpacesStore/5ea14f3c-dad1-400d-b3e2-b10e0dc390d8');
insert into modello (id, titolo, descrizione, uri)
  values (2, 'Modello pratica', 'Modello con dati generici di pratica', '');
insert into modello (id, titolo, descrizione, uri, modellopadre, protocollabile)
  values (3, 'Determina', 'Determina del responsabile di servizio', 'workspace://SpacesStore/9dee22bf-e194-42d0-943f-1d85c998f3c9', 2, true);
insert into modello (id, titolo, descrizione, uri, modellopadre, protocollabile)
  values (4, 'Impegni', 'Impegni determina del responsabile di servizio', 'workspace://SpacesStore/f9a1f6ca-62d3-4f6e-a0c5-9ae347241dde', null, true);
SELECT setval('modelli.modello_id_seq', 5, true);

-- modello protocollo
--insert into segnalibro (id, segnalibro, codice, modello)
--  values (1, 'numeroprotocollo', '{ determina -> determina.protocollo ? determina.protocollo.iddocumento : "YYYYNNNNNNNN" }', 2);
--insert into segnalibro (id, segnalibro, codice, modello)
--  values (2, 'dataprotocollo', '{ determina -> determina.protocollo ? determina.protocollo.dataprotocollo : "GG/MM/YYY"}', 2);
-- modello determina
insert into segnalibro (id, segnalibro, codice, modello)
  values (3, 'codiceinterno', '{ determina -> determina.pratica.codiceinterno }', 3);
insert into segnalibro (id, segnalibro, codice, modello)
  values (4, 'oggetto', '{ determina -> determina.oggetto }', 3);
SELECT setval('modelli.segnalibro_id_seq', 5, true);

insert into procedimentomodello (id, procedimento, modello)
    values (1, 1, 2);
SELECT setval('modelli.procedimentomodello_id_seq', 2, true);

insert into tipopraticamodello (id, tipopratica, modello)
  values (1, 3, 2);
SELECT setval('modelli.tipopraticamodello_id_seq', 2, true);
