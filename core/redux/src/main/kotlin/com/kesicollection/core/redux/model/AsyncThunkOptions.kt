package com.kesicollection.core.redux.model

import com.kesicollection.core.redux.aliases.Dispatcher

data class AsyncThunkOptions(val dispatch: Dispatcher, val getState: Any)