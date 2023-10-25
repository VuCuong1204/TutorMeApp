package vn.tutorme.mobile.data.source.remote.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import vn.tutorme.mobile.data.source.remote.base.BaseApiResponse
import vn.tutorme.mobile.data.source.remote.base.IApiService
import vn.tutorme.mobile.data.source.remote.model.classinfo.ClassInfoMainResponse
import vn.tutorme.mobile.data.source.remote.model.course.CourseStateResponse
import vn.tutorme.mobile.data.source.remote.model.course.courseinfo.CourseMainResponse

interface ICourseService : IApiService {

    @GET("course/info")
    fun getCourseInfo(@Query("courseId") courseId: String): Call<CourseMainResponse>

    @GET("class/list/from/course")
    fun getClassListFromCourse(
        @Query("courseId") courseId: String
    ): Call<ClassInfoMainResponse>

    @GET("check/register/student")
    fun checkCourseRegistered(
        @Query("courseId") courseId: String,
        @Query("userId") userId: String,
    ): Call<CourseStateResponse>

    @POST("register/course/student")
    fun registerCourse(
        @Query("classId") classId: String,
        @Query("studentId") studentId: String,
        @Query("lessonFirst") lessonFirst: Int,
        @Query("lessonSecond") lessonSecond: Int,
    ): Call<BaseApiResponse>
}
