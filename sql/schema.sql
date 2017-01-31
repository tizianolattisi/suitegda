-- Generazione database suite

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

-- creazione schemi
CREATE SCHEMA base;
ALTER SCHEMA base OWNER TO postgres;
CREATE SCHEMA anagrafiche;
ALTER SCHEMA anagrafiche OWNER TO postgres;
CREATE SCHEMA protocollo;
ALTER SCHEMA protocollo OWNER TO postgres;
CREATE SCHEMA procedimenti;
ALTER SCHEMA procedimenti OWNER TO postgres;
CREATE SCHEMA pratiche;
ALTER SCHEMA pratiche OWNER TO postgres;
CREATE SCHEMA pubblicazioni;
ALTER SCHEMA pubblicazioni OWNER TO postgres;
CREATE SCHEMA sedute;
ALTER SCHEMA sedute OWNER TO postgres;
CREATE SCHEMA finanziaria;
ALTER SCHEMA finanziaria OWNER TO postgres;
CREATE SCHEMA deliberedetermine;
ALTER SCHEMA deliberedetermine OWNER TO postgres;
CREATE SCHEMA generale;
ALTER SCHEMA generale OWNER TO postgres;
CREATE SCHEMA modelli;
ALTER SCHEMA modelli OWNER TO postgres;
CREATE SCHEMA richieste;
ALTER SCHEMA richieste OWNER TO postgres;
CREATE SCHEMA urp;
ALTER SCHEMA urp OWNER TO postgres;
CREATE SCHEMA pec;
ALTER SCHEMA pec OWNER TO postgres;

-- Create pgplsql
CREATE OR REPLACE FUNCTION public.create_plpgsql_language ()
        RETURNS TEXT
        AS $$
            CREATE PROCEDURAL LANGUAGE plpgsql;
            SELECT 'language plpgsql created'::TEXT;
        $$
LANGUAGE 'sql';
SELECT CASE WHEN (SELECT true::BOOLEAN FROM pg_language WHERE lanname='plpgsql')
    THEN (SELECT 'language plpgsql already installed'::TEXT)
    ELSE (SELECT public.create_plpgsql_language())
    END;
DROP FUNCTION public.create_plpgsql_language ();
ALTER PROCEDURAL LANGUAGE plpgsql OWNER TO postgres;

SET default_tablespace = '';
SET default_with_oids = false;


-- Generale
SET search_path = generale, pg_catalog;

CREATE TABLE costante
(
    id bigserial NOT NULL,
    nome character varying(32),
    descrizione character varying(255),
    valore character varying(32),
    tipocostante character varying(16)
);
ALTER TABLE costante OWNER TO postgres;
ALTER TABLE ONLY costante
    ADD CONSTRAINT costante_pkey PRIMARY KEY (id);

CREATE TABLE etichetta
(
    id bigserial NOT NULL,
    nome character varying(255),
    device character varying(255),
    descrizione character varying(255),
    definizione character varying(2048),
    linguaggio character varying(255),
    contesto character varying(255)
);
ALTER TABLE etichetta OWNER TO postgres;
ALTER TABLE ONLY etichetta
    ADD CONSTRAINT etichetta_pkey PRIMARY KEY (id);

CREATE TABLE withtimestamp
(
  rec_creato timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  rec_creato_da character varying(40),
  rec_modificato timestamp,
  rec_modificato_da character varying(40)
);
ALTER TABLE withtimestamp OWNER TO postgres;

CREATE OR REPLACE FUNCTION generale.insert_pid()
  RETURNS trigger AS
$BODY$begin
  new.pid = pg_backend_pid();
  SELECT client_addr, client_port INTO new.client_addr, new.client_port FROM pg_stat_activity WHERE pid=pg_backend_pid();
  return new;
end;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION generale.insert_pid() OWNER TO postgres;

CREATE TABLE generale.sessionigda
(
  utente character varying,
  pid integer,
  rec_creato timestamp without time zone,
  fine_sessione timestamp without time zone,
  client_addr character varying,
  client_port integer,
  fine_sessione_applname timestamp without time zone,
  appl_name character varying
);
ALTER TABLE generale.sessionigda OWNER TO postgres;
CREATE TRIGGER trg_ins_sessionegda
  BEFORE INSERT
  ON generale.sessionigda
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_pid();

CREATE OR REPLACE FUNCTION generale.finesessione()
  RETURNS trigger AS
$BODY$
DECLARE
  pid_fine int;
  applname varchar;
  clientaddr varchar;
begin
  pid_fine := pg_backend_pid();
  SELECT application_name, client_addr INTO applname, clientaddr FROM pg_stat_activity WHERE pid=pid_fine;
  IF (SELECT COUNT(*) FROM generale.sessionigda WHERE utente=new.utente AND pid=pid_fine AND fine_sessione IS NULL) > 0
	THEN UPDATE generale.sessionigda SET fine_sessione=current_timestamp WHERE utente=new.utente AND pid=pid_fine AND fine_sessione IS NULL;
  ELSIF (SELECT COUNT(*) FROM generale.sessionigda WHERE utente=new.utente AND appl_name=applname AND client_addr=clientaddr AND fine_sessione IS NULL) > 0
	THEN UPDATE generale.sessionigda SET fine_sessione_applname=current_timestamp
		WHERE utente=new.utente AND appl_name=applname AND client_addr=clientaddr AND fine_sessione IS NULL;
  ELSE INSERT INTO generale.sessionigda(utente, pid, fine_sessione, appl_name, fine_sessione_applname)
	VALUES(new.utente, pid_fine, current_timestamp, applname, current_timestamp);
  END IF;
  return new;
end;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION generale.finesessione() OWNER TO postgres;

CREATE TABLE generale.finesessionigda
(
  utente character varying
);
ALTER TABLE generale.finesessionigda OWNER TO postgres;
CREATE TRIGGER tr_upd_finesessionigda
  BEFORE UPDATE
  ON generale.finesessionigda
  FOR EACH ROW
  EXECUTE PROCEDURE generale.finesessione();

CREATE OR REPLACE FUNCTION update_statopecprotocollo()
  RETURNS trigger AS
$BODY$
DECLARE
	nuovostato varchar;
begin
SELECT CASE WHEN (SELECT tipo FROM protocollo.protocollo WHERE iddocumento=new.protocollo)='USCITA' THEN
		CASE WHEN (SELECT count(*) FROM protocollo.soggettoprotocollo WHERE protocollo=new.protocollo AND pec AND messaggiopec IS NULL)>0 THEN 'DAINVIARE'
		WHEN (SELECT count(*) FROM pec.messaggi m, protocollo.soggettoprotocollo sp WHERE sp.protocollo=m.protocollo AND sp.messaggiopec=m.id AND m.protocollo=new.protocollo AND folder='OUT' AND stato_inviato=FALSE)>0 THEN 'DAINVIARE'
		WHEN (SELECT count(*) FROM pec.messaggi m, protocollo.soggettoprotocollo sp WHERE sp.protocollo=m.protocollo AND sp.messaggiopec=m.id AND m.protocollo=new.protocollo AND folder='OUT')=0 THEN 'ERRORE'
		WHEN (SELECT count(*) FROM pec.messaggi m, protocollo.soggettoprotocollo sp WHERE sp.protocollo=m.protocollo AND sp.messaggiopec=m.id AND m.protocollo=new.protocollo AND folder='OUT' AND stato_anomalia)>0 THEN 'ANOMALIA'
		WHEN (SELECT count(*) FROM pec.messaggi m, protocollo.soggettoprotocollo sp WHERE sp.protocollo=m.protocollo AND sp.messaggiopec=m.id AND m.protocollo=new.protocollo AND folder='OUT' AND stato_consegnato=FALSE)>0 THEN 'INVIATO'
		ELSE 'CONSEGNATO' END
	ELSE 'CONSEGNATO'
	END INTO nuovostato;
IF (COALESCE(new.protocollo, '')!='' AND (SELECT count(*) FROM protocollo.pecprotocollo WHERE protocollo=new.protocollo)=0) THEN
	INSERT INTO protocollo.pecprotocollo(protocollo, stato)
	VALUES(new.protocollo, nuovostato);
ELSE
	UPDATE protocollo.pecprotocollo
	SET stato = nuovostato
	WHERE protocollo=new.protocollo;
END IF;
return new;
end$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_statopecprotocollo()
  OWNER TO postgres;


-- Base
SET search_path = base, pg_catalog;

CREATE TABLE giunta (
  id bigserial NOT NULL,
  numero integer NOT NULL,
  datanascita date,
  datacessazione date,
  note character varying(255)
);
ALTER TABLE base.giunta OWNER TO postgres;
ALTER TABLE ONLY giunta
ADD CONSTRAINT giunta_pkey PRIMARY KEY (id);

CREATE TABLE utente (
    id bigserial NOT NULL,
    amministratore boolean,
    attributoreprotocollo boolean NOT NULL DEFAULT FALSE,
    email character varying(255),
    login character varying(40),
    modellatorepratiche boolean NOT NULL DEFAULT FALSE,
    nome character varying(60),
    sigla character varying(10),
    operatoreanagrafiche boolean NOT NULL DEFAULT TRUE,
    operatorepratiche boolean NOT NULL DEFAULT TRUE,
    operatoreprotocollo boolean NOT NULL DEFAULT TRUE,
    operatoreurp boolean NOT NULL DEFAULT TRUE,
    password character varying(255),
    superutente boolean NOT NULL DEFAULT FALSE,
    supervisoreanagrafiche boolean NOT NULL DEFAULT FALSE,
    supervisorepratiche boolean NOT NULL DEFAULT FALSE,
    supervisoreprotocollo boolean NOT NULL DEFAULT FALSE,
    supervisoreurp boolean NOT NULL DEFAULT FALSE,
    ricercatoreprotocollo boolean NOT NULL DEFAULT FALSE,
    spedisceprotocollo boolean NOT NULL DEFAULT FALSE,
    istruttorepratiche boolean NOT NULL DEFAULT FALSE,
    attributorepratiche boolean NOT NULL DEFAULT FALSE,
    pubblicaalbo boolean NOT NULL DEFAULT FALSE,
    disabilitato boolean NOT NULL DEFAULT FALSE,
    nuovodocsuconsolidato boolean NOT NULL DEFAULT FALSE,
    soggetto bigint
) INHERITS (generale.withtimestamp);
ALTER TABLE base.utente OWNER TO postgres;
ALTER TABLE ONLY utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (id);
-- ALTER TABLE ONLY utente
--     ADD CONSTRAINT fk_utente_soggetto FOREIGN KEY (soggetto) REFERENCES anagrafiche.soggetto(id);
ALTER TABLE base.utente
  ADD COLUMN richieste boolean DEFAULT false;

CREATE TABLE ufficio (
    id bigserial NOT NULL,
    descrizione character varying(255),
    denominazione character varying(500),
    sportello boolean NOT NULL DEFAULT FALSE,
    mittenteodestinatario boolean NOT NULL DEFAULT FALSE,
    attribuzione boolean NOT NULL DEFAULT FALSE,
    assessorato boolean NOT NULL DEFAULT FALSE,
    pec character varying(255)
) INHERITS (generale.withtimestamp);
ALTER TABLE base.ufficio OWNER TO postgres;
ALTER TABLE ONLY ufficio
    ADD CONSTRAINT ufficio_pkey PRIMARY KEY (id);
ALTER TABLE base.ufficio
  ADD COLUMN richieste boolean DEFAULT false;

CREATE TABLE ufficioutente (
    id bigserial NOT NULL,
    ospite boolean NOT NULL DEFAULT FALSE,
    riservato boolean NOT NULL DEFAULT FALSE,
    ricerca boolean NOT NULL DEFAULT TRUE,
    visualizza boolean NOT NULL DEFAULT TRUE,
    daiperletto boolean NOT NULL DEFAULT FALSE,
    inseriscepratica boolean NOT NULL DEFAULT FALSE,
    modificapratica boolean NOT NULL DEFAULT FALSE,
    consolida boolean NOT NULL DEFAULT FALSE,
    responsabile boolean NOT NULL DEFAULT FALSE,
    procedimenti boolean NOT NULL DEFAULT FALSE,
    leggepec boolean NOT NULL DEFAULT FALSE,
    ufficio bigint,
    utente bigint
) INHERITS (generale.withtimestamp);
ALTER TABLE base.ufficioutente OWNER TO postgres;
ALTER TABLE ONLY ufficioutente
    ADD CONSTRAINT ufficioutente_pkey PRIMARY KEY (id);
ALTER TABLE ONLY ufficioutente
    ADD CONSTRAINT fk_ufficioutente_ufficio FOREIGN KEY (ufficio) REFERENCES ufficio(id);
ALTER TABLE ONLY ufficioutente
    ADD CONSTRAINT fk_ufficioutente_utente FOREIGN KEY (utente) REFERENCES utente(id);


-- Anagrafiche
SET search_path = anagrafiche, pg_catalog;

CREATE TABLE relazione (
    id bigserial NOT NULL,
    descrizione character varying(255),
    inversa character varying(255),
    asx boolean NOT NULL DEFAULT FALSE,
    psx boolean NOT NULL DEFAULT FALSE,
    esx boolean NOT NULL DEFAULT FALSE,
    adx boolean NOT NULL DEFAULT FALSE,
    pdx boolean NOT NULL DEFAULT FALSE,
    edx boolean NOT NULL DEFAULT FALSE
) INHERITS (generale.withtimestamp);
ALTER TABLE anagrafiche.relazione OWNER TO postgres;
ALTER TABLE ONLY relazione
    ADD CONSTRAINT relazione_pkey PRIMARY KEY (id);

