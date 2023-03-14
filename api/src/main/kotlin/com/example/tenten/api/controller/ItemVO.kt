package com.example.tenten.api.controller

import com.example.tenten.integrated.db.entity.ContentProvider
import com.example.tenten.integrated.db.enum.ItemCategory

data class ItemVO(
    val id: Long,
    val name: String,
    val price: Long,
    val creatorId: Long,
    val contentProvider: ContentProvider,
    val itemCategory: ItemCategory
)
