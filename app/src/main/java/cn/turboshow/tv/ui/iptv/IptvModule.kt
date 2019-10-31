package cn.turboshow.tv.ui.iptv

import androidx.lifecycle.ViewModel
import cn.turboshow.tv.di.FragmentScoped
import cn.turboshow.tv.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
internal abstract class IptvModule {
    @FragmentScoped
    @ContributesAndroidInjector(modules = [])
    internal abstract fun contributeIptvOverlayFragment(): IptvOverlayFragment

    @Binds
    @IntoMap
    @ViewModelKey(IptvViewModel::class)
    abstract fun bindIptvViewModel(viewModel: IptvViewModel): ViewModel
}