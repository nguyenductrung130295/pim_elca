package vn.elca.training.web;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        projectService.initData();
        return "please access localhost:/8080/project/list";
    }

    /**
     * Show create project page
     * 
     * @param model
     * @return
     */
    @RequestMapping("/new")
    String newProject(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("listMember", "");
        model.addAttribute("type", "new");
        model.addAttribute("groupProject", groupService.getAllGroup());
        return "new";
    }

    /**
     * Create project by method post, check error and save
     * 
     * @param project
     * @param listMemberVISA
     * @param bindingResult
     * @param model
     * @return
     */
    @PostMapping("/create")
    String createProject(@ModelAttribute("project") Project project,
            @RequestParam("project_member") String listMemberVISA, BindingResult bindingResult, Model model) {
        boolean existNumber = false;
        try {
            projectService.checkProjectNumberExits(project.getProjectNumber());
        } catch (ProjectNumberAlreadyExistsException e) {
            existNumber = true;
        }
        if (bindingResult.hasErrors() || AppUtils.isNeedMandatoryProjectField(project) || existNumber
                || checkByVisa(AppUtils.splitVisaMember(listMemberVISA)) != "") {
            model.addAttribute("errorValidate", "true");
            model.addAttribute("listMember", listMemberVISA);
            model.addAttribute("type", "new");
            model.addAttribute("existProject", existNumber);
            model.addAttribute("groupProject", groupService.getAllGroup());
            return "new";
        }
        if (!projectService.createProject(project, AppUtils.splitVisaMember(listMemberVISA))) {
            return "redirect:/project/error";
        }
        return "redirect:list";
    }

    /**
     * Show edit project page
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/{id}/edit")
    String editProject(@PathVariable("id") Long id, Model model) {
        Project project = projectService.findProjectById(id);
        model.addAttribute("type", "edit");
        model.addAttribute("project", project);
        model.addAttribute("groupProject", groupService.getAllGroup());
        model.addAttribute("listMember", projectService.getListMemberVisaOfProject(project));
        return "new";
    }

    /**
     * Update project
     * 
     * @param id
     * @param project
     * @param listMemberVISA
     * @param bindingResult
     * @param model
     * @return
     */
    @PostMapping("/{id}/update")
    String updateProject(@PathVariable("id") Long id, @ModelAttribute("project") Project project,
            @RequestParam("project_member") String listMemberVISA, BindingResult bindingResult, Model model) {
        boolean existNumber = false;
        try {
            projectService.checkProjectNumberExits(project.getProjectNumber());
        } catch (ProjectNumberAlreadyExistsException e) {
            existNumber = true;
        }
        if (bindingResult.hasErrors() || AppUtils.isNeedMandatoryProjectField(project) || !existNumber
                || checkByVisa(AppUtils.splitVisaMember(listMemberVISA)) != "") {
            model.addAttribute("errorValidate", "true");
            model.addAttribute("listMember", listMemberVISA);
            model.addAttribute("type", "edit");
            model.addAttribute("existProject", !existNumber);
            model.addAttribute("groupProject", groupService.getAllGroup());
            return "new";// template new
        }
        if (!projectService.updateProject(project, AppUtils.splitVisaMember(listMemberVISA))) {
            return "redirect:/project/error";
        }
        return "redirect:/project/list";
    }

    /**
     * show page error when have any exception
     * 
     * @return
     */
    @RequestMapping("/error")
    String errorPage() {
        return "errorpage";
    }

    /**
     * Get list project and show search list page
     * 
     * @return
     */
    @RequestMapping("/list")
    ModelAndView listProject() {
        if ("".equals(sessionValue.getTextQuery()) && "".equals(sessionValue.getStatusQuery())) {
            return new ModelAndView("list", "projectList", projectService.findProjectAll());
        } else {
            return new ModelAndView("list", "projectList",
                    projectService.findProjectByQuery(sessionValue.getTextQuery(), sessionValue.getStatusQuery()));
        }
    }

    /**
     * Search project by name or project number or customer name and project status
     * 
     * @param strQuery
     * @param statusQuery
     * @return list of project
     */
    @PostMapping("/query")
    ModelAndView query(@RequestParam(value = "text_search") String strQuery,
            @RequestParam(value = "status_search") String statusQuery) {
        sessionValue.setTextQuery(strQuery);
        sessionValue.setStatusQuery(statusQuery);
        return new ModelAndView("list", "projectList", projectService.findProjectByQuery(strQuery, statusQuery))
                .addObject("strQuery", strQuery).addObject("statusQuery", statusQuery);
    }

    /**
     * show search and list project when call get method
     */
    @GetMapping("/query")
    String queryreset(@RequestParam(value = "text_search") String strQuery,
            @RequestParam(value = "status_search") String statusQuery, Model model) {
        sessionValue.setTextQuery(strQuery);
        sessionValue.setStatusQuery(statusQuery);
        model.addAttribute("strQuery", "");
        model.addAttribute("statusQuery", "");
        return "redirect:list";
    }

    /**
     * Check project number exists when entered Number Project From Field on Create Project Page
     * 
     * @param projectNumber
     * @return
     */
    @RequestMapping("/checkprojectid")
    @ResponseBody
    String projectExists(@RequestParam("project_number") int projectNumber) {
        try {
            projectService.checkProjectNumberExits(projectNumber);
            return "success";
        } catch (ProjectNumberAlreadyExistsException e) {
            return "error";
        }
    }

    /**
     * Binder for start data and end date field in form
     * 
     * @param binder
     */
    @InitBinder("project")
    public void customizeBinding(WebDataBinder binder) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        dateFormatter.setLenient(false);
        binder.registerCustomEditor(Date.class, "startDate", new CustomDateEditor(dateFormatter, true));
        binder.registerCustomEditor(Date.class, "endDate", new CustomDateEditor(dateFormatter, true));
    }

    /**
     * Delete all of selected project be called by ajax
     * 
     * @param projectIds
     * @return
     */
    @PostMapping("/delallselect")
    @ResponseBody
    String deleteListProject(@RequestParam("list_number[]") Long[] projectIds) {
        try {
            projectService.deleteProjectByListIdAndNewStatus(projectIds);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }

    /**
     * Ajax call to delete selected project and response status.
     * 
     * @param idProject
     * @return string of status is deleted success or fail.
     */
    @RequestMapping("/delete")
    @ResponseBody
    String deleteProject(@RequestParam("idproject") Long idProject) {
        try {
            projectService.deleteProjectByIdAndNewStatus(idProject);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    /**
     * Ajax call Check visa exist of employee memmber for project
     * 
     * @param visa
     * @return list visa not exists in database
     */
    @PostMapping("/checkvisa")
    @ResponseBody
    String checkVisa(@RequestParam("list_visa[]") String[] visa) {
        return checkByVisa(visa);
    }

    /**
     * Check visa member exist
     * 
     * @param visa
     * @return list visa not exist in db
     */
    private String checkByVisa(String[] visa) {
        return visa == null ? "" : employeeService.checkedEmployee(visa);
    }
}
