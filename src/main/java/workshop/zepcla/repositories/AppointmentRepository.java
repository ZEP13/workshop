package workshop.zepcla.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import workshop.zepcla.entities.AppointmentEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    boolean existsByDateAndTime(LocalDate date, LocalTime time);

    boolean existsByIdClientAndDateAndTime(Long id_client, LocalDate date, LocalTime time);

    List<AppointmentEntity> findByDate(LocalDate date);

    List<AppointmentEntity> findByIdClient(Long id_client);

    List<AppointmentEntity> findByIdCreator(Long id_creator);
}