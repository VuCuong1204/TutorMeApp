package vn.tutorme.mobile.data.repo.convert

import vn.tutorme.mobile.base.common.converter.IConverter
import vn.tutorme.mobile.data.source.remote.model.banner.bannerevent.BannerInfoEventDTO
import vn.tutorme.mobile.domain.model.banner.BannerEventInfo

class BannerEventListDTOConvertToBannerListEvent : IConverter<List<BannerInfoEventDTO>, List<BannerEventInfo>> {
    override fun convert(source: List<BannerInfoEventDTO>): List<BannerEventInfo> {
        val list = mutableListOf<BannerEventInfo>()
        source.forEach {
            list.add(BannerEventDTOConvertToBannerEvent().convert(it))
        }

        return list
    }
}

class BannerEventDTOConvertToBannerEvent : IConverter<BannerInfoEventDTO, BannerEventInfo> {
    override fun convert(source: BannerInfoEventDTO): BannerEventInfo {
        return BannerEventInfo(
            id = source.id,
            banner = source.banner,
            title = source.title,
            createTime = source.createTime,
            content = source.content,
            countRegister = source.countRegister,
            describe = source.describe,
            joinInstructions = source.joinInstructions
        )
    }
}