CREATE TABLE stato (
    id bigserial NOT NULL,
    codice character varying(3),
    descrizione character varying(255)
);
ALTER TABLE anagrafiche.stato OWNER TO postgres;
ALTER TABLE ONLY stato
    ADD CONSTRAINT stato_pkey PRIMARY KEY (id);
ALTER TABLE ONLY stato
    ADD CONSTRAINT stato_codice_key UNIQUE (codice);

CREATE TABLE anagrafiche.provincia
(
  id bigserial NOT NULL,
  codice character varying(2),
  descrizione character varying(255),
  attiva boolean NOT NULL DEFAULT TRUE
);
ALTER TABLE anagrafiche.provincia OWNER TO postgres;
ALTER TABLE ONLY provincia
    ADD CONSTRAINT provincia_pkey PRIMARY KEY (id);
ALTER TABLE ONLY provincia
    ADD CONSTRAINT provincia_codice_key UNIQUE (codice);

CREATE TABLE alboprofessionale (
    id bigserial NOT NULL,
    descrizione character varying(255)
);
ALTER TABLE anagrafiche.alboprofessionale OWNER TO postgres;
ALTER TABLE ONLY alboprofessionale
    ADD CONSTRAINT alboprofessionale_pkey PRIMARY KEY (id);

CREATE TABLE gruppo (
    id bigserial NOT NULL,
    descrizione character varying(255),
    persona boolean NOT NULL DEFAULT FALSE,
    azienda boolean NOT NULL DEFAULT FALSE,
    ente boolean NOT NULL DEFAULT FALSE
) INHERITS (generale.withtimestamp);
ALTER TABLE anagrafiche.gruppo OWNER TO postgres;
ALTER TABLE ONLY gruppo
    ADD CONSTRAINT gruppo_pkey PRIMARY KEY (id);

CREATE TABLE anagrafiche.titolosoggetto (
    id serial NOT NULL,
    descrizione character varying(20)
);
ALTER TABLE anagrafiche.titolosoggetto OWNER TO postgres;
ALTER TABLE ONLY anagrafiche.titolosoggetto
    ADD CONSTRAINT titolosoggetto_pkey PRIMARY KEY (id);

CREATE TABLE titolostudio (
    id serial NOT NULL,
    descrizione character varying(100),
    titolirientranti character varying(255),
    bonus boolean NOT NULL DEFAULT FALSE
);
ALTER TABLE anagrafiche.titolostudio OWNER TO postgres;
ALTER TABLE ONLY titolostudio
    ADD CONSTRAINT titolostudio_pkey PRIMARY KEY (id);

CREATE TABLE soggetto (
    id bigserial NOT NULL,
    codicefiscale character varying(16),
    cognome character varying(50),
    nome character varying(50),
    nick character varying(50),
    denominazione character varying(255),
    denominazione2 character varying(255),
    denominazione3 character varying(255),
    ragionesociale character varying(100),
    partitaiva character varying(11),
    sessosoggetto character varying(2),
    tipo character varying(15),
    titolosoggetto int,
    referente character varying(100),
    comunedinascita character varying(100),
    datanascita date,
    datacessazione date,
    descrizionecessazione character varying(255),
    alboprofessionale bigint,
    provinciaalbo character varying(2),
    numeroiscrizionealbo character varying(15),
    indicepao character varying(255),
    indicepaaoo character varying(255),
    residente boolean,
    codiceanagrafe character varying(255)
) INHERITS (generale.withtimestamp);
ALTER TABLE anagrafiche.soggetto OWNER TO postgres;
ALTER TABLE ONLY soggetto
    ADD CONSTRAINT soggetto_pkey PRIMARY KEY (id);
ALTER TABLE ONLY soggetto
    ADD CONSTRAINT fk_soggetto_alboprofessionale FOREIGN KEY (alboprofessionale) REFERENCES alboprofessionale(id);
ALTER TABLE ONLY soggetto
    ADD CONSTRAINT fk_soggetto_titolosoggetto FOREIGN KEY (titolosoggetto) REFERENCES titolosoggetto(id);

-- Tabella base.utente
ALTER TABLE ONLY base.utente
    ADD CONSTRAINT fk_utente_soggetto FOREIGN KEY (soggetto) REFERENCES anagrafiche.soggetto(id);
-- /Tabella base.utente

CREATE TABLE grupposoggetto (
    id bigserial NOT NULL,
    soggetto bigint NOT NULL,
    gruppo bigint NOT NULL,
    datanascita date,
    datacessazione date,
    note character varying(255)
) INHERITS (generale.withtimestamp);
ALTER TABLE anagrafiche.grupposoggetto OWNER TO postgres;
ALTER TABLE ONLY grupposoggetto
    ADD CONSTRAINT grupposoggetto_pkey PRIMARY KEY (id);
ALTER TABLE ONLY grupposoggetto
    ADD CONSTRAINT fk_grupposoggetto_soggetto FOREIGN KEY (soggetto) REFERENCES soggetto(id);
ALTER TABLE ONLY grupposoggetto
    ADD CONSTRAINT fk_grupposoggetto_gruppo FOREIGN KEY (gruppo) REFERENCES gruppo(id);

CREATE TABLE titolostudiosoggetto (
    id bigserial NOT NULL,
    soggetto bigint NOT NULL,
    titolostudio int NOT NULL,
    dettaglio character varying(255),
    datatitolo date,
    secondario boolean NOT NULL DEFAULT FALSE
) INHERITS (generale.withtimestamp);
ALTER TABLE anagrafiche.titolostudiosoggetto OWNER TO postgres;
ALTER TABLE ONLY titolostudiosoggetto
    ADD CONSTRAINT titolostudiosoggetto_pkey PRIMARY KEY (id);
ALTER TABLE ONLY titolostudiosoggetto
    ADD CONSTRAINT fk_titolostudiosoggetto_soggetto FOREIGN KEY (soggetto) REFERENCES soggetto(id);
ALTER TABLE ONLY titolostudiosoggetto
    ADD CONSTRAINT fk_titolostudiosoggetto_titolostudio FOREIGN KEY (titolostudio) REFERENCES titolostudio(id);

-- la tabella "reale" delle relazioni soggetto contiene solo le relazioni "dritte"
CREATE TABLE zrelazionesoggetto (
    id bigserial NOT NULL,
    soggetto bigint,
    relazione bigint,
    relazionato bigint,
    datanascita date,
    datacessazione date,
    abilitatoweb boolean DEFAULT FALSE
) INHERITS (generale.withtimestamp);
ALTER TABLE anagrafiche.zrelazionesoggetto OWNER TO postgres;
ALTER TABLE ONLY zrelazionesoggetto
    ADD CONSTRAINT zrelazionesoggetto_pkey PRIMARY KEY (id);
ALTER TABLE ONLY zrelazionesoggetto
    ADD CONSTRAINT fk_zrelazionesoggetto_soggetto FOREIGN KEY (soggetto) REFERENCES soggetto(id);
ALTER TABLE ONLY zrelazionesoggetto
    ADD CONSTRAINT fk_zrelazionesoggetto_soggettor FOREIGN KEY (relazionato) REFERENCES soggetto(id);
ALTER TABLE ONLY zrelazionesoggetto
    ADD CONSTRAINT fk_zrelazionesoggetto_relazione FOREIGN KEY (relazione) REFERENCES relazione(id);

-- la sequenza deve avere il nome senza la "z"
CREATE SEQUENCE anagrafiche.relazionesoggetto_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 2
  CACHE 1;
ALTER TABLE anagrafiche.relazionesoggetto_id_seq
  OWNER TO postgres;
ALTER TABLE anagrafiche.zrelazionesoggetto ALTER COLUMN id SET DEFAULT nextval('anagrafiche.relazionesoggetto_id_seq'::regclass);
DROP SEQUENCE anagrafiche.zrelazionesoggetto_id_seq;

-- la vista delle relazioni è una union delle relazioni reali "dritte" con quelle "girate"
-- tre regole gestiscono insert/update/delete dalla vista alla tabella reale.
CREATE OR REPLACE VIEW anagrafiche.relazionesoggetto AS
 SELECT zrelazionesoggetto.id,
    zrelazionesoggetto.soggetto,
    zrelazionesoggetto.relazione,
    zrelazionesoggetto.relazionato,
    zrelazionesoggetto.datanascita,
    zrelazionesoggetto.datacessazione,
    zrelazionesoggetto.abilitatoweb,
    false AS invertita,
    zrelazionesoggetto.rec_creato,
    zrelazionesoggetto.rec_creato_da,
    zrelazionesoggetto.rec_modificato,
    zrelazionesoggetto.rec_modificato_da
   FROM anagrafiche.zrelazionesoggetto
UNION
 SELECT - zrelazionesoggetto.id::integer AS id,
    zrelazionesoggetto.relazionato AS soggetto,
    zrelazionesoggetto.relazione,
    zrelazionesoggetto.soggetto AS relazionato,
    zrelazionesoggetto.datanascita,
    zrelazionesoggetto.datacessazione,
    zrelazionesoggetto.abilitatoweb,
    true AS invertita,
    zrelazionesoggetto.rec_creato,
    zrelazionesoggetto.rec_creato_da,
    zrelazionesoggetto.rec_modificato,
    zrelazionesoggetto.rec_modificato_da
   FROM anagrafiche.zrelazionesoggetto;
ALTER TABLE anagrafiche.relazionesoggetto
  OWNER TO postgres;

CREATE RULE relazionesoggetto_delete AS ON DELETE TO relazionesoggetto DO INSTEAD
	DELETE FROM zrelazionesoggetto
	WHERE zrelazionesoggetto.id::integer = abs(old.id);

CREATE OR REPLACE RULE relazionesoggetto_insert AS
    ON INSERT TO anagrafiche.relazionesoggetto DO INSTEAD
  INSERT INTO anagrafiche.zrelazionesoggetto (soggetto, relazione, relazionato, datanascita, datacessazione, abilitatoweb,
    rec_creato, rec_creato_da, rec_modificato, rec_modificato_da)
  VALUES (new.soggetto, new.relazione, new.relazionato, new.datanascita, new.datacessazione, new.abilitatoweb,
    new.rec_creato, new.rec_creato_da, new.rec_modificato, new.rec_modificato_da)
  RETURNING zrelazionesoggetto.id,
    zrelazionesoggetto.soggetto,
    zrelazionesoggetto.relazione,
    zrelazionesoggetto.relazionato,
    zrelazionesoggetto.datanascita,
    zrelazionesoggetto.datacessazione,
    zrelazionesoggetto.abilitatoweb,
    false AS bool,
    zrelazionesoggetto.rec_creato,
    zrelazionesoggetto.rec_creato_da,
    zrelazionesoggetto.rec_modificato,
    zrelazionesoggetto.rec_modificato_da;

CREATE OR REPLACE RULE relazionesoggetto_update AS
    ON UPDATE TO anagrafiche.relazionesoggetto DO INSTEAD nothing; 

CREATE OR REPLACE RULE relazionesoggetto_update_dritta AS
    ON UPDATE TO anagrafiche.relazionesoggetto
   WHERE new.invertita = false DO
  UPDATE anagrafiche.zrelazionesoggetto SET soggetto = new.soggetto, relazione = new.relazione, relazionato = new.relazionato,
    datanascita = new.datanascita, datacessazione = new.datacessazione, abilitatoweb = new.abilitatoweb,
    rec_creato = new.rec_creato, rec_creato_da = new.rec_creato_da, rec_modificato = new.rec_modificato, rec_modificato_da = new.rec_modificato_da
  WHERE zrelazionesoggetto.id::integer = abs(old.id);

CREATE OR REPLACE RULE relazionesoggetto_update_invertita AS
    ON UPDATE TO anagrafiche.relazionesoggetto
   WHERE new.invertita DO
  UPDATE anagrafiche.zrelazionesoggetto SET soggetto = new.relazionato, relazione = new.relazione, relazionato = new.soggetto,
    datanascita = new.datanascita, datacessazione = new.datacessazione, abilitatoweb = new.abilitatoweb,
    rec_creato = new.rec_creato, rec_creato_da = new.rec_creato_da, rec_modificato = new.rec_modificato, rec_modificato_da = new.rec_modificato_da
  WHERE zrelazionesoggetto.id::integer = abs(old.id);


CREATE TABLE indirizzo (
    id bigserial NOT NULL,
    tipo character varying(50),
    via character varying(100),
    civico character varying(8),
    cap character varying(5),
    frazione character varying(100),
    comune character varying(100),
    provincia character varying(2),
    stato character varying(3),
    soggetto bigint,
    descrizione character varying(255),
    principale boolean NOT NULL DEFAULT false,
    datanascita date,
    datacessazione date
) INHERITS (generale.withtimestamp);
ALTER TABLE anagrafiche.indirizzo OWNER TO postgres;
ALTER TABLE ONLY indirizzo
    ADD CONSTRAINT indirizzo_pkey PRIMARY KEY (id);
ALTER TABLE ONLY indirizzo
    ADD CONSTRAINT fk_indirizzo_soggetto FOREIGN KEY (soggetto) REFERENCES soggetto(id);
ALTER TABLE ONLY indirizzo
    ADD CONSTRAINT fk_indirizzo_stato FOREIGN KEY (stato) REFERENCES stato(codice);
ALTER TABLE ONLY indirizzo
    ADD CONSTRAINT fk_indirizzo_provincia FOREIGN KEY (provincia) REFERENCES provincia(codice);

CREATE TABLE riferimento (
    id bigserial NOT NULL,
    tipo character varying(50),
    soggetto bigint,
    riferimento character varying(255),
    descrizione character varying(255),
    lavoro boolean NOT NULL DEFAULT false,
    pubblicabile boolean NOT NULL DEFAULT false
) INHERITS (generale.withtimestamp);
ALTER TABLE anagrafiche.riferimento OWNER TO postgres;
ALTER TABLE ONLY riferimento
    ADD CONSTRAINT riferimento_pkey PRIMARY KEY (id);
ALTER TABLE ONLY riferimento
    ADD CONSTRAINT fk_riferimento_soggetto FOREIGN KEY (soggetto) REFERENCES soggetto(id);
ALTER TABLE anagrafiche.riferimento
    ADD COLUMN principaleentrata boolean NOT NULL DEFAULT FALSE;
ALTER TABLE anagrafiche.riferimento
    ADD COLUMN principaleuscita boolean NOT NULL DEFAULT FALSE;

-- Finanziaria (per ora solo i servizi per delibere e determine)
SET search_path = finanziaria, pg_catalog;

CREATE TABLE servizio (
    id bigserial NOT NULL,
    descrizione character varying(1024),
    ufficio bigint,
    attribuzione bigint,
    referentepolitico character varying(100),
    responsabileprocedura character varying(10)
);
ALTER TABLE finanziaria.servizio OWNER TO postgres;
ALTER TABLE ONLY servizio
    ADD CONSTRAINT servizio_pkey PRIMARY KEY (id);
ALTER TABLE ONLY servizio
    ADD CONSTRAINT fk_servizio_ufficio FOREIGN KEY (ufficio) REFERENCES base.ufficio(id);
ALTER TABLE ONLY servizio
    ADD CONSTRAINT fk_servizio_attribuzione FOREIGN KEY (attribuzione) REFERENCES base.ufficio (id);

CREATE TABLE capitolo (
    id bigserial NOT NULL,
    numero character varying(6),
    descrizione character varying(1024)
);
ALTER TABLE finanziaria.capitolo OWNER TO postgres;
ALTER TABLE ONLY capitolo
    ADD CONSTRAINT capitolo_pkey PRIMARY KEY (id);


	
CREATE TABLE progetto (
    id bigserial NOT NULL,
    anno int NOT NULL ,
    codiceprogetto character varying (5) NULL ,
    progetto character varying (250) NULL ,
    ufficioresponsabile bigint NULL ,
    investimento character varying NULL ,
    servizio character varying NULL ,
    motivazione character varying NULL ,
    descrizione character varying NULL ,
    referentepolitico character varying (255) NULL ,
    approvazioneconsiglio boolean NULL ,
    dataconsiglio date NULL 
) INHERITS (generale.withtimestamp);
ALTER TABLE finanziaria.progetto OWNER TO postgres;
ALTER TABLE ONLY progetto
    ADD CONSTRAINT progetto_pkey PRIMARY KEY (id);
ALTER TABLE ONLY progetto
    ADD CONSTRAINT fk_progetto_ufficio FOREIGN KEY (ufficioresponsabile) REFERENCES base.ufficio(id);


-- Procedimenti
SET search_path = procedimenti, pg_catalog;

CREATE TABLE procedimento (
    id bigserial NOT NULL,
    descrizione character varying(255),
    normativa character varying(255),
    maxgiorniistruttoria integer,
    iniziativa character varying(20),
    soggetto bigint,
    attivo boolean,
    tipodettaglio character varying(255)
) INHERITS (generale.withtimestamp);
ALTER TABLE procedimenti.procedimento OWNER TO postgres;
ALTER TABLE ONLY procedimento
    ADD CONSTRAINT procedimento_pkey PRIMARY KEY (id);
ALTER TABLE ONLY procedimento
    ADD CONSTRAINT fk_procedimento_soggetto FOREIGN KEY (soggetto) REFERENCES anagrafiche.soggetto(id);

CREATE TABLE norma (
    id bigserial NOT NULL,
    tipo character varying(255),
    descrizione character varying(255),
    idobject character varying(255)
);
ALTER TABLE procedimenti.norma OWNER TO postgres;
ALTER TABLE ONLY norma
    ADD CONSTRAINT norma_pkey PRIMARY KEY (id);

CREATE TABLE normaprocedimento (
    id bigserial NOT NULL,
    procedimento bigint,
    norma bigint
);
ALTER TABLE procedimenti.normaprocedimento OWNER TO postgres;
ALTER TABLE ONLY normaprocedimento
    ADD CONSTRAINT normaprocedimento_pkey PRIMARY KEY (id);
ALTER TABLE ONLY normaprocedimento
    ADD CONSTRAINT fk_normaprocedimento_procedimento FOREIGN KEY (procedimento) REFERENCES procedimento(id);
ALTER TABLE ONLY normaprocedimento
    ADD CONSTRAINT fk_normaprocedimento_norma FOREIGN KEY (norma) REFERENCES norma(id);

CREATE TABLE pratiche.fase (
  id bigserial NOT NULL,
  descrizione character varying(255) NOT NULL,
  esclusivadaufficio bigint,
  istruttoria boolean,
  evidenza boolean default false
);
ALTER TABLE pratiche.fase OWNER TO postgres;
ALTER TABLE ONLY pratiche.fase
ADD CONSTRAINT fase_pkey PRIMARY KEY (id);
ALTER TABLE ONLY pratiche.fase
ADD CONSTRAINT fk_fase_ufficio FOREIGN KEY (esclusivadaufficio) REFERENCES base.ufficio(id);

CREATE TABLE faseprocedimento (
  id bigserial NOT NULL,
  procedimento bigint,
  fase bigint,
  testo character varying(512),
  dascartare character varying(512),
  progressivo integer,
  confermabile boolean default false,
  confermata bigint,
  testoconfermata character varying(512),
  rifiutabile boolean default false,
  rifiutata bigint,
  testorifiutata character varying(512),
  condizione text,
  azione text,
  usoresponsabile boolean default false,
  cariche character varying(512)
) INHERITS (generale.withtimestamp);
ALTER TABLE procedimenti.faseprocedimento OWNER TO postgres;
ALTER TABLE ONLY faseprocedimento
ADD CONSTRAINT faseprocedimento_pkey PRIMARY KEY (id);
ALTER TABLE ONLY faseprocedimento
ADD CONSTRAINT fk_faseprocedimento_procedimento FOREIGN KEY (procedimento) REFERENCES procedimento(id);
ALTER TABLE ONLY faseprocedimento
ADD CONSTRAINT fk_faseprocedimento_fase FOREIGN KEY (fase) REFERENCES pratiche.fase(id);
ALTER TABLE ONLY faseprocedimento
ADD CONSTRAINT fk_faseprocedimento_confermata FOREIGN KEY (confermata) REFERENCES procedimenti.faseprocedimento(id);
ALTER TABLE ONLY faseprocedimento
ADD CONSTRAINT fk_faseprocedimento_rifiutata FOREIGN KEY (rifiutata) REFERENCES procedimenti.faseprocedimento(id);

CREATE TABLE ufficioprocedimento (
    id bigserial NOT NULL,
    procedimento bigint,
    ufficio bigint,
    principale boolean
) INHERITS (generale.withtimestamp);
ALTER TABLE procedimenti.ufficioprocedimento OWNER TO postgres;
ALTER TABLE ONLY ufficioprocedimento
    ADD CONSTRAINT ufficioprocedimento_pkey PRIMARY KEY (id);
ALTER TABLE ONLY ufficioprocedimento
    ADD CONSTRAINT fk_ufficioprocedimento_procedimento FOREIGN KEY (procedimento) REFERENCES procedimento(id);
ALTER TABLE ONLY ufficioprocedimento
    ADD CONSTRAINT fk_ufficioprocedimento_ufficio FOREIGN KEY (ufficio) REFERENCES base.ufficio(id);

CREATE TABLE ufficioutenteprocedimento (
    id bigserial NOT NULL,
    procedimento bigint,
    ufficioutente bigint,
    responsabile boolean,
    abilitato boolean,
    abituale boolean
) INHERITS (generale.withtimestamp);
ALTER TABLE procedimenti.ufficioutenteprocedimento OWNER TO postgres;
ALTER TABLE ONLY ufficioutenteprocedimento
    ADD CONSTRAINT ufficioutenteprocedimento_pkey PRIMARY KEY (id);
ALTER TABLE ONLY ufficioutenteprocedimento
    ADD CONSTRAINT fk_ufficioutenteprocedimento_procedimento FOREIGN KEY (procedimento) REFERENCES procedimento(id);
ALTER TABLE ONLY ufficioutenteprocedimento
    ADD CONSTRAINT fk_ufficioutenteprocedimento_ufficioutente FOREIGN KEY (ufficioutente) REFERENCES base.ufficioutente(id);
ALTER TABLE procedimenti.ufficioutenteprocedimento
  DROP CONSTRAINT fk_ufficioutenteprocedimento_ufficioutente;
