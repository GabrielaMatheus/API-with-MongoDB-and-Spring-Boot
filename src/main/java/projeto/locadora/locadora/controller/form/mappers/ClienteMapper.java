package projeto.locadora.locadora.controller.form.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import projeto.locadora.locadora.controller.form.ClienteForm;
import projeto.locadora.locadora.model.Cliente;

@Mapper
public interface ClienteMapper {
    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);

    Cliente clienteFormToCliente(ClienteForm clienteForm);
}
