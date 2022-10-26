package projeto.locadora.locadora.controller.form.mappers;

import javax.annotation.processing.Generated;
import projeto.locadora.locadora.controller.form.ClienteForm;
import projeto.locadora.locadora.model.Cliente;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-10-26T14:53:41-0300",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class ClienteMapperImpl implements ClienteMapper {

    @Override
    public Cliente clienteFormToCliente(ClienteForm clienteForm) {
        if ( clienteForm == null ) {
            return null;
        }

        Cliente cliente = new Cliente();

        cliente.setId( clienteForm.getId() );
        cliente.setCpf( clienteForm.getCpf() );
        cliente.setNome( clienteForm.getNome() );
        cliente.setSobrenome( clienteForm.getSobrenome() );
        cliente.setDataNascimento( clienteForm.getDataNascimento() );
        cliente.setEmail( clienteForm.getEmail() );
        cliente.setTelefone( clienteForm.getTelefone() );

        return cliente;
    }
}
