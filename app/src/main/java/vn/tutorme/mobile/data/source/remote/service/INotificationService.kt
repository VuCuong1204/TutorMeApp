package vn.tutorme.mobile.data.source.remote.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import vn.tutorme.mobile.data.source.remote.base.BaseApiResponse
import vn.tutorme.mobile.data.source.remote.base.IApiService
import vn.tutorme.mobile.data.source.remote.model.notification.NotificationCountResponse
import vn.tutorme.mobile.data.source.remote.model.notification.NotificationInfoResponse

interface INotificationService : IApiService {

    @GET("notification/list")
    fun getNotificationList(
        @Query("userId") userId: String?,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): Call<NotificationInfoResponse>

    @POST("notification/delete")
    fun deleteNotification(@Query("notificationId") notificationId: Int): Call<BaseApiResponse>

    @POST("notification/update/read")
    fun updateNotification(@Query("notificationId") notificationId: Int): Call<BaseApiResponse>

    @POST("notification/update/read/all")
    fun updateNotificationAll(@Query("userId") userId: String): Call<BaseApiResponse>

    @POST("insert/notification")
    fun insertNotification(
        @Query("title") title: String,
        @Query("content") content: String,
        @Query("notifyState") notifyState: Int,
        @Query("notifyType") notifyType: Int,
        @Query("timeSend") timeSend: Long,
        @Query("userId") userId: String,
        @Query("lessonId") lessonId: Int,
        @Query("classId") classId: String,
    ): Call<BaseApiResponse>

    @GET("count/notification/unread")
    fun getCountNotificationUnRead(@Query("userId") userId: String): Call<NotificationCountResponse>

    @POST("send")
    fun sendNotificationBeginLesson(
        @Query("tokens") tokens: String,
        @Query("lessonId") lessonId: String,
        @Query("classId") classId: String,
        @Query("title") title: String,
        @Query("body") body: String,
        @Query("notificationType") notificationType: String
    ): Call<BaseApiResponse>
}
