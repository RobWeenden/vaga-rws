--INSERT INTO public.empresa(id_empresa, categoria, cnpj, insc_estadual, insc_municipal, nome_fantasia, razao_social) VALUES ('562859e9-50a1-4c5d-b583-3fbec4a68c2b', 'EA', '1234568588', '1554654665', '2321324645465', 'FantasiaEmpresa01', 'RazaoEmpresa01');

INSERT INTO public.usuario(id_usuario, nome, cpf, email, senha, data_nascimento, cargo, departamento, telefone, ativo) VALUES ('18993f4f-e2b3-44f5-b7d0-2d46a609963c', 'admin', '28922999004', 'admin@admin.com', '$2a$10$COX.yCeU9ZLLeKFPdnoKku2wF5e6bIMWxbvv06yvPpxXJEi5IQVwa', '1990-12-17', 'CEO', 'Gestão', '33325158595',true);
--INSERT INTO public.usuario(id_usuario, id_empresa, ativo, cpf, email, senha, data_nascimento, nome) VALUES ('8b9a2bce-9003-47c0-b773-e73832dc41ba', '562859e9-50a1-4c5d-b583-3fbec4a68c2b', true, '09426039074', 'admin@admin.com', '$2a$10$COX.yCeU9ZLLeKFPdnoKku2wF5e6bIMWxbvv06yvPpxXJEi5IQVwa', '2024-12-17', 'admin');

INSERT INTO public.perfil(id_perfil, perfil) VALUES ('cceccb63-795e-4cb9-a3bd-f3425b367e48', 'ROLE_ADMIN');
INSERT INTO public.perfil(id_perfil, perfil) VALUES ('926dd884-eb21-4f18-9a79-a4cf1f87c06e', 'ROLE_COLABORADOR');
INSERT INTO public.perfil(id_perfil, perfil) VALUES ('341b787f-62ce-49cd-8a02-680a87ccf3f9', 'ROLE_RESPONSAVEL');

INSERT INTO public.usuario_perfil (id_usuario, id_perfil) VALUES('18993f4f-e2b3-44f5-b7d0-2d46a609963c', 'cceccb63-795e-4cb9-a3bd-f3425b367e48');
-- INSERT INTO public.usuario_perfil (id_usuario, id_perfil) VALUES('18993f4f-e2b3-44f5-b7d0-2d46a609963c', '926dd884-eb21-4f18-9a79-a4cf1f87c06e');
-- INSERT INTO public.usuario_perfil (id_usuario, id_perfil) VALUES('18993f4f-e2b3-44f5-b7d0-2d46a609963c', '341b787f-62ce-49cd-8a02-680a87ccf3f9');

--CONSTRAINT UNIQUE EMAIL
ALTER TABLE usuario ADD CONSTRAINT unique_email UNIQUE (email);

--CONSTRAINT INDEX
CREATE INDEX email_idx ON public.usuario (email);
CREATE INDEX usuario_idx ON public.usuario (id_usuario);
CREATE INDEX perfil_idx ON public.perfil (id_perfil);
CREATE INDEX perfil_desc_idx ON public.perfil (perfil);
--CREATE INDEX empresa_indx ON public.empresa (id_empresa);
--CREATE INDEX usuario_id_empresa_idx ON public.usuario (id_empresa);

