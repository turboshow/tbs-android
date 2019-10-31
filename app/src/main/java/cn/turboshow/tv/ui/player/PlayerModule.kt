package cn.turboshow.tv.ui.player

import androidx.lifecycle.ViewModel
import cn.turboshow.tv.di.FragmentScoped
import cn.turboshow.tv.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
internal abstract class PlayerModule {
    @FragmentScoped
    @ContributesAndroidInjector(modules = [])
    internal abstract fun contributePlayerOverlayFragment(): PlayerOverlayFragment

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    abstract fun bindPlayerViewModel(viewModel: PlayerViewModel): ViewModel
}