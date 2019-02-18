package com.alltimeslucky.battletron.server.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores all instances of Session.
 */
public class SessionRepository {

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
    }

    public boolean contains(String sessionId) {
        return data.containsKey(sessionId);
    }
}
