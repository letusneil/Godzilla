package com.letusneil.godzilla.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<GodzillaDatabase> =
    Room.databaseBuilder<GodzillaDatabase>(
        context = context.applicationContext,
        name = context.getDatabasePath("godzilla.db").absolutePath,
    )
