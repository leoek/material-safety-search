package mss.domain.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String searchTerm;
    private LocalDateTime start;
    private LocalDateTime end;
    private String ipAddress;
    @OneToMany(mappedBy = "log")
    private List<DwellTime> dwellTimes;
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

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public List<DwellTime> getDwellTimes() {
        return dwellTimes;
    }

    public void setDwellTimes(List<DwellTime> dwellTimes) {
        this.dwellTimes = dwellTimes;
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
