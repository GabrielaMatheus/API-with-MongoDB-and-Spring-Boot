package projeto.locadora.locadora.controller.form.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import projeto.locadora.locadora.controller.form.AcessorioForm;
import projeto.locadora.locadora.model.Acessorio;

@Mapper
public interface AcessorioMapper {
    AcessorioMapper INSTANCE = Mappers.getMapper(AcessorioMapper.class);

    Acessorio acessorioFormToAcessorio(AcessorioForm acessorioForm);
}
