package com.example.tenten.integrated.db.model

import com.example.tenten.integrated.db.enum.Authority
import com.example.tenten.integrated.db.enum.Grade

data class User(
    val id: Long = 0L,
    var name: String? = "",
    var nickname: String? = "",
    var email: String? = "",
    var grade: Grade = Grade.GREEN,
    var point: Long = 0,
    var authority: Authority = Authority.NORMAL,
)
