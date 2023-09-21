package vn.tutorme.mobile.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.tutorme.mobile.data.repo.authen.AuthRepoImpl
import vn.tutorme.mobile.domain.repo.IAuthRepo

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideUserRepository(userRepoImpl: AuthRepoImpl): IAuthRepo
}
