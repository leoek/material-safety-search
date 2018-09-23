package mss.service;

import mss.domain.entity.Log;
import mss.domain.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoggingService {
    private final LogRepository logRepository;

    private static final Logger log = LoggerFactory.getLogger(LoggingService.class);


    @Autowired
    public LoggingService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }


    public Optional<Log> addLog(Log logObject) {
        log.info("Added log: " + logObject);
        Log savedLog = logRepository.save(logObject);
        return logRepository.findById(savedLog.getId());
    }
}
