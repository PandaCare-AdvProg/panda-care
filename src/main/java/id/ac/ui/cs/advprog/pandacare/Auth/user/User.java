package id.ac.ui.cs.advprog.pandacare.Auth.user;

import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails{

    @Id
    @GeneratedValue
    private Integer id;

    private Integer nik;

    private String name;

    private String email;

    private String password;

    private String address;

    private String phonenum;

    private String role;


}
