package com.vagarws.rest.util;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.vagarws.rest.entities.Candidatura;
import com.vagarws.rest.entities.Perfil;
import com.vagarws.rest.entities.Usuario;
import com.vagarws.rest.entities.Vaga;
import com.vagarws.rest.to.request.CandidaturaRequest;
import com.vagarws.rest.to.request.PerfilRequest;
import com.vagarws.rest.to.request.UsuarioRequest;
import com.vagarws.rest.to.request.VagaRequest;

/**
 * Interface responsavel em converter/transpor os dados de um objeto para outro
 * contendo os mesmo nomes de campos Gerenciada pelo MapStruct Para que esta
 * anotação seja gerenciada pelo Injeção de dependencia do Spring é necessario
 * Realizar o Build para criar o a classe no pacote de target
 * 
 * Nesta Interface pode se utilizar de todas as ferramentas do MapStruct Porém
 * procure manter o padrão ja estabelecido nas regras, pois o foco dessa
 * interface/implementação é somente para que o MapStruct gerencie a passagem de
 * dados de classes de campos semelhantes sem a necessidade de uma classe
 * concreta que passa dados por dados de forma manual
 * 
 * A interface busca abstrair esse tipo de manuseio e gerar um codigo mais limpo
 * e também mais reutilizavel Para objetos que possui poucos campos em sua
 * estrutura e classes internas recomendamos que faça de forma manual, pois a
 * proposta desta interface é lidar com classes mais complexas ou que possui
 * muitos campos para ser gerenciado em algum tipo de insert/update etc.
 * 
 * @author robson.silva
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomMapperUtil {

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateTOEntity(UsuarioRequest to, @MappingTarget Usuario entity);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateTOEntity(PerfilRequest to, @MappingTarget Perfil entity);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateTOEntity(VagaRequest to, @MappingTarget Vaga entity);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateEntityTO(Vaga entity, @MappingTarget VagaRequest to);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateTOEntity(CandidaturaRequest to, @MappingTarget Candidatura entity);
}
