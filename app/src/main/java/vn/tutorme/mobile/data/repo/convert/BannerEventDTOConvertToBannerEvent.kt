package vn.tutorme.mobile.data.repo.convert

import vn.tutorme.mobile.base.common.converter.IConverter
import vn.tutorme.mobile.data.source.remote.model.banner.bannerevent.BannerInfoEventDTO
import vn.tutorme.mobile.domain.model.banner.BannerEventInfo

class BannerEventDTOConvertToBannerEvent : IConverter<List<BannerInfoEventDTO>, List<BannerEventInfo>> {
    override fun convert(source: List<BannerInfoEventDTO>): List<BannerEventInfo> {
        val list = mutableListOf<BannerEventInfo>()
        source.forEach {
            list.add(BannerEventInfo(
                id = it.id,
                banner = it.banner,
                title = it.title,
                createTime = it.createTime,
                content = it.content,
                countRegister = it.countRegister,
                describe = it.describe,
                joinInstructions = it.joinInstructions
            ))
        }

        return list
    }
}
