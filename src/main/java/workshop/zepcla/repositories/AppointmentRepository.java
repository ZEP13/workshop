package workshop.zepcla.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import workshop.zepcla.entities.AppointmentEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    boolean existsByClientIdAndDateAndTime(Long clientId, LocalDate date, LocalTime time);

    boolean existsByDateAndTime(LocalDate date, LocalTime time);

    List<AppointmentEntity> findByClientId(Long clientId);

    List<AppointmentEntity> findByCreatorId(Long creatorId);

    List<AppointmentEntity> findByDate(LocalDate date);
}
