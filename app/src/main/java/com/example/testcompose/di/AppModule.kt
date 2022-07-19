package com.example.testcompose.di

import android.app.Application
import androidx.room.Room
import com.example.testcompose.common.Constants
import com.example.testcompose.data.local.CurrenciesDatabase
import com.example.testcompose.data.remote.CurrencyApi
import com.example.testcompose.data.repository.CurrencyRepositoryImpl
import com.example.testcompose.domain.repository.CurrencyRepository
import com.example.testcompose.domain.use_case.GetCurrencyListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCurrencyDatabase(app: Application): CurrenciesDatabase {
        return Room.databaseBuilder(
            app,
            CurrenciesDatabase::class.java,
            CurrenciesDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): Interceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun provideHttpClient(httpLoggingInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(Interceptor {
                val newRequest = it.request().newBuilder()
                    .addHeader("apikey", Constants.API_KEY)
                    .build()
                it.proceed(newRequest)
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideCurrencyApi(okHttpClient: OkHttpClient): CurrencyApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCurrencyRepository(api: CurrencyApi, db: CurrenciesDatabase): CurrencyRepository {
        return CurrencyRepositoryImpl(api, db.currencyDao)
    }

    @Provides
    @Singleton
    fun provideGetCurrencyListUseCase(repository: CurrencyRepository): GetCurrencyListUseCase {
        return GetCurrencyListUseCase(repository)
    }
}