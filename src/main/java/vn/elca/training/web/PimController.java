package vn.elca.training.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import vn.elca.training.entities.Project;
import vn.elca.training.services.IProjectService;

@Controller("project")
@SessionAttributes("strQuery")
@RequestMapping("/project")
public class PimController {
    @Autowired
    IProjectService projectService;

    @RequestMapping("/")
    @ResponseBody
    String test() {
        return "lkasjdlkfas";
    }

    @RequestMapping("/new")
    ModelAndView newProject() {
        return new ModelAndView("new", "project", new Project());
    }

    @RequestMapping("/{id}/edit")
    ModelAndView editProject(@PathVariable("id") Long id) {
        Project project = projectService.findProjectById(id);
        System.out.println("################ Project: " + project.getName());
        return new ModelAndView("new", "project", project);
    }

    @PostMapping("/update")
    String updateProject() {
        return "redirect:list";
    }

    @RequestMapping("/list")
    ModelAndView listProject() {
        return new ModelAndView("list", "projectList", projectService.findProjectAll());
    }

    @RequestMapping("/query")
    @ResponseBody
    List<Project> query(@RequestParam(value = "name") String name) {
        if ("".equals(name)) {
            // return projectService.findAll();
        } else {
            // return projectService.findByNameWithQueryDSL(name);
        }
        return null;
    }
}
