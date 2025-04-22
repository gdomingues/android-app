package com.gdomingues.androidapp.ui.watchlist

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class WatchlistModule {

    @Binds
    abstract fun bindWatchlistViewModel(viewModel: WatchlistViewModelImpl): WatchlistViewModel
}
