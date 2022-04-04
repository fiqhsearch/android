package com.fiqhsearcher.di

import android.content.Context
import com.fiqhsearcher.supabase.PropertiesStorage
import com.fiqhsearcher.supabase.SupabaseClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.supabase.common.AbsentStorage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    @Provides
    @Singleton
    fun getSupabaseClient(@ApplicationContext context: Context): SupabaseClient {
        return SupabaseClient(
            url = "https://axbeytrzoapsldwtjqvx.supabase.co",
            authorization = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImF4YmV5dHJ6b2Fwc2xkd3RqcXZ4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE2NDgyNzAwMTEsImV4cCI6MTk2Mzg0NjAxMX0.L-wHDZ1FekHJJY4wEkklPjO8au11sc7L04zBbowaDs8",
            storage = PropertiesStorage(context)
        )
    }

}