package vn.tutorme.mobile.di

import vn.tutorme.mobile.domain.repo.IUserRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.tutorme.mobile.data.repo.UserRepoImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideUserRepository(userRepoImpl: UserRepoImpl): IUserRepo
}
