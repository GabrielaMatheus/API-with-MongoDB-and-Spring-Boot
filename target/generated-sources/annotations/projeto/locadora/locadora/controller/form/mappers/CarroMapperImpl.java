package projeto.locadora.locadora.controller.form.mappers;

import javax.annotation.processing.Generated;
import projeto.locadora.locadora.controller.form.CarroForm;
import projeto.locadora.locadora.model.Carro;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-10-05T20:11:26-0300",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class CarroMapperImpl implements CarroMapper {

    @Override
    public Carro carroFormToCarro(CarroForm carroForm) {
        if ( carroForm == null ) {
            return null;
        }

        Carro carro = new Carro();

        carro.setId( carroForm.getId() );
        carro.setPlaca( carroForm.getPlaca() );
        carro.setMarca( carroForm.getMarca() );
        carro.setModelo( carroForm.getModelo() );
        carro.setAno( carroForm.getAno() );
        carro.setCor( carroForm.getCor() );
        carro.setValor( carroForm.getValor() );

        return carro;
    }
}
