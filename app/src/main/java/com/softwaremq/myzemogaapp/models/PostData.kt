package com.softwaremq.myzemogaapp.models

import com.google.gson.annotations.Expose
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class PostData(
    @Expose
    var userId: Int = 0,
    @Expose
    @PrimaryKey
    var id: Int = 0,
    @Expose
    var title: String = "",
    @Expose
    var body: String = "",
    var favorite: Boolean = false
): RealmModel