package projeto.locadora.locadora.controller.form.mappers;

import javax.annotation.processing.Generated;
import projeto.locadora.locadora.controller.form.AluguelValorForm;
import projeto.locadora.locadora.model.AluguelValor;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-10-11T14:25:57-0300",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class AluguelValorMapperImpl implements AluguelValorMapper {

    @Override
    public AluguelValor aluguelValorFormToAluguelValor(AluguelValorForm aluguelForm) {
        if ( aluguelForm == null ) {
            return null;
        }

        AluguelValor aluguelValor = new AluguelValor();

        aluguelValor.setId( aluguelForm.getId() );
        aluguelValor.setIdAluguel( aluguelForm.getIdAluguel() );
        aluguelValor.setValor( aluguelForm.getValor() );

        return aluguelValor;
    }
}
