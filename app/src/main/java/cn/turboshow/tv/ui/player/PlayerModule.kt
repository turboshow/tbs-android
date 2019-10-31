package cn.turboshow.tv.ui.player

import androidx.lifecycle.ViewModel
import cn.turboshow.tv.di.ViewModelKey
import cn.turboshow.tv.ui.iptv.IptvViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
internal abstract class PlayerModule {
    @Binds
    @IntoMap
    @ViewModelKey(IptvViewModel::class)
    abstract fun bindPlayerViewModel(viewModel: PlayerViewModel): ViewModel
}