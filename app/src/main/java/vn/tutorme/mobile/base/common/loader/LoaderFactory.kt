package vn.tutorme.mobile.base.common.loader

import vn.tutorme.mobile.base.common.loader.image.GlideImageLoaderImpl
import vn.tutorme.mobile.base.common.loader.image.IImageLoader

object LoaderFactory {
    private val imageLoader = GlideImageLoaderImpl()

    fun glide(): IImageLoader {
        return imageLoader
    }
}
