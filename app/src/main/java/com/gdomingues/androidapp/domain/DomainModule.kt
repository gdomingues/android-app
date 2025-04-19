package com.gdomingues.androidapp.domain

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindUriBuilder(defaultUriBuilder: DefaultUriBuilder): UriBuilder
}
