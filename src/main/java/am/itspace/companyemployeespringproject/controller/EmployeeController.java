package am.itspace.companyemployeespringproject.controller;

import am.itspace.companyemployeespringproject.entity.Company;
import am.itspace.companyemployeespringproject.entity.Employee;
import am.itspace.companyemployeespringproject.repository.CompanyRepository;
import am.itspace.companyemployeespringproject.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    private final CompanyRepository companyRepository;

    @Value("${companyEmployeeProject.images.folder}")
    private String folderPath;

    @GetMapping("/employees")
    public String goToEmployeesPage(ModelMap modelMap) {
        modelMap.addAttribute("employeeList", employeeRepository.findAll());
        return "employees";
    }

    @GetMapping("/employee/add")
    public String goToAddEmployeePage(ModelMap modelMap) {
        modelMap.addAttribute("companyList", companyRepository.findAll());
        return "addEmployee";
    }

    @GetMapping(value = "/employee/getProfilePic", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("fileName") String fileName) throws IOException {
        InputStream inputStream = new FileInputStream(folderPath + fileName);
        return IOUtils.toByteArray(inputStream);
    }

    @PostMapping("/employee/add")
    public String addEmployee(@ModelAttribute Employee employee,
                              @RequestParam("image") MultipartFile file,
                              ModelMap modelMap) throws IOException {
        if (!file.isEmpty() && file.getSize() > 0) {
            if (file.getContentType() != null && !file.getContentType().contains("image")) {
                modelMap.addAttribute("fileContentTypeError", "wrong file content type");
                return "addEmployee";
            }
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File newFile = new File(folderPath + fileName);
            file.transferTo(newFile);
            employee.setPicUrl(fileName);
        }
        Company company = employee.getCompany();
        company.setSize(company.getSize() + 1);
        employeeRepository.save(employee);
        companyRepository.save(company);
        return "redirect:/employees";
    }

    @GetMapping("/employee/remove")
    public String removeEmployee(@RequestParam("id") int id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        Employee employee = employeeOptional.get();
        employee.getCompany().setSize(employee.getCompany().getSize() - 1);
        employeeRepository.deleteById(id);
        return "redirect:/employees";
    }
}
