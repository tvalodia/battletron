package com.alltimeslucky.battletron.server.session.service;

import java.util.UUID;

public class SessionFactory {

    public Session get(String sessionId) {
        return new Session(sessionId);
    }

    public Session get() {
        return new Session(UUID.randomUUID().toString());
    }
}
