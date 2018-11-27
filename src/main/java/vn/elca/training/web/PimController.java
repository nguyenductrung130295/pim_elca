package vn.elca.training.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import vn.elca.training.entities.Project;
import vn.elca.training.exception.ProjectNumberAlreadyExistsException;
import vn.elca.training.services.IGroupService;
import vn.elca.training.services.IProjectService;
import vn.elca.training.utils.AppUtils;

@Controller("project")
@SessionAttributes("strQuery")
@RequestMapping("/project")
public class PimController {
    @Autowired
    IProjectService projectService;
    @Autowired
    IGroupService groupService;

    @RequestMapping("/")
    @ResponseBody
    String test() {
        return "lkasjdlkfas";
    }

    @RequestMapping("/new")
    String newProject(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("listMember", "");
        model.addAttribute("type", "new");
        model.addAttribute("groupProject", groupService.getAllGroup());
        return "new";
    }

    @PostMapping("/create")
    String createProject(@ModelAttribute("project") Project project,
            @RequestParam("project_member") String listMemberVISA, BindingResult bindingResult, Model model) {
        System.out.println("#######Create method invoked: " + project.getStatus() + " ######VISA:" + listMemberVISA);
        if (bindingResult.hasErrors() || AppUtils.isNeedMandatoryProjectField(project)) {
            model.addAttribute("errorValidate", "true");
            model.addAttribute("listMember", "");// ????????????????????????
            model.addAttribute("type", "new");
            model.addAttribute("groupProject", groupService.getAllGroup());
            return "new";
        }
        project.setId(Long.valueOf(project.getProjectNumber()));// ?????NOTE:whatelksdjfalksjfdlaksjdflaksjdlk
        if (projectService.createProject(project) == 0) {
            return "redirect:error";
        }
        return "redirect:list";
    }

    @RequestMapping("/{id}/edit")
    String editProject(@PathVariable("id") Long id, Model model) {
        Project project = projectService.findProjectById(id);
        model.addAttribute("type", "edit");
        model.addAttribute("project", project);
        model.addAttribute("groupProject", groupService.getAllGroup());
        model.addAttribute("listMember", "TYU,HHH,UUU,GGG");// ????????????
        return "new";
    }

    @PostMapping("/update")
    String updateProject() {
        return "redirect:list";
    }

    @RequestMapping("/error")
    String errorPage() {
        return "errorpage";
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

    @RequestMapping("/checkprojectid")
    @ResponseBody
    String projectExists(@RequestParam("projectid") Long id) {
        try {
            projectService.checkIdProjectExits(id);
            return "success";
        } catch (ProjectNumberAlreadyExistsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "error";
        }
    }

    @InitBinder("project")
    public void customizeBinding(WebDataBinder binder) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        dateFormatter.setLenient(false);
        binder.registerCustomEditor(Date.class, "startDate", new CustomDateEditor(dateFormatter, true));
        binder.registerCustomEditor(Date.class, "endDate", new CustomDateEditor(dateFormatter, true));
    }
}
