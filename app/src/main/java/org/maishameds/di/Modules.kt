/*
 * Copyright 2020 MaishaMeds
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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