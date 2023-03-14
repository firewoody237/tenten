package com.example.tenten.api.controller

import com.example.tenten.integrated.db.enum.Service

data class ContentProviderVO(
    val id: Long?,
    val name: String?,
    val service: Service?,
)
