package com.company.inventory.simple_inventory.model;

import com.company.inventory.simple_inventory.core.enums.Role;
import com.company.inventory.simple_inventory.repository.UserRepository;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true)
    private String username;

    private String firstname;

    private String lastname;

    @Column(unique = true)
    private String email;

    private String password;

    private LocalDateTime LastLogin;

    @Getter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "user")
    private Set<Transaction> transactions = new HashSet<>();

    public Set<Transaction> getAllUserTransactions() {return Collections.unmodifiableSet(transactions);}

    public void addUserTransaction(Transaction transaction) {
        if (transactions == null) transactions = new HashSet<>();
        transactions.add(transaction);
        transaction.setUser(this);
    }

    public void removeUserTransaction(Transaction transaction) {
        if (transactions == null) return;
        transactions.remove(transaction);
        transaction.setUser(null);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
