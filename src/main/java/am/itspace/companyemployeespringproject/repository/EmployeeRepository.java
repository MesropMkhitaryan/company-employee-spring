package am.itspace.companyemployeespringproject.repository;

import am.itspace.companyemployeespringproject.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
