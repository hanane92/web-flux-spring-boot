package hanan.kogo.ENTITIES;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Transaction {

    @Id
    private String id;
    private Instant instant;
    private double price;
    @DBRef //je fais reference a une societe qui va etre stocke dans une autre collection societe
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)//details societe seront pas [serialise]afficher pr chaque transation
    //affiches seulement au cas d'insertion
    private Societe societe;

}
