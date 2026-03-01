package workshop.zepcla.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import workshop.zepcla.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<UserEntity> findByFirstNameAndLastName(String firstName, String lastName);

}
