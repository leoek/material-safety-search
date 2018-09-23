package mss.domain.entity;

import java.util.Date;

public class Log {
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
