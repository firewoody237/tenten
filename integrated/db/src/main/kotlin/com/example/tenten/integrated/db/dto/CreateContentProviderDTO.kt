package com.example.tenten.integrated.db.dto

import com.example.tenten.integrated.db.enum.Service

data class CreateContentProviderDTO(
    val name: String? = null,
    val service: Service? = Service.NONE
)
