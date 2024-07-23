package turing.turing.domain.notice.fcm;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.stereotype.Service;
import turing.turing.domain.notice.fcm.dto.FcmSendDeviceDto;
import turing.turing.domain.notice.fcm.dto.FcmSendDto;

import java.io.IOException;
import java.util.List;

@Service
public interface FcmService {

    int sendMessageTo(FcmSendDto fcmSendDto) throws IOException, FirebaseMessagingException;
    List<FcmSendDeviceDto> selectFcmSendList();
}
