package dev.vmillet.tripr.base.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.UUID


@Entity
@Table(name = "\"User\"")
class User {

    @Id
    @Column(
        nullable = false,
        updatable = false
    )
    @SequenceGenerator(
        name = "primary_sequence",
        sequenceName = "primary_sequence",
        allocationSize = 1,
        initialValue = 10000
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "primary_sequence"
    )
    var id: Long? = null

    @Column(
        nullable = false,
        unique = true
    )
    var email: String? = null

    @Column(nullable = false)
    var password: String? = null

    @Column
    var passwordResetUid: UUID? = null

    @Column
    var passwordResetStart: OffsetDateTime? = null

    @OneToMany(mappedBy = "user")
    var refreshTokens: MutableSet<RefreshToken>? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "role_id",
        nullable = false
    )
    var role: Role? = null

}
