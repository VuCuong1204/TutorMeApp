package vn.tutorme.mobile.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
//
//    @Binds
//    abstract fun provideUserRepository(userRepoImpl: UserRepoImpl): IUserRepo
}
