package hanan.kogo.DAO;

import hanan.kogo.ENTITIES.Societe;
import hanan.kogo.ENTITIES.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction,String> {

    //meme si j pas une relation bidirect entre societe et transaction , cette methode me permet de recuperer les transaction
    //d'une societe
    public Flux<Transaction> findBySociete(Societe societe);
}
