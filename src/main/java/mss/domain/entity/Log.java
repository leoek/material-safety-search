package mss.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Log {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Date timestamp;
    private String ipAddress;
    private Float dwellTime;
    private Integer resultClicks;
    private Boolean anyResults;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Float getDwellTime() {
        return dwellTime;
    }

    public void setDwellTime(Float dwellTime) {
        this.dwellTime = dwellTime;
    }

    public Integer getResultClicks() {
        return resultClicks;
    }

    public void setResultClicks(Integer resultClicks) {
        this.resultClicks = resultClicks;
    }

    public Boolean getAnyResults() {
        return anyResults;
    }

    public void setAnyResults(Boolean anyResults) {
        this.anyResults = anyResults;
    }
}
