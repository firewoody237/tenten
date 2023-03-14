package com.example.tenten.integrated.db.entity

import com.example.tenten.integrated.db.enum.ItemCategory
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

@Entity
@EntityListeners(value = [AuditingEntityListener::class])
@Table(name = "item")
data class Item(
    @Id
    @GeneratedValue
    val id: Long = 0L,
    @Column
    var name: String? = "",
    @Column
    var price: Long? = 0L,
    @Column
    val creatorId: Long? = null,
    @ManyToOne
    @JoinColumn
    var contentProvider: ContentProvider? = null,
    @Column
    @Enumerated(EnumType.STRING)
    var itemCategory: ItemCategory? = ItemCategory.NONE
) : BaseTime() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Item(id=$id, name='$name', price='$price', creatorId='$creatorId', contentProvider='$contentProvider', itemCategory='$itemCategory')"
    }
}
