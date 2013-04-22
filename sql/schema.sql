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


-- Base
SET search_path = base, pg_catalog;

CREATE TABLE utente (
    id bigserial NOT NULL,
    amministratore boolean,
    attributoreprotocollo boolean NOT NULL DEFAULT FALSE,
    email character varying(255),
    login character varying(255),
    modellatorepratiche boolean NOT NULL DEFAULT FALSE,
    nome character varying(255),
    sigla character varying(255),
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
    tipo character varying(255),
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

-- Finanziaria (per ora solo i servizi per delibere e determine)
SET search_path = finanziaria, pg_catalog;

CREATE TABLE servizio (
    id bigserial NOT NULL,
    descrizione character varying(1024),
    ufficio bigint
);
ALTER TABLE finanziaria.servizio OWNER TO postgres;
ALTER TABLE ONLY servizio
    ADD CONSTRAINT servizio_pkey PRIMARY KEY (id);
ALTER TABLE ONLY servizio
    ADD CONSTRAINT fk_servizio_ufficio FOREIGN KEY (ufficio) REFERENCES base.ufficio(id);

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
    iniziativa character varying(255),
    soggetto bigint,
    attivo boolean
);
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

CREATE TABLE ufficioprocedimento (
    id bigserial NOT NULL,
    procedimento bigint,
    ufficio bigint,
    principale boolean
);
ALTER TABLE procedimenti.ufficioprocedimento OWNER TO postgres;
ALTER TABLE ONLY ufficioprocedimento
    ADD CONSTRAINT ufficioprocedimento_pkey PRIMARY KEY (id);
ALTER TABLE ONLY ufficioprocedimento
    ADD CONSTRAINT fk_ufficioprocedimento_procedimento FOREIGN KEY (procedimento) REFERENCES procedimento(id);
ALTER TABLE ONLY ufficioprocedimento
    ADD CONSTRAINT fk_ufficioprocedimento_ufficio FOREIGN KEY (ufficio) REFERENCES base.ufficio(id);

CREATE TABLE utenteprocedimento (
    id bigserial NOT NULL,
    procedimento bigint,
    utente bigint,
    ufficio bigint,
    responsabile boolean,
    abilitato boolean,
    abituale boolean
);
ALTER TABLE procedimenti.utenteprocedimento OWNER TO postgres;
ALTER TABLE ONLY utenteprocedimento
    ADD CONSTRAINT utenteprocedimento_pkey PRIMARY KEY (id);
ALTER TABLE ONLY utenteprocedimento
    ADD CONSTRAINT fk_utenteprocedimento_procedimento FOREIGN KEY (procedimento) REFERENCES procedimento(id);
ALTER TABLE ONLY utenteprocedimento
    ADD CONSTRAINT fk_utenteprocedimento_utente FOREIGN KEY (utente) REFERENCES base.utente(id);
ALTER TABLE ONLY ufficioprocedimento
    ADD CONSTRAINT fk_utenteprocedimento_ufficio FOREIGN KEY (ufficio) REFERENCES base.ufficio(id);

CREATE TABLE pratiche.tipopratica (
    id bigserial NOT NULL,
    codice character varying(255),
    descrizione character varying(255),
    tipopadre bigint,
    procedimento bigint
);
ALTER TABLE pratiche.tipopratica OWNER TO postgres;
ALTER TABLE ONLY pratiche.tipopratica
    ADD CONSTRAINT tipopratica_pkey PRIMARY KEY (id);
ALTER TABLE ONLY pratiche.tipopratica
    ADD CONSTRAINT fk_tipopratica_tipopadre FOREIGN KEY (tipopadre) REFERENCES pratiche.tipopratica(id);
ALTER TABLE ONLY pratiche.tipopratica
    ADD CONSTRAINT fk_tipopratica_procedimento FOREIGN KEY (procedimento) REFERENCES procedimenti.procedimento(id);

CREATE TABLE tipopraticaprocedimento (
    id bigserial NOT NULL,
    procedimento bigint,
    tipopratica bigint
);
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
    titolare boolean,
    segretario boolean,
    delegato boolean,
    suassenza boolean,
    delegante bigint

);
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


