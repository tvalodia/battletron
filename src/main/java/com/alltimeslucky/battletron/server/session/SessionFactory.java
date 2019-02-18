package com.alltimeslucky.battletron.server.session;

public class SessionFactory {

    public Session get(String sessionId) {
        return new Session(sessionId);
    }
}
