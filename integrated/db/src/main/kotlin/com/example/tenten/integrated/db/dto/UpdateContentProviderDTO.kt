package com.example.tenten.integrated.db.dto

import com.example.tenten.integrated.db.enum.Service

data class UpdateContentProviderDTO(
    val id: Long? = null,
    val name: String? = null,
    val service: Service? = Service.NONE
)
