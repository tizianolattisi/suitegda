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

CREATE PROCEDURAL LANGUAGE plpgsql;
ALTER PROCEDURAL LANGUAGE plpgsql OWNER TO postgres;

SET default_tablespace = '';
SET default_with_oids = false;


-- Base
SET search_path = base, pg_catalog;

CREATE TABLE utente (
    id bigserial NOT NULL,
    amministratore boolean,
    attributoreprotocollo boolean,
    email character varying(255),
    login character varying(255),
    modellatorepratiche boolean,
    nome character varying(255),
    operatoreanagrafiche boolean,
    operatorepratiche boolean,
    operatoreprotocollo boolean,
    password character varying(255),
    superutente boolean,
    supervisoreanagrafiche boolean,
    supervisorepratiche boolean,
    supervisoreprotocollo boolean,
    ricercatoreprotocollo boolean
);
ALTER TABLE base.utente OWNER TO postgres;
ALTER TABLE ONLY utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (id);

CREATE TABLE ufficio (
    id bigserial NOT NULL,
    descrizione character varying(255)
);
ALTER TABLE base.ufficio OWNER TO postgres;
ALTER TABLE ONLY ufficio
    ADD CONSTRAINT ufficio_pkey PRIMARY KEY (id);

CREATE TABLE ufficioutente (
    id bigserial NOT NULL,
    privato boolean,
    ricerca boolean,
    visualizza boolean,
    ufficio bigint,
    utente bigint
);
ALTER TABLE base.ufficioutente OWNER TO postgres;
ALTER TABLE ONLY ufficioutente
    ADD CONSTRAINT ufficioutente_pkey PRIMARY KEY (id);
ALTER TABLE ONLY ufficioutente
    ADD CONSTRAINT fk_ufficioutente_ufficio FOREIGN KEY (ufficio) REFERENCES ufficio(id);
ALTER TABLE ONLY ufficioutente
    ADD CONSTRAINT fk_ufficioutente_utente FOREIGN KEY (utente) REFERENCES utente(id);


-- Anagrafiche
SET search_path = anagrafiche, pg_catalog;

CREATE TABLE soggetto (
    id bigserial NOT NULL,
    codicefiscale character varying(255),
    cognome character varying(255),
    denominazione character varying(255),
    nick character varying(255),
    nome character varying(255),
    ragionesociale character varying(255),
    sessosoggetto character varying(255),
    tipologiasoggetto character varying(255),
    titolosoggetto character varying(255)
);
ALTER TABLE anagrafiche.soggetto OWNER TO postgres;
ALTER TABLE ONLY soggetto
    ADD CONSTRAINT soggetto_pkey PRIMARY KEY (id);

CREATE TABLE indirizzo (
    id bigserial NOT NULL,
    civico character varying(255),
    provincia character varying(255),
    via character varying(255),
    soggetto bigint
);
ALTER TABLE anagrafiche.indirizzo OWNER TO postgres;
ALTER TABLE ONLY indirizzo
    ADD CONSTRAINT indirizzo_pkey PRIMARY KEY (id);
ALTER TABLE ONLY indirizzo
    ADD CONSTRAINT fk_indirizzo_soggetto FOREIGN KEY (soggetto) REFERENCES soggetto(id);


-- Procedimenti
SET search_path = procedimenti, pg_catalog;

CREATE TABLE procedimento (
    id bigserial NOT NULL,
    descrizione character varying(255)
);
ALTER TABLE procedimenti.procedimento OWNER TO postgres;
ALTER TABLE ONLY procedimento
    ADD CONSTRAINT procedimento_pkey PRIMARY KEY (id);


-- Pratiche
SET search_path = pratiche, pg_catalog;

CREATE TABLE tipologiapratica (
    id bigserial NOT NULL,
    codice character varying(255),
    descrizione character varying(255),
    tipologiapadre bigint,
    procedimento bigint
);
ALTER TABLE pratiche.tipologiapratica OWNER TO postgres;
ALTER TABLE ONLY tipologiapratica
    ADD CONSTRAINT tipologiapratica_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tipologiapratica
    ADD CONSTRAINT fk_tipologiapratica_tipologiapadre FOREIGN KEY (tipologiapadre) REFERENCES pratiche.tipologiapratica(id);
ALTER TABLE ONLY tipologiapratica
    ADD CONSTRAINT fk_tipologiapratica_procedimento FOREIGN KEY (procedimento) REFERENCES procedimenti.procedimento(id);


CREATE TABLE pratica (
    id bigserial NOT NULL,
    anno integer,
    datapratica date,
    descrizione character varying(255),
    idpratica character varying(255),
    note character varying(255),
    attribuzione bigint,
    gestione bigint,
    ubicazione bigint,
    dettaglioubicazione character varying(255),
    tipologiapratica bigint
);
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
    ADD CONSTRAINT fk_pratica_tipologiapratica FOREIGN KEY (tipologiapratica) REFERENCES pratiche.tipologiapratica(id);



-- Protocollo
SET search_path = protocollo, pg_catalog;

CREATE TABLE fascicolo (
    id bigserial NOT NULL,
    categoria integer,
    classe integer,
    descrizione character varying(255),
    note character varying(2048),
    fascicolo integer
);
ALTER TABLE protocollo.fascicolo OWNER TO postgres;
ALTER TABLE ONLY fascicolo
    ADD CONSTRAINT fascicolo_pkey PRIMARY KEY (id);

CREATE TABLE protocollo (
    id bigserial NOT NULL,
    convalidaattribuzioni boolean,
    convalidaprotocollo boolean,
    anno integer,
    annullamentorichiesto boolean,
    annullato boolean,
    corrispostoostornato boolean,
    dataprotocollo date,
    datariferimentomittente date,
    iddocumento character varying(255),
    note character varying(1024),
    oggetto character varying(1024),
    richiederisposta boolean,
    riferimentomittente character varying(255),
    riservato boolean,
    spedito boolean,
    tipo character varying(255),
    tiporiferimentomittente character varying(255),
    sportello bigint,
    fascicolo bigint
);
ALTER TABLE protocollo.protocollo OWNER TO postgres;
ALTER TABLE ONLY protocollo
    ADD CONSTRAINT protocollo_iddocumento_key UNIQUE (iddocumento);
ALTER TABLE ONLY protocollo
    ADD CONSTRAINT protocollo_pkey PRIMARY KEY (id);
ALTER TABLE ONLY protocollo
    ADD CONSTRAINT fk_protocollo_sportello FOREIGN KEY (sportello) REFERENCES base.ufficio(id);
ALTER TABLE ONLY protocollo
    ADD CONSTRAINT fk_protocollo_fascicolo FOREIGN KEY (fascicolo) REFERENCES protocollo.fascicolo(id);

CREATE TABLE attribuzione (
    id bigserial NOT NULL,
    letto boolean,
    principale boolean,
    protocollo character varying(255),
    ufficio bigint
);
ALTER TABLE protocollo.attribuzione OWNER TO postgres;
ALTER TABLE ONLY attribuzione
    ADD CONSTRAINT attribuzione_pkey PRIMARY KEY (id);
ALTER TABLE ONLY attribuzione
    ADD CONSTRAINT fk_attribuzione_protocollo FOREIGN KEY (protocollo) REFERENCES protocollo(iddocumento);
ALTER TABLE ONLY attribuzione
    ADD CONSTRAINT fk_attribuzione_ufficio FOREIGN KEY (ufficio) REFERENCES base.ufficio(id);

CREATE TABLE praticaprotocollo (
    id bigserial NOT NULL,
    titolo character varying(255),
    pratica bigint,
    protocollo bigint
);
ALTER TABLE protocollo.praticaprotocollo OWNER TO postgres;
ALTER TABLE ONLY praticaprotocollo
    ADD CONSTRAINT praticaprotocollo_pkey PRIMARY KEY (id);
ALTER TABLE ONLY praticaprotocollo
    ADD CONSTRAINT fk_praticaprotocollo_pratica FOREIGN KEY (pratica) REFERENCES pratiche.pratica(id);
ALTER TABLE ONLY praticaprotocollo
    ADD CONSTRAINT fk_praticaprotocollo_protocollo FOREIGN KEY (protocollo) REFERENCES protocollo(id);

CREATE TABLE riferimentoprotocollo (
    id bigserial NOT NULL,
    precedente character varying(255),
    protocollo character varying(255)
);
ALTER TABLE protocollo.riferimentoprotocollo OWNER TO postgres;
ALTER TABLE ONLY riferimentoprotocollo
    ADD CONSTRAINT riferimentoprotocollo_pkey PRIMARY KEY (id);
ALTER TABLE ONLY riferimentoprotocollo
    ADD CONSTRAINT fk_riferimentoprotocollo_precedente FOREIGN KEY (precedente) REFERENCES protocollo(iddocumento);
ALTER TABLE ONLY riferimentoprotocollo
    ADD CONSTRAINT fk_riferimentoprotocollo_protocollo FOREIGN KEY (protocollo) REFERENCES protocollo(iddocumento);

CREATE TABLE soggettoprotocollo (
    id bigserial NOT NULL,
    conoscenza boolean,
    corrispondenza boolean,
    notifica boolean,
    titolo character varying(255),
    protocollo character varying(255),
    soggetto bigint
);
ALTER TABLE protocollo.soggettoprotocollo OWNER TO postgres;
ALTER TABLE ONLY soggettoprotocollo
    ADD CONSTRAINT soggettoprotocollo_pkey PRIMARY KEY (id);
ALTER TABLE ONLY soggettoprotocollo
    ADD CONSTRAINT fk_soggettoprotocollo_protocollo FOREIGN KEY (protocollo) REFERENCES protocollo(iddocumento);
ALTER TABLE ONLY soggettoprotocollo
    ADD CONSTRAINT fk_soggettoprotocollo_soggetto FOREIGN KEY (soggetto) REFERENCES anagrafiche.soggetto(id);

CREATE TABLE ufficioprotocollo (
    id bigserial NOT NULL,
    protocollo character varying(255),
    ufficio bigint
);
ALTER TABLE protocollo.ufficioprotocollo OWNER TO postgres;
ALTER TABLE ONLY ufficioprotocollo
    ADD CONSTRAINT ufficioprotocollo_pkey PRIMARY KEY (id);
ALTER TABLE ONLY ufficioprotocollo
    ADD CONSTRAINT fk_ufficioprotocollo_protocollo FOREIGN KEY (protocollo) REFERENCES protocollo(iddocumento);
ALTER TABLE ONLY ufficioprotocollo
    ADD CONSTRAINT fk_ufficioprotocollo_ufficio FOREIGN KEY (ufficio) REFERENCES base.ufficio(id);


-- Pubblicazioni
SET search_path = pubblicazioni, pg_catalog;

CREATE TABLE pubblicazione (
    id bigserial NOT NULL,
    titolo character varying(255),
    descrizione character varying(2048),
    richiedente character varying(255),
    inizioconsultazione date,
    fineconsultazione date,
    pubblicato boolean
);
ALTER TABLE pubblicazioni.pubblicazione OWNER TO postgres;
ALTER TABLE ONLY pubblicazione
    ADD CONSTRAINT pubblicazione_pkey PRIMARY KEY (id);

-- Sequenze (tmp)
--SET search_path = public, pg_catalog;

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