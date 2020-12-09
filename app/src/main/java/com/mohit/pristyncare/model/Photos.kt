package com.mohit.pristyncare.model

import java.io.Serializable

data class Photos(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val photo: List<Photo>,
    val total: String
) : Serializable