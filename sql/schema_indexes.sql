SET search_path = pratiche, pg_catalog;
CREATE UNIQUE INDEX pratica_idpratica
   ON pratiche.pratica (idpratica ASC NULLS LAST)
  WITH (FILLFACTOR=95);
ALTER TABLE pratiche.pratica CLUSTER ON pratica_idpratica;

SET search_path = protocollo, pg_catalog;
CREATE UNIQUE INDEX protocollo_iddocumento
  ON protocollo.protocollo
  USING btree
  (iddocumento )
  WITH (FILLFACTOR=95);
ALTER TABLE protocollo.protocollo CLUSTER ON protocollo_iddocumento;

CREATE INDEX soggettoprotocollo_protocollo
  ON protocollo.soggettoprotocollo
  USING btree
  (protocollo );
CREATE INDEX soggettoprotocollo_soggetto
  ON protocollo.soggettoprotocollo
  USING btree
  (soggetto );

CREATE INDEX attribuzione_protocollo
  ON protocollo.attribuzione
  USING btree
  (protocollo );

CREATE INDEX ufficioprotocollo_protocollo
  ON protocollo.ufficioprotocollo
  USING btree
  (protocollo );

CREATE INDEX praticaprotocollo_protocollo
  ON protocollo.praticaprotocollo
  USING btree
  (protocollo );
CREATE INDEX praticaprotocollo_pratica
  ON protocollo.praticaprotocollo
  USING btree
  (pratica );



