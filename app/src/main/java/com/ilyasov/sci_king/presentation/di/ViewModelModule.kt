package com.ilyasov.sci_king.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ilyasov.sci_king.ViewModelKey
import com.ilyasov.sci_king.ViewModelProviderFactory
import com.ilyasov.sci_king.presentation.viewmodels.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SciArticlesViewModel::class)
    abstract fun bindSciArticlesViewModel(viewModel: SciArticlesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SavedArticlesViewModel::class)
    abstract fun bindSavedArticlesViewModel(viewModel: SavedArticlesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ParseArticleViewModel::class)
    abstract fun bindParseArticleViewModel(viewModel: ParseArticleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    abstract fun bindSignInViewModel(viewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    abstract fun bindSignUpViewModel(viewModel: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserProfileViewModel::class)
    abstract fun binUserProfileViewModel(viewModel: UserProfileViewModel): ViewModel

}