package com.example.orgware.kotlinemap.app

import io.realm.RealmConfiguration

class RealmConfigFactory {

    companion object {
        fun createAdminRealmRealmConfiguration(): RealmConfiguration? {
            return RealmConfiguration.Builder().name("SampleModule").build()
        }
    }
}