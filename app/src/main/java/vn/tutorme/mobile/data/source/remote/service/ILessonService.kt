package vn.tutorme.mobile.data.source.remote.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import vn.tutorme.mobile.data.source.remote.base.BaseApiResponse
import vn.tutorme.mobile.data.source.remote.base.IApiService
import vn.tutorme.mobile.data.source.remote.model.banner.bannerevent.BannerInfoEventResponse
import vn.tutorme.mobile.data.source.remote.model.banner.bannerjob.BannerInfoJobResponse
import vn.tutorme.mobile.data.source.remote.model.classinfo.ClassInfoResponse
import vn.tutorme.mobile.data.source.remote.model.course.CourseInfoResponse
import vn.tutorme.mobile.data.source.remote.model.feedback.FeedbackInfoResponse
import vn.tutorme.mobile.data.source.remote.model.lesson.LessonInfoMainResponse
import vn.tutorme.mobile.data.source.remote.model.lesson.LessonInfoResponse
import vn.tutorme.mobile.data.source.remote.model.user.StudentInfoResponse

interface ILessonService : IApiService {

    @GET("banner/event")
    fun getBannerEvent(
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): Call<BannerInfoEventResponse>

    @GET("banner/job")
    fun getBannerJob(
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): Call<BannerInfoJobResponse>

    @POST("week/lesson")
    fun getLessonList(
        @Query("id") teacherId: String,
        @Query("beginTime") beginTime: Long,
        @Query("endTime") endTime: Long,
        @Query("stateRate") stateRate: Int?,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): Call<LessonInfoResponse>

    @GET("register/class")
    fun getClassTeacherRegistered(
        @Query("currentTime") currentTime: Long?,
        @Query("state") state: Int,
        @Query("teacherId") teacherId: String?,
    ): Call<ClassInfoResponse>

    @GET("course")
    fun getCourseList(
        @Query("currentTime") currentTime: Long,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): Call<CourseInfoResponse>

    @GET("lesson/day")
    fun getLessonStudentInDay(
        @Query("id") studentId: String,
        @Query("beginTime") beginTime: Long,
        @Query("endTime") endTime: Long,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): Call<LessonInfoResponse>

    @GET("course/class")
    fun getClassStudentRegistered(
        @Query("id") studentId: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): Call<ClassInfoResponse>

    @GET("lesson/list/class/detail")
    fun getLessonStudentInClass(
        @Query("classId") classId: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): Call<LessonInfoResponse>

    @POST("update/register/class")
    fun updateStateClassRegister(
        @Query("classId") classId: String,
        @Query("state") state: Int,
        @Query("teacherId") teacherId: String?,
    ): Call<ClassInfoResponse>

    @GET("/class/register/list")
    fun getClassTeacherList(
        @Query("id") id: String,
        @Query("type") type: Int,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): Call<ClassInfoResponse>

    @GET("lesson/info")
    fun getLessonDetail(
        @Query("lessonId") lessonId: Int
    ): Call<LessonInfoMainResponse>

    @GET("student/lesson/list")
    fun getStudentInLesson(
        @Query("classId") classId: String
    ): Call<StudentInfoResponse>

    @PUT("update/state/assessment")
    fun attendanceStudent(
        @Query("lessonId") lessonId: Int,
        @Query("studentId") studentId: String
    ): Call<BaseApiResponse>

    @PUT("update/state/lesson")
    fun updateStateLesson(
        @Query("lessonId") lessonId: Int,
        @Query("state") state: Int
    ): Call<LessonInfoMainResponse>

    @POST("insert/feedback/lesson")
    fun feedBackLesson(
        @Query("lessonId") lessonId: Int,
        @Query("content") content: String
    ): Call<BaseApiResponse>

    @GET("feedback/list/lesson")
    fun getFeedbackList(
        @Query("lessonId") lessonId: Int
    ): Call<FeedbackInfoResponse>
}
