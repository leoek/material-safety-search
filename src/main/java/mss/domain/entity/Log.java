package mss.domain.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String searchTerm;
    private Date timestamp;
    private String ipAddress;
    private Float dwellTime;
    private Integer resultClicks;
    private Boolean anyResults;
    private Integer resultCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
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

    public Integer getResultCount() {
        return resultCount;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }
}
