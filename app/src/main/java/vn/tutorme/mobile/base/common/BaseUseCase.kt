package vn.tutorme.mobile.base.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

abstract class BaseUseCase<REQUEST : BaseUseCase.RequestValue, RESPONSE> {
    private val dispatcher = Dispatchers.IO

    protected abstract suspend fun execute(rv: REQUEST): RESPONSE

    suspend operator fun invoke(request: REQUEST): Flow<RESPONSE> {
        val flow = flow {
            val response = withContext(dispatcher) {
                execute(request)
            }
            emit(response)
        }.flowOn(dispatcher)
        return flow
    }

    class VoidRequest : RequestValue

    interface RequestValue
}
