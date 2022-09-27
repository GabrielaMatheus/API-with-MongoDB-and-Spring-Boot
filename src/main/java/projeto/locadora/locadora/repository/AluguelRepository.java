package projeto.locadora.locadora.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projeto.locadora.locadora.model.Aluguel;

@Repository
public interface AluguelRepository extends MongoRepository<Aluguel, String> {
    Aluguel getReferenceById(String id);

    @Aggregation(pipeline = { "{$match:{placa_carro: :#{#placa},dataDevolucao: null}}" })
    Optional<Aluguel> findByPlacaCarro(@Param("placa") String placa);

    //não exige que a dataDevolução esteja vazia.
    @Aggregation(pipeline = { "{$match:{placa_carro: :#{#placa}}}" })
    List<Aluguel> findByPlaca(@Param("placa") String placa);
}
