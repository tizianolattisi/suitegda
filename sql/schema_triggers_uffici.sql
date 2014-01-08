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
