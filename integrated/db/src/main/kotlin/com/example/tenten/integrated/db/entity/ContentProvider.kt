package com.example.tenten.integrated.db.entity

import com.example.tenten.integrated.db.enum.Service
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

@Entity
@EntityListeners(value = [AuditingEntityListener::class])
@Table(name = "content_provider")
data class ContentProvider(
    @Id
    @GeneratedValue
    val id: Long = 0L,
    @Column
    var name: String? = "",
    @Column
    @Enumerated(EnumType.STRING)
    var service: Service = Service.NONE
): BaseTime() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContentProvider

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "ContentProvider(id=$id, name='$name', service='$service')"
    }
}
