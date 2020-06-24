package hanan.kogo.WEB;

import hanan.kogo.DAO.SocieteRepository;
import hanan.kogo.ENTITIES.Societe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//we can't use spring data Rest , the reactive version doesn't exist yet, so we create our RestApi
@RestController
public class SocieteRestReactiveController {

    @Autowired
    private SocieteRepository societeRepository;

    @GetMapping(value = "/societes")
    public Flux<Societe> findAll(){
        return societeRepository.findAll();
    }

    @GetMapping(value = "/societes/{id}")
    public Mono<Societe> getOne(@PathVariable String id){
        return societeRepository.findById(id);
    }

    @PostMapping("/societes")
    public Mono<Societe> saveS(@RequestBody Societe societe){
        return societeRepository.save(societe);
    }

    @DeleteMapping("/societes/{id}")
    public Mono<Void> DeleteS(@PathVariable String id){
        return societeRepository.deleteById(id);
    }

    @PutMapping("/societes/{id}")
    public Mono<Societe> updateS(@RequestBody Societe societe,@PathVariable String id){
        societe.setId(id);
        return societeRepository.save(societe);
    }

}
