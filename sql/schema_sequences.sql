select setval('anagrafiche.alboprofessionale_id_seq', coalesce((select max(id)+1 from anagrafiche.alboprofessionale), 1), false);
select setval('anagrafiche.gruppo_id_seq', coalesce((select max(id)+1 from anagrafiche.gruppo), 1), false);
select setval('anagrafiche.grupposoggetto_id_seq', coalesce((select max(id)+1 from anagrafiche.grupposoggetto), 1), false);
select setval('anagrafiche.indirizzo_id_seq', coalesce((select max(id)+1 from anagrafiche.indirizzo), 1), false);
select setval('anagrafiche.relazione_id_seq', coalesce((select max(id)+1 from anagrafiche.relazione), 1), false);
select setval('anagrafiche.relazionesoggetto_id_seq', coalesce((select max(id)+1 from anagrafiche.zrelazionesoggetto), 1), false);
select setval('anagrafiche.titolosoggetto_id_seq', coalesce((select max(id)+1 from anagrafiche.titolosoggetto), 1), false);
select setval('anagrafiche.riferimento_id_seq', coalesce((select max(id)+1 from anagrafiche.riferimento), 1), false);
select setval('anagrafiche.soggetto_id_seq', coalesce((select max(id)+1 from anagrafiche.soggetto), 1), false);
select setval('anagrafiche.stato_id_seq', coalesce((select max(id)+1 from anagrafiche.stato), 1), false);

select setval('base.ufficio_id_seq', coalesce((select max(id)+1 from base.ufficio), 1), false);
select setval('base.ufficioutente_id_seq', coalesce((select max(id)+1 from base.ufficioutente), 1), false);
select setval('base.utente_id_seq', coalesce((select max(id)+1 from base.utente), 1), false);

select setval('deliberedetermine.determina_id_seq', coalesce((select max(id)+1 from deliberedetermine.determina), 1), false);
select setval('deliberedetermine.movimentodetermina_id_seq', coalesce((select max(id)+1 from deliberedetermine.movimentodetermina), 1), false);
select setval('deliberedetermine.serviziodetermina_id_seq', coalesce((select max(id)+1 from deliberedetermine.serviziodetermina), 1), false);
select setval('deliberedetermine.ufficiodetermina_id_seq', coalesce((select max(id)+1 from deliberedetermine.ufficiodetermina), 1), false);

select setval('pratiche.dipendenza_id_seq', coalesce((select max(id)+1 from pratiche.dipendenza), 1), false);
select setval('pratiche.pratica_id_seq', coalesce((select max(id)+1 from pratiche.pratica), 1), false);
select setval('pratiche.tipopratica_id_seq', coalesce((select max(id)+1 from pratiche.tipopratica), 1), false);
select setval('pratiche.dipendenzapratica_id_seq', coalesce((select max(id)+1 from pratiche.zdipendenzapratica), 1), false);

select setval('procedimenti.carica_id_seq', coalesce((select max(id)+1 from procedimenti.carica), 1), false);
select setval('procedimenti.delega_id_seq', coalesce((select max(id)+1 from procedimenti.delega), 1), false);
select setval('procedimenti.norma_id_seq', coalesce((select max(id)+1 from procedimenti.norma), 1), false);
select setval('procedimenti.normaprocedimento_id_seq', coalesce((select max(id)+1 from procedimenti.normaprocedimento), 1), false);
select setval('procedimenti.procedimento_id_seq', coalesce((select max(id)+1 from procedimenti.procedimento), 1), false);
select setval('procedimenti.tipopraticaprocedimento_id_seq', coalesce((select max(id)+1 from procedimenti.tipopraticaprocedimento), 1), false);
select setval('procedimenti.ufficioprocedimento_id_seq', coalesce((select max(id)+1 from procedimenti.ufficioprocedimento), 1), false);
select setval('procedimenti.utenteprocedimento_id_seq', coalesce((select max(id)+1 from procedimenti.utenteprocedimento), 1), false);

select setval('protocollo.attribuzione_id_seq', coalesce((select max(id)+1 from protocollo.attribuzione), 1), false);
select setval('protocollo.fascicolo_id_seq', coalesce((select max(id)+1 from protocollo.fascicolo), 1), false);
select setval('protocollo.praticaprotocollo_id_seq', coalesce((select max(id)+1 from protocollo.praticaprotocollo), 1), false);
select setval('protocollo.protocollo_id_seq', coalesce((select max(id)+1 from protocollo.protocollo), 1), false);
select setval('protocollo.oggetto_id_seq', coalesce((select max(id)+1 from protocollo.oggetto), 1), false);
select setval('protocollo.riferimentoprotocollo_id_seq', coalesce((select max(id)+1 from protocollo.riferimentoprotocollo), 1), false);
select setval('protocollo.soggettoprotocollo_id_seq', coalesce((select max(id)+1 from protocollo.soggettoprotocollo), 1), false);
select setval('protocollo.soggettoriservatoprotocollo_id_seq', coalesce((select max(id)+1 from protocollo.soggettoriservatoprotocollo), 1), false);
select setval('protocollo.titolo_id_seq', coalesce((select max(id)+1 from protocollo.titolo), 1), false);
select setval('protocollo.ufficioprotocollo_id_seq', coalesce((select max(id)+1 from protocollo.ufficioprotocollo), 1), false);

select setval('sedute.caricacommissione_id_seq', coalesce((select max(id)+1 from sedute.caricacommissione), 1), false);
select setval('sedute.commissione_id_seq', coalesce((select max(id)+1 from sedute.commissione), 1), false);
select setval('sedute.seduta_id_seq', coalesce((select max(id)+1 from sedute.seduta), 1), false);
select setval('sedute.tiposeduta_id_seq', coalesce((select max(id)+1 from sedute.tiposeduta), 1), false);
