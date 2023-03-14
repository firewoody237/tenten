package com.example.tenten.integrated.db.dto

import com.example.tenten.integrated.db.entity.ContentProvider
import com.example.tenten.integrated.db.enum.ItemCategory

data class UpdateItemDTO(
    val id: Long?,
    val name: String?,
    val price: Long?,
    val creatorId: Long?,
    val contentProvider: ContentProvider?,
    val itemCategory: ItemCategory? = ItemCategory.NONE
)
