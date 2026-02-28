package workshop.zepcla.entities;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import workshop.zepcla.customAnotations.ValidPhone;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(nullable = false)
    @NotBlank(message = "First name is required")
    private String firstname;

    @Column(nullable = false)
    @NotBlank(message = "Last name is required")
    private String lastname;

    @Email(message = "Email should be valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    private String email;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Phone number is required")
    @ValidPhone(countries = { "BE", "FR" })
    private String phone;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*]).*$", message = "Password must contain at least one digit and one special character")
    private String password;

    @Builder.Default
    @Column(nullable = false)
    @ColumnDefault("'CLIENT'")
    private String role = "CLIENT";
}
