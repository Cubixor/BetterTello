package me.cubixor.bettertello

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.cubixor.bettertello.data.FileManager
import me.cubixor.telloapi.api.Tello
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Singleton
    @Provides
    fun provideTello(): Tello {
        val tello = Tello.build()
        val fileManager = FileManager()
        tello.addFileListener(fileManager)

        Log.d("AppModule", "TELLO: ${tello.hashCode()}")

        return tello
    }
}