package com.example.bugismakassar.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    var id: String? = null,
    var title: String? = null,
    var media: String? = null,
    var source: String? = null,
    var description: String? = null,
    var type: Int? = 0
) : Parcelable