ALTER TABLE procedimenti.ufficioutenteprocedimento
  ADD CONSTRAINT fk_ufficioutenteprocedimento_ufficioutente FOREIGN KEY  (ufficioutente) REFERENCES base.ufficioutente (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE CASCADE;

CREATE TABLE protocollo.fascicolo (
    id bigserial NOT NULL,
    categoria integer,
    classe integer,
    descrizione character varying(255),
    note character varying(2048),
    fascicolo integer
) INHERITS (generale.withtimestamp);
ALTER TABLE protocollo.fascicolo OWNER TO postgres;
ALTER TABLE ONLY protocollo.fascicolo
    ADD CONSTRAINT fascicolo_pkey PRIMARY KEY (id);

CREATE TABLE pratiche.tipopratica (
    id bigserial NOT NULL,
    codice character varying(10),
    descrizione character varying(255),
    tipopadre bigint,
    formulacodifica character varying(255),
    lunghezzaprogressivo integer,
    progressivoanno boolean NOT NULL DEFAULT false,
    progressivogiunta boolean NOT NULL DEFAULT false,
    fascicolo bigint,
    foglia boolean NOT NULL DEFAULT false,
    approvata boolean NOT NULL DEFAULT false,
    obsoleta boolean NOT NULL DEFAULT false
) INHERITS (generale.withtimestamp);
ALTER TABLE pratiche.tipopratica OWNER TO postgres;
ALTER TABLE ONLY pratiche.tipopratica
    ADD CONSTRAINT tipopratica_pkey PRIMARY KEY (id);
ALTER TABLE ONLY pratiche.tipopratica
    ADD CONSTRAINT fk_tipopratica_tipopadre FOREIGN KEY (tipopadre) REFERENCES pratiche.tipopratica(id);
ALTER TABLE ONLY pratiche.tipopratica
    ADD CONSTRAINT fk_tipopratica_fascicolo FOREIGN KEY (fascicolo) REFERENCES protocollo.fascicolo(id);

CREATE TABLE tipopraticaprocedimento (
    id bigserial NOT NULL,
    procedimento bigint,
    tipopratica bigint
) INHERITS (generale.withtimestamp);
ALTER TABLE procedimenti.tipopraticaprocedimento OWNER TO postgres;
ALTER TABLE ONLY tipopraticaprocedimento
    ADD CONSTRAINT tipopraticaprocedimento_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tipopraticaprocedimento
    ADD CONSTRAINT fk_tipopraticaprocedimento_procedimento FOREIGN KEY (procedimento) REFERENCES procedimento(id);
ALTER TABLE ONLY tipopraticaprocedimento
    ADD CONSTRAINT fk_tipopraticaprocedimento_tipopratica FOREIGN KEY (tipopratica) REFERENCES pratiche.tipopratica(id);

CREATE TABLE carica (
    id bigserial NOT NULL,
    descrizione character varying(1024),
    codicecarica character varying(255)
);
ALTER TABLE procedimenti.carica OWNER TO postgres;
ALTER TABLE ONLY carica
    ADD CONSTRAINT carica_pkey PRIMARY KEY (id);

CREATE TABLE delega (
    id bigserial NOT NULL,
    carica bigint,
    utente bigint,
    ufficio bigint,
    servizio bigint,
    procedimento bigint,
    inizio date,
    fine date,
    titolare boolean NOT NULL DEFAULT FALSE,
    segretario boolean NOT NULL DEFAULT FALSE,
    delegato boolean NOT NULL DEFAULT FALSE,
    suassenza boolean NOT NULL DEFAULT FALSE,
    delegante bigint
) INHERITS (generale.withtimestamp);
ALTER TABLE procedimenti.delega OWNER TO postgres;
ALTER TABLE ONLY delega
    ADD CONSTRAINT delega_pkey PRIMARY KEY (id);
ALTER TABLE ONLY delega
    ADD CONSTRAINT fk_delega_carica FOREIGN KEY (carica) REFERENCES procedimenti.carica(id);
ALTER TABLE ONLY delega
    ADD CONSTRAINT fk_delega_utente FOREIGN KEY (utente) REFERENCES base.utente(id);
ALTER TABLE ONLY delega
    ADD CONSTRAINT fk_delega_ufficio FOREIGN KEY (ufficio) REFERENCES base.ufficio(id);
ALTER TABLE ONLY delega
    ADD CONSTRAINT fk_delega_servizio FOREIGN KEY (servizio) REFERENCES finanziaria.servizio(id);
ALTER TABLE ONLY delega
    ADD CONSTRAINT fk_delega_procedimento FOREIGN KEY (procedimento) REFERENCES procedimenti.procedimento(id);
ALTER TABLE ONLY delega
    ADD CONSTRAINT fk_delega_utentedelegante FOREIGN KEY (delegante) REFERENCES base.utente(id);
ALTER TABLE procedimenti.delega
  ADD COLUMN impedimento boolean NOT NULL DEFAULT false;


-- Pratiche
SET search_path = pratiche, pg_catalog;

CREATE TABLE pratica (
    id bigserial NOT NULL,
    anno integer NOT NULL,
    datapratica date,
    idpratica character varying(9) NOT NULL,
    tipo bigint NOT NULL,
    codiceinterno character varying(50) NOT NULL,
    codiceaggiuntivo character varying(50),
    descrizione character varying(1024) NOT NULL,
    note character varying(6144),
    attribuzione bigint,
    gestione bigint,
    ubicazione bigint,
    dettaglioubicazione character varying(255),
    fase bigint,
    fascicolo bigint,
    riservata boolean NOT NULL DEFAULT FALSE,
    archiviata boolean NOT NULL DEFAULT FALSE,
    annoinventario integer,
    numeroinventario character varying(10),
    datachiusura date,
    datatermineistruttoria date,
    datascadenza date,
    procedimento bigint,
    codificaanomala boolean NOT NULL DEFAULT FALSE
) INHERITS (generale.withtimestamp);
ALTER TABLE pratiche.pratica OWNER TO postgres;
ALTER TABLE ONLY pratica
    ADD CONSTRAINT pratica_idpratica_key UNIQUE (idpratica);
ALTER TABLE ONLY pratica
    ADD CONSTRAINT pratica_codiceinterno_key UNIQUE (codiceinterno);
ALTER TABLE ONLY pratica
    ADD CONSTRAINT pratica_pkey PRIMARY KEY (id);
ALTER TABLE ONLY pratica
    ADD CONSTRAINT fk_pratica_attribuzione FOREIGN KEY (attribuzione) REFERENCES base.ufficio(id);
ALTER TABLE ONLY pratica
    ADD CONSTRAINT fk_pratica_gestione FOREIGN KEY (gestione) REFERENCES base.ufficio(id);
ALTER TABLE ONLY pratica
    ADD CONSTRAINT fk_pratica_ubicazione FOREIGN KEY (ubicazione) REFERENCES base.ufficio(id);
ALTER TABLE ONLY pratica
    ADD CONSTRAINT fk_pratica_tipopratica FOREIGN KEY (tipo) REFERENCES pratiche.tipopratica(id);
ALTER TABLE ONLY pratica
    ADD CONSTRAINT fk_pratica_fascicolo FOREIGN KEY (fascicolo) REFERENCES protocollo.fascicolo(id);
ALTER TABLE ONLY pratica
    ADD CONSTRAINT fk_pratica_fase FOREIGN KEY (fase) REFERENCES pratiche.fase(id);
ALTER TABLE ONLY pratica
    ADD CONSTRAINT fk_pratica_procedimento FOREIGN KEY (procedimento) REFERENCES procedimenti.procedimento(id);

CREATE TABLE dipendenza (
    id bigserial NOT NULL,
    descrizionedominante character varying(255),
    descrizionedipendente character varying(255)
) INHERITS (generale.withtimestamp);
ALTER TABLE pratiche.dipendenza OWNER TO postgres;
ALTER TABLE ONLY dipendenza
    ADD CONSTRAINT dipendenza_id_key UNIQUE (id);

CREATE TABLE zdipendenzapratica (
    id bigserial NOT NULL,
    praticadominante character varying(9),
    praticadipendente character varying(9),
    dipendenza bigint
) INHERITS (generale.withtimestamp);
ALTER TABLE pratiche.zdipendenzapratica OWNER TO postgres;
ALTER TABLE ONLY zdipendenzapratica
    ADD CONSTRAINT zdipendenzapratica_id_key UNIQUE (id);
ALTER TABLE ONLY zdipendenzapratica
    ADD CONSTRAINT fk_zdipendenzapratica_praticadominante FOREIGN KEY (praticadominante) REFERENCES pratiche.pratica(idpratica);
ALTER TABLE ONLY zdipendenzapratica
    ADD CONSTRAINT fk_zdipendenzapratica_praticadipendente FOREIGN KEY (praticadipendente) REFERENCES pratiche.pratica(idpratica);
ALTER TABLE ONLY zdipendenzapratica
    ADD CONSTRAINT fk_zdipendenzapratica_dipendenza FOREIGN KEY (dipendenza) REFERENCES pratiche.dipendenza(id);

-- la sequenza deve avere il nome senza la "z"
CREATE SEQUENCE pratiche.dipendenzapratica_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 2
  CACHE 1;
ALTER TABLE pratiche.dipendenzapratica_id_seq
  OWNER TO postgres;
ALTER TABLE pratiche.zdipendenzapratica ALTER COLUMN id SET DEFAULT nextval('pratiche.dipendenzapratica_id_seq'::regclass);
DROP SEQUENCE pratiche.zdipendenzapratica_id_seq;

-- la vista delle dipendenze è una union delle dipendenze reali "dritte" con quelle "girate"
-- tre regole gestiscono insert/update/delete dalla vista alla tabella reale.
CREATE OR REPLACE VIEW pratiche.dipendenzapratica AS
    SELECT zdipendenzapratica.id,
        zdipendenzapratica.praticadominante,
        zdipendenzapratica.dipendenza,
        zdipendenzapratica.praticadipendente,
        false AS invertita,
        rec_creato,
	    rec_creato_da,
	    rec_modificato,
	    rec_modificato_da
    FROM pratiche.zdipendenzapratica
UNION
    SELECT - zdipendenzapratica.id::integer AS id,
        zdipendenzapratica.praticadipendente AS praticadominante,
        zdipendenzapratica.dipendenza,
        zdipendenzapratica.praticadominante AS praticadipendente,
        true AS invertita,
        rec_creato,
	    rec_creato_da,
	    rec_modificato,
	    rec_modificato_da
    FROM pratiche.zdipendenzapratica;

CREATE RULE dipendenzapratica_delete AS ON DELETE TO dipendenzapratica DO INSTEAD
	DELETE FROM zdipendenzapratica
	WHERE zdipendenzapratica.id::integer = abs(old.id);

CREATE OR REPLACE RULE dipendenzapratica_insert AS
    ON INSERT TO pratiche.dipendenzapratica DO INSTEAD
        INSERT INTO pratiche.zdipendenzapratica (praticadominante, dipendenza, praticadipendente, rec_creato_da)
        SELECT
            CASE new.invertita
                WHEN true THEN new.praticadipendente
                ELSE new.praticadominante
            END AS praticadominante, new.dipendenza,
            CASE new.invertita
                WHEN true THEN new.praticadominante
                ELSE  new.praticadipendente
            END AS praticadipendente,
            new.rec_creato_da
    RETURNING zdipendenzapratica.id, zdipendenzapratica.praticadominante, zdipendenzapratica.dipendenza,
        zdipendenzapratica.praticadipendente, false AS bool, rec_creato, rec_creato_da, rec_modificato, rec_modificato_da;

CREATE OR REPLACE RULE dipendenzapratica_update AS
    ON UPDATE TO pratiche.dipendenzapratica DO INSTEAD nothing;

CREATE OR REPLACE RULE dipendenzapratica_update_dritta AS
    ON UPDATE TO pratiche.dipendenzapratica WHERE new.invertita = false DO
        UPDATE pratiche.zdipendenzapratica SET praticadominante = new.praticadominante, dipendenza = new.dipendenza,
            praticadipendente = new.praticadipendente, rec_modificato_da=new.rec_modificato_da
        WHERE zdipendenzapratica.id::integer = abs(old.id);

CREATE OR REPLACE RULE dipendenzapratica_update_invertita AS
    ON UPDATE TO pratiche.dipendenzapratica WHERE new.invertita DO
        UPDATE pratiche.zdipendenzapratica SET praticadominante = new.praticadipendente, dipendenza = new.dipendenza,
                praticadipendente = new.praticadominante, rec_modificato_da=new.rec_modificato_da
        WHERE zdipendenzapratica.id::integer = abs(old.id);

CREATE TABLE fasepratica (
  id bigserial NOT NULL,
  pratica bigint,
  fase bigint,
  testo character varying(512),
  dascartare character varying(512),
  progressivo integer,
  confermabile boolean default false,
  confermata bigint,
  testoconfermata character varying(512),
  rifiutabile boolean default false,
  rifiutata bigint,
  testorifiutata character varying(512),
  condizione text,
  azione text,
  completata boolean default false,
  negata boolean default false,
  attiva boolean default false,
  usoresponsabile boolean default false,
  cariche character varying(512)
) INHERITS (generale.withtimestamp);
ALTER TABLE pratiche.fasepratica OWNER TO postgres;
ALTER TABLE ONLY fasepratica
ADD CONSTRAINT fasepratica_pkey PRIMARY KEY (id);
ALTER TABLE ONLY fasepratica
ADD CONSTRAINT fk_fasepratica_pratica FOREIGN KEY (pratica) REFERENCES pratiche.pratica(id);
ALTER TABLE ONLY fasepratica
ADD CONSTRAINT fk_fasepratica_fase FOREIGN KEY (fase) REFERENCES pratiche.fase(id);
ALTER TABLE ONLY fasepratica
ADD CONSTRAINT fk_fasepratica_confermata FOREIGN KEY (confermata) REFERENCES pratiche.fasepratica(id);
ALTER TABLE ONLY fasepratica
ADD CONSTRAINT fk_fasepratica_rifiutata FOREIGN KEY (rifiutata) REFERENCES pratiche.fasepratica(id);

CREATE TABLE utentepratica (
  id bigserial NOT NULL,
  pratica character varying(9),
  utente bigint,
  responsabile boolean,
  istruttore boolean,
  dal date,
  al date,
  motivazione character varying(512)
) INHERITS (generale.withtimestamp);
ALTER TABLE pratiche.utentepratica OWNER TO postgres;
ALTER TABLE ONLY utentepratica
ADD CONSTRAINT utentepratica_pkey PRIMARY KEY (id);
ALTER TABLE ONLY utentepratica
ADD CONSTRAINT fk_utentepratica_pratica FOREIGN KEY (pratica) REFERENCES pratica(idpratica);
ALTER TABLE ONLY utentepratica
ADD CONSTRAINT fk_utentepratica_utente FOREIGN KEY (utente) REFERENCES base.utente(id);

CREATE TABLE visto (
  id bigserial NOT NULL,
  codicecarica character varying(255),
  pratica character varying(9),
  fase bigint,
  utente bigint,
  responsabile bigint,
  data  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  negato boolean default false,
  commento character varying
);
ALTER TABLE pratiche.visto
  ADD COLUMN completato boolean;
ALTER TABLE pratiche.visto OWNER TO postgres;
ALTER TABLE ONLY visto
ADD CONSTRAINT visto_pkey PRIMARY KEY (id);
ALTER TABLE ONLY visto
ADD CONSTRAINT fk_visto_pratica FOREIGN KEY (pratica) REFERENCES pratiche.pratica(idpratica);
ALTER TABLE ONLY visto
ADD CONSTRAINT fk_visto_fase FOREIGN KEY (fase) REFERENCES pratiche.fase(id);
ALTER TABLE ONLY visto
ADD CONSTRAINT fk_visto_utente FOREIGN KEY (utente) REFERENCES base.utente(id);
ALTER TABLE ONLY visto
ADD CONSTRAINT fk_visto_responsabile FOREIGN KEY (responsabile) REFERENCES base.utente(id);


-- Protocollo
SET search_path = protocollo, pg_catalog;

CREATE TABLE protocollo (
    id bigserial NOT NULL,
    tipo character varying(10) NOT NULL,
    anno integer NOT NULL,
    iddocumento character varying(12) NOT NULL,
    dataprotocollo timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sportello bigint NOT NULL,
    oggetto character varying(1024) NOT NULL,
    note character varying(1024),
    tiporiferimentomittente character varying(25),
    riferimentomittente character varying(255),
    datariferimentomittente date,
    riservato boolean,
    convalidaattribuzioni boolean NOT NULL DEFAULT FALSE,
    dataconvalidaattribuzioni timestamp,
    esecutoreconvalidaattribuzioni character varying(40),
    convalidaprotocollo boolean NOT NULL DEFAULT FALSE,
    numeroconvalidaprotocollo character varying(10),
    dataconvalidaprotocollo timestamp,
    esecutoreconvalidaprotocollo character varying(40),
    consolidadocumenti boolean NOT NULL DEFAULT FALSE,
    dataconsolidadocumenti timestamp,
    esecutoreconsolidadocumenti character varying(40),
    numeroricevuta character varying(10),
    dataricevuta timestamp,
    annullamentorichiesto boolean,
    annullato boolean,
    corrispostoostornato boolean,
    richiederisposta boolean,
    spedito boolean,
    dataspedizione timestamp,
    esecutorespedizione character varying(40),
    controlloreposta character varying(40),
    scansionemassiva boolean,
    fascicolo bigint,
    numeroatto integer,
    dataatto date
) INHERITS (generale.withtimestamp);
ALTER TABLE protocollo.protocollo OWNER TO postgres;
ALTER TABLE ONLY protocollo
    ADD CONSTRAINT protocollo_iddocumento_key UNIQUE (iddocumento);
ALTER TABLE ONLY protocollo
    ADD CONSTRAINT protocollo_pkey PRIMARY KEY (id);
ALTER TABLE ONLY protocollo
    ADD CONSTRAINT fk_protocollo_sportello FOREIGN KEY (sportello) REFERENCES base.ufficio(id);
ALTER TABLE ONLY protocollo
    ADD CONSTRAINT fk_protocollo_fascicolo FOREIGN KEY (fascicolo) REFERENCES protocollo.fascicolo(id);

CREATE TABLE protocollo.protocollomodificato
(
  id bigserial NOT NULL,
  tipo character varying(10) NOT NULL,
  anno integer NOT NULL,
  iddocumento character varying(12) NOT NULL,
  dataprotocollo timestamp without time zone NOT NULL DEFAULT now(),
  sportello bigint NOT NULL,
  oggetto character varying(1024) NOT NULL,
  note character varying(1024),
  tiporiferimentomittente character varying(25),
  riferimentomittente character varying(255),
  datariferimentomittente date,
  riservato boolean NOT NULL DEFAULT false,
  convalidaattribuzioni boolean NOT NULL DEFAULT false,
  dataconvalidaattribuzioni timestamp without time zone,
  esecutoreconvalidaattribuzioni character varying(40),
  convalidaprotocollo boolean NOT NULL DEFAULT false,
  numeroconvalidaprotocollo character varying(10),
  dataconvalidaprotocollo timestamp without time zone,
  esecutoreconvalidaprotocollo character varying(40),
  consolidadocumenti boolean NOT NULL DEFAULT false,
  dataconsolidadocumenti timestamp without time zone,
  esecutoreconsolidadocumenti character varying(40),
  numeroricevuta character varying(10),
  dataricevuta timestamp without time zone,
  annullamentorichiesto boolean NOT NULL DEFAULT false,
  annullato boolean NOT NULL DEFAULT false,
  corrispostoostornato boolean NOT NULL DEFAULT false,
  richiederisposta boolean NOT NULL DEFAULT false,
  spedito boolean NOT NULL DEFAULT false,
  dataspedizione timestamp without time zone,
  esecutorespedizione character varying(40),
  controlloreposta character varying(40),
  scansionemassiva boolean,
  fascicolo bigint,
  numeroatto integer,
  dataatto date,
  CONSTRAINT protocollomodificato_pkey PRIMARY KEY (id)
)
INHERITS (generale.withtimestamp)
WITH (
  OIDS=FALSE
);
ALTER TABLE protocollo.protocollomodificato
  OWNER TO postgres;
GRANT ALL ON TABLE protocollo.protocollomodificato TO postgres;

CREATE OR REPLACE FUNCTION protocollo.update_protocollo()
  RETURNS trigger AS
$BODY$begin
INSERT INTO protocollo.protocollomodificato (rec_creato, rec_creato_da, rec_modificato, rec_modificato_da, tipo, anno, iddocumento, dataprotocollo,
	sportello, oggetto, note, tiporiferimentomittente, riferimentomittente, datariferimentomittente, riservato, convalidaattribuzioni,
	dataconvalidaattribuzioni, esecutoreconvalidaattribuzioni, convalidaprotocollo, numeroconvalidaprotocollo, dataconvalidaprotocollo,
	esecutoreconvalidaprotocollo, consolidadocumenti, dataconsolidadocumenti, esecutoreconsolidadocumenti, numeroricevuta, dataricevuta,
	annullamentorichiesto, annullato, corrispostoostornato, richiederisposta, spedito, dataspedizione, esecutorespedizione, controlloreposta,
	scansionemassiva, fascicolo, numeroatto, dataatto)
VALUES (old.rec_creato, old.rec_creato_da, old.rec_modificato, old.rec_modificato_da, old.tipo, old.anno, old.iddocumento, old.dataprotocollo,
	old.sportello, old.oggetto, old.note, old.tiporiferimentomittente, old.riferimentomittente, old.datariferimentomittente, old.riservato,
	old.convalidaattribuzioni, old.dataconvalidaattribuzioni, old.esecutoreconvalidaattribuzioni, old.convalidaprotocollo,
	old.numeroconvalidaprotocollo, old.dataconvalidaprotocollo, old.esecutoreconvalidaprotocollo, old.consolidadocumenti,
	old.dataconsolidadocumenti, old.esecutoreconsolidadocumenti, old.numeroricevuta, old.dataricevuta, old.annullamentorichiesto, old.annullato,
	old.corrispostoostornato, old.richiederisposta, old.spedito, old.dataspedizione, old.esecutorespedizione, old.controlloreposta,
	old.scansionemassiva, old.fascicolo, old.numeroatto, old.dataatto);
return new;
end$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION protocollo.update_protocollo()
  OWNER TO postgres;

CREATE TRIGGER log_update_protocollo
  BEFORE UPDATE
  ON protocollo.protocollo
  FOR EACH ROW
  EXECUTE PROCEDURE protocollo.update_protocollo();

CREATE TABLE attribuzione (
    id bigserial NOT NULL,
    protocollo character varying(12) NOT NULL,
    ufficio bigint NOT NULL,
    dataattribuzioneprotocollo timestamp,
    letto boolean NOT NULL DEFAULT FALSE,
    dataletto timestamp,
    esecutoreletto character varying(40),
    principale boolean NOT NULL DEFAULT FALSE,
    evidenza character varying(1)
) INHERITS (generale.withtimestamp);
ALTER TABLE protocollo.attribuzione OWNER TO postgres;
ALTER TABLE ONLY attribuzione
    ADD CONSTRAINT attribuzione_pkey PRIMARY KEY (id);
ALTER TABLE ONLY attribuzione
    ADD CONSTRAINT fk_attribuzione_protocollo FOREIGN KEY (protocollo) REFERENCES protocollo(iddocumento);
ALTER TABLE ONLY attribuzione
    ADD CONSTRAINT fk_attribuzione_ufficio FOREIGN KEY (ufficio) REFERENCES base.ufficio(id);
ALTER TABLE protocollo.attribuzione
  ADD COLUMN dataprincipale timestamp without time zone;
ALTER TABLE protocollo.attribuzione
  ADD COLUMN esecutoreprincipale character varying(40);

CREATE OR REPLACE FUNCTION protocollo.update_attribuzioneprincipale()
  RETURNS trigger AS
$BODY$
DECLARE
	utente_sessione varchar;
	utente_appl varchar;
	postgres_user varchar;
begin
  INSERT INTO protocollo.attribuzionemodificata (rec_creato, rec_creato_da, rec_modificato, rec_modificato_da, attribuzione, protocollo, ufficio,
	dataattribuzioneprotocollo, letto, dataletto, esecutoreletto, principale, evidenza, dataprincipale, esecutoreprincipale)
  VALUES (old.rec_creato, old.rec_creato_da, old.rec_modificato, old.rec_modificato_da, old.id, old.protocollo, old.ufficio,
	old.dataattribuzioneprotocollo, old.letto, old.dataletto, old.esecutoreletto, old.principale, old.evidenza, old.dataprincipale,
	old.esecutoreprincipale);

  new.dataprincipale := 'now';
  SELECT usename, substring(application_name, 7) INTO postgres_user, utente_appl FROM pg_stat_activity WHERE pid=pg_backend_pid();
  SELECT utente INTO utente_sessione FROM generale.sessionigda WHERE pid=pg_backend_pid();
  SELECT CASE WHEN postgres_user='suitepa' THEN CASE WHEN utente_appl IS NOT NULL THEN utente_appl ELSE utente_sessione END ELSE postgres_user END
	INTO new.esecutoreprincipale;

  return new;
end$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION protocollo.update_attribuzioneprincipale()
  OWNER TO postgres;

CREATE TRIGGER log_update_attribuzioneprincipale
  BEFORE UPDATE OF principale
  ON protocollo.attribuzione
  FOR EACH ROW
  EXECUTE PROCEDURE protocollo.update_attribuzioneprincipale();

ALTER TABLE protocollo.attribuzione
    ADD COLUMN dataprincipale timestamp without time zone;
ALTER TABLE protocollo.attribuzione
    ADD COLUMN esecutoreprincipale character varying(40);

CREATE TABLE protocollo.attribuzionecancellata (
  id bigserial NOT NULL,
  protocollo character varying(12) NOT NULL,
  ufficio bigint NOT NULL,
  dataattribuzioneprotocollo timestamp without time zone,
  letto boolean NOT NULL DEFAULT false,
  dataletto timestamp without time zone,
  esecutoreletto character varying(40),
  principale boolean NOT NULL DEFAULT false,
  evidenza character varying(1),
  rec_cancellato timestamp without time zone NOT NULL
);
ALTER TABLE protocollo.attribuzionecancellata
  OWNER TO postgres;

ALTER TABLE protocollo.attribuzionecancellata
    ADD COLUMN rec_creato timestamp without time zone;
ALTER TABLE protocollo.attribuzionecancellata
    ADD COLUMN  rec_creato_da character varying(40);
ALTER TABLE protocollo.attribuzionecancellata
    ADD COLUMN  rec_modificato timestamp without time zone;
ALTER TABLE protocollo.attribuzionecancellata
    ADD COLUMN  rec_modificato_da character varying(40);
ALTER TABLE protocollo.attribuzionecancellata
    ADD COLUMN dataprincipale timestamp without time zone;
ALTER TABLE protocollo.attribuzionecancellata
    ADD COLUMN esecutoreprincipale character varying(40);
ALTER TABLE protocollo.attribuzionecancellata
    ADD COLUMN esecutorecancellato character varying(40);

CREATE OR REPLACE FUNCTION protocollo.delete_attribuzione()
  RETURNS trigger AS
$BODY$
DECLARE
	utente_sessione varchar;
	utente_appl varchar;
	postgres_user varchar;
begin
  SELECT usename, substring(application_name, 7) INTO postgres_user, utente_appl FROM pg_stat_activity WHERE pid=pg_backend_pid();
  SELECT utente INTO utente_sessione FROM generale.sessionigda WHERE pid=pg_backend_pid();
  INSERT INTO protocollo.attribuzionecancellata (protocollo, ufficio, dataattribuzioneprotocollo, letto, dataletto,
	esecutoreletto, principale, evidenza, rec_cancellato, rec_creato, rec_creato_da, rec_modificato, rec_modificato_da,
	dataprincipale, esecutoreprincipale, esecutorecancellato)
  VALUES (old.protocollo, old.ufficio, old.dataattribuzioneprotocollo, old.letto, old.dataletto, old.esecutoreletto,
	old.principale, old.evidenza, current_timestamp, old.rec_creato, old.rec_creato_da, old.rec_modificato, old.rec_modificato_da,
	old.dataprincipale, old.esecutoreprincipale,
	CASE WHEN postgres_user='suitepa' THEN CASE WHEN utente_appl IS NOT NULL THEN utente_appl ELSE utente_sessione END ELSE postgres_user END);
return old;
end$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION protocollo.delete_attribuzione()
  OWNER TO postgres;

CREATE TRIGGER trg_del_attribuzione
  BEFORE DELETE
  ON protocollo.attribuzione
  FOR EACH ROW
  EXECUTE PROCEDURE protocollo.delete_attribuzione();

CREATE TABLE protocollo.attribuzionemodificata
(
  id bigserial NOT NULL,
  attribuzione bigint NOT NULL,
  protocollo character varying(12) NOT NULL,
  ufficio bigint NOT NULL,
  dataattribuzioneprotocollo timestamp without time zone,
  letto boolean NOT NULL DEFAULT false,
  dataletto timestamp without time zone,
  esecutoreletto character varying(40),
  principale boolean NOT NULL DEFAULT false,
  evidenza character varying(1),
  dataprincipale timestamp without time zone,
  esecutoreprincipale character varying(40),
  CONSTRAINT attribuzionemodificata_pkey PRIMARY KEY (id),
  CONSTRAINT fk_attribuzionemodificata_protocollo FOREIGN KEY (protocollo)
      REFERENCES protocollo.protocollo (iddocumento) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_attribuzionemodificata_ufficio FOREIGN KEY (ufficio)
      REFERENCES base.ufficio (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
INHERITS (generale.withtimestamp)
WITH (
  OIDS=FALSE
);
ALTER TABLE protocollo.attribuzionemodificata
  OWNER TO postgres;

CREATE OR REPLACE FUNCTION protocollo.update_attribuzioneprincipale()
  RETURNS trigger AS
$BODY$begin
INSERT INTO protocollo.attribuzionemodificata (rec_creato, rec_creato_da, rec_modificato, rec_modificato_da, attribuzione, protocollo, ufficio,
	dataattribuzioneprotocollo, letto, dataletto, esecutoreletto, principale, evidenza, dataprincipale, esecutoreprincipale)
VALUES (old.rec_creato, old.rec_creato_da, old.rec_modificato, old.rec_modificato_da, old.id, old.protocollo, old.ufficio,
	old.dataattribuzioneprotocollo, old.letto, old.dataletto, old.esecutoreletto, old.principale, old.evidenza, old.dataprincipale,
	old.esecutoreprincipale);
return new;
end$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION protocollo.update_attribuzioneprincipale()
  OWNER TO postgres;

CREATE TRIGGER log_update_attribuzioneprincipale
  BEFORE UPDATE OF principale
  ON protocollo.attribuzione
  FOR EACH ROW
  EXECUTE PROCEDURE protocollo.update_attribuzioneprincipale();

CREATE TABLE oggetto (
    id bigserial NOT NULL,
    descrizione character varying(255)
) INHERITS (generale.withtimestamp);
ALTER TABLE protocollo.oggetto OWNER TO postgres;
ALTER TABLE ONLY oggetto
    ADD CONSTRAINT oggetto_pkey PRIMARY KEY (id);

CREATE TABLE praticaprotocollo (
    id bigserial NOT NULL,
    oggetto bigint,
    pratica character varying(9),
    protocollo character varying(12),
    originale boolean NOT NULL DEFAULT FALSE
) INHERITS (generale.withtimestamp);
ALTER TABLE protocollo.praticaprotocollo OWNER TO postgres;
ALTER TABLE ONLY praticaprotocollo
    ADD CONSTRAINT praticaprotocollo_pkey PRIMARY KEY (id);
ALTER TABLE ONLY praticaprotocollo
    ADD CONSTRAINT fk_praticaprotocollo_pratica FOREIGN KEY (pratica) REFERENCES pratiche.pratica(idpratica);
ALTER TABLE ONLY praticaprotocollo
    ADD CONSTRAINT fk_praticaprotocollo_protocollo FOREIGN KEY (protocollo) REFERENCES protocollo(iddocumento);
ALTER TABLE ONLY praticaprotocollo
    ADD CONSTRAINT fk_praticaprotocollo_oggetto FOREIGN KEY (oggetto) REFERENCES oggetto(id);

CREATE TABLE riferimentoprotocollo (
    id bigserial NOT NULL,
    precedente character varying(255),
    protocollo character varying(12)
) INHERITS (generale.withtimestamp);
ALTER TABLE protocollo.riferimentoprotocollo OWNER TO postgres;
ALTER TABLE ONLY riferimentoprotocollo
    ADD CONSTRAINT riferimentoprotocollo_pkey PRIMARY KEY (id);
ALTER TABLE ONLY riferimentoprotocollo
    ADD CONSTRAINT fk_riferimentoprotocollo_precedente FOREIGN KEY (precedente) REFERENCES protocollo(iddocumento);
ALTER TABLE ONLY riferimentoprotocollo
    ADD CONSTRAINT fk_riferimentoprotocollo_protocollo FOREIGN KEY (protocollo) REFERENCES protocollo(iddocumento);

CREATE TABLE titolo (
    id bigserial NOT NULL,
    descrizione character varying(255),
    tipo character varying(50)
) INHERITS (generale.withtimestamp);
ALTER TABLE protocollo.titolo OWNER TO postgres;
ALTER TABLE ONLY titolo
    ADD CONSTRAINT titolo_pkey PRIMARY KEY (id);

CREATE TABLE soggettoprotocollo (
    id bigserial NOT NULL,
    titolo bigint,
    protocollo character varying(12),
    soggetto bigint,
    corrispondenza boolean NOT NULL DEFAULT FALSE,
    notifica boolean NOT NULL DEFAULT FALSE,
    conoscenza boolean NOT NULL DEFAULT FALSE,
    primoinserimento boolean NOT NULL DEFAULT FALSE,
    datainizio date,
    datafine date,
    annullato boolean NOT NULL DEFAULT FALSE,
    principale boolean NOT NULL DEFAULT FALSE,
    soggettoreferente bigint,
    abilitatoweb boolean NOT NULL DEFAULT FALSE,
    note character varying(255) 
) INHERITS (generale.withtimestamp);
ALTER TABLE protocollo.soggettoprotocollo OWNER TO postgres;
ALTER TABLE ONLY soggettoprotocollo
    ADD CONSTRAINT soggettoprotocollo_pkey PRIMARY KEY (id);
ALTER TABLE ONLY soggettoprotocollo
    ADD CONSTRAINT fk_soggettoprotocollo_protocollo FOREIGN KEY (protocollo) REFERENCES protocollo(iddocumento);
ALTER TABLE ONLY soggettoprotocollo
    ADD CONSTRAINT fk_soggettoprotocollo_soggetto FOREIGN KEY (soggetto) REFERENCES anagrafiche.soggetto(id);
ALTER TABLE ONLY soggettoprotocollo
    ADD CONSTRAINT fk_soggettoprotocollo_referente FOREIGN KEY (soggettoreferente) REFERENCES anagrafiche.soggetto(id);
ALTER TABLE ONLY soggettoprotocollo
    ADD CONSTRAINT fk_soggettoprotocollo_titolo FOREIGN KEY (titolo) REFERENCES titolo(id);

ALTER TABLE protocollo.soggettoprotocollo
  ADD COLUMN pec boolean NOT NULL DEFAULT false;
ALTER TABLE protocollo.soggettoprotocollo
  ADD COLUMN indirizzo character varying;
ALTER TABLE protocollo.soggettoprotocollo
  ADD COLUMN messaggiopec bigint;

-- i soggetti di primo inserimento vengono solo annullati
CREATE OR REPLACE FUNCTION protocollo.delete_soggettoprotocollo()
  RETURNS trigger AS
$BODY$
begin
  if old.primoinserimento IS TRUE AND old.annullato IS NOT TRUE THEN
     UPDATE protocollo.soggettoprotocollo SET annullato = TRUE WHERE id = old.id;
     return NULL;
  end if;
  return old;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION protocollo.delete_soggettoprotocollo() OWNER TO postgres;
CREATE TRIGGER trg_del_soggettoprotocollo
  BEFORE DELETE
  ON protocollo.soggettoprotocollo
  FOR EACH ROW
  EXECUTE PROCEDURE protocollo.delete_soggettoprotocollo();

CREATE TRIGGER update_statopecprotocollo
  AFTER UPDATE OF messaggiopec
  ON protocollo.soggettoprotocollo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_statopecprotocollo();

CREATE TABLE soggettoriservatoprotocollo (
    id bigserial NOT NULL,
    titolo bigint,
    protocollo character varying(12),
    soggetto bigint,
    corrispondenza boolean NOT NULL DEFAULT FALSE,
    notifica boolean NOT NULL DEFAULT FALSE,
    conoscenza boolean NOT NULL DEFAULT FALSE,
    primoinserimento boolean NOT NULL DEFAULT FALSE,
    datainizio date,
    datafine date,
    annullato boolean NOT NULL DEFAULT FALSE,
    principale boolean NOT NULL DEFAULT FALSE,
    soggettoreferente bigint,
    abilitatoweb boolean NOT NULL DEFAULT FALSE,
    note character varying(255) 
) INHERITS (generale.withtimestamp);
ALTER TABLE protocollo.soggettoriservatoprotocollo OWNER TO postgres;
ALTER TABLE ONLY soggettoriservatoprotocollo
    ADD CONSTRAINT soggettoriservatoprotocollo_pkey PRIMARY KEY (id);
ALTER TABLE ONLY soggettoriservatoprotocollo
    ADD CONSTRAINT fk_soggettoriservatoprotocollo_protocollo FOREIGN KEY (protocollo) REFERENCES protocollo(iddocumento);
ALTER TABLE ONLY soggettoriservatoprotocollo
    ADD CONSTRAINT fk_soggettoriservatoprotocollo_soggetto FOREIGN KEY (soggetto) REFERENCES anagrafiche.soggetto(id);
ALTER TABLE ONLY soggettoriservatoprotocollo
    ADD CONSTRAINT fk_soggettoriservatoprotocollo_titolo FOREIGN KEY (titolo) REFERENCES titolo(id);

ALTER TABLE protocollo.soggettoriservatoprotocollo
  ADD COLUMN pec boolean NOT NULL DEFAULT false;
ALTER TABLE protocollo.soggettoriservatoprotocollo
  ADD COLUMN indirizzo bigint;
ALTER TABLE protocollo.soggettoriservatoprotocollo
  ADD COLUMN messaggiopec bigint;

CREATE TRIGGER update_statopecprotocollo
  AFTER UPDATE OF messaggiopec
  ON protocollo.soggettoriservatoprotocollo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_statopecprotocollo();

-- i soggetti riservati di primo inserimento vengono solo annullati
CREATE OR REPLACE FUNCTION protocollo.delete_soggettoriservatoprotocollo()
  RETURNS trigger AS
$BODY$
begin
  if old.primoinserimento IS TRUE AND old.annullato IS NOT TRUE THEN
     UPDATE protocollo.soggettoriservatoprotocollo SET annullato = TRUE WHERE id = old.id;
     return NULL;
  end if;
  return old;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION protocollo.delete_soggettoriservatoprotocollo() OWNER TO postgres;
CREATE TRIGGER trg_del_soggettoriservatoprotocollo
  BEFORE DELETE
  ON protocollo.soggettoriservatoprotocollo
  FOR EACH ROW
  EXECUTE PROCEDURE protocollo.delete_soggettoriservatoprotocollo();

CREATE TABLE ufficioprotocollo (
    id bigserial NOT NULL,
    protocollo character varying(12),
    ufficio bigint
) INHERITS (generale.withtimestamp);
ALTER TABLE protocollo.ufficioprotocollo OWNER TO postgres;
ALTER TABLE ONLY ufficioprotocollo
    ADD CONSTRAINT ufficioprotocollo_pkey PRIMARY KEY (id);
ALTER TABLE ONLY ufficioprotocollo
    ADD CONSTRAINT fk_ufficioprotocollo_protocollo FOREIGN KEY (protocollo) REFERENCES protocollo(iddocumento);
ALTER TABLE ONLY ufficioprotocollo
    ADD CONSTRAINT fk_ufficioprotocollo_ufficio FOREIGN KEY (ufficio) REFERENCES base.ufficio(id);

CREATE TABLE motivazioneannullamento (
       id bigserial NOT NULL,
       descrizione character varying(100)
);
ALTER TABLE protocollo.motivazioneannullamento OWNER TO postgres;
ALTER TABLE ONLY motivazioneannullamento
    ADD CONSTRAINT motivazioneannullamento_pkey PRIMARY KEY (id);

CREATE TABLE annullamentoprotocollo (
       id bigserial NOT NULL,
       protocollo character varying(12) NOT NULL,
       messaggio bigint,
       datarichiesta timestamp NOT NULL,
       esecutorerichiesta character varying(40) NOT NULL,      
       motivazioneannullamento bigint NOT NULL,
       dataautorizzazione timestamp,
       esecutoreautorizzazione character varying(40),
       respinto boolean NOT NULL DEFAULT False,
       autorizzato boolean NOT NULL DEFAULT False
);
ALTER TABLE protocollo.annullamentoprotocollo OWNER TO postgres;
ALTER TABLE ONLY annullamentoprotocollo
    ADD CONSTRAINT annullamentoprotocollo_pkey PRIMARY KEY (id);
ALTER TABLE ONLY annullamentoprotocollo
    ADD CONSTRAINT fk_annullamentoprotocollo_protocollo FOREIGN KEY (protocollo) REFERENCES protocollo(iddocumento);
ALTER TABLE ONLY annullamentoprotocollo
    ADD CONSTRAINT fk_annullamentoprotocollo_motivazioneannullamento FOREIGN KEY (motivazioneannullamento) REFERENCES motivazioneannullamento(id);

CREATE TABLE protocollo.mailbox (
  id bigserial NOT NULL,
  descrizione character varying(255),
  host character varying(255),
  username character varying(255),
  password character varying(255),
  inbox character varying(255)
);
ALTER TABLE protocollo.mailbox OWNER TO postgres;
ALTER TABLE ONLY mailbox
ADD CONSTRAINT mailbox_pkey PRIMARY KEY (id);

CREATE TABLE protocollo.pecprotocollo
(
  id bigserial NOT NULL,
  protocollo character varying(12),
  body character varying,
  segnatura character varying,
  CONSTRAINT pecprotocollo_pkey PRIMARY KEY (id),
  CONSTRAINT fk_pecprotocollo_protocollo FOREIGN KEY (protocollo)
      REFERENCES protocollo.protocollo (iddocumento) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
INHERITS (generale.withtimestamp);
ALTER TABLE protocollo.pecprotocollo OWNER TO postgres;
ALTER TABLE protocollo.pecprotocollo
  ADD COLUMN stato character varying;

CREATE TABLE protocollo.tiporiferimentomittente
(
  id serial NOT NULL,
  descrizione character varying,
  CONSTRAINT tiporiferimentomittente_pkey PRIMARY KEY (id)
);
ALTER TABLE protocollo.tiporiferimentomittente OWNER TO postgres;


-- Pubblicazioni
SET search_path = pubblicazioni, pg_catalog;

CREATE OR REPLACE FUNCTION pubblicazioni.update_data_pubblicazione()
  RETURNS trigger AS
$BODY$begin
  new.datapubblicazione := 'now';
  return new;
end$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION pubblicazioni.update_data_pubblicazione()
  OWNER TO postgres;

CREATE TABLE pubblicazione (
    id bigserial NOT NULL,
    protocollo character varying(12),
    descrizione character varying(2048),
    richiedente character varying(255),
    organo character varying(255),
    datapubblicazione date,
    durataconsultazione integer,
    tipoattopubblicazione bigint NOT NULL,
    pubblicato boolean,
    dataatto date,
    numeroatto integer
) INHERITS (generale.withtimestamp);
ALTER TABLE pubblicazioni.pubblicazione OWNER TO postgres;
ALTER TABLE ONLY pubblicazione
    ADD CONSTRAINT pubblicazione_pkey PRIMARY KEY (id);
ALTER TABLE ONLY pubblicazione
ADD CONSTRAINT fk_pubblicazione_protocollo FOREIGN KEY (protocollo) REFERENCES protocollo.protocollo(iddocumento);
ALTER TABLE pubblicazioni.pubblicazione
  ADD COLUMN organo character varying(255);
ALTER TABLE pubblicazioni.pubblicazione
  ADD COLUMN esecutorepubblicazione character varying(40);
CREATE TRIGGER data_pubblicazione
  BEFORE UPDATE OF pubblicato
  ON pubblicazioni.pubblicazione
  FOR EACH ROW
  EXECUTE PROCEDURE pubblicazioni.update_data_pubblicazione();



CREATE TABLE tipoattopubblicazione (
  id bigserial NOT NULL,
  chiave character varying(255),
  descrizione character varying(255)
);
ALTER TABLE pubblicazioni.tipoattopubblicazione OWNER TO postgres;
ALTER TABLE ONLY tipoattopubblicazione
ADD CONSTRAINT tipoattopubblicazione_pkey PRIMARY KEY (id);


-- Sedute
SET search_path = sedute, pg_catalog;

CREATE TABLE commissione (
    id bigserial NOT NULL,
    descrizione character varying(1024)
);
ALTER TABLE sedute.commissione OWNER TO postgres;
ALTER TABLE ONLY commissione
    ADD CONSTRAINT commissione_pkey PRIMARY KEY (id);

CREATE TABLE tiposeduta (
    id bigserial NOT NULL,
    descrizione character varying(1024),
    commissione bigint,
    tipopratica bigint
);
ALTER TABLE sedute.tiposeduta OWNER TO postgres;
ALTER TABLE ONLY tiposeduta
    ADD CONSTRAINT tiposeduta_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tiposeduta
    ADD CONSTRAINT fk_tiposeduta_commissione FOREIGN KEY (commissione) REFERENCES sedute.commissione(id);
ALTER TABLE ONLY tiposeduta
    ADD CONSTRAINT fk_tiposeduta_tipopratica FOREIGN KEY (tipopratica) REFERENCES pratiche.tipopratica(id);

CREATE TABLE caricacommissione (
    id bigserial NOT NULL,
    carica bigint,
    commissione bigint
);
ALTER TABLE sedute.caricacommissione OWNER TO postgres;
ALTER TABLE ONLY caricacommissione
    ADD CONSTRAINT caricacommissione_pkey PRIMARY KEY (id);
ALTER TABLE ONLY caricacommissione
    ADD CONSTRAINT fk_caricacommissione_carica FOREIGN KEY (carica) REFERENCES procedimenti.carica(id);
ALTER TABLE ONLY caricacommissione
    ADD CONSTRAINT fk_caricacommissione_commissione FOREIGN KEY (commissione) REFERENCES sedute.commissione(id);

CREATE TABLE seduta (
    id bigserial NOT NULL,
    datacreazione date,
    tiposeduta bigint,
    dataoraconvocazione timestamp,
    faseseduta character varying(255),
    statoseduta character varying(255),
    inizioseduta timestamp,
    cambiostatoseduta timestamp,
    fineseduta timestamp
);
ALTER TABLE sedute.seduta OWNER TO postgres;
ALTER TABLE ONLY seduta
    ADD CONSTRAINT seduta_pkey PRIMARY KEY (id);
ALTER TABLE ONLY seduta
    ADD CONSTRAINT fk_seduta_tiposeduta FOREIGN KEY (tiposeduta) REFERENCES sedute.tiposeduta(id);
    

-- Determine
SET search_path = deliberedetermine, pg_catalog;

CREATE TABLE determina (
    id bigserial NOT NULL,
    idpratica character varying(10),
    codiceinterno character varying(50),
    oggetto character varying(1024),
    dientrata boolean NOT NULL DEFAULT FALSE,
    dispesa boolean NOT NULL DEFAULT FALSE,
    spesaimpegnoesistente boolean NOT NULL DEFAULT FALSE,
    diregolarizzazione boolean NOT NULL DEFAULT FALSE,
    diliquidazione boolean NOT NULL DEFAULT FALSE,
    diincarico boolean NOT NULL DEFAULT FALSE,
    anno integer,
    numero integer,
    data date,
    referentepolitico character varying(255),
    responsabile bigint,
    protocollo character varying(12),
    pubblicabile character varying,
    pluriennale boolean,
    finoadanno int,
    progetto bigint,
    responsabileprocedimento bigint,
    benioservizi boolean NOT NULL DEFAULT FALSE,
    convenzioneattiva boolean NOT NULL DEFAULT FALSE,
    noadesioneconvenzione boolean NOT NULL DEFAULT FALSE,
    cpvpresente boolean NOT NULL DEFAULT FALSE,
    cpvnonfruibile boolean NOT NULL DEFAULT FALSE,
    convenzione character varying,
    mercato character varying,
    oggettoconvenzione character varying,
    bando character varying,
    cpv character varying,
    motivoanomalia character varying
) INHERITS (generale.withtimestamp);
ALTER TABLE deliberedetermine.determina OWNER TO postgres;
ALTER TABLE ONLY determina
    ADD CONSTRAINT determina_pkey PRIMARY KEY (id);
ALTER TABLE ONLY determina
    ADD CONSTRAINT determina_idpratica_key UNIQUE (idpratica);
ALTER TABLE ONLY determina
    ADD CONSTRAINT fk_determina_pratica FOREIGN KEY (idpratica) REFERENCES pratiche.pratica(idpratica);
ALTER TABLE ONLY determina
    ADD CONSTRAINT fk_determina_responsabile FOREIGN KEY (responsabile) REFERENCES base.utente(id);
ALTER TABLE ONLY determina
      ADD CONSTRAINT fk_determina_protocollo FOREIGN KEY (protocollo) REFERENCES protocollo.protocollo(iddocumento);
ALTER TABLE ONLY determina
      ADD CONSTRAINT fk_determina_progetto FOREIGN KEY (progetto) REFERENCES finanziaria.progetto(id);
ALTER TABLE ONLY determina
    ADD CONSTRAINT fk_determina_responsabileprocedimento FOREIGN KEY (responsabileprocedimento) REFERENCES anagrafiche.soggetto(id);
ALTER TABLE deliberedetermine.determina
  ADD COLUMN incompatibilitaresponsabile boolean NOT NULL DEFAULT false;
ALTER TABLE deliberedetermine.determina
  ADD COLUMN incompatibilitadelegato bigint;

CREATE TABLE serviziodetermina (
    id bigserial NOT NULL,
    determina bigint,
    idpratica character varying(10),
    servizio bigint NOT NULL,
    principale boolean NOT NULL DEFAULT false
) INHERITS (generale.withtimestamp);
ALTER TABLE deliberedetermine.serviziodetermina OWNER TO postgres;
ALTER TABLE ONLY serviziodetermina
    ADD CONSTRAINT serviziodetermina_pkey PRIMARY KEY (id);
ALTER TABLE ONLY serviziodetermina
    ADD CONSTRAINT fk_serviziodetermina_determina FOREIGN KEY (determina) REFERENCES deliberedetermine.determina(id);
ALTER TABLE ONLY serviziodetermina
    ADD CONSTRAINT fk_serviziodetermina_servizio FOREIGN KEY (servizio) REFERENCES finanziaria.servizio(id);

CREATE TABLE ufficiodetermina (
    id bigserial NOT NULL,
    determina bigint,
    idpratica character varying(10),
    ufficio bigint NOT NULL,
    principale boolean NOT NULL DEFAULT false
) INHERITS (generale.withtimestamp);
ALTER TABLE deliberedetermine.ufficiodetermina OWNER TO postgres;
ALTER TABLE ONLY ufficiodetermina
    ADD CONSTRAINT ufficiodetermina_pkey PRIMARY KEY (id);
ALTER TABLE ONLY ufficiodetermina
    ADD CONSTRAINT fk_ufficiodetermina_determina FOREIGN KEY (determina) REFERENCES deliberedetermine.determina(id);
ALTER TABLE ONLY ufficiodetermina
    ADD CONSTRAINT fk_ufficiodetermina_ufficio FOREIGN KEY (ufficio) REFERENCES base.ufficio(id);

CREATE TABLE movimentodetermina (
    id bigserial NOT NULL,
    determina bigint,
    archivio character(1),
    eu character(1),
    capitolo bigint,
    articolo character varying(255),
    codicemeccanografico character varying(255),
    impegno character varying(255),
    sottoimpegno character varying(255),
    descrizioneimpegno character varying(255),
    annoimpegno bigint,
    importo numeric(15,2),
    importoimpegnoaccertamento numeric(15,2),
    tipomovimento character varying(255),
    annoesercizio bigint,
    codicebeneficiario bigint,
    descrizionebeneficiario character varying(255),
    codicecup character varying(15),
    codicecig character varying(40),
    cespite  character varying(20),
    descrizionecespite character varying(255),
    modalitaindividuazionebeneficiario character varying(50),
    normatitoloattribuzione character varying(50),
    albobeneficiari character varying(50)
) INHERITS (generale.withtimestamp);
ALTER TABLE deliberedetermine.movimentodetermina OWNER TO postgres;
ALTER TABLE ONLY movimentodetermina
    ADD CONSTRAINT movimentodetermina_pkey PRIMARY KEY (id);
ALTER TABLE ONLY movimentodetermina
    ADD CONSTRAINT fk_movimentodetermina_determina FOREIGN KEY (determina) REFERENCES determina(id);
ALTER TABLE ONLY movimentodetermina
    ADD CONSTRAINT fk_movimentodetermina_capitolo FOREIGN KEY (capitolo) REFERENCES finanziaria.capitolo(id);

CREATE TABLE spesaimpegnoesistente (
    id bigserial NOT NULL,
    determina bigint,
    capitolo integer,
    impegno character varying(255),
    sottoimpegno character varying(255),
    annoimpegno integer,
    importo numeric(15,2),
    annoesercizio integer,
    codicebeneficiario bigint,
    descrizionebeneficiario character varying(255),
    codicecup character varying(15),
    codicecig character varying(40),
    cespite  character varying(20),
    descrizionecespite character varying(255)
) INHERITS (generale.withtimestamp);
ALTER TABLE deliberedetermine.spesaimpegnoesistente OWNER TO postgres;
ALTER TABLE ONLY spesaimpegnoesistente
    ADD CONSTRAINT spesaimpegnoesistente_pkey PRIMARY KEY (id);
ALTER TABLE ONLY spesaimpegnoesistente
    ADD CONSTRAINT fk_spesaimpegnoesistente_determina FOREIGN KEY (determina) REFERENCES determina(id);


-- modelli
SET search_path = modelli, pg_catalog;

CREATE TABLE modello (
  id bigserial NOT NULL,
  titolo character varying(255),
  descrizione character varying(1024),
  uri character varying(2048),
  modellopadre bigint,
  protocollabile boolean NOT NULL DEFAULT false
);
ALTER TABLE modelli.modello OWNER TO postgres;
ALTER TABLE ONLY modello
ADD CONSTRAINT modello_pkey PRIMARY KEY (id);
ALTER TABLE ONLY modello
ADD CONSTRAINT fk_modello_modellopadre FOREIGN KEY (modellopadre) REFERENCES modelli.modello(id);

CREATE TABLE tipopraticamodello (
  id bigserial not null,
  tipopratica bigint,
  modello bigint
);
ALTER TABLE modelli.tipopraticamodello OWNER TO postgres;
ALTER TABLE ONLY tipopraticamodello
ADD CONSTRAINT tipopraticamodello_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tipopraticamodello
ADD CONSTRAINT fk_tipopraticamodello_tipopratica FOREIGN KEY (tipopratica) REFERENCES pratiche.tipopratica(id);
ALTER TABLE ONLY tipopraticamodello
ADD CONSTRAINT fk_tipopraticamodello_modello FOREIGN KEY (modello) REFERENCES modelli.modello(id);

CREATE TABLE procedimentomodello (
  id bigserial not null,
  procedimento bigint,
  modello bigint
);
ALTER TABLE modelli.procedimentomodello OWNER TO postgres;
ALTER TABLE ONLY procedimentomodello
ADD CONSTRAINT procedimentomodello_pkey PRIMARY KEY (id);
ALTER TABLE ONLY procedimentomodello
ADD CONSTRAINT fk_procedimentomodello_procedimento FOREIGN KEY (procedimento) REFERENCES procedimenti.procedimento(id);
ALTER TABLE ONLY procedimentomodello
ADD CONSTRAINT fk_procedimentomodello_modello FOREIGN KEY (modello) REFERENCES modelli.modello(id);

CREATE TABLE segnalibro (
  id bigserial not null,
  segnalibro character varying(255),
  codice text,
  modello bigint,
  layout character varying(32)
);
ALTER TABLE modelli.segnalibro OWNER TO postgres;
ALTER TABLE ONLY segnalibro
ADD CONSTRAINT segnalibro_pkey PRIMARY KEY (id);
ALTER TABLE ONLY segnalibro
ADD CONSTRAINT fk_segnalibro_modello FOREIGN KEY (modello) REFERENCES modelli.modello(id);


-- messaggi
SET search_path = richieste, pg_catalog;

CREATE TABLE richieste.richiesta
(
  id bigserial NOT NULL,
  data timestamp NOT NULL DEFAULT now(),
  testo character varying NOT NULL,
  mittente bigint NOT NULL,
  cancellabile boolean NOT NULL DEFAULT true,
  datascadenza timestamp,
  giornipreavviso integer,
  richiestaprecedente bigint,
  relazione integer,
  richiestaautomatica boolean NOT NULL DEFAULT true,
  fase integer,
  statorichiesta character varying,
  CONSTRAINT richiesta_pkey PRIMARY KEY (id),
  CONSTRAINT fk_richiesta_mittente FOREIGN KEY (mittente)
      REFERENCES base.utente (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_richiesta_richiestaprecedente FOREIGN KEY (richiestaprecedente)
      REFERENCES richieste.richiesta (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
) INHERITS (generale.withtimestamp);
ALTER TABLE richieste.richiesta
  OWNER TO postgres;
CREATE TRIGGER trg_ins_ts_richiesta
  BEFORE INSERT
  ON richieste.richiesta
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_richiesta
  BEFORE UPDATE
  ON richieste.richiesta
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

CREATE TABLE richieste.destinatarioutente
(
  id bigserial NOT NULL,
  richiesta bigint NOT NULL,
  destinatario bigint NOT NULL,
  conoscenza boolean NOT NULL DEFAULT false,
  letto boolean NOT NULL DEFAULT false,
  dataletto timestamp without time zone,
  esecutoreletto character varying(40),
  richiestacancellabile boolean NOT NULL,
  CONSTRAINT destinatarioutente_pkey PRIMARY KEY (id)
) INHERITS (generale.withtimestamp);;
ALTER TABLE richieste.destinatarioutente
  OWNER TO postgres;
ALTER TABLE ONLY richieste.destinatarioutente
    ADD CONSTRAINT fk_destinatarioutente_richiesta FOREIGN KEY (richiesta) REFERENCES richieste.richiesta(id);
ALTER TABLE ONLY richieste.destinatarioutente
    ADD CONSTRAINT fk_destinatarioutente_destinatario FOREIGN KEY (destinatario) REFERENCES base.utente(id);
CREATE TRIGGER trg_ins_ts_destinatarioutente
  BEFORE INSERT
  ON richieste.destinatarioutente
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_destinatarioutente
  BEFORE UPDATE
  ON richieste.destinatarioutente
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

CREATE TABLE richieste.destinatarioufficio
(
  id bigserial NOT NULL,
  richiesta bigint NOT NULL,
  destinatario bigint NOT NULL,
  assegnatario bigint NOT NULL,
  conoscenza boolean NOT NULL DEFAULT false,
  letto boolean NOT NULL DEFAULT false,
  dataletto timestamp without time zone,
  esecutoreletto character varying(40),
  richiestacancellabile boolean NOT NULL,
  CONSTRAINT destinatarioufficio_pkey PRIMARY KEY (id)
) INHERITS (generale.withtimestamp);;
ALTER TABLE richieste.destinatarioufficio
  OWNER TO postgres;
ALTER TABLE ONLY richieste.destinatarioufficio
    ADD CONSTRAINT fk_destinatarioufficio_richiesta FOREIGN KEY (richiesta) REFERENCES richieste.richiesta(id);
ALTER TABLE ONLY richieste.destinatarioufficio
    ADD CONSTRAINT fk_destinatarioufficio_destinatario FOREIGN KEY (destinatario) REFERENCES base.ufficio(id);
ALTER TABLE ONLY richieste.destinatarioufficio
    ADD CONSTRAINT fk_destinatarioufficio_assegnatario FOREIGN KEY (assegnatario) REFERENCES base.utente(id);
CREATE TRIGGER trg_ins_ts_destinatarioufficio
  BEFORE INSERT
  ON richieste.destinatarioufficio
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_destinatarioufficio
  BEFORE UPDATE
  ON richieste.destinatarioufficio
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

/*
CREATE TABLE messaggi.allegato
(
  id bigserial NOT NULL,
  messaggio bigint NOT NULL,
  titolo character varying NOT NULL,
  percorso character varying NOT NULL,
  estensione character varying(10) NOT NULL,
  CONSTRAINT allegato_pkey PRIMARY KEY (id)
);
ALTER TABLE messaggi.allegato
  OWNER TO postgres;
ALTER TABLE ONLY messaggi.allegato
    ADD CONSTRAINT fk_allegato_messaggio FOREIGN KEY (messaggio) REFERENCES messaggi.messaggio(id);
*/

CREATE TABLE richieste.richiestaprotocollo
(
  id bigserial NOT NULL,
  richiesta bigint NOT NULL,
  protocollo character varying(12) NOT NULL,
  oggetto bigint,
  CONSTRAINT richiestaprotocollo_pkey PRIMARY KEY (id),
  CONSTRAINT fk_richiestaprotocollo_oggetto FOREIGN KEY (oggetto)
      REFERENCES protocollo.oggetto (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_richiestaprotocollo_protocollo FOREIGN KEY (protocollo)
      REFERENCES protocollo.protocollo (iddocumento) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_richiestaprotocollo_richiesta FOREIGN KEY (richiesta)
      REFERENCES richieste.richiesta (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
) INHERITS (generale.withtimestamp);
ALTER TABLE richieste.richiestaprotocollo
  OWNER TO postgres;
CREATE TRIGGER trg_ins_ts_richiestaprotocollo
  BEFORE INSERT
  ON richieste.richiestaprotocollo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_richiestaprotocollo
  BEFORE UPDATE
  ON richieste.richiestaprotocollo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

CREATE TABLE richieste.richiestapratica
(
  id bigserial NOT NULL,
  richiesta bigint NOT NULL,
  pratica character varying(12) NOT NULL,
  oggetto bigint,
  CONSTRAINT richiestapratica_pkey PRIMARY KEY (id),
  CONSTRAINT fk_richiestapratica_oggetto FOREIGN KEY (oggetto)
      REFERENCES protocollo.oggetto (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_richiestapratica_pratica FOREIGN KEY (pratica)
      REFERENCES pratiche.pratica (idpratica) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_richiestapratica_richiesta FOREIGN KEY (richiesta)
      REFERENCES richieste.richiesta (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
) INHERITS (generale.withtimestamp);
ALTER TABLE richieste.richiestapratica
  OWNER TO postgres;
CREATE TRIGGER trg_ins_ts_richiestapratica
  BEFORE INSERT
  ON richieste.richiestapratica
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_richiestapratica
  BEFORE UPDATE
  ON richieste.richiestapratica
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

CREATE TABLE richieste.vistoindividuale
(
  id bigserial NOT NULL,
  richiesta bigint NOT NULL,
  destinatario bigint NOT NULL,
  dataletto timestamp without time zone,
  esecutoreletto character varying(40),
  CONSTRAINT vistoindividuale_pkey PRIMARY KEY (id),
  CONSTRAINT fk_vistoindividuale_richiesta FOREIGN KEY (richiesta)
      REFERENCES richieste.richiesta (id)
) ;
ALTER TABLE richieste.vistoindividuale
  OWNER TO postgres;


-- Ticket
SET search_path = urp, pg_catalog;

CREATE TABLE sportello (
    id BIGSERIAL NOT NULL,
    descrizione CHARACTER VARYING(255),
    attivo boolean NOT NULL DEFAULT false,
    utente bigint
);
ALTER TABLE urp.sportello OWNER TO postgres;
ALTER TABLE ONLY sportello
ADD CONSTRAINT sportello_pkey PRIMARY KEY (id);
ALTER TABLE ONLY sportello
ADD CONSTRAINT fk_sportello_utente FOREIGN KEY (utente) REFERENCES base.utente(id);

CREATE TABLE servizioalcittadino (
    id bigserial NOT NULL,
    codiceservizio CHARACTER VARYING(255),
    descrizione character varying(255)
);
ALTER TABLE urp.servizioalcittadino OWNER TO postgres;
ALTER TABLE ONLY servizioalcittadino
ADD CONSTRAINT servizioalcittadino_pkey PRIMARY KEY (id);

CREATE TABLE servizioalcittadinosportello (
    id BIGSERIAL NOT NULL,
    servizioalcittadino bigint,
    sportello bigint,
    attivo boolean NOT NULL DEFAULT true
);
ALTER TABLE urp.servizioalcittadinosportello OWNER TO postgres;
ALTER TABLE ONLY servizioalcittadinosportello
ADD CONSTRAINT servizioalcittadinosportello_pkey PRIMARY KEY (id);
ALTER TABLE ONLY servizioalcittadinosportello
ADD CONSTRAINT fk_servizioalcittadinosportello_sportello FOREIGN KEY (sportello) REFERENCES urp.sportello(id);
ALTER TABLE ONLY servizioalcittadinosportello
ADD CONSTRAINT fk_servizioalcittadinosportello_servizioalcittadino FOREIGN KEY (servizioalcittadino) REFERENCES urp.servizioalcittadino(id);
ALTER TABLE ONLY servizioalcittadinosportello
ADD CONSTRAINT unique_servizioalcittadino_sportello UNIQUE (servizioalcittadino, sportello);

CREATE TABLE ticket (
    id bigserial NOT NULL,
    sportello bigint,
    servizioalcittadino bigint,
    utente bigint,
    numero integer,
    tsemesso timestamp without time zone,
    chiamato boolean NOT NULL DEFAULT false,
    tschiamato timestamp without time zone,
    servito boolean NOT NULL DEFAULT false,
    annullato boolean NOT NULL DEFAULT false,
    tschiuso timestamp without time zone
);
ALTER TABLE urp.ticket OWNER TO postgres;
ALTER TABLE ONLY ticket
ADD CONSTRAINT ticket_pkey PRIMARY KEY (id);
ALTER TABLE ONLY ticket
ADD CONSTRAINT fk_ticket_sportello FOREIGN KEY (sportello) REFERENCES urp.sportello(id);
ALTER TABLE ONLY ticket
ADD CONSTRAINT fk_ticket_servizioalcittadino FOREIGN KEY (servizioalcittadino) REFERENCES urp.servizioalcittadino(id);
ALTER TABLE ONLY ticket
ADD CONSTRAINT fk_ticket_utente FOREIGN KEY (utente) REFERENCES base.utente(id);

CREATE TABLE urp.aperturaurp (
    id bigserial NOT NULL,
    giornosettimana integer,
    anticipoapertura integer,
    oraapertura integer,
    minutoapertura integer,
    orachiusura integer,
    minutochiusura integer,
    anticipochiusura integer
);
ALTER TABLE urp.aperturaurp OWNER TO postgres;
ALTER TABLE ONLY aperturaurp
ADD CONSTRAINT aperturaurp_pkey PRIMARY KEY (id);

CREATE TABLE urp.notiziaurp (
  id bigserial NOT NULL,
  titolo CHARACTER VARYING(128),
  testo character varying(512),
  iniziopubblicazione timestamp without time zone,
  finepubblicazione timestamp without time zone
);
ALTER TABLE urp.notiziaurp OWNER TO postgres;
ALTER TABLE ONLY notiziaurp
ADD CONSTRAINT notiziaurp_pkey PRIMARY KEY (id);

-- PEC
SET search_path = pec, pg_catalog;

CREATE TABLE pec.allegati
(
  id bigserial NOT NULL,
  id_messaggio bigint,
  data bytea,
  file_name character varying,
  content_type character varying,
  size bigint,
  store_file_name character varying,
  store_path character varying,
  store_url character varying,
  dt_cancellazione timestamp without time zone,
  dt_creazione timestamp without time zone,
  ts_modifica timestamp without time zone,
  id_utente_cancellazione bigint,
  id_utente_creazione bigint,
  id_utente_modifica bigint,
  CONSTRAINT allegati_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE pec.allegati
  OWNER TO postgres;

CREATE TABLE pec.messaggi
(
  id bigserial NOT NULL,
  stato_accettato boolean NOT NULL,
  accettato_id bigint,
  stato_anomalia boolean NOT NULL,
  anomalia_id bigint,
  archiviato boolean,
  archiviato_data timestamp without time zone,
  archiviato_id_utente bigint,
  stato_consegnato boolean NOT NULL,
  consegnato_id bigint,
  data_invio timestamp without time zone,
  data_invio_originale timestamp without time zone,
  data_ricezione timestamp without time zone,
  destinatari character varying,
  mittente_email character varying,
  eml_file character varying,
  dt_cancellazione timestamp without time zone,
  dt_creazione timestamp without time zone,
  ts_modifica timestamp without time zone,
  id_utente_cancellazione bigint,
  id_utente_creazione bigint,
  id_utente_modifica bigint,
  folder character varying,
  stato_inoltrato boolean,
  inoltrato_destinatari character varying,
  inoltrato_data timestamp without time zone,
  inoltrato_id_utente bigint,
  stato_inviato boolean,
  letto boolean,
  letto_data timestamp without time zone,
  letto_id_utente bigint,
  message_id character varying,
  messaggio character varying,
  mittente_nome character varying,
  oggetto character varying,
  postacert_body character varying,
  postacert_contenttype character varying,
  postacert_file character varying,
  processato boolean,
  protocollo character varying,
  mittente_username character varying,
  x_ricevuta character varying,
  x_riferimento_message_id character varying,
  x_tipo_ricevuta character varying,
  mailbox character varying,
  destinatari_cc character varying,
  destinatari_ccn character varying,
  errore_invio character varying,
  url_documentale character varying,
  postacert_subject character varying,
  daticert_xml character varying,
  segnatura_xml character varying,
  CONSTRAINT messaggi_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE pec.messaggi
  OWNER TO postgres;

CREATE TRIGGER trg_upd_statopecprotocollo
  AFTER UPDATE OF stato_inviato, stato_accettato, stato_anomalia, stato_consegnato
  ON pec.messaggi
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_statopecprotocollo();


CREATE TABLE pec.notifiche
(
  id bigserial NOT NULL,
  tipo character varying,
  id_messaggio_padre bigint,
  destinatari character varying,
  oggetto character varying,
  messaggio character varying,
  allegati character varying,
  stato_inviato boolean,
  data_invio timestamp without time zone,
  protocollo character varying,
  errore character varying,
  dt_cancellazione timestamp without time zone,
  dt_creazione timestamp without time zone,
  ts_modifica timestamp without time zone,
  id_utente_cancellazione bigint,
  id_utente_creazione bigint,
  id_utente_modifica bigint,
  CONSTRAINT notifiche_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE pec.notifiche
  OWNER TO postgres;

CREATE TABLE pec.regole
(
  id bigserial NOT NULL,
  azione character varying,
  criterio character varying,
  evento character varying,
  nome character varying,
  note character varying,
  ordine integer,
  dt_cancellazione timestamp without time zone,
  dt_creazione timestamp without time zone,
  ts_modifica timestamp without time zone,
  id_utente_cancellazione bigint,
  id_utente_creazione bigint,
  id_utente_modifica bigint,
  classe character varying,
  CONSTRAINT regole_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE pec.regole
  OWNER TO postgres;

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;