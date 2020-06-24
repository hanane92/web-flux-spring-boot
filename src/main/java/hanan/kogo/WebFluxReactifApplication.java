package hanan.kogo;

import hanan.kogo.DAO.SocieteRepository;
import hanan.kogo.DAO.TransactionRepository;
import hanan.kogo.ENTITIES.Societe;
import hanan.kogo.ENTITIES.Transaction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.stream.Stream;

@SpringBootApplication
public class WebFluxReactifApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(WebFluxReactifApplication.class, args);
    }

    @Bean
    CommandLineRunner start(SocieteRepository societeRepository, TransactionRepository transactionRepository){


        return args->{
    // une fois je termine la suppression et si tt passe bien je vais inserer les societes
            societeRepository.deleteAll().subscribe(null,null,()->{

                Stream.of("SG","AWB","AXA").forEach(s->{
                    societeRepository.save(new Societe(s,s,100+Math.random()*900))//save return un mono [pas bloquant]
                            .subscribe(soc->{
                                System.out.println(soc.toString());
                            });
                    //100+Math.random()*900):un nombre aleatoire entre 100 et 1000
                });

                //add transaction for every society and return list of transactions,but before we have to delete all
                //transactions
                transactionRepository.deleteAll().subscribe(null,null,()->{
                    Stream.of("SG","AWB","BMCE","AXA").forEach(s->{
                        societeRepository.findById(s).subscribe(soc->{
                            for (int i = 0; i <10 ; i++) {
                                Transaction transaction = new Transaction();
                                transaction.setInstant(Instant.now());
                                transaction.setPrice(soc.getPrice()*(1+Math.random()*12-6)/100);
                                transaction.setSociete(soc);

                                transactionRepository.save(transaction).subscribe(t->{
                                    System.out.println(t.toString());
                                });
                            }

                        });
                    });
                });

            });
           // System.out.println("ce message sera affiche avant l'insertion des societes prcq on est dans la programmtion reactive");
        };
    }
}
