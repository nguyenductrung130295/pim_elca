package vn.elca.training.web;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import vn.elca.training.entities.Project;

@Controller("project")
@RequestMapping("/project")
public class PimController {
    @RequestMapping("/")
    @ResponseBody
    String test() {
        return "lkasjdlkfas";
    }

    @RequestMapping("/new")
    ModelAndView newProject() {
        return new ModelAndView("new", "project", new Project());
    }

    @RequestMapping("/list")
    ModelAndView listProject() {
        return new ModelAndView("list", "projectList", new ArrayList<Project>());
    }
}
