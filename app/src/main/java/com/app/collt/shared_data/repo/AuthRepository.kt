package com.app.collt.shared_data.repo

import android.content.Context
import com.app.collt.domain.models.ApiUser
import com.app.collt.domain.models.request.CreateProfileRequestModel
import com.app.collt.domain.models.request.LoginRequestModel
import com.app.collt.domain.models.request.SendContactListRequestModel
import com.app.collt.domain.models.response.AddProfilePicResponseModel
import com.app.collt.domain.models.response.ApiError
import com.app.collt.domain.models.response.AvailableUserNameListResponseModel
import com.app.collt.domain.models.response.CheckUserNameAvailableResponseModel
import com.app.collt.domain.models.response.CreateProfileResponseModel
import com.app.collt.domain.models.response.ErrorModel
import com.app.collt.domain.models.response.GetUserContactListResponseModel
import com.app.collt.domain.models.response.LoginResponseModel
import com.app.collt.domain.models.response.PhoneAvailableResponseModel
import com.app.collt.domain.models.response.PhoneExistResponseModel
import com.app.collt.domain.models.response.SendContactListResponseModel
import com.app.collt.domain.models.response.SetUserNameResponseModel
import com.app.collt.domain.models.response.SignUpResponseModel
import com.app.collt.extension.logError
import com.app.collt.helper.StatusCode.STATUS_401
import com.app.collt.helper.StatusCode.STATUS_409
import com.app.collt.helper.StatusCode.STATUS_412
import com.app.collt.helper.StatusCode.STATUS_422
import com.app.collt.helper.StatusCode.STATUS_426
import com.app.collt.shared_data.base.BaseView
import com.app.collt.shared_data.endpoint.ApiEndPoint
import com.app.collt.utils.Resource
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.koin.core.component.getScopeId
import retrofit2.HttpException

class AuthRepository(var context: Context, private val apiEndPoint: ApiEndPoint) : AuthRepo {

    override suspend fun callUserLoginAPI(
        loginRequestModel: LoginRequestModel, baseView: BaseView, disposable: CompositeDisposable
    ): Flow<Resource<LoginResponseModel>> = flow {
        emit(Resource.loading())
        val response = apiEndPoint.callUserLoginAPI(loginRequestModel)
        emit(Resource.success(response))
    }.catch {
        var errorModel: ErrorModel? = null
        when (it) {
            is HttpException -> {
                val responseBody = it.response()?.errorBody()
                errorModel = getErrorMessage(responseBody)
                emit(Resource.error(it.message, LoginResponseModel().apply {
                    error = ApiError(
                        errorModel?.errorCode,
                        "",
                        errorModel?.message,
                        errorModel?.type ?: ""
                    )
                }))/*when (it.code()) {
                    STATUS_401 -> {
                        view?.autoLogout()
                    }

                    STATUS_409 -> {
                        view?.loginFirst()
                    }

                    STATUS_422 -> {
                        view?.onOTPExpire(errorModel?.message)
                    }

                    STATUS_412 -> {
                        view?.onVerificationError()
                    }

                    STATUS_426 -> {
                        view?.onSubscribeRequired(errorModel?.message)
                    }

                    else -> {
                        logError(msg = "onError: ${errorModel?.message}")
                    }
                }*/
            }
        }
    }

    override fun getUsers(): Flow<Resource<List<ApiUser>>> = flow {
        emit(Resource.loading())
        val response = apiEndPoint.getUsers()
        emit(Resource.success(response))
    }.catch {
        emit(Resource.error(it.message ?: getScopeId()))
    }

    override fun checkPhoneExist(phone: String): Flow<Resource<PhoneExistResponseModel>> = flow {
        emit(Resource.loading())
        val response = apiEndPoint.checkPhoneExist(phone)
        emit(Resource.success(response))
    }.catch {
        var errorModel: ErrorModel? = null
        when (it) {
            is HttpException -> {
                when (it.code()) {/*STATUS_401 -> {
                        view?.autoLogout()
                    }

                    STATUS_409 -> {
                        view?.loginFirst()
                    }*/
                }

                val responseBody = it.response()?.errorBody()
                errorModel = getErrorMessage(responseBody)
                emit(Resource.error(it.message, PhoneExistResponseModel().apply {
                    error = ApiError(
                        errorModel?.errorCode,
                        "",
                        errorModel?.message,
                        errorModel?.type ?: ""
                    )
                }))

                /*when (it.code()) {
                    STATUS_401 -> {
                        view?.autoLogout()
                    }

                    STATUS_409 -> {
                        view?.loginFirst()
                    }

                    STATUS_422 -> {
                        view?.onOTPExpire(errorModel?.message)
                    }

                    STATUS_412 -> {
                        view?.onVerificationError()
                    }

                    STATUS_426 -> {
                        view?.onSubscribeRequired(errorModel?.message)
                    }

                    else -> {
                        logError(msg = "onError: ${errorModel?.message}")
                    }
                }*/
            }
        }
    }

    override fun checkPhoneAvailable(
        phone: String
    ): Flow<Resource<PhoneAvailableResponseModel>> = flow {
        emit(Resource.loading())
        var response = apiEndPoint.checkPhoneAvailable(phone)
        emit(Resource.success(response))
    }.catch {
        var errorModel: ErrorModel? = null
        when (it) {
            is HttpException -> {
                val responseBody = it.response()?.errorBody()
                errorModel = getErrorMessage(responseBody)
                emit(Resource.error(it.message, PhoneAvailableResponseModel().apply {
                    error = ApiError(
                        errorModel?.errorCode,
                        "",
                        errorModel?.message,
                        errorModel?.type ?: ""
                    )
                }))/*when (it.code()) {
                        STATUS_401 -> {
                            view?.autoLogout()
                        }

                        STATUS_409 -> {
                            view?.loginFirst()
                        }

                        STATUS_422 -> {
                            view?.onOTPExpire(errorModel?.message)
                        }

                        STATUS_412 -> {
                            view?.onVerificationError()
                        }

                        STATUS_426 -> {
                            view?.onSubscribeRequired(errorModel?.message)
                        }

                        else -> {
                            logError(msg = "onError: ${errorModel?.message}")
                        }
                    }*/
            }
        }
    }

    override fun callSignUpAPI(
        params: HashMap<String, String>, view: BaseView?
    ): Flow<Resource<SignUpResponseModel>> = flow {
        emit(Resource.loading())
        val response = apiEndPoint.callUserSignUpAPI(params)
        emit(Resource.success(response))
    }.catch {
        var errorModel: ErrorModel? = null
        when (it) {
            is HttpException -> {
                var response = it.response()
                val responseBody = it.response()?.errorBody()
                errorModel = getErrorMessage(responseBody)
                emit(
                    Resource.error(
                        it.message,
                        SignUpResponseModel().apply {
                            error = ApiError(
                                response?.code(),
                                "",
                                errorModel?.message,
                                errorModel?.type ?: ""
                            )
                        }
                    )
                )
                when (it.code()) {
                    STATUS_401 -> {
                        view?.autoLogout()
                    }

                    STATUS_409 -> {
                        view?.loginFirst()
                    }

                    STATUS_422 -> {
                        view?.onOTPExpire(errorModel?.message)
                    }

                    STATUS_412 -> {
                        view?.onVerificationError()
                    }

                    STATUS_426 -> {
                        view?.onSubscribeRequired(errorModel?.message)
                    }

                    else -> {
                        logError(msg = "onError: ${errorModel?.message}")
                    }
                }

            }
        }
    }

    override fun createUserProfile(
        createProfileRequestModel: CreateProfileRequestModel
    ): Flow<Resource<CreateProfileResponseModel>> = flow {
        emit(Resource.loading())
        val response = apiEndPoint.createUserProfile(/*header,*/ createProfileRequestModel)
        emit(Resource.success(response))
    }.catch {
        when (it) {
            is HttpException -> {/* when (it.code()) {
                         *//*STATUS_401 -> {
                            view?.autoLogout()
                        }

                        STATUS_409 -> {
                            view?.loginFirst()
                        }*//*
                    }*/
                var errorModel: ErrorModel? = null
                val responseBody = it.response()?.errorBody()
                errorModel = getErrorMessage(responseBody)
                emit(Resource.error(it.message, CreateProfileResponseModel().apply {
                    error = ApiError(
                        errorModel?.errorCode,
                        "",
                        errorModel?.message,
                        errorModel?.type ?: ""
                    )
                }))

                /*when (it.code()) {
                    STATUS_401 -> {
                        view?.autoLogout()
                    }

                    STATUS_409 -> {
                        view?.loginFirst()
                    }

                    STATUS_422 -> {
                        view?.onOTPExpire(errorModel?.message)
                    }

                    STATUS_412 -> {
                        view?.onVerificationError()
                    }

                    STATUS_426 -> {
                        view?.onSubscribeRequired(errorModel?.message)
                    }

                    else -> {
                        logError(msg = "onError: ${errorModel?.message}")
                    }
                }*/
            }
        }
//            emit(Resource.error(it.message ?: getScopeId()))
    }

