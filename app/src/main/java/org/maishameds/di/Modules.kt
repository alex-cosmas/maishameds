package org.maishameds.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.maishameds.data.Database
import org.maishameds.data.repository.PostRepository
import org.maishameds.ui.viewmodel.PostViewModel

private val databaseModule: Module = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            Database::class.java,
            "maishameds-db"
        ).build()
    }
}

private val daoModule: Module = module {
    single { get<Database>().postDao() }
}

private val repositoryModule: Module = module {
    single { PostRepository(get(), get()) }
}

private val viewModelModule: Module = module {
    viewModel { PostViewModel(get()) }
}

val appModules: List<Module> = listOf(
    databaseModule,
    daoModule,
    repositoryModule,
    viewModelModule,
)