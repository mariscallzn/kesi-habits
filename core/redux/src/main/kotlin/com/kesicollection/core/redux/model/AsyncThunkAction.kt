package com.kesicollection.core.redux.model

import com.kesicollection.core.redux.aliases.ReducerPayload
import com.kesicollection.core.redux.creator.AsyncThunk

data class AsyncThunkAction<Returned, ThunkArg>(
    val arg: ThunkArg,
    val reducerPayload: ReducerPayload<Returned, ThunkArg>,
    val asyncThunk: AsyncThunk<Returned, ThunkArg>
)