


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



CREATE OR REPLACE FUNCTION pratiche.insert_uffici_pratica()
  RETURNS trigger AS
$BODY$
begin
  if new.gestione is NULL then
     new.gestione := new.attribuzione;
  end if;
  if new.ubicazione is NULL then
     new.ubicazione := new.attribuzione;
  end if;
  return new;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION pratiche.insert_uffici_pratica()
  OWNER TO postgres;

CREATE TRIGGER trg_ins_uffici_pratica
  BEFORE INSERT
  ON pratiche.pratica
  FOR EACH ROW
  EXECUTE PROCEDURE pratiche.insert_uffici_pratica();
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
