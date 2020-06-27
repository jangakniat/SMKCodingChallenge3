package com.pedolu.smkcodingchallenge3.data.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_summary")
data class LocalSummaryModel(
    var confirmed: String,
    var recovered: String,
    var deaths: String,
    var last_update: String,
    @PrimaryKey var country_name: String
) {
    constructor() : this("", "", "", "", "")
}