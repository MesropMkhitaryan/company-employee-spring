package am.itspace.companyemployeespringproject.repository;

import am.itspace.companyemployeespringproject.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
