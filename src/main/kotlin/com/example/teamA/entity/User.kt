package com.example.teamA.entity

import lombok.Data
import org.hibernate.annotations.GenericGenerator
import java.util.Date
import java.util.UUID
import javax.persistence.*

@Entity
@Table(name="users")
@Data
data class User(
    @Id
    //var uuid : UUID = UUID.randomUUID(),
    var uuid : String = UUID.randomUUID().toString(),
    @Column(name="name", nullable = false)
    var name : String,
    @Column(name="email", nullable = false, unique = true)
    val email : String,
    @Column(name="password", nullable = false)
    var password: String,
    @Column(name="created_at", nullable = false)
    var created_at : Date,
    @Column(name="updated_at", nullable = false)
    var updated_at : Date
)

