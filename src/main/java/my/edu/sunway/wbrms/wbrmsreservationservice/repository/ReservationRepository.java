package my.edu.sunway.wbrms.wbrmsreservationservice.repository;

import my.edu.sunway.wbrms.wbrmsreservationservice.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReservationRepository extends JpaRepository<ReservationEntity, UUID> {
}
