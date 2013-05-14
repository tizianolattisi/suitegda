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
CREATE TABLE withtimestamp
(
  rec_creato timestamp,
  rec_creato_da character varying(40),
  rec_modificato timestamp,
  rec_modificato_da character varying(40)
);
ALTER TABLE withtimestamp OWNER TO postgres;

CREATE OR REPLACE FUNCTION insert_timestamp()
  RETURNS trigger AS
$BODY$
begin
  if new.rec_creato is NULL then
     new.rec_creato := 'now';
  end if;
  return new;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION insert_timestamp() OWNER TO postgres;

CREATE OR REPLACE FUNCTION update_timestamp()
  RETURNS trigger AS
$BODY$
begin
  new.rec_modificato := 'now';
  return new;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_timestamp() OWNER TO postgres;


-- Base
SET search_path = base, pg_catalog;

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
    password character varying(255),
    superutente boolean NOT NULL DEFAULT FALSE,
    supervisoreanagrafiche boolean NOT NULL DEFAULT FALSE,
    supervisorepratiche boolean NOT NULL DEFAULT FALSE,
    supervisoreprotocollo boolean NOT NULL DEFAULT FALSE,
    ricercatoreprotocollo boolean NOT NULL DEFAULT FALSE,
    istruttorepratiche boolean NOT NULL DEFAULT FALSE
) INHERITS (generale.withtimestamp);
ALTER TABLE base.utente OWNER TO postgres;
ALTER TABLE ONLY utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (id);
CREATE TRIGGER trg_ins_ts_utente
  BEFORE INSERT
  ON base.utente
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_utente
  BEFORE UPDATE
  ON base.utente
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();


CREATE TABLE ufficio (
    id bigserial NOT NULL,
    descrizione character varying(255),
    sportello boolean NOT NULL DEFAULT FALSE,
    mittenteodestinatario boolean NOT NULL DEFAULT FALSE,
    attribuzione boolean NOT NULL DEFAULT FALSE
) INHERITS (generale.withtimestamp);
ALTER TABLE base.ufficio OWNER TO postgres;
ALTER TABLE ONLY ufficio
    ADD CONSTRAINT ufficio_pkey PRIMARY KEY (id);
CREATE TRIGGER trg_ins_ts_ufficio
  BEFORE INSERT
  ON base.ufficio
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_ufficio
  BEFORE UPDATE
  ON base.ufficio
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

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
CREATE TRIGGER trg_ins_ts_ufficioutente
  BEFORE INSERT
  ON base.ufficioutente
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_ufficioutente
  BEFORE UPDATE
  ON base.ufficioutente
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();


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
CREATE TRIGGER trg_ins_ts_relazione
  BEFORE INSERT
  ON anagrafiche.relazione
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_relazione
  BEFORE UPDATE
  ON anagrafiche.relazione
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

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

CREATE TABLE alboprofessionale (
    id bigserial NOT NULL,
    descrizione character varying(255)
) INHERITS (generale.withtimestamp);
ALTER TABLE anagrafiche.alboprofessionale OWNER TO postgres;
ALTER TABLE ONLY alboprofessionale
    ADD CONSTRAINT alboprofessionale_pkey PRIMARY KEY (id);
CREATE TRIGGER trg_ins_ts_alboprofessionale
  BEFORE INSERT
  ON anagrafiche.alboprofessionale
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_alboprofessionale
  BEFORE UPDATE
  ON anagrafiche.alboprofessionale
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

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
CREATE TRIGGER trg_ins_ts_gruppo
  BEFORE INSERT
  ON anagrafiche.gruppo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_gruppo
  BEFORE UPDATE
  ON anagrafiche.gruppo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

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
    titolosoggetto character varying(20),
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
    codiceanagrafe bigint
) INHERITS (generale.withtimestamp);
ALTER TABLE anagrafiche.soggetto OWNER TO postgres;
ALTER TABLE ONLY soggetto
    ADD CONSTRAINT soggetto_pkey PRIMARY KEY (id);
ALTER TABLE ONLY soggetto
    ADD CONSTRAINT fk_soggetto_alboprofessionale FOREIGN KEY (alboprofessionale) REFERENCES alboprofessionale(id);
CREATE TRIGGER trg_ins_ts_soggetto
  BEFORE INSERT
  ON anagrafiche.soggetto
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_soggetto
  BEFORE UPDATE
  ON anagrafiche.soggetto
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

CREATE TABLE grupposoggetto (
    id bigserial NOT NULL,
    soggetto bigint,
    gruppo bigint,
    datanascita date,
    datacessazione date
) INHERITS (generale.withtimestamp);
ALTER TABLE anagrafiche.grupposoggetto OWNER TO postgres;
ALTER TABLE ONLY grupposoggetto
    ADD CONSTRAINT grupposoggetto_pkey PRIMARY KEY (id);
ALTER TABLE ONLY grupposoggetto
    ADD CONSTRAINT fk_grupposoggetto_soggetto FOREIGN KEY (soggetto) REFERENCES soggetto(id);
ALTER TABLE ONLY grupposoggetto
    ADD CONSTRAINT fk_grupposoggetto_gruppo FOREIGN KEY (gruppo) REFERENCES gruppo(id);
CREATE TRIGGER trg_ins_ts_grupposoggetto
  BEFORE INSERT
  ON anagrafiche.grupposoggetto
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_grupposoggetto
  BEFORE UPDATE
  ON anagrafiche.grupposoggetto
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

-- la tabella "reale" delle relazioni soggetto contiene solo le relazioni "dritte"
CREATE TABLE zrelazionesoggetto (
    id bigserial NOT NULL,
    soggetto bigint,
    relazione bigint,
    relazionato bigint,
    datanascita date,
    datacessazione date
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
CREATE TRIGGER trg_ins_ts_zrelazionesoggetto
  BEFORE INSERT
  ON anagrafiche.zrelazionesoggetto
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_zrelazionesoggetto
  BEFORE UPDATE
  ON anagrafiche.zrelazionesoggetto
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

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
CREATE VIEW relazionesoggetto AS
        SELECT id, soggetto, relazione, relazionato, datanascita, datacessazione,
            FALSE AS invertita
        FROM zrelazionesoggetto
    UNION
        SELECT (-(id)::integer) AS id, relazionato AS soggetto, relazione,
            soggetto AS relazionato, datanascita, datacessazione,
            TRUE AS invertita
        FROM zrelazionesoggetto;

CREATE RULE relazionesoggetto_delete AS ON DELETE TO relazionesoggetto DO INSTEAD
	DELETE FROM zrelazionesoggetto
	WHERE ((zrelazionesoggetto.id)::integer = old.id);

CREATE RULE relazionesoggetto_insert AS ON INSERT TO relazionesoggetto DO INSTEAD
	INSERT INTO zrelazionesoggetto (id, soggetto, relazione, relazionato, datanascita, datacessazione)
            VALUES (new.id, new.soggetto, new.relazione, new.relazionato, new.datanascita, new.datacessazione)
        RETURNING zrelazionesoggetto.id, zrelazionesoggetto.soggetto,
            zrelazionesoggetto.relazione, zrelazionesoggetto.relazionato,
            zrelazionesoggetto.datanascita, zrelazionesoggetto.datacessazione, FALSE;

CREATE RULE relazionesoggetto_update AS ON UPDATE TO relazionesoggetto DO INSTEAD
	UPDATE zrelazionesoggetto SET id = new.id, soggetto = new.soggetto,
            relazione = new.relazione, relazionato = new.relazionato,
            datanascita = new.datanascita, datacessazione = new.datacessazione
        WHERE ((zrelazionesoggetto.id)::integer = old.id);

CREATE TABLE indirizzo (
    id bigserial NOT NULL,
    tipo character varying(50),
    via character varying(100),
    civico character varying(8),
    cap character varying(5),
    localita character varying(100),
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
CREATE TRIGGER trg_ins_ts_indirizzo
  BEFORE INSERT
  ON anagrafiche.indirizzo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_indirizzo
  BEFORE UPDATE
  ON anagrafiche.indirizzo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

CREATE TABLE riferimento (
    id bigserial NOT NULL,
    tipo character varying(50),
    soggetto bigint,
    riferimento character varying(255),
    descrizione character varying(255)
) INHERITS (generale.withtimestamp);
ALTER TABLE anagrafiche.riferimento OWNER TO postgres;
ALTER TABLE ONLY riferimento
    ADD CONSTRAINT riferimento_pkey PRIMARY KEY (id);
ALTER TABLE ONLY riferimento
    ADD CONSTRAINT fk_riferimento_soggetto FOREIGN KEY (soggetto) REFERENCES soggetto(id);
CREATE TRIGGER trg_ins_ts_riferimento
  BEFORE INSERT
  ON anagrafiche.riferimento
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_riferimento
  BEFORE UPDATE
  ON anagrafiche.riferimento
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

-- Finanziaria (per ora solo i servizi per delibere e determine)
SET search_path = finanziaria, pg_catalog;

CREATE TABLE servizio (
    id bigserial NOT NULL,
    descrizione character varying(1024),
    ufficio bigint
) INHERITS (generale.withtimestamp);
ALTER TABLE finanziaria.servizio OWNER TO postgres;
ALTER TABLE ONLY servizio
    ADD CONSTRAINT servizio_pkey PRIMARY KEY (id);
ALTER TABLE ONLY servizio
    ADD CONSTRAINT fk_servizio_ufficio FOREIGN KEY (ufficio) REFERENCES base.ufficio(id);
CREATE TRIGGER trg_ins_ts_servizio
  BEFORE INSERT
  ON finanziaria.servizio
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_servizio
  BEFORE UPDATE
  ON finanziaria.servizio
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

CREATE TABLE capitolo (
    id bigserial NOT NULL,
    descrizione character varying(1024)
);
ALTER TABLE finanziaria.capitolo OWNER TO postgres;
ALTER TABLE ONLY capitolo
    ADD CONSTRAINT capitolo_pkey PRIMARY KEY (id);

-- Procedimenti
SET search_path = procedimenti, pg_catalog;

CREATE TABLE procedimento (
    id bigserial NOT NULL,
    descrizione character varying(255),
    normativa character varying(255),
    maxgiorniistruttoria integer,
    iniziativa character varying(20),
    soggetto bigint,
    attivo boolean
) INHERITS (generale.withtimestamp);
ALTER TABLE procedimenti.procedimento OWNER TO postgres;
ALTER TABLE ONLY procedimento
    ADD CONSTRAINT procedimento_pkey PRIMARY KEY (id);
ALTER TABLE ONLY procedimento
    ADD CONSTRAINT fk_procedimento_soggetto FOREIGN KEY (soggetto) REFERENCES anagrafiche.soggetto(id);
CREATE TRIGGER trg_ins_ts_procedimento
  BEFORE INSERT
  ON procedimenti.procedimento
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_procedimento
  BEFORE UPDATE
  ON procedimenti.procedimento
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

CREATE TABLE norma (
    id bigserial NOT NULL,
    tipo character varying(255),
    descrizione character varying(255),
    idobject character varying(255)
) INHERITS (generale.withtimestamp);
ALTER TABLE procedimenti.norma OWNER TO postgres;
ALTER TABLE ONLY norma
    ADD CONSTRAINT norma_pkey PRIMARY KEY (id);
CREATE TRIGGER trg_ins_ts_norma
  BEFORE INSERT
  ON procedimenti.norma
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_norma
  BEFORE UPDATE
  ON procedimenti.norma
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

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
CREATE TRIGGER trg_ins_ts_normaprocedimento
  BEFORE INSERT
  ON procedimenti.normaprocedimento
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_normaprocedimento
  BEFORE UPDATE
  ON procedimenti.normaprocedimento
  FOR EACH  ROW
  EXECUTE PROCEDURE generale.update_timestamp();

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
CREATE TRIGGER trg_ins_ts_ufficioprocedimento
  BEFORE INSERT
  ON procedimenti.ufficioprocedimento
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_ufficioprocedimento
  BEFORE UPDATE
  ON procedimenti.ufficioprocedimento
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

CREATE TABLE utenteprocedimento (
    id bigserial NOT NULL,
    procedimento bigint,
    utente bigint,
    ufficio bigint,
    responsabile boolean,
    abilitato boolean,
    abituale boolean
) INHERITS (generale.withtimestamp);
ALTER TABLE procedimenti.utenteprocedimento OWNER TO postgres;
ALTER TABLE ONLY utenteprocedimento
    ADD CONSTRAINT utenteprocedimento_pkey PRIMARY KEY (id);
ALTER TABLE ONLY utenteprocedimento
    ADD CONSTRAINT fk_utenteprocedimento_procedimento FOREIGN KEY (procedimento) REFERENCES procedimento(id);
ALTER TABLE ONLY utenteprocedimento
    ADD CONSTRAINT fk_utenteprocedimento_utente FOREIGN KEY (utente) REFERENCES base.utente(id);
ALTER TABLE ONLY ufficioprocedimento
    ADD CONSTRAINT fk_utenteprocedimento_ufficio FOREIGN KEY (ufficio) REFERENCES base.ufficio(id);
CREATE TRIGGER trg_ins_ts_utenteprocedimento
  BEFORE INSERT
  ON procedimenti.utenteprocedimento
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_utenteprocedimento
  BEFORE UPDATE
  ON procedimenti.utenteprocedimento
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

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
CREATE TRIGGER trg_ins_ts_fascicolo
  BEFORE INSERT
  ON protocollo.fascicolo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_fascicolo
  BEFORE UPDATE
  ON protocollo.fascicolo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

CREATE TABLE pratiche.tipopratica (
    id bigserial NOT NULL,
    codice character varying(10),
    descrizione character varying(255),
    tipopadre bigint,
    procedimento bigint,
    formulacodifica character varying(255),
    porzionenumeroda integer,
    porzionenumeroa integer,
    fascicolo bigint,
    foglia boolean NOT NULL DEFAULT false,
    approvata boolean NOT NULL DEFAULT false
) INHERITS (generale.withtimestamp);
ALTER TABLE pratiche.tipopratica OWNER TO postgres;
ALTER TABLE ONLY pratiche.tipopratica
    ADD CONSTRAINT tipopratica_pkey PRIMARY KEY (id);
ALTER TABLE ONLY pratiche.tipopratica
    ADD CONSTRAINT fk_tipopratica_tipopadre FOREIGN KEY (tipopadre) REFERENCES pratiche.tipopratica(id);
ALTER TABLE ONLY pratiche.tipopratica
    ADD CONSTRAINT fk_tipopratica_procedimento FOREIGN KEY (procedimento) REFERENCES procedimenti.procedimento(id);
ALTER TABLE ONLY pratiche.tipopratica
    ADD CONSTRAINT fk_tipopratica_fascicolo FOREIGN KEY (fascicolo) REFERENCES protocollo.fascicolo(id);
CREATE TRIGGER trg_ins_ts_tipopratica
  BEFORE INSERT
  ON pratiche.tipopratica
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_tipopratica
  BEFORE UPDATE
  ON pratiche.tipopratica
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

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
CREATE TRIGGER trg_ins_ts_tipopraticaprocedimento
  BEFORE INSERT
  ON procedimenti.tipopraticaprocedimento
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_tipopraticaprocedimento
  BEFORE UPDATE
  ON procedimenti.tipopraticaprocedimento
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

CREATE TABLE carica (
    id bigserial NOT NULL,
    descrizione character varying(1024),
    codicecarica character varying(255)
) INHERITS (generale.withtimestamp);
ALTER TABLE procedimenti.carica OWNER TO postgres;
ALTER TABLE ONLY carica
    ADD CONSTRAINT carica_pkey PRIMARY KEY (id);
CREATE TRIGGER trg_ins_ts_carica
  BEFORE INSERT
  ON procedimenti.carica
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_carica
  BEFORE UPDATE
  ON procedimenti.carica
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

CREATE TABLE delega (
    id bigserial NOT NULL,
    carica bigint,
    utente bigint,
    ufficio bigint,
    servizio bigint,
    procedimento bigint,
    inizio date,
    fine date,
    titolare boolean,
    segretario boolean,
    delegato boolean,
    suassenza boolean,
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
CREATE TRIGGER trg_ins_ts_delega
  BEFORE INSERT
  ON procedimenti.delega
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_delega
  BEFORE UPDATE
  ON procedimenti.delega
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();


-- Pratiche
SET search_path = pratiche, pg_catalog;

CREATE TABLE pratica (
    id bigserial NOT NULL,
    anno integer,
    datapratica date,
    descrizione character varying(1024),
    idpratica character varying(9),
    codiceinterno character varying(50),
    note character varying(255),
    attribuzione bigint,
    gestione bigint,
    ubicazione bigint,
    dettaglioubicazione character varying(255),
    tipo bigint,
    riservata boolean NOT NULL DEFAULT FALSE,
    fascicolo bigint
) INHERITS (generale.withtimestamp);
ALTER TABLE pratiche.pratica OWNER TO postgres;
ALTER TABLE ONLY pratica
    ADD CONSTRAINT pratica_idpratica_key UNIQUE (idpratica);
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
CREATE TRIGGER trg_ins_ts_pratica
  BEFORE INSERT
  ON pratiche.pratica
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_pratica
  BEFORE UPDATE
  ON pratiche.pratica
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();
CREATE UNIQUE INDEX pratica_idpratica
   ON pratiche.pratica (idpratica ASC NULLS LAST)
  WITH (FILLFACTOR=95);
ALTER TABLE pratiche.pratica CLUSTER ON pratica_idpratica;

CREATE TABLE dipendenza (
    id bigserial NOT NULL,
    descrizionedominante character varying(255),
    descrizionedipendente character varying(255)
) INHERITS (generale.withtimestamp);
ALTER TABLE pratiche.dipendenza OWNER TO postgres;
ALTER TABLE ONLY dipendenza
    ADD CONSTRAINT dipendenza_id_key UNIQUE (id);
CREATE TRIGGER trg_ins_ts_dipendenza
  BEFORE INSERT
  ON pratiche.dipendenza
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_dipendenza
  BEFORE UPDATE
  ON pratiche.dipendenza
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

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
CREATE TRIGGER trg_ins_ts_zdipendenzapratica
  BEFORE INSERT
  ON pratiche.zdipendenzapratica
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_zdipendenzapratica
  BEFORE UPDATE
  ON pratiche.zdipendenzapratica
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

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
CREATE VIEW dipendenzapratica AS
        SELECT id, praticadominante, dipendenza, praticadipendente,
            FALSE AS invertita
        FROM zdipendenzapratica
    UNION
        SELECT (-(id)::integer) AS id, praticadipendente AS praticadominante, dipendenza,
            praticadominante AS praticadipendente,
            TRUE AS invertita
        FROM zdipendenzapratica;

CREATE RULE dipendenzapratica_delete AS ON DELETE TO dipendenzapratica DO INSTEAD
	DELETE FROM zdipendenzapratica
	WHERE ((zdipendenzapratica.id)::integer = old.id);

CREATE RULE dipendenzapratica_insert AS ON INSERT TO dipendenzapratica DO INSTEAD
	INSERT INTO zdipendenzapratica (id, praticadominante, dipendenza, praticadipendente)
            VALUES (new.id, new.praticadominante, new.dipendenza, new.praticadipendente)
        RETURNING zdipendenzapratica.id, zdipendenzapratica.praticadominante,
            zdipendenzapratica.dipendenza, zdipendenzapratica.praticadipendente, FALSE;

CREATE RULE dipendenzapratica_update AS ON UPDATE TO dipendenzapratica DO INSTEAD
	UPDATE zdipendenzapratica SET id = new.id, praticadominante = new.praticadominante,
            dipendenza = new.dipendenza, praticadipendente = new.praticadipendente
        WHERE ((zdipendenzapratica.id)::integer = old.id);

-- Protocollo
SET search_path = protocollo, pg_catalog;

CREATE TABLE protocollo (
    id bigserial NOT NULL,
    tipo character varying(10) NOT NULL,
    anno integer NOT NULL,
    iddocumento character varying(12) NOT NULL,
    dataprotocollo timestamp NOT NULL,
    sportello bigint NOT NULL,
    oggetto character varying(1024) NOT NULL,
    note character varying(1024),
    tiporiferimentomittente character varying(25),
    riferimentomittente character varying(255),
    datariferimentomittente date,
    riservato boolean,
    convalidaattribuzioni boolean,
    dataconvalidaattribuzioni timestamp,
    esecutoreconvalidaattribuzioni character varying(40),
    convalidaprotocollo boolean,
    numeroconvalidaprotocollo character varying(10),
    dataconvalidaprotocollo timestamp,
    esecutoreconvalidaprotocollo character varying(40),
    consolidadocumenti boolean,
    dataconsolidadocumenti timestamp,
    esecutoreconsolidadocumenti character varying(40),
    nrricevuta character varying(10),
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
    fascicolo bigint
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
CREATE TRIGGER trg_ins_ts_protocollo
  BEFORE INSERT
  ON protocollo.protocollo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_protocollo
  BEFORE UPDATE
  ON protocollo.protocollo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();
CREATE UNIQUE INDEX protocollo_iddocumento
  ON protocollo.protocollo
  USING btree
  (iddocumento )
  WITH (FILLFACTOR=95);
ALTER TABLE protocollo.protocollo CLUSTER ON protocollo_iddocumento;

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
CREATE TRIGGER trg_ins_ts_attribuzione
  BEFORE INSERT
  ON protocollo.attribuzione
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_attribuzione
  BEFORE UPDATE
  ON protocollo.attribuzione
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

CREATE TABLE oggetto (
    id bigserial NOT NULL,
    descrizione character varying(255)
) INHERITS (generale.withtimestamp);
ALTER TABLE protocollo.oggetto OWNER TO postgres;
ALTER TABLE ONLY oggetto
    ADD CONSTRAINT oggetto_pkey PRIMARY KEY (id);
CREATE TRIGGER trg_ins_ts_oggetto
  BEFORE INSERT
  ON protocollo.oggetto
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_oggetto
  BEFORE UPDATE
  ON protocollo.oggetto
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

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
CREATE TRIGGER trg_ins_ts_praticaprotocollo
  BEFORE INSERT
  ON protocollo.praticaprotocollo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_praticaprotocollo
  BEFORE UPDATE
  ON protocollo.praticaprotocollo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

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
CREATE TRIGGER trg_ins_ts_riferimentoprotocollo
  BEFORE INSERT
  ON protocollo.riferimentoprotocollo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_riferimentoprotocollo
  BEFORE UPDATE
  ON protocollo.riferimentoprotocollo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

CREATE TABLE titolo (
    id bigserial NOT NULL,
    descrizione character varying(255),
    tipo character varying(50)
) INHERITS (generale.withtimestamp);
ALTER TABLE protocollo.titolo OWNER TO postgres;
ALTER TABLE ONLY titolo
    ADD CONSTRAINT titolo_pkey PRIMARY KEY (id);
CREATE TRIGGER trg_ins_ts_titolo
  BEFORE INSERT
  ON protocollo.titolo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_titolo
  BEFORE UPDATE
  ON protocollo.titolo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

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
    abilitatoweb boolean NOT NULL DEFAULT FALSE
) INHERITS (generale.withtimestamp);
ALTER TABLE protocollo.soggettoprotocollo OWNER TO postgres;
ALTER TABLE ONLY soggettoprotocollo
    ADD CONSTRAINT soggettoprotocollo_pkey PRIMARY KEY (id);
ALTER TABLE ONLY soggettoprotocollo
    ADD CONSTRAINT fk_soggettoprotocollo_protocollo FOREIGN KEY (protocollo) REFERENCES protocollo(iddocumento);
ALTER TABLE ONLY soggettoprotocollo
    ADD CONSTRAINT fk_soggettoprotocollo_soggetto FOREIGN KEY (soggetto) REFERENCES anagrafiche.soggetto(id);
ALTER TABLE ONLY soggettoprotocollo
    ADD CONSTRAINT fk_soggettoprotocollo_titolo FOREIGN KEY (titolo) REFERENCES titolo(id);
CREATE TRIGGER trg_ins_ts_soggettoprotocollo
  BEFORE INSERT
  ON protocollo.soggettoprotocollo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_soggettoprotocollo
  BEFORE UPDATE
  ON protocollo.soggettoprotocollo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

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
    abilitatoweb boolean NOT NULL DEFAULT FALSE
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
CREATE TRIGGER trg_ins_ts_soggettoriservatoprotocollo
  BEFORE INSERT
  ON protocollo.soggettoriservatoprotocollo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_soggettoriservatoprotocollo
  BEFORE UPDATE
  ON protocollo.soggettoriservatoprotocollo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

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
CREATE TRIGGER trg_ins_ts_ufficioprotocollo
  BEFORE INSERT
  ON protocollo.ufficioprotocollo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_ufficioprotocollo
  BEFORE UPDATE
  ON protocollo.ufficioprotocollo
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();


-- Pubblicazioni
SET search_path = pubblicazioni, pg_catalog;

CREATE TABLE pubblicazione (
    id bigserial NOT NULL,
    titolo character varying(25),
    descrizione character varying(2048),
    richiedente character varying(255),
    inizioconsultazione date,
    fineconsultazione date,
    pubblicato boolean
) INHERITS (generale.withtimestamp);
ALTER TABLE pubblicazioni.pubblicazione OWNER TO postgres;
ALTER TABLE ONLY pubblicazione
    ADD CONSTRAINT pubblicazione_pkey PRIMARY KEY (id);
CREATE TRIGGER trg_ins_ts_pubblicazione
  BEFORE INSERT
  ON pubblicazioni.pubblicazione
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_pubblicazione
  BEFORE UPDATE
  ON pubblicazioni.pubblicazione
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();


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
    datapratica date,
    dientrata boolean,
    dispesa boolean,
    diregolarizzazione boolean,
    referentepolitico character varying(255),
    ufficioresponsabile character varying(255),
    nomeresponsabile character varying(255),
    anno integer,
    numero integer,
    vistoresponsabile boolean,
    datavistoresponsabile date,
    titolarevistoresponsabile boolean,
    segretariovistoresponsabile boolean,
    delegatovistoresponsabile boolean,
    utentevistoresponsabile bigint,
    vistobilancio boolean,
    datavistobilancio date,
    titolarevistobilancio boolean,
    segretariovistobilancio boolean,
    delegatovistobilancio boolean,
    utentevistobilancio bigint,
    vistonegato boolean,
    datavistonegato date,
    titolarevistonegato boolean,
    segretariovistonegato boolean,
    delegatovistonegato boolean,
    utentevistonegato bigint,
    iddocumento character varying(12)
) INHERITS (generale.withtimestamp);
ALTER TABLE deliberedetermine.determina OWNER TO postgres;
ALTER TABLE ONLY determina
    ADD CONSTRAINT determina_pkey PRIMARY KEY (id);
ALTER TABLE ONLY determina
    ADD CONSTRAINT fk_determina_utentevistoresponsabile FOREIGN KEY (utentevistoresponsabile) REFERENCES base.utente(id);
ALTER TABLE ONLY determina
    ADD CONSTRAINT fk_determina_utentevistobilancio FOREIGN KEY (utentevistobilancio) REFERENCES base.utente(id);
ALTER TABLE ONLY determina
    ADD CONSTRAINT fk_determina_utentevistonegato FOREIGN KEY (utentevistonegato) REFERENCES base.utente(id);
CREATE TRIGGER trg_ins_ts_determina
  BEFORE INSERT
  ON deliberedetermine.determina
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_determina
  BEFORE UPDATE
  ON deliberedetermine.determina
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

CREATE TABLE serviziodetermina (
    id bigserial NOT NULL,
    determina bigint,
    servizio bigint
) INHERITS (generale.withtimestamp);
ALTER TABLE deliberedetermine.serviziodetermina OWNER TO postgres;
ALTER TABLE ONLY serviziodetermina
    ADD CONSTRAINT serviziodetermina_pkey PRIMARY KEY (id);
ALTER TABLE ONLY serviziodetermina
    ADD CONSTRAINT fk_serviziodetermina_determina FOREIGN KEY (determina) REFERENCES deliberedetermine.determina(id);
ALTER TABLE ONLY serviziodetermina
    ADD CONSTRAINT fk_serviziodetermina_servizio FOREIGN KEY (servizio) REFERENCES finanziaria.servizio(id);
CREATE TRIGGER trg_ins_ts_serviziodetermina
  BEFORE INSERT
  ON deliberedetermine.serviziodetermina
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_serviziodetermina
  BEFORE UPDATE
  ON deliberedetermine.serviziodetermina
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

CREATE TABLE ufficiodetermina (
    id bigserial NOT NULL,
    determina bigint,
    ufficio bigint
) INHERITS (generale.withtimestamp);
ALTER TABLE deliberedetermine.ufficiodetermina OWNER TO postgres;
ALTER TABLE ONLY ufficiodetermina
    ADD CONSTRAINT ufficiodetermina_pkey PRIMARY KEY (id);
ALTER TABLE ONLY ufficiodetermina
    ADD CONSTRAINT fk_ufficiodetermina_determina FOREIGN KEY (determina) REFERENCES deliberedetermine.determina(id);
ALTER TABLE ONLY ufficiodetermina
    ADD CONSTRAINT fk_ufficiodetermina_ufficio FOREIGN KEY (ufficio) REFERENCES base.ufficio(id);
CREATE TRIGGER trg_ins_ts_ufficiodetermina
  BEFORE INSERT
  ON deliberedetermine.ufficiodetermina
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_ufficiodetermina
  BEFORE UPDATE
  ON deliberedetermine.ufficiodetermina
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

CREATE TABLE movimentodetermina (
    id bigserial NOT NULL,
    determina bigint,
    capitolo bigint,
    articolo character varying(255),
    codicemeccanografico character varying(255),
    impegno character varying(255),
    sottoimpegno character varying(255),
    descrizioneimpegno character varying(255),
    annoimpegno bigint,
    importo numeric(15,2),
    tipomovimento character varying(255),
    annoesercizio bigint
) INHERITS (generale.withtimestamp);
ALTER TABLE deliberedetermine.movimentodetermina OWNER TO postgres;
ALTER TABLE ONLY movimentodetermina
    ADD CONSTRAINT movimentodetermina_pkey PRIMARY KEY (id);
ALTER TABLE ONLY movimentodetermina
    ADD CONSTRAINT fk_movimentodetermina_determina FOREIGN KEY (determina) REFERENCES determina(id);
ALTER TABLE ONLY movimentodetermina
    ADD CONSTRAINT fk_movimentodetermina_capitolo FOREIGN KEY (capitolo) REFERENCES finanziaria.capitolo(id);
CREATE TRIGGER trg_ins_ts_movimentodetermina
  BEFORE INSERT
  ON deliberedetermine.movimentodetermina
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_movimentodetermina
  BEFORE UPDATE
  ON deliberedetermine.movimentodetermina
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

-- Sequenze (tmp)
SET search_path = public, pg_catalog;

--CREATE TABLE sequence (
--    seq_name character varying(50) NOT NULL,
--    seq_count numeric(38,0)
--);
--INSERT INTO sequence (seq_name, seq_count) VALUES ('SEQ_GEN', 0);
--ALTER TABLE public.sequence OWNER TO postgres;




REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;