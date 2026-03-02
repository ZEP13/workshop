package workshop.zepcla.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import workshop.zepcla.entities.EnterpriseEntity;

public interface EnterpriseRepository extends JpaRepository<EnterpriseEntity, Long> {

    boolean existsByName(String name);
}
