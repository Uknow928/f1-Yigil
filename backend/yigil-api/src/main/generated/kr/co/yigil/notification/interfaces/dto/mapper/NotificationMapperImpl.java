package kr.co.yigil.notification.interfaces.dto.mapper;

import javax.annotation.processing.Generated;
import kr.co.yigil.notification.domain.Notification;
import kr.co.yigil.notification.interfaces.dto.NotificationInfoDto;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-04T18:48:06+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationInfoDto notificationToNotificationInfoDto(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationInfoDto notificationInfoDto = new NotificationInfoDto();

        notificationInfoDto.setMessage( notification.getMessage() );

        notificationInfoDto.setCreateDate( notification.getCreatedAt().toString() );

        return notificationInfoDto;
    }
}
