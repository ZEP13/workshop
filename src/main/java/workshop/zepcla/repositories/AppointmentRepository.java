package workshop.zepcla.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import workshop.zepcla.entities.AppointmentEntity;
import workshop.zepcla.entities.UserEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
    boolean existsByClientAndDateAndTime(UserEntity client, LocalDate date, LocalTime time);

    boolean existsByDateAndTime(LocalDate date, LocalTime time);

    List<AppointmentEntity> findByClient(UserEntity client);

    List<AppointmentEntity> findByDate(LocalDate date);

    List<AppointmentEntity> findByCreator(UserEntity creator);

    Optional<AppointmentEntity> findByToken(String token);

    List<AppointmentEntity> findAllByToken(String token);
}
