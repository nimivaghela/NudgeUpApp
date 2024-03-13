package com.app.collt.shared_data.repo

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
import com.app.collt.shared_data.base.BaseView
import com.app.collt.utils.Resource
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody

interface AuthRepo {

    suspend fun callUserLoginAPI(
        loginRequestModel: LoginRequestModel,
        baseView: BaseView,
        disposable: CompositeDisposable,
    ): Flow<Resource<LoginResponseModel>>

    fun callSignUpAPI(
        params: HashMap<String, String>,
        view: BaseView?
    ): Flow<Resource<SignUpResponseModel>>

    fun getUsers(): Flow<Resource<List<ApiUser>>>

    fun checkPhoneExist(phone: String): Flow<Resource<PhoneExistResponseModel>>

    fun checkPhoneAvailable(phone: String): Flow<Resource<PhoneAvailableResponseModel>>

    fun checkUserNameAvailable(name: String): Flow<Resource<CheckUserNameAvailableResponseModel>>

    fun getAvailableUserNameList(name: String): Flow<Resource<AvailableUserNameListResponseModel>>

    fun setUserName(name: String): Flow<Resource<SetUserNameResponseModel>>

    fun sendContactList(sendContactListRequestModel: SendContactListRequestModel): Flow<Resource<SendContactListResponseModel>>

    fun getSyncContactList(view: BaseView?): Flow<Resource<GetUserContactListResponseModel>>

    fun addProfilePic(
        file: RequestBody
    ): Flow<Resource<AddProfilePicResponseModel>>

    fun createUserProfile(
        createProfileRequestModel: CreateProfileRequestModel
    ): Flow<Resource<CreateProfileResponseModel>>


}