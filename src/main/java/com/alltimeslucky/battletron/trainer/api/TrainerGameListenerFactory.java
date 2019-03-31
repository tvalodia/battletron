package com.alltimeslucky.battletron.trainer.api;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

public class TrainerGameListenerFactory {

    public TrainerGameListener get(String url) {
        WebTarget t = ClientBuilder.newClient().target(url);
        //t.property(ClientProperties.CONNECT_TIMEOUT, 5000);
        //t.property(ClientProperties.READ_TIMEOUT, 5000);

        // create a new client proxy for the RemoteAi resource
        TrainerApi trainerResource = WebResourceFactory.newResource(TrainerApi.class, t);

        return new TrainerGameListener(trainerResource);
    }
}
