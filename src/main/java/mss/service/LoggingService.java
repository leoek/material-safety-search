package mss.service;

import mss.domain.entity.Log;
import mss.domain.entity.TopTerms;
import mss.domain.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public TopTerms getTopTerms(Integer count) {
        return new TopTerms(logRepository.getAll(PageRequest.of(0, count)).getContent());
    }
}
