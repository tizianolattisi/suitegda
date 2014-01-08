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

-- Base						------------
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

-- Anagrafiche					---------------
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

CREATE TRIGGER trg_ins_ts_titolostudiosoggetto
  BEFORE INSERT
  ON anagrafiche.titolostudiosoggetto
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_titolostudiosoggetto
  BEFORE UPDATE
  ON anagrafiche.titolostudiosoggetto
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

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

-- Procedimenti					-----------
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

CREATE TRIGGER trg_ins_ts_faseprocedimento
  BEFORE INSERT
  ON procedimenti.faseprocedimento
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_faseprocedimento
  BEFORE UPDATE
  ON procedimenti.faseprocedimento
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

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

-- Protocollo					-----------
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

-- Pratiche					----------
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

CREATE TRIGGER trg_ins_ts_fasepratica
  BEFORE INSERT
  ON pratiche.fasepratica
  FOR EACH ROW
  EXECUTE PROCEDURE generale.insert_timestamp();
CREATE TRIGGER trg_upd_ts_fasepratica
  BEFORE UPDATE
  ON pratiche.fasepratica
  FOR EACH ROW
  EXECUTE PROCEDURE generale.update_timestamp();

-- Pubblicazioni				--------
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

-- Delibere e determine				---------
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

-- Messaggi / richieste				-------
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
