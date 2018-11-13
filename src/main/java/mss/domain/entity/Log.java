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
    private String publicIp;
    private String localIp;
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "log")
    private List<DwellTime> dwellTimes;
    private Integer resultClicks;
    private Boolean anyResults;
    private Integer resultCount;
    private String session;

    public boolean equals(Object obj){
        if (obj == null) return false;
        if (!(obj instanceof Log )) return false;
        return (obj == this || ((Log) obj).getId().equals(this.getId()));
    }

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

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
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
