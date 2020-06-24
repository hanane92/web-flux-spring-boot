package hanan.kogo.DAO;

import hanan.kogo.ENTITIES.Societe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SocieteRepository extends ReactiveMongoRepository<Societe,String> {
}
