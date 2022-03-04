package com.softwaremq.myzemogaapp.models

import com.google.gson.annotations.Expose
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class UserData(
    @Expose
    @PrimaryKey
    var id: Int = 0,
    @Expose
    var name: String = "",
    @Expose
    var username: String = "",
    @Expose
    var email: String = "",
    @Expose
    var phone: String = "",
    @Expose
    var website: String = "",
):RealmModel