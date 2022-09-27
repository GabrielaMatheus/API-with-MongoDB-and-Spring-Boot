package projeto.locadora.locadora.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import projeto.locadora.locadora.model.AluguelValor;

@Repository
public interface AluguelValorRepository extends MongoRepository<AluguelValor, String> {
    AluguelValor findByIdAluguel(String idAluguel);

    AluguelValor getReferenceByIdAluguel(String id);
}
