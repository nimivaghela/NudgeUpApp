package com.app.collt.ui.auth.login.view_model

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.collt.domain.models.ApiUser
import com.app.collt.domain.models.request.CreateProfileRequestModel
import com.app.collt.domain.models.request.LoginRequestModel
import com.app.collt.domain.models.request.SendContactListRequestModel
import com.app.collt.domain.models.response.AddProfilePicResponseModel
import com.app.collt.domain.models.response.AvailableUserNameListResponseModel
import com.app.collt.domain.models.response.CheckUserNameAvailableResponseModel
import com.app.collt.domain.models.response.CreateProfileResponseModel
import com.app.collt.domain.models.response.GetUserContactListResponseModel
import com.app.collt.domain.models.response.LoginResponseModel
import com.app.collt.domain.models.response.PhoneAvailableResponseModel
import com.app.collt.domain.models.response.PhoneExistResponseModel
import com.app.collt.domain.models.response.SendContactListResponseModel
import com.app.collt.domain.models.response.SetUserNameResponseModel
import com.app.collt.domain.models.response.SignUpResponseModel
import com.app.collt.helper.THIS_TAG
import com.app.collt.shared_data.base.BaseView
import com.app.collt.shared_data.base.BaseViewModel
import com.app.collt.shared_data.repo.AuthRepo
import com.app.collt.utils.Resource
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import timber.log.Timber

class LoginViewModel(var authRepo: AuthRepo) : BaseViewModel(authRepo) {

    private val _stateFlow = MutableStateFlow<Resource<List<ApiUser>>>(Resource.loading())
    val stateFlow: MutableStateFlow<Resource<List<ApiUser>>>
        get() = _stateFlow

    private val _loginResponseFlow =
        MutableStateFlow<Resource<LoginResponseModel>>(Resource.loading())
    val loginResponseFlow: MutableStateFlow<Resource<LoginResponseModel>>
        get() = _loginResponseFlow

    private val _phoneExistResponseFlow =
        MutableStateFlow<Resource<PhoneExistResponseModel>>(Resource.loading())
    val phoneExistResponseFlow: MutableStateFlow<Resource<PhoneExistResponseModel>>
        get() = _phoneExistResponseFlow

    private val _phoneAvailableResponseFlow =
        MutableStateFlow<Resource<PhoneAvailableResponseModel>>(Resource.loading())
    val phoneAvailableResponseFlow: MutableStateFlow<Resource<PhoneAvailableResponseModel>>
        get() = _phoneAvailableResponseFlow

    private val _signUpResponseFlow =
        MutableStateFlow<Resource<SignUpResponseModel>>(Resource.loading())
    val signUpResponseFlow: MutableStateFlow<Resource<SignUpResponseModel>>
        get() = _signUpResponseFlow

    private val _createUserProfileResponseFlow =
        MutableStateFlow<Resource<CreateProfileResponseModel>>(Resource.loading())
    val createUserProfileResponseFlow: MutableStateFlow<Resource<CreateProfileResponseModel>>
        get() = _createUserProfileResponseFlow

    private val _checkAvailableUserNameResponseFlow =
        MutableStateFlow<Resource<CheckUserNameAvailableResponseModel>>(Resource.loading())
    val checkAvailableUserNameResponseFlow: MutableStateFlow<Resource<CheckUserNameAvailableResponseModel>>
        get() = _checkAvailableUserNameResponseFlow

    private val _availableUserNameListResponseFlow =
        MutableStateFlow<Resource<AvailableUserNameListResponseModel>>(Resource.loading())
    val availableUserNameListResponseFlow: MutableStateFlow<Resource<AvailableUserNameListResponseModel>>
        get() = _availableUserNameListResponseFlow

    private val _setUserNameResponseFlow =
        MutableStateFlow<Resource<SetUserNameResponseModel>>(Resource.loading())
    val setUserNameResponseFlow: MutableStateFlow<Resource<SetUserNameResponseModel>>
        get() = _setUserNameResponseFlow

    private val _sendContactListResponseModel =
        MutableStateFlow<Resource<SendContactListResponseModel>>(Resource.loading())
    val sendContactListResponseModel: MutableStateFlow<Resource<SendContactListResponseModel>>
        get() = _sendContactListResponseModel

    private val _addProfilePicResponseModel =
        MutableStateFlow<Resource<AddProfilePicResponseModel>>(Resource.loading())
    val addProfilePicResponseModel: MutableStateFlow<Resource<AddProfilePicResponseModel>>
        get() = _addProfilePicResponseModel

    private val _getSyncContactResponseModel =
        MutableStateFlow<Resource<GetUserContactListResponseModel>>(Resource.loading())
    val getSyncContactResponseModel: MutableStateFlow<Resource<GetUserContactListResponseModel>>
        get() = _getSyncContactResponseModel


    fun callLoginAPI(
        loginRequestModel: LoginRequestModel, baseView: BaseView, disposable: CompositeDisposable
    ) {
        viewModelScope.launch {
            authRepo.callUserLoginAPI(
                loginRequestModel, baseView, disposable
            ).flowOn(Dispatchers.IO).catch {
                Timber.d(it)
            }.collect {
                loginResponseFlow.value = it
            }
        }
    }

    fun callSignUpAPI(
        params: HashMap<String, String>,
        view: BaseView?

    ) {
        viewModelScope.launch {
            authRepo.callSignUpAPI(params,view).flowOn(Dispatchers.IO).catch {}.collect {
                signUpResponseFlow.value = it
            }
        }
    }

    fun fetchUsers() {
        viewModelScope.launch {
            authRepo.getUsers().flowOn(Dispatchers.IO).catch { }.collect {
                stateFlow.value = it
            }
        }
    }

    fun checkExistPhone(phone: String) {
        viewModelScope.launch {
            authRepo.checkPhoneExist(phone).flowOn(Dispatchers.IO).catch {
                Log.d(THIS_TAG, "checkExistPhone: ")
            }.collect {
                phoneExistResponseFlow.value = it
            }
        }
    }

    fun checkAvailablePhone(
        phone: String, baseView: BaseView, disposable: CompositeDisposable
    ) {
        viewModelScope.launch {
            authRepo.checkPhoneAvailable(phone).flowOn(Dispatchers.IO).catch {
                Log.d(THIS_TAG, "checkAvailablePhone: ")
            }.collect {
                phoneAvailableResponseFlow.value = it
            }
        }
    }

    fun createUserProfile(
        createProfileRequestModel: CreateProfileRequestModel
    ) {
        viewModelScope.launch {
            authRepo.createUserProfile(createProfileRequestModel).flowOn(Dispatchers.IO).catch { }
                .collect {
                    createUserProfileResponseFlow.value = it
                }
        }
    }

    fun checkUserNameAvailable(name: String) {
        viewModelScope.launch {
            authRepo.checkUserNameAvailable(name).flowOn(Dispatchers.IO).catch { }.collect {
                checkAvailableUserNameResponseFlow.value = it
            }
        }
    }

    fun getAvailableUserNameList(name: String) {
        viewModelScope.launch {
            authRepo.getAvailableUserNameList(name).flowOn(Dispatchers.IO).catch { }.collect {
                availableUserNameListResponseFlow.value = it
            }
        }
    }

    fun getSyncContactList(view: BaseView?) {
        viewModelScope.launch {
            authRepo.getSyncContactList(view).flowOn(Dispatchers.IO).catch { }.collect {
                getSyncContactResponseModel.value = it
            }
        }
    }

    fun setUserName(name: String) {
        viewModelScope.launch {
            authRepo.setUserName(name).flowOn(Dispatchers.IO).catch {

            }.collect {
                setUserNameResponseFlow.value = it
            }
        }
    }

    fun sendContactList(sendContactListModel: SendContactListRequestModel) {
        viewModelScope.launch {
            authRepo.sendContactList(sendContactListModel).flowOn(Dispatchers.IO).catch { }
                .collect {
                    sendContactListResponseModel.value = it
                }
        }
    }

    fun addProfilePic(
        file: RequestBody
    ) {
        viewModelScope.launch {
            authRepo.addProfilePic(file).flowOn(Dispatchers.IO).catch { }.collect {
                addProfilePicResponseModel.value = it
            }
        }
    }


}