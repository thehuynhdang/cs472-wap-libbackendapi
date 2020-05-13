package g8.library.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import g8.library.api.dataaccess.SingletoneDataLoader;

@Component
public class AppEventListenerBean {
	private Logger logger = LoggerFactory.getLogger(AppEventListenerBean.class);
	
	@EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
		//load singleton data into the memory at the start
		SingletoneDataLoader.getInstance();
		logger.info("****DATA LOADED.");
    }
}
