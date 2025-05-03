INSERT INTO public.usuario(id_usuario, nome, cpf, email, senha, data_nascimento, cargo, departamento, telefone, ativo) VALUES ('18993f4f-e2b3-44f5-b7d0-2d46a609963c3edf', 'admin', '28922999004', 'admin@admin.com', '$2a$10$COX.yCeU9ZLLeKFPdnoKku2wF5e6bIMWxbvv06yvPpxXJEi5IQVwa', '1990-12-17', 'CEO', 'Gestão', '33325158595',true);

INSERT INTO public.perfil(id_perfil, perfil) VALUES ('cceccb63-795e-4cb9-a3bd-f3425b367e48', 'ROLE_ADMIN');

INSERT INTO public.usuario_perfil (id_usuario, id_perfil) VALUES('18993f4f-e2b3-44f5-b7d0-2d46a609963c3edf', 'cceccb63-795e-4cb9-a3bd-f3425b367e48');


--CONSTRAINT UNIQUE EMAIL
ALTER TABLE usuario ADD CONSTRAINT unique_email UNIQUE (email);

--CONSTRAINT INDEX
CREATE INDEX email_idx ON public.usuario (email);
CREATE INDEX usuario_idx ON public.usuario (id_usuario);
CREATE INDEX perfil_idx ON public.perfil (id_perfil);
CREATE INDEX perfil_desc_idx ON public.perfil (perfil);