-- Pratiche
SET search_path = pratiche, pg_catalog;

CREATE TABLE protocollo.fascicolo (
    id bigserial NOT NULL,
    categoria integer,
    classe integer,
    descrizione character varying(255),
    note character varying(2048),
    fascicolo integer
);
ALTER TABLE protocollo.fascicolo OWNER TO postgres;
ALTER TABLE ONLY protocollo.fascicolo
    ADD CONSTRAINT fascicolo_pkey PRIMARY KEY (id);

CREATE TABLE pratica (
    id bigserial NOT NULL,
    anno integer,
    datapratica date,
    descrizione character varying(255),
    idpratica character varying(255),
    codiceinterno character varying(255),
    note character varying(255),
    attribuzione bigint,
    gestione bigint,
    ubicazione bigint,
    dettaglioubicazione character varying(255),
    tipo bigint,
    riservata boolean,
    fascicolo bigint
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
    ADD CONSTRAINT fk_pratica_tipopratica FOREIGN KEY (tipo) REFERENCES pratiche.tipopratica(id);
ALTER TABLE ONLY pratica
    ADD CONSTRAINT fk_pratica_fascicolo FOREIGN KEY (fascicolo) REFERENCES protocollo.fascicolo(id);



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
    iddocumento character varying(12),
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
    ufficio bigint,
    evidenza character varying(1)
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

CREATE TABLE soggettoriservatoprotocollo (
    id bigserial NOT NULL,
    conoscenza boolean,
    corrispondenza boolean,
    notifica boolean,
    titolo character varying(255),
    protocollo character varying(255),
    soggetto bigint
);
ALTER TABLE protocollo.soggettoriservatoprotocollo OWNER TO postgres;
ALTER TABLE ONLY soggettoriservatoprotocollo
    ADD CONSTRAINT soggettoriservatoprotocollo_pkey PRIMARY KEY (id);
ALTER TABLE ONLY soggettoriservatoprotocollo
    ADD CONSTRAINT fk_soggettoriservatoprotocollo_protocollo FOREIGN KEY (protocollo) REFERENCES protocollo(iddocumento);
ALTER TABLE ONLY soggettoriservatoprotocollo
    ADD CONSTRAINT fk_soggettoriservatoprotocollo_soggetto FOREIGN KEY (soggetto) REFERENCES anagrafiche.soggetto(id);

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
    idpratica character varying(255),
    codiceinterno character varying(255),
    oggetto character varying(2048),
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
);
ALTER TABLE deliberedetermine.determina OWNER TO postgres;
ALTER TABLE ONLY determina
    ADD CONSTRAINT determina_pkey PRIMARY KEY (id);
ALTER TABLE ONLY determina
    ADD CONSTRAINT fk_determina_utentevistoresponsabile FOREIGN KEY (utentevistoresponsabile) REFERENCES base.utente(id);
ALTER TABLE ONLY determina
    ADD CONSTRAINT fk_determina_utentevistobilancio FOREIGN KEY (utentevistobilancio) REFERENCES base.utente(id);
ALTER TABLE ONLY determina
    ADD CONSTRAINT fk_determina_utentevistonegato FOREIGN KEY (utentevistonegato) REFERENCES base.utente(id);


CREATE TABLE serviziodetermina (
    id bigserial NOT NULL,
    determina bigint,
    servizio bigint
);
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
    ufficio bigint
);
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
);
ALTER TABLE deliberedetermine.movimentodetermina OWNER TO postgres;
ALTER TABLE ONLY movimentodetermina
    ADD CONSTRAINT movimentodetermina_pkey PRIMARY KEY (id);
ALTER TABLE ONLY movimentodetermina
    ADD CONSTRAINT fk_movimentodetermina_determina FOREIGN KEY (determina) REFERENCES determina(id);
ALTER TABLE ONLY movimentodetermina
    ADD CONSTRAINT fk_movimentodetermina_capitolo FOREIGN KEY (capitolo) REFERENCES finanziaria.capitolo(id);

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