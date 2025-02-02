package kr.co.yigil.notification.domain;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

public interface NotificationService {

    Flux<ServerSentEvent<Notification>> getNotificationStream(Long memberId);

    void sendNotification(NotificationType notificationType, Long senderId, Long receiverId);

    Slice<Notification> getNotificationSlice(Long memberId, PageRequest pageRequest);
}
