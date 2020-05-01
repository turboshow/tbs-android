package cn.turboshow.tv.di

import cn.turboshow.tv.ui.player.PlayerActivity
import cn.turboshow.tv.ui.player.PlayerModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            PlayerModule::class
        ]
    )
    internal abstract fun bindPlayerActivity(): PlayerActivity
}
