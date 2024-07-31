package com.example.listerpros.api


import com.example.listerpros.model.addnote.ResponseJobNotes
import com.example.listerpros.model.addnotesmodified.ResponseAddNoteModified
import com.example.listerpros.model.editprofile.ResponseEditProfile
import com.example.listerpros.model.getjobdetails.ResponseJobDetails
import com.example.listerpros.model.getmyjobs.GetMyJobs
import com.example.listerpros.model.getmyprofile.ResponseGetProfile
import com.example.listerpros.model.locationstatus.ResponseLocationStatus
import com.example.listerpros.model.login.ResponseLogin
import com.example.listerpros.model.timesheet.responseclockin.ResponseClockIn
import com.example.listerpros.model.timesheet.gettimesheet.ResponseTimeSheet
import com.example.listerpros.model.timesheet.responseclockout.ResponseClockOut
import com.example.listerpros.model.updatejobstatus.UpdateJobStatus
import com.example.listerpros.model.updatetaskstatus.UpdateTaskStatus
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseLogin>

    @FormUrlEncoded
    @POST("clock-in-out")
    fun clockIn(
        @Field("status") status: String,
        @Field("start_date") startDate: String,
        @Field("end_time") endDate: String,
        @Field("clock_in") clock_in: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double
    ): Call<ResponseClockIn>

    @FormUrlEncoded
    @POST("clock-in-out")
    fun clockOut(
        @Field("status") status: String,
        @Field("start_date") startDate: String,
        @Field("end_date") endDate: String,
        @Field("clock_out") clock_out: String,
        @Field("note") note: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double
    ): Call<ResponseClockOut>

    @FormUrlEncoded
    @POST("manage-job-notes")
    fun jobNotes(
        @Field("job_id") job_id: String,
        @Field("notes") notes: String,
    ): Call<ResponseJobNotes>

    @FormUrlEncoded
    @POST("manage-job-notes")
    fun addNotesModified(
        @Field("job_id") job_id: String,
        @Field("notes") notes: String,
        @Field("id") id: String,
    ): Call<ResponseAddNoteModified>


    @GET("clock-in-out")
    fun getTimesheet(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Call<ResponseTimeSheet>


    @GET("my-profile")
    fun getProfile(): Call<ResponseGetProfile>

    @FormUrlEncoded
    @POST("my-profile")
    fun editProfile(
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
        @Field("email") email: String,
        @Field("cell_phone") cellPhone: String
    ): Call<ResponseEditProfile>

    @GET("my-jobs")
    fun getJobs(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Call<GetMyJobs>

    @GET("my-jobs/{id}")
    fun getJobDetails(
        @Path("id") id: String
    ): Call<ResponseJobDetails>

    @FormUrlEncoded
    @POST("on-your-way")
    fun updateLocationStatus(
        @Field("job_id") job_id: String,
        @Field("location_status") location_status: String
    ): Call<ResponseLocationStatus>


    @FormUrlEncoded
    @PUT("manage-task-status/{id}")
    fun updateTaskStatus(
        @Path("id") id: String,
        @Field("notes") notes: String,
        @Field("status") status: String
    ): Call<UpdateTaskStatus>

    @FormUrlEncoded
    @PUT("manage-job-status/{id}")
    fun updateJobStatus(
        @Path("id") id: String,
        @Field("status") status: String
    ) : Call<UpdateJobStatus>
}