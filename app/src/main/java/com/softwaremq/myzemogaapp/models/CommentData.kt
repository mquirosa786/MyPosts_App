package com.softwaremq.myzemogaapp.models

import com.google.gson.annotations.Expose
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class CommentData(
    @Expose
    var postId: Int = 0,
    @Expose
    @PrimaryKey
    var id: Int = 0,
    @Expose
    var name: String = "",
    @Expose
    var email: String = "",
    @Expose
    var body: String = "",
): RealmModel