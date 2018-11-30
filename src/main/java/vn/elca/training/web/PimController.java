package vn.elca.training.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
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
import vn.elca.training.services.IEmployeeService;
import vn.elca.training.services.IGroupService;
import vn.elca.training.services.IProjectService;
import vn.elca.training.utils.AppUtils;

@Controller("project")
@SessionAttributes({ "strQuery", "statusQuery" })
@RequestMapping("/project")
@Scope("session")
public class PimController {
    @Autowired
    IProjectService projectService;
    @Autowired
    IGroupService groupService;
    @Autowired
    StoreSessionValue sessionValue;
    @Autowired
    IEmployeeService employeeService;

    @RequestMapping("/")
    @ResponseBody
    String test() {
        return "please access localhost:/8080/project/list";
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
        boolean existNumber;
        try {
            projectService.checkProjectNumberExits(project.getProjectNumber());
            existNumber = false;
        } catch (ProjectNumberAlreadyExistsException e) {
            // e.printStackTrace();
            System.out.println("EXIST PROJECT NUMBER");
            existNumber = true;
        }
        System.out.println("-------------------<<>>:" + existNumber);
        if (bindingResult.hasErrors() || AppUtils.isNeedMandatoryProjectField(project) || existNumber) {
            model.addAttribute("errorValidate", "true");
            model.addAttribute("listMember", "");// ????????????????????????
            model.addAttribute("type", "new");
            model.addAttribute("existProject", existNumber);
            model.addAttribute("groupProject", groupService.getAllGroup());
            return "new";
        }
        project.setId(Long.valueOf(project.getProjectNumber()));// ?????NOTE:whatelksdjfalksjfdlaksjdflaksjdlk
        if (!projectService.createProject(project)) {
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
        if ("".equals(sessionValue.getTextQuery()) && "".equals(sessionValue.getStatusQuery())) {
            return new ModelAndView("list", "projectList", projectService.findProjectAll());
        } else {
            return new ModelAndView("list", "projectList",
                    projectService.findProjectByQuery(sessionValue.getTextQuery(), sessionValue.getStatusQuery()));
        }
    }

    @PostMapping("/query")
    ModelAndView query(@RequestParam(value = "text_search") String strQuery,
            @RequestParam(value = "status_search") String statusQuery) {
        sessionValue.setTextQuery(strQuery);
        sessionValue.setStatusQuery(statusQuery);
        return new ModelAndView("list", "projectList", projectService.findProjectByQuery(strQuery, statusQuery))
                .addObject("strQuery", strQuery).addObject("statusQuery", statusQuery);
    }

    @GetMapping("/query")
    String queryreset(@RequestParam(value = "text_search") String strQuery,
            @RequestParam(value = "status_search") String statusQuery, Model model) {
        sessionValue.setTextQuery(strQuery);
        sessionValue.setStatusQuery(statusQuery);
        model.addAttribute("strQuery", "");
        model.addAttribute("statusQuery", "");
        return "redirect:list";
    }

    @RequestMapping("/checkprojectid")
    @ResponseBody
    String projectExists(@RequestParam("project_number") int projectNumber) {
        try {
            projectService.checkProjectNumberExits(projectNumber);
            return "success";
        } catch (ProjectNumberAlreadyExistsException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
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

    @PostMapping("/delallselect")
    @ResponseBody
    List<Integer> deleteListProject(@RequestParam("list_number[]") int[] projectNumberList) {
        return projectService.delteProjectNumberList(projectNumberList);
    }

    @RequestMapping("/delete")
    @ResponseBody
    String deleteProject(@RequestParam("idproject") Long idProject) {
        if (projectService.deleteProjectById(idProject)) {
            return "success";
        } else {
            return "fail";
        }
    }

    @PostMapping("/checkvisa")
    @ResponseBody
    String checkVisa(@RequestParam("list_visa[]") String[] visa) {
        String result = "";
        for (int i = 0; i < visa.length; i++) {
            if (!employeeService.checkedEmployee(visa[i])) {
                result += visa[i] + ",";
            }
        }
        if (result.length() > 1) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
