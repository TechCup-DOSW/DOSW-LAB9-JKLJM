package edu.eci.dosw.techcup_futbol.model.UsersAndSecurity;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordAudit {
    private static final Logger logger = LoggerFactory.getLogger(RecordAudit.class);

    private int id;
    private String action;
    private LocalDateTime date;

    public static RecordAudit createAudit(String action, User user) {
        RecordAudit newLog = new RecordAudit();
        
        newLog.action = action; 
        newLog.date = LocalDateTime.now(); //Captures the exact moment
        
        logger.info("Audit registered: user={} action={}", user.getName(), action);
        
        return newLog;
    }
}