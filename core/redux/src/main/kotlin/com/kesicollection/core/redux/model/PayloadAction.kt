package com.kesicollection.core.redux.model

data class PayloadAction<Payload, ThunkArg>(
    val payload: Result<Payload>,
    val type: String,
    val thunkArgs: ThunkArg,
)