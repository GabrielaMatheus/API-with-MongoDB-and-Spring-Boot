package projeto.locadora.locadora.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import projeto.locadora.locadora.model.Cliente;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, String> {
    Cliente getReferenceById(String id);

    Optional<Cliente> findByCpf(String cpf);
}
