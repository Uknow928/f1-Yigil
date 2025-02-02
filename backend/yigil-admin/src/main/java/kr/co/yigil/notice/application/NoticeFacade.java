package kr.co.yigil.notice.application;

import kr.co.yigil.notice.domain.NoticeCommand.NoticeCreateRequest;
import kr.co.yigil.notice.domain.NoticeCommand.NoticeUpdateRequest;
import kr.co.yigil.notice.domain.NoticeInfo.NoticeDetail;
import kr.co.yigil.notice.domain.NoticeInfo.NoticeListInfo;
import kr.co.yigil.notice.domain.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeFacade {
    private final NoticeService noticeService;

    public NoticeListInfo getNoticeList(PageRequest pageRequest) {
        return noticeService.getNoticeList(pageRequest);
    }

    public void createNotice(
        NoticeCreateRequest noticeCommand) {
        noticeService.createNotice(noticeCommand);
    }

    public NoticeDetail readNotice(Long noticeId) {
        return noticeService.getNotice(noticeId);
    }

    public void updateNotice(Long noticeId, NoticeUpdateRequest noticeCommand) {
            noticeService.updateNotice(noticeId, noticeCommand);
    }
    public void deleteNotice(Long noticeId) {
        noticeService.deleteNotice(noticeId);
    }
}
