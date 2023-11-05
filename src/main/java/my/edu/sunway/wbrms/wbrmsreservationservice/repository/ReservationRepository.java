package my.edu.sunway.wbrms.wbrmsreservationservice.repository;

import my.edu.sunway.wbrms.wbrmsreservationservice.entity.ReservationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<ReservationEntity, UUID>,
        PagingAndSortingRepository<ReservationEntity, UUID>, JpaSpecificationExecutor<ReservationEntity> {

    @Query("SELECT reservation FROM ReservationEntity reservation WHERE reservation.desiredDateTime >= :today ORDER BY reservation.desiredDateTime DESC")
    Page<ReservationEntity> findByDesiredDateTimeBeforeOrderByDesiredDateTimeDesc(LocalDateTime today, Pageable pageable);
}
