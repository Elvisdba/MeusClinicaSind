ALTER TABLE fin_servicos ADD COLUMN id_cliente INTEGER NOT NULL;

ALTER TABLE fin_servicos
  ADD CONSTRAINT fk_fin_servicos_id_cliente FOREIGN KEY (id_cliente) REFERENCES seg_cliente (id)
   ON UPDATE NO ACTION ON DELETE NO ACTION;