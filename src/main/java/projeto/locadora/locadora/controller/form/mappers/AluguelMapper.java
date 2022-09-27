package projeto.locadora.locadora.controller.form.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import projeto.locadora.locadora.controller.form.AluguelForm;
import projeto.locadora.locadora.model.Aluguel;

@Mapper
public interface AluguelMapper {
    AluguelMapper INSTANCE = Mappers.getMapper(AluguelMapper.class);

    Aluguel aluguelFormToAluguel(AluguelForm aluguelForm);
}