    override fun checkUserNameAvailable(name: String): Flow<Resource<CheckUserNameAvailableResponseModel>> =
        flow {
            emit(Resource.loading())
            val response = apiEndPoint.checkUserNameAvailable(name)
            emit(Resource.success(response))
        }.catch {
            emit(Resource.error(it.message ?: getScopeId()))
        }

    override fun getAvailableUserNameList(name: String): Flow<Resource<AvailableUserNameListResponseModel>> =
        flow {
            emit(Resource.loading())
            val response = apiEndPoint.getUserNameListAvailable(name)
            emit(Resource.success(response))
        }.catch {
            emit(Resource.error(it.message ?: getScopeId()))
        }

    override fun setUserName(name: String): Flow<Resource<SetUserNameResponseModel>> = flow {
        emit(Resource.loading())
        val response = apiEndPoint.setUserName(name)
        emit(Resource.success(response))
    }.catch {
        when (it) {
            is HttpException -> {
                var errorModel: ErrorModel? = null
                val responseBody = it.response()?.errorBody()
                errorModel = getErrorMessage(responseBody)
                emit(Resource.error(it.message, SetUserNameResponseModel().apply {
                    error = ApiError(
                        errorModel?.errorCode,
                        "",
                        errorModel?.message,
                        errorModel?.type ?: ""
                    )
                }))

                /*when (it.code()) {
                    STATUS_401 -> {
                        view?.autoLogout()
                    }

                    STATUS_409 -> {
                        view?.loginFirst()
                    }

                    STATUS_422 -> {
                        view?.onOTPExpire(errorModel?.message)
                    }

                    STATUS_412 -> {
                        view?.onVerificationError()
                    }

                    STATUS_426 -> {
                        view?.onSubscribeRequired(errorModel?.message)
                    }

                    else -> {
                        logError(msg = "onError: ${errorModel?.message}")
                    }
                }*/
            }
        }
    }

    override fun sendContactList(sendContactListRequestModel: SendContactListRequestModel): Flow<Resource<SendContactListResponseModel>> =
        flow {
            emit(Resource.loading())
            val response = apiEndPoint.sendContactList(sendContactListRequestModel)
            emit(Resource.success(response))
        }.catch {
            when (it) {
                is HttpException -> {
                    var errorModel: ErrorModel? = null
                    val response = it.response()
                    val responseBody = it.response()?.errorBody()
                    errorModel = getErrorMessage(responseBody)
                    emit(Resource.error(it.message, SendContactListResponseModel().apply {
                        error = ApiError(
                            response?.code(),
                            "",
                            errorModel?.message,
                            errorModel?.type ?: ""
                        )
                    }))

                }
            }

        }

    override fun addProfilePic(
        file: RequestBody
    ): Flow<Resource<AddProfilePicResponseModel>> = flow {
        emit(Resource.loading())
        val response = apiEndPoint.addProfilePic(file)
        emit(Resource.success(response))
    }.catch {
        emit(Resource.error(it.message ?: getScopeId()))
    }

    override fun getSyncContactList(view: BaseView?): Flow<Resource<GetUserContactListResponseModel>> =
        flow {
            emit(Resource.loading())
            val response = apiEndPoint.getUserContactList()
            emit(Resource.success(response))
        }.catch {
            when (it) {
                is HttpException -> {
                    var errorModel: ErrorModel? = null
                    val responseBody = it.response()?.errorBody()
                    errorModel = getErrorMessage(responseBody)
                    when (it.code()) {
                        STATUS_401 -> {
                            view?.autoLogout()
                        }

                        STATUS_409 -> {
                            view?.loginFirst()
                        }

                        STATUS_422 -> {
                            view?.onOTPExpire(errorModel?.message)
                        }

                        STATUS_412 -> {
                            view?.onVerificationError()
                        }

                        STATUS_426 -> {
                            view?.onSubscribeRequired(errorModel?.message)
                        }

                        else -> {
                            logError(msg = "onError: ${errorModel?.message}")
                        }
                    }
                }
            }
        }

    private fun getErrorMessage(responseBody: ResponseBody?): ErrorModel? {
        try {
            responseBody?.let {
                val json = JsonParser.parseString(it.string())
                return Gson().fromJson(json, ErrorModel::class.java)
            } ?: let {
                return null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}