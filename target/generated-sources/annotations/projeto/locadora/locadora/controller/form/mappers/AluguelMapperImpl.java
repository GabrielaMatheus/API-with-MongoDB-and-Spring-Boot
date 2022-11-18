package projeto.locadora.locadora.controller.form.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import projeto.locadora.locadora.controller.form.AluguelForm;
import projeto.locadora.locadora.model.Aluguel;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-11-16T14:23:28-0300",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
public class AluguelMapperImpl implements AluguelMapper {

    @Override
    public Aluguel aluguelFormToAluguel(AluguelForm aluguelForm) {
        if ( aluguelForm == null ) {
            return null;
        }

        Aluguel aluguel = new Aluguel();

        aluguel.setId( aluguelForm.getId() );
        aluguel.setPlaca_carro( aluguelForm.getPlaca_carro() );
        List<String> list = aluguelForm.getAcessorios();
        if ( list != null ) {
            aluguel.setAcessorios( new ArrayList<String>( list ) );
        }
        aluguel.setCpf( aluguelForm.getCpf() );
        aluguel.setDataAluguel( aluguelForm.getDataAluguel() );
        aluguel.setDataDevolucao( aluguelForm.getDataDevolucao() );
        aluguel.setTempoSolicitado( aluguelForm.getTempoSolicitado() );

        return aluguel;
    }
}
