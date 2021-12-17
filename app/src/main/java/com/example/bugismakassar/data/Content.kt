package com.example.bugismakassar.data

import com.google.android.gms.tasks.Task

data class Content(
    var title: String? = null,
    var media: String? = null,
    var description: String? = null,
    var uploader: String? = null,
    var type: Int? = 0
)
