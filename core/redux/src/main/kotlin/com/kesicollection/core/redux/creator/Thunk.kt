package com.kesicollection.core.redux.creator

import com.kesicollection.core.redux.aliases.ReducerPayload
import com.kesicollection.core.redux.model.ActionCreator
import com.kesicollection.core.redux.model.AsyncThunkAction

fun <Returned, ThunkArg> createAsyncThunk(
    actionType: String,
    reducerPayload: ReducerPayload<Returned, ThunkArg>
): AsyncThunk<Returned, ThunkArg> = AsyncThunk(
    ActionCreator("$actionType/fulfilled"),
    ActionCreator("$actionType/rejected"),
    ActionCreator("$actionType/pending"),
    reducerPayload
)

class AsyncThunk<Returned, ThunkArg>(
    val fulfilled: ActionCreator<Returned, ThunkArg>,
    val rejected: ActionCreator<Returned, ThunkArg>,
    val pending: ActionCreator<Returned, ThunkArg>,
    private val reducerPayload: ReducerPayload<Returned, ThunkArg>
) {
    operator fun invoke(arg: ThunkArg): AsyncThunkAction<Returned, ThunkArg> =
        AsyncThunkAction(arg, reducerPayload, this)
}