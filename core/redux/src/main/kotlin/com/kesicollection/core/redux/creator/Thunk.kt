package com.kesicollection.core.redux.creator

import com.kesicollection.core.redux.aliases.Dispatcher

typealias ReducerPayload<Returned, ThunkArg> = suspend (arg: ThunkArg, options: AsyncThunkOptions) -> Returned

fun <Returned, ThunkArg> createAsyncThunk(
    type: String,
    reducerPayload: ReducerPayload<Returned, ThunkArg>
): AsyncThunk<Returned, ThunkArg> = AsyncThunk(
    "$type/fulfilled",
    "$type/rejected",
    "$type/pending",
    reducerPayload
)

//TODO: RejectWithValues. fulfillWithValues (Default)
data class AsyncThunkOptions(val dispatch: Dispatcher, val getState: Any)

data class AsyncThunkAction<Returned, ThunkArg>(
    val arg: ThunkArg,
    val reducerPayload: ReducerPayload<Returned, ThunkArg>,
    val asyncThunk: AsyncThunk<Returned, ThunkArg>
)

class AsyncThunk<Returned, ThunkArg>(
    val fulfilled: String,
    val rejected: String,
    val pending: String,
    private val reducerPayload: ReducerPayload<Returned, ThunkArg>
) {
    operator fun invoke(arg: ThunkArg): AsyncThunkAction<Returned, ThunkArg> =
        AsyncThunkAction(arg, reducerPayload, this)
}