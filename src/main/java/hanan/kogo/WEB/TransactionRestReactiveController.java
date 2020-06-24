package hanan.kogo.WEB;

import hanan.kogo.DAO.SocieteRepository;
import hanan.kogo.DAO.TransactionRepository;
import hanan.kogo.ENTITIES.Societe;
import hanan.kogo.ENTITIES.Transaction;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

//we can't use spring data Rest , the reactive version doesn't exist yet, so we create our RestApi
@RestController
public class TransactionRestReactiveController {

    private WebClient webClient;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SocieteRepository societeRepository;

    @GetMapping(value = "/transactions")
    public Flux<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @GetMapping(value = "/transactions/{id}")
    public Mono<Transaction> getOne(@PathVariable String id) {
        return transactionRepository.findById(id);
    }

    @PostMapping("/transactions")
    public Mono<Transaction> saveS(@RequestBody Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @DeleteMapping("/transactions/{id}")
    public Mono<Void> DeleteS(@PathVariable String id) {
        return transactionRepository.deleteById(id);
    }

    @PutMapping("/transactions/{id}")
    public Mono<Transaction> updateS(@RequestBody Transaction transaction, @PathVariable String id) {
        transaction.setId(id);
        return transactionRepository.save(transaction);
    }

    @GetMapping(value = "/streamTransactions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Transaction> getStreamTransactions() {
        return transactionRepository.findAll();
    }

    @GetMapping(value = "/transactionBySociety/{id}")
    public Flux<Transaction> transactionBySociety(@PathVariable String id) {
        Societe societe = new Societe();
        societe.setId(id);//je recupere seulement l'id de la societe
        return transactionRepository.findBySociete(societe);
    }

    @GetMapping(value = "/streamTransactionBySociety/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Transaction> streamTransactionBySociety(@PathVariable String id) {

        //return societeRepository.findById(id)-> return a MONO
        //flatMapMany-> permet de le transformer en format FLUX
        return societeRepository.findById(id).flatMapMany(soc -> {

            //creation d'un stream sous forme d'un timer pr chaque seconde
            Flux<Long> intervale = Flux.interval(Duration.ofMillis(1000));//apres chaque seconde je retourne une transaction
            //je genere un flux de transaction depuis un stream qui retourne la transaction cree
            Flux<Transaction> transactionFlux = Flux.fromStream(Stream.generate(() -> {
                Transaction transaction = new Transaction();
                transaction.setInstant(Instant.now());
                transaction.setSociete(soc);
                transaction.setPrice(soc.getPrice() * (1 + Math.random() * 12 - 6) / 100);
                return transaction;
            }));
            return Flux.zip(intervale, transactionFlux)//zip me regroupe les deux flux que j'ai retourne avant et a l'aide de MAP
                    //je peux choisir ce que je veux avoir en final comme resultat
                    .map(data -> {
                        return data.getT2();
                    }).share();//cette ligne demande a sspringFlux de partager les memes donnees avec d'autres client tant que
                               //ce stream est ouvert

            //alors a la fin pour chaque seconde je retourne un flux de transaction

        });
    }


    //cette methode responsable de connecter ce micro a l'autre micro [event] via WebClient
    /*@GetMapping(value = "/events/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Event> events(@PathVariable String id) {
      webClient = WebClient.create("http://localhost:8081");//localhost de l'autre microservice
      Flux<Event> eventFlux = webClient.get()
              .uri("/streamEvents/"+id)
              .retrieve().bodyToFlux(Event.class);
      return eventFlux;
    }*/

    //au lieu de retourner obet event je retourne la valeur value seulement
    //chaque stream que je recois je le transforme en flux apres je retire seulement la value et je la retourne
    @GetMapping(value = "/events/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Double> events(@PathVariable String id) {
        webClient = WebClient.create("http://localhost:8081");//localhost de l'autre microservice
        Flux<Double> eventFlux = webClient.get()
                .uri("/streamEvents/"+id)
                .retrieve().bodyToFlux(Event.class)
                .map(data->data.getValue());
        return eventFlux;
    }

    //pr tester le threat utilise -un seul thread non bloquant-
    @GetMapping("/test")
    public String test()
      {
        return Thread.currentThread().getName();
      }

}

@Data @NoArgsConstructor
class Event{

    private Instant instant;
    private double value;
    private String societeId;

}



