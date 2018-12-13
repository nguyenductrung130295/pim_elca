package vn.elca.training.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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
import vn.elca.training.exception.DifferenceStatusProjectDeleteException;
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
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String PAGE_TYPE_EDIT = "edit";
    private static final String PAGE_TYPE_NEW = "new";
    @Autowired
    IProjectService projectService;
    @Autowired
    IGroupService groupService;
    @Autowired
    StoreSessionValue sessionValue;
    @Autowired
    IEmployeeService employeeService;

    @RequestMapping("/")
    String test() {
        projectService.initData();
        return "redirect:list";
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
        boolean flagError = false;
        boolean existNumber = false;
        try {
            projectService.checkProjectNumberExits(project.getProjectNumber());
        } catch (ProjectNumberAlreadyExistsException e) {
            existNumber = true;
        }
        if (bindingResult.hasErrors() || AppUtils.isNeedMandatoryProjectField(project)// || existNumber
                || checkByVisa(AppUtils.splitVisaMember(listMemberVISA)) != "") {
            flagError = true;
        }
        if (flagError || existNumber) {
            model.addAttribute("errorValidate", flagError);
            model.addAttribute("listMember", listMemberVISA);
            model.addAttribute("type", PAGE_TYPE_NEW);
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
        model.addAttribute("type", PAGE_TYPE_EDIT);
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
        if (bindingResult.hasErrors() || AppUtils.isNeedMandatoryProjectField(project)
                || (listMemberVISA.length() > 0 && checkByVisa(AppUtils.splitVisaMember(listMemberVISA)) != "")) {
            model.addAttribute("errorValidate", "true");
            model.addAttribute("listMember", listMemberVISA);
            model.addAttribute("type", PAGE_TYPE_EDIT);
            model.addAttribute("existProject", false);
            model.addAttribute("groupProject", groupService.getAllGroup());
            return "new";// template new
        }
        try {
            if (!projectService.updateProject(project, AppUtils.splitVisaMember(listMemberVISA))) {
                return "redirect:/project/error";
            }
            return "redirect:/project/list";
        } catch (ObjectOptimisticLockingFailureException e) {
            model.addAttribute("errorValidate", "false");
            model.addAttribute("errorUpdate", "true");
            model.addAttribute("listMember", listMemberVISA);
            model.addAttribute("type", PAGE_TYPE_EDIT);
            model.addAttribute("existProject", false);
            model.addAttribute("groupProject", groupService.getAllGroup());
            return "new";// template new
        }
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
    ModelAndView listProject(@RequestParam(value = "p") Optional<Integer> page) {
        int pageNumber = AppUtils.PAGE_NUMBER_DEFAULT;
        if (page.hashCode() != 0) {
            pageNumber = page.hashCode();
        }
        if ("".equals(sessionValue.getTextQuery()) && "".equals(sessionValue.getStatusQuery())) {
            return new ModelAndView("list", "projectList",
                    projectService.findProjectAll(pageNumber, AppUtils.ROW_ON_PAGE));
        } else {
            return new ModelAndView("list", "projectList", projectService.findProjectByQuery(
                    sessionValue.getTextQuery(), sessionValue.getStatusQuery(), pageNumber, AppUtils.ROW_ON_PAGE));
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
    ModelAndView query(@RequestParam(value = "p") Optional<Integer> page,
            @RequestParam(value = "text_search") String strQuery,
            @RequestParam(value = "status_search") String statusQuery) {
        int pageNumber = AppUtils.PAGE_NUMBER_DEFAULT;
        if (page.hashCode() != 0) {
            pageNumber = page.hashCode();
        }
        sessionValue.setTextQuery(strQuery);
        sessionValue.setStatusQuery(statusQuery);
        return new ModelAndView("list", "projectList",
                projectService.findProjectByQuery(strQuery, statusQuery, pageNumber, AppUtils.ROW_ON_PAGE))
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
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
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
        } catch (NoSuchElementException | ObjectOptimisticLockingFailureException e) {
            return "fail_null";
            // e.printStackTrace();
        } catch (DifferenceStatusProjectDeleteException e) {
            return "fail_status";
            // e.printStackTrace();
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
        } catch (NoSuchElementException | ObjectOptimisticLockingFailureException e1) {
            return "fail_null";
            // e1.printStackTrace();
        } catch (DifferenceStatusProjectDeleteException e1) {
            return "fail_status";
            // e1.printStackTrace();
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
     * Function Check visa member exist
     * 
     * @param visa
     * @return list visa not exist in db
     */
    private String checkByVisa(String[] visa) {
        return (visa == null || visa.length == 0) ? "" : employeeService.checkedEmployee(visa);
    }
}
