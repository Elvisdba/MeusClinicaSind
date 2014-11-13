ALTER TABLE fin_servicos ADD COLUMN id_tipo_documento INTEGER;
ALTER TABLE fin_servicos 
    ADD CONSTRAINT fk_fin_servicos_id_tipo_documento 
        FOREIGN KEY (id_tipo_documento) REFERENCES fin_tipo_documento (id)
            ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE fin_servicos
   ALTER COLUMN id_tipo_documento SET NOT NULL;