package projeto.locadora.locadora.controller.form.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import projeto.locadora.locadora.controller.form.AluguelValorForm;
import projeto.locadora.locadora.model.AluguelValor;

@Mapper
public interface AluguelValorMapper {
    AluguelValorMapper INSTANCE = Mappers.getMapper(AluguelValorMapper.class);

    AluguelValor aluguelValorFormToAluguelValor(AluguelValorForm aluguelForm);
}
