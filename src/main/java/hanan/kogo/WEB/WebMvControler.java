package hanan.kogo.WEB;

import hanan.kogo.DAO.SocieteRepository;
import hanan.kogo.DAO.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Data @NoArgsConstructor @AllArgsConstructor
public class WebMvControler {

    @Autowired
    private SocieteRepository societeRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute("societes",societeRepository.findAll());
        return "index";
    }
}
