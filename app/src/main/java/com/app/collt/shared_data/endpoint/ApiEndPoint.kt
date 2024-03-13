package com.app.collt.shared_data.endpoint

import com.app.collt.domain.models.ApiUser
import com.app.collt.domain.models.request.CreateProfileRequestModel
import com.app.collt.domain.models.request.LoginRequestModel
import com.app.collt.domain.models.request.SendContactListRequestModel
import com.app.collt.domain.models.response.AddProfilePicResponseModel
import com.app.collt.domain.models.response.CheckUserNameAvailableResponseModel
import com.app.collt.domain.models.response.CreateProfileResponseModel
import com.app.collt.domain.models.response.AvailableUserNameListResponseModel
import com.app.collt.domain.models.response.GetUserContactListResponseModel
import com.app.collt.domain.models.response.LoginResponseModel
import com.app.collt.domain.models.response.PhoneAvailableResponseModel
import com.app.collt.domain.models.response.PhoneExistResponseModel
import com.app.collt.domain.models.response.SendContactListResponseModel
import com.app.collt.domain.models.response.SetUserNameResponseModel
import com.app.collt.domain.models.response.SignUpResponseModel
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiEndPoint {

    @POST("/auth/user/sign-in-phone")
    suspend fun callUserLoginAPI(@Body loginRequestModel: LoginRequestModel): LoginResponseModel

    @FormUrlEncoded
    @POST("/auth/user/sign-up")
    suspend fun callUserSignUpAPI(@FieldMap params: Map<String, String>): SignUpResponseModel

    @GET("v1/airlines")
    suspend fun getUsers(): List<ApiUser>

    @GET("/auth/user/phone-exist/{phone}")
    suspend fun checkPhoneExist(@Path("phone") phone: String): PhoneExistResponseModel

    @GET("/auth/user/phone-available/{phone}")
    suspend fun checkPhoneAvailable(@Path("phone") phone: String): PhoneAvailableResponseModel

    @PUT("/user/profile")
    suspend fun createUserProfile(
        @Body createProfileRequest: CreateProfileRequestModel
    ): CreateProfileResponseModel

    @PUT("/user/user-name/{user_name}")
    suspend fun setUserName(
        @Path("user_name") user_name: String
    ): SetUserNameResponseModel

    @PUT("/user/profile/contact/sync")
    suspend fun sendContactList(
        @Body sendContactListRequestModel: SendContactListRequestModel
    ): SendContactListResponseModel

    @GET("/user-name/check-available/{user_name}")
    suspend fun checkUserNameAvailable(
        @Path("user_name") user_name: String
    ): CheckUserNameAvailableResponseModel

    @GET("/user-name/list-available/{user_name}")
    suspend fun getUserNameListAvailable(
        @Path("user_name") user_name: String
    ): AvailableUserNameListResponseModel

    //    {{baseUrl}}/user/profile/contact/sync
    @GET("/user/profile/contact/sync")
    suspend fun getUserContactList(): GetUserContactListResponseModel


    @POST("/user/media")
    suspend fun addProfilePic(
        @Body file: RequestBody
    ): AddProfilePicResponseModel


}