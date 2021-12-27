package com.example.bugismakassar.data

import android.os.Parcelable
import com.google.android.gms.tasks.Task
import kotlinx.parcelize.Parcelize

@Parcelize
data class Content(
    var id: String? = null,
    var title: String? = null,
    var media: String? = null,
    var description: String? = null,
    var uploader: String? = null,
    var type: Int? = 0
) : Parcelable
