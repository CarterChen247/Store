package com.dropbox.android.sample

import android.app.Application
import android.content.Context
import com.dropbox.android.external.fs3.Persister
import com.dropbox.android.external.store4.Store
import com.dropbox.android.sample.data.model.Post
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.core.FlipperClient
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.soloader.SoLoader
import okio.BufferedSource
import java.io.IOException

class SampleApp : Application() {
    lateinit var roomStore: Store<String, List<Post>>

    lateinit var storeMultiParam: Store<Pair<String, RedditConfig>, List<Post>>

    lateinit var configStore: Store<Unit, RedditConfig>

    lateinit var persister: Persister<BufferedSource, Pair<String, String>>

    override fun onCreate() {
        super.onCreate()
        initPersister()
        roomStore = Graph.provideRoomStore(this)
        storeMultiParam = Graph.provideRoomStoreMultiParam(this)
        configStore = Graph.provideConfigStore(this)

        initFlipper()
    }

    private fun initFlipper() {
        SoLoader.init(this, false)

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val client: FlipperClient = AndroidFlipperClient.getInstance(this)
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            client.start()
        }
    }

    private fun initPersister() {
        try {
            persister = Graph.newPersister(this.cacheDir)
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }
    }
}
