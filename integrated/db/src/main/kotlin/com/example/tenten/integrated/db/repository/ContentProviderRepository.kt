package com.example.tenten.integrated.db.repository

import com.example.tenten.integrated.db.entity.ContentProvider
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ContentProviderRepository: JpaRepository<ContentProvider, Long> {
    fun existsByName(name: String): Boolean
}