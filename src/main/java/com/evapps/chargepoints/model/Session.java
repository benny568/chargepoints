package com.evapps.chargepoints.model;

import org.hibernate.annotations.Proxy;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Proxy(lazy=false) // Had to put this in to eliminate the error: "org.hibernate.LazyInitializationException: could not initialize proxy [org.ev.chargingstations.model.StationInfo#831] - no Session"
@Table(name="session")
public class Session {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    String loc;
    String type;
    String status;
    LocalDateTime startedAt;
    LocalDateTime stoppedAt;
    long elapsedMins;

    public Session() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public long getElapsedMins() {
        return elapsedMins;
    }

    public void setElapsedMins(long elapsedMins) {
        this.elapsedMins = elapsedMins;
    }

    public LocalDateTime getStoppedAt() {
        return stoppedAt;
    }

    public void setStoppedAt(LocalDateTime stoppedAt) {
        this.stoppedAt = stoppedAt;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("------------------------------------\n");
        sb.append("Id        : " + this.id + "\n");
        sb.append("Location  : " + this.loc + "\n" );
        sb.append("Type      : " + this.type + "\n" );
        sb.append("Status    : " + this.status + "\n" );
        sb.append("Started at: " + this.startedAt + "\n" );
        sb.append("Stopped at: " + this.stoppedAt + "\n" );
        sb.append("Duration  : " + this.elapsedMins + "\n" );
        sb.append("------------------------------------\n");

        return sb.toString();
    }
}
