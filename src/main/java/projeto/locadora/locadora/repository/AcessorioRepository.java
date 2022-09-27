package projeto.locadora.locadora.repository;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import projeto.locadora.locadora.model.Acessorio;

@Repository
public interface AcessorioRepository extends MongoRepository<Acessorio, String> {
    Acessorio getReferenceById(String id);

    Object findById(int i);

    Optional<Acessorio> findByDoc(String doc);
}
