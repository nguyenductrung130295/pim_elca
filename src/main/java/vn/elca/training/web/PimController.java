package vn.elca.training.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("project")
public class PimController {
    @RequestMapping("/")
    @ResponseBody
    String test() {
        return "lkasjdlkfas";
    }
}
