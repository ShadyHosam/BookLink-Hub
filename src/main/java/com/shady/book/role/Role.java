package com.shady.book.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shady.book.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue
    private Integer id;

    // The role must be unqiue
    @Column(unique = true)
    private String name;

    // One User maybe have many roles, one Role maybe have many users. so it's a many-to-many relationship
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> users;


    @CreatedDate
    @Column(nullable = false , updatable = false)
    private LocalDateTime createdDate;

    @CreatedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
}
