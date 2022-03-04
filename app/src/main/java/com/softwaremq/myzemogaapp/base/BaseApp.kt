package com.softwaremq.myzemogaapp.base

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class BaseApp : Application(){
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        val configuration = RealmConfiguration.Builder()
            .name("zamoga.db")
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .allowWritesOnUiThread(true)
            .allowQueriesOnUiThread(true)
            .build()

        Realm.setDefaultConfiguration(configuration)
    }
}