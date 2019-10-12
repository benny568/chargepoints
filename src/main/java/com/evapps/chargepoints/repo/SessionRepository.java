package com.evapps.chargepoints.repo;

import com.evapps.chargepoints.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {

}