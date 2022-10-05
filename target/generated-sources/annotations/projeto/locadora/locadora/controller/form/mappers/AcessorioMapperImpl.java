package projeto.locadora.locadora.controller.form.mappers;

import javax.annotation.processing.Generated;
import projeto.locadora.locadora.controller.form.AcessorioForm;
import projeto.locadora.locadora.model.Acessorio;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-10-05T20:11:26-0300",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class AcessorioMapperImpl implements AcessorioMapper {

    @Override
    public Acessorio acessorioFormToAcessorio(AcessorioForm acessorioForm) {
        if ( acessorioForm == null ) {
            return null;
        }

        Acessorio acessorio = new Acessorio();

        acessorio.setId( acessorioForm.getId() );
        acessorio.setDoc( acessorioForm.getDoc() );
        acessorio.setNome( acessorioForm.getNome() );
        acessorio.setValor( acessorioForm.getValor() );

        return acessorio;
    }
}
