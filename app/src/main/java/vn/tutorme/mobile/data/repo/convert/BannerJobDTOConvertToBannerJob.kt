package vn.tutorme.mobile.data.repo.convert

import vn.tutorme.mobile.base.common.converter.IConverter
import vn.tutorme.mobile.data.source.remote.model.banner.bannerjob.BannerInfoJobDTO
import vn.tutorme.mobile.domain.model.banner.BannerJobInfo

class BannerJobDTOConvertToBannerJob : IConverter<List<BannerInfoJobDTO>, List<BannerJobInfo>> {
    override fun convert(source: List<BannerInfoJobDTO>): List<BannerJobInfo> {
        val list = mutableListOf<BannerJobInfo>()
        source.forEach {
            list.add(BannerJobInfo(
                id = it.id,
                banner = it.banner,
                title = it.title,
                income = it.income,
                countMember = it.countMember,
                deadlineRegister = it.deadlineRegister,
                describe = it.describe,
                requestJob = it.requestJob,
                benefit = it.benefit
            ))
        }

        return list
    }
}
