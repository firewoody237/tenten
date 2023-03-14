package com.example.tenten.integrated.db.repository

import com.example.tenten.integrated.db.entity.ContentProvider
import com.example.tenten.integrated.db.entity.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ItemRepository: JpaRepository<Item, Long> {
    fun existsByNameAndContentProvider(name: String, contentProvider: ContentProvider): Boolean
    fun findByNameContains(name: String): MutableList<Item>

    fun findByCreatorIdAndContentProvider(creatorId: Long, contentProvider: ContentProvider): MutableList<Item>
    fun findByCreatorId(creatorId: Long): MutableList<Item>
    fun findByContentProvider(contentProvider: ContentProvider): MutableList<Item>
}