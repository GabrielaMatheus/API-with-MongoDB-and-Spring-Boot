package projeto.locadora.locadora.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import projeto.locadora.locadora.model.Acessorio;

import java.util.Optional;

@Repository
public interface AcessorioRepository extends MongoRepository<Acessorio, String> {

    Optional<Acessorio> findByDoc(String doc);
    Optional<Acessorio> findByDoc(String id, String email);
}
