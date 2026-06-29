package vendas2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    /**
     * teste de template
     * @return string
     */
    @GetMapping("/")
    public String index() {
        System.out.println("[ViewController] index respondeu");
        return "index";  // Retorna /templates/index.html
    }

}
