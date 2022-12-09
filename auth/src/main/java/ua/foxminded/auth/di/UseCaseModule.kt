package ua.foxminded.auth.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ua.foxminded.auth.models.*

@InstallIn(ViewModelComponent::class)
@Module
object UseCaseModule {

    @Provides
    fun provideRepository(): AuthErrorsRepository {
        return FirebaseCommonErrorsRepositoryImpl
    }

    @Provides
    fun provideResetPasswordUseCase(errorsRepository: AuthErrorsRepository): ResetPasswordErrorUseCase {
        return ResetPasswordErrorUseCase(errorsRepository)
    }

    @Provides
    fun provideSignInErrorUseCase(errorsRepository: AuthErrorsRepository): SignInErrorUseCase {
        return SignInErrorUseCase(errorsRepository)
    }

    @Provides
    fun provideSignUpErrorUseCase(errorsRepository: AuthErrorsRepository): SignUpErrorUseCase {
        return SignUpErrorUseCase(errorsRepository)
    }
}