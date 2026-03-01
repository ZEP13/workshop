package workshop.zepcla.specifications;

import org.springframework.data.jpa.domain.Specification;

import workshop.zepcla.entities.UserEntity;

public class UserSpecification {

    public static Specification<UserEntity> hasLastName(String lastName) {
        return ((root, query, criteriaBuilder) -> lastName == null ? null
                : criteriaBuilder.like(root.get("lastName"), lastName));
    }

    public static Specification<UserEntity> hasEmail(String email) {
        return ((root, query, criteriaBuilder) -> email == null ? null
                : criteriaBuilder.like(root.get("email"), email));
    }

    public static Specification<UserEntity> hasFirstName(String firstName) {
        return ((root, query, criteriaBuilder) -> firstName == null ? null
                : criteriaBuilder.like(root.get("firstName"), firstName));

    }

    public static Specification<UserEntity> hasRole(String role) {
        return ((root, query, criteriaBuilder) -> role == null ? null
                : criteriaBuilder.like(root.get("role"), role));
    }

    public static Specification<UserEntity> hasPhoneNumber(String phoneNumber) {
        return ((root, query, criteriaBuilder) -> phoneNumber == null ? null
                : criteriaBuilder.like(root.get("phone"), phoneNumber));
    }
}
