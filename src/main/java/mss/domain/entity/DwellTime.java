package mss.domain.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dwelltime")
public class DwellTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="logId")
    private Log log;
    private LocalDateTime start;
    private LocalDateTime end;
    private String datasheetId;
    private String section;

    public boolean equals(Object obj){
        if (obj == null) return false;
        if (!(obj instanceof DwellTime )) return false;
        return (obj == this || ((DwellTime) obj).getId().equals(this.getId()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
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

    public String getDatasheetId() {
        return datasheetId;
    }

    public void setDatasheetId(String datasheetId) {
        this.datasheetId = datasheetId;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
