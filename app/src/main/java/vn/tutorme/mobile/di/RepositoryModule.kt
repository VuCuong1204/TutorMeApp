package vn.tutorme.mobile.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.tutorme.mobile.data.repo.authen.AuthRepoImpl
import vn.tutorme.mobile.data.repo.banner.BannerRepoImpl
import vn.tutorme.mobile.data.repo.course.CourseRepoImpl
import vn.tutorme.mobile.data.repo.lesson.LessonRepoImpl
import vn.tutorme.mobile.data.repo.location.LocationRepoImpl
import vn.tutorme.mobile.data.repo.notification.NotificationRepoImpl
import vn.tutorme.mobile.domain.repo.IAuthRepo
import vn.tutorme.mobile.domain.repo.IBannerRepo
import vn.tutorme.mobile.domain.repo.ICourseRepo
import vn.tutorme.mobile.domain.repo.ILessonRepo
import vn.tutorme.mobile.domain.repo.ILocationRepo
import vn.tutorme.mobile.domain.repo.INotificationRepo

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideUserRepository(userRepoImpl: AuthRepoImpl): IAuthRepo

    @Binds
    abstract fun provideBannerRepository(bannerRepoImpl: BannerRepoImpl): IBannerRepo

    @Binds
    abstract fun provideLessonRepository(lessonRepoImpl: LessonRepoImpl): ILessonRepo

    @Binds
    abstract fun provideNotificationRepository(notificationImpl: NotificationRepoImpl): INotificationRepo

    @Binds
    abstract fun provideLocationRepository(locationRepoImpl: LocationRepoImpl): ILocationRepo

    @Binds
    abstract fun provideCourseRepository(courseImpl: CourseRepoImpl): ICourseRepo
}
