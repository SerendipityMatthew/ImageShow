import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import viewmodel.SharedViewModel

val coroutineDispatcherModule = module {
    single<CoroutineDispatcher>(named("IODispatcher")) { Dispatchers.IO }
    single<CoroutineDispatcher>(named("MainDispatcher")) { Dispatchers.Main }
    single<CoroutineDispatcher>(named("DefaultDispatcher")) { Dispatchers.Default }
}

val mvvmModule = module {
    viewModelOf(::SharedViewModel) {
    }
}


val allModules = module {
    includes(coroutineDispatcherModule, mvvmModule)
}