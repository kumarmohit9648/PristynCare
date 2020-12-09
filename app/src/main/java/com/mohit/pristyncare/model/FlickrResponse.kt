package com.mohit.pristyncare.model

import java.io.Serializable

data class FlickrResponse(
    val photos: Photos,
    val stat: String
) : Serializable