-- Generazione database suite

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

-- creazione schemi
CREATE SCHEMA base;
ALTER SCHEMA base OWNER TO pypapidev;
CREATE SCHEMA anagrafiche;
ALTER SCHEMA anagrafiche OWNER TO pypapidev;
CREATE SCHEMA protocollo;
ALTER SCHEMA protocollo OWNER TO pypapidev;
CREATE SCHEMA pratiche;
ALTER SCHEMA pratiche OWNER TO pypapidev;

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
    supervisoreprotocollo boolean
);
ALTER TABLE base.utente OWNER TO tiziano;
ALTER TABLE ONLY utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (id);

CREATE TABLE ufficio (
    id bigserial NOT NULL,
    descrizione character varying(255)
);
ALTER TABLE base.ufficio OWNER TO tiziano;
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
ALTER TABLE base.ufficioutente OWNER TO tiziano;
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
ALTER TABLE anagrafiche.soggetto OWNER TO tiziano;
ALTER TABLE ONLY soggetto
    ADD CONSTRAINT soggetto_pkey PRIMARY KEY (id);

CREATE TABLE indirizzo (
    id bigserial NOT NULL,
    civico character varying(255),
    provincia character varying(255),
    via character varying(255),
    soggetto bigint
);
ALTER TABLE anagrafiche.indirizzo OWNER TO tiziano;
ALTER TABLE ONLY indirizzo
    ADD CONSTRAINT indirizzo_pkey PRIMARY KEY (id);
ALTER TABLE ONLY indirizzo
    ADD CONSTRAINT fk_indirizzo_soggetto FOREIGN KEY (soggetto) REFERENCES soggetto(id);



-- Pratiche
SET search_path = pratiche, pg_catalog;

CREATE TABLE pratica (
    id bigserial NOT NULL,
    anno integer,
    datapratica date,
    descrizione character varying(255),
    idpratica character varying(255),
    note character varying(255)
);
ALTER TABLE pratiche.pratica OWNER TO tiziano;
ALTER TABLE ONLY pratica
    ADD CONSTRAINT pratica_idpratica_key UNIQUE (idpratica);
ALTER TABLE ONLY pratica
    ADD CONSTRAINT pratica_pkey PRIMARY KEY (id);



-- Protocollo
SET search_path = protocollo, pg_catalog;

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
    sportello bigint
);
ALTER TABLE protocollo.protocollo OWNER TO tiziano;
ALTER TABLE ONLY protocollo
    ADD CONSTRAINT protocollo_iddocumento_key UNIQUE (iddocumento);
ALTER TABLE ONLY protocollo
    ADD CONSTRAINT protocollo_pkey PRIMARY KEY (id);
ALTER TABLE ONLY protocollo
    ADD CONSTRAINT fk_protocollo_sportello FOREIGN KEY (sportello) REFERENCES base.ufficio(id);

CREATE TABLE attribuzione (
    id bigserial NOT NULL,
    letto boolean,
    principale boolean,
    protocollo character varying(255),
    ufficio bigint
);
ALTER TABLE protocollo.attribuzione OWNER TO tiziano;
ALTER TABLE ONLY attribuzione
    ADD CONSTRAINT attribuzione_pkey PRIMARY KEY (id);
ALTER TABLE ONLY attribuzione
    ADD CONSTRAINT fk_attribuzione_protocollo FOREIGN KEY (protocollo) REFERENCES protocollo(iddocumento);
ALTER TABLE ONLY attribuzione
    ADD CONSTRAINT fk_attribuzione_ufficio FOREIGN KEY (ufficio) REFERENCES base.ufficio(id);

CREATE TABLE fascicolo (
    id bigserial NOT NULL,
    categoria integer,
    classe integer,
    descrizione character varying(255),
    note character varying(2048),
    fascicolo integer
);
ALTER TABLE protocollo.fascicolo OWNER TO tiziano;
ALTER TABLE ONLY fascicolo
    ADD CONSTRAINT fascicolo_pkey PRIMARY KEY (id);

CREATE TABLE praticaprotocollo (
    id bigserial NOT NULL,
    titolo character varying(255),
    pratica bigint,
    protocollo bigint
);
ALTER TABLE protocollo.praticaprotocollo OWNER TO tiziano;
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
ALTER TABLE protocollo.riferimentoprotocollo OWNER TO tiziano;
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
ALTER TABLE protocollo.soggettoprotocollo OWNER TO tiziano;
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
ALTER TABLE protocollo.ufficioprotocollo OWNER TO tiziano;
ALTER TABLE ONLY ufficioprotocollo
    ADD CONSTRAINT ufficioprotocollo_pkey PRIMARY KEY (id);
ALTER TABLE ONLY ufficioprotocollo
    ADD CONSTRAINT fk_ufficioprotocollo_protocollo FOREIGN KEY (protocollo) REFERENCES protocollo(iddocumento);
ALTER TABLE ONLY ufficioprotocollo
    ADD CONSTRAINT fk_ufficioprotocollo_ufficio FOREIGN KEY (ufficio) REFERENCES base.ufficio(id);


-- Sequenze (tmp)
--SET search_path = public, pg_catalog;

--CREATE TABLE sequence (
--    seq_name character varying(50) NOT NULL,
--    seq_count numeric(38,0)
--);
--INSERT INTO sequence (seq_name, seq_count) VALUES ('SEQ_GEN', 0);
--ALTER TABLE public.sequence OWNER TO tiziano;




REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;