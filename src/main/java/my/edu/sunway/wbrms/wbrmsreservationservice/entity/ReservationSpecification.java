package my.edu.sunway.wbrms.wbrmsreservationservice.entity;

import my.edu.sunway.wbrms.wbrmsreservationservice.dto.Status;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ReservationSpecification {

    private ReservationSpecification() {}

    public static Specification<ReservationEntity> hasName(String name) {
        return (root, query, cb) -> name == null ? null : cb.equal(root.get("name"), name);
    }

    public static Specification<ReservationEntity> hasPhone(String phone) {
        return (root, query, cb) -> phone == null ? null : cb.equal(root.get("phone"), phone);
    }

    public static Specification<ReservationEntity> hasStatus(Status status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<ReservationEntity> hasDesiredDateTime(LocalDateTime desiredDateTime) {
        return (root, query, cb) -> desiredDateTime == null ? null : cb.equal(root.get("desiredDateTime"), desiredDateTime);
    }

    public static Specification<ReservationEntity> hasPax(Integer minPax, Integer maxPax) {
        return (root, query, cb) -> {
            if (minPax == null && maxPax == null) {
                return null;
            } else if (minPax == null) {
                return cb.lessThanOrEqualTo(root.get("pax"), maxPax);
            } else if (maxPax == null) {
                return cb.greaterThanOrEqualTo(root.get("pax"), minPax);
            } else {
                return cb.between(root.get("pax"), minPax, maxPax);
            }
        };
    }

}
