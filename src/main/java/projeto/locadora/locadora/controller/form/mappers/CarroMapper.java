package projeto.locadora.locadora.controller.form.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import projeto.locadora.locadora.controller.form.CarroForm;
import projeto.locadora.locadora.model.Carro;

@Mapper
public interface CarroMapper {
    CarroMapper INSTANCE = Mappers.getMapper(CarroMapper.class);

    Carro carroFormToCarro(CarroForm carroForm);
}
