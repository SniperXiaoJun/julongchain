/**
 * Copyright Dingxuan. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bcia.javachain.events.producer;

import com.google.protobuf.InvalidProtocolBufferException;
import org.bcia.javachain.common.exception.ValidateException;
import org.bcia.javachain.common.log.JavaChainLog;
import org.bcia.javachain.common.log.JavaChainLogFactory;
import org.bcia.javachain.common.util.Expiration;
import org.bcia.javachain.common.util.ValidateUtils;
import org.bcia.javachain.msp.IIdentity;
import org.bcia.javachain.msp.IMsp;
import org.bcia.javachain.msp.mgmt.GlobalMspManagement;
import org.bcia.javachain.protos.node.EventsPackage;
import org.bouncycastle.asn1.x509.Time;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.bcia.javachain.common.util.CommConstant.PATH_SEPARATOR;

/**
 * 类描述
 *
 * @author zhouhui
 * @date 2018/05/17
 * @company Dingxuan
 */
public class EventHandler implements IEventHandler {
    private static JavaChainLog log = JavaChainLogFactory.getLog(EventHandler.class);

    private Map<String, EventsPackage.Interest> interestedEvents;
    private Date sessionEndDate;
    private EventsServerConfig eventsServerConfig;

    private IEventHubServer eventHubServer;
    private IEventProcessor eventProcessor;

    @Override
    public EventsPackage.Event handleMessage(EventsPackage.SignedEvent signedEvent) throws
            InvalidProtocolBufferException, ValidateException {
        EventsPackage.Event event = validateEventMessage(signedEvent);
        if (event.hasRegister()) {
            EventsPackage.Register eventRegister = event.getRegister();
            register(eventRegister.getEventsList());
        } else if (event.hasUnregister()) {
            EventsPackage.Unregister eventUnregister = event.getUnregister();
            unregister(eventUnregister.getEventsList());
        }

        return event;
    }

    private void register(List<EventsPackage.Interest> eventsList) {
        if (eventsList == null && eventsList.size() > 0) {
            for (EventsPackage.Interest interest : eventsList) {
                try {
                    EventsUtils.registerHandler(eventProcessor, interest, this);
                    interestedEvents.put(getInterestKey(interest), interest);
                } catch (ValidateException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private String getInterestKey(EventsPackage.Interest interest) throws ValidateException {
        EventsPackage.EventType eventType = interest.getEventType();
        ValidateUtils.isNotNull(eventType, "eventType can not be null");

        String key = null;
        if (EventsPackage.EventType.BLOCK.equals(eventType)) {
            key = PATH_SEPARATOR + String.valueOf(EventsPackage.EventType.BLOCK_VALUE);
        } else if (EventsPackage.EventType.FILTEREDBLOCK.equals(eventType)) {
            key = PATH_SEPARATOR + String.valueOf(EventsPackage.EventType.FILTEREDBLOCK_VALUE);
        } else if (EventsPackage.EventType.REJECTION.equals(eventType)) {
            key = PATH_SEPARATOR + String.valueOf(EventsPackage.EventType.REJECTION_VALUE);
        } else if (EventsPackage.EventType.SMART_CONTRACT.equals(eventType)) {
            key = PATH_SEPARATOR + String.valueOf(EventsPackage.EventType.SMART_CONTRACT_VALUE) + PATH_SEPARATOR +
                    interest.getSmartcontractRegInfo().getSmartcontractId() + PATH_SEPARATOR + interest
                    .getSmartcontractRegInfo().getEventName();
        } else {
            key = PATH_SEPARATOR + String.valueOf(eventType.getNumber());
        }

        return key;
    }

    private void unregister(List<EventsPackage.Interest> eventsList) {
        if (eventsList == null && eventsList.size() > 0) {
            for (EventsPackage.Interest interest : eventsList) {
                try {
                    EventsUtils.deRegisterHandler(eventProcessor, interest, this);
                    interestedEvents.remove(getInterestKey(interest), interest);
                } catch (ValidateException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private EventsPackage.Event validateEventMessage(EventsPackage.SignedEvent signedEvent) throws
            InvalidProtocolBufferException, ValidateException {
        ValidateUtils.isNotNull(signedEvent, "signedEvent can not be null");
        ValidateUtils.isNotNull(signedEvent.getEventBytes(), "event can not be null");
        ValidateUtils.isNotNull(signedEvent.getSignature(), "signature can not be null");

        EventsPackage.Event event = EventsPackage.Event.parseFrom(signedEvent.getEventBytes());
        byte[] creator = event.getCreator().toByteArray();

        Time expireTime = Expiration.expiresAt(creator);
        Date nowDate = new Date();

        if (expireTime != null) {
            this.sessionEndDate = expireTime.getDate();

            if (nowDate.after(sessionEndDate)) {
                //dangqian
                throw new ValidateException("identity expired");
            }
        }

        if (event.getTimestamp() != null) {
            long nowMilliseconds = nowDate.getTime();
            long eventMilliseconds = event.getTimestamp().getSeconds() * 1000 + event.getTimestamp().getNanos() /
                    1000000;

            //TODO:namijibie
            if (nowMilliseconds - eventMilliseconds > eventsServerConfig.getTimeWindow()) {
                throw new ValidateException("out of range");
            }
        }

        eventsServerConfig.getBindingInspector().bind(event);

        IMsp localMsp = GlobalMspManagement.getLocalMsp();
        //TODO

        IIdentity eventIdentity = localMsp.deserializeIdentity(creator);

//        eventIdentity.satisfiesPrincipal();

        eventIdentity.verify(signedEvent.getEventBytes().toByteArray(), signedEvent.getSignature().toByteArray());

        return event;
    }

    @Override
    public Date getSessionEndDate() {
        return sessionEndDate;
    }

    @Override
    public void sendMessage(EventsPackage.Event event) {


    }
}