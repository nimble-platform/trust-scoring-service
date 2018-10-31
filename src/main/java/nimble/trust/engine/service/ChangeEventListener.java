package nimble.trust.engine.service;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import nimble.trust.engine.domain.NotificationLog;
import nimble.trust.engine.domain.Status;
import nimble.trust.web.dto.ChangeEvent;


@Component
public class ChangeEventListener {

	private static Logger log = LoggerFactory.getLogger(ChangeEventListener.class);
	
	@Autowired
	private NotificationLogService notificationLogService;
	
	@Autowired
	private StatusService statusService;
	
	@Autowired
	private ChangeEventHandlerService changeEventHandlerService;

	
	@Autowired
    public ChangeEventListener(EventBus eventBus, AsyncEventBus asyncEventBus) {
        eventBus.register(this); // register this instance with the event bus so it receives any events
        asyncEventBus.register(this);
    }
	
	@Subscribe
	public void changeNotificationEvent(ChangeEvent changeEvent) throws Exception {
		
		NotificationLog notificationLog = new NotificationLog();
		notificationLog.setContent(changeEvent.getCompanyIdentifier());
		notificationLog.setType(changeEvent.getChangeType());
		notificationLog.setStatus(statusService.findOrCreate(Status.Pending));
		notificationLog = notificationLogService.save(notificationLog);
		log.info("changeEventNotification-received:"+changeEvent);
		
		try {
			changeEventHandlerService.handleChangeEvent(changeEvent);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		notificationLog.setStatus(statusService.findOrCreate(Status.Completed));
		notificationLog = notificationLogService.save(notificationLog);
		
		log.info("changeEventNotification-completed-ok:"+changeEvent);
		
	}
	


}
