package projeto.locadora.locadora.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import projeto.locadora.locadora.model.Carro;

@Repository
public interface CarroRepository extends MongoRepository<Carro, String> {
    Carro getReferenceById(String id);

    Optional<Carro> findByPlaca(String placa);
}
