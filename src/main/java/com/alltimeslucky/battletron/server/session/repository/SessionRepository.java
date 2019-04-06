package com.alltimeslucky.battletron.server.session.repository;

import com.alltimeslucky.battletron.server.session.service.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

/**
 * Stores all instances of Session.
 */
@Repository
public class SessionRepository {

    private static final Logger LOG = LogManager.getLogger();

    private Map<String, Session> data;

    public SessionRepository() {
        data = new ConcurrentHashMap<>();
    }

    /**
     * Inserts a new Session into the repository.
     *
     * @param sessionId   The key to use for the Session
     * @param session The Session to insert.
     */
    public void add(String sessionId, Session session) {
        data.put(sessionId, session);
    }

    public Session get(String sessionId) {
        return data.get(sessionId);
    }

    public void delete(String sessionId) {
        data.remove(sessionId);
        LOG.debug(String.format("Session %s has been removed", sessionId));
    }

    public boolean contains(String sessionId) {
        return data.containsKey(sessionId);
    }

    public void clear() {
        data.clear();
    }
}
