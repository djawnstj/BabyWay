package com.djawnstj.babyway.global.common

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
        protected set

    @field:CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.MIN

    @field:UpdateTimestamp
    val updatedAt: LocalDateTime = LocalDateTime.MIN

    @field:Column(name = "deleted_at", nullable = true)
    var deletedAt: LocalDateTime? = null
        protected set

    fun delete() {
        deletedAt = LocalDateTime.now()
    }

    fun recoverDeleteStatus() {
        deletedAt = null
    }
}
