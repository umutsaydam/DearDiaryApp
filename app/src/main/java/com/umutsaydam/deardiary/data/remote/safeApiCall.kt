package com.umutsaydam.deardiary.data.remote

import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.sealedStates.Resource
import retrofit2.Response

suspend fun <T : Any, Result> safeApiCall(
    isInternetAvailable: Boolean,
    apiCall: suspend () -> Response<T>,
    successCode: Int,
    errorMessages: Map<Int, Int> = emptyMap(),
    map: (T) -> Result
): Resource<Result> {
    if (isInternetAvailable){
        val response = apiCall()

        if (response.code() == successCode) {
            response.body()?.let {
                return Resource.Success(map(it))
            }
        } else {
            val message = errorMessages[response.code()] ?: R.string.something_went_wrong
            return Resource.Error(response.code(), message)
        }
        return Resource.Error(R.string.something_went_wrong)
    }else{
        return Resource.Error(R.string.no_internet)
    }
}