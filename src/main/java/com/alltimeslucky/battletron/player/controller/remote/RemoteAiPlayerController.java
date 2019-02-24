package com.alltimeslucky.battletron.player.controller.remote;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.player.controller.remote.api.RemoteAiApi;
import com.alltimeslucky.battletron.player.controller.remote.api.RemoteAiGameDto;
import com.alltimeslucky.battletron.player.model.Direction;
import com.alltimeslucky.battletron.player.model.Player;
import com.alltimeslucky.battletron.server.game.api.dto.GameDto;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

public class RemoteAiPlayerController implements PlayerController {

    private String url;
    private Player player;
    RemoteAiApi directionResource;

    public RemoteAiPlayerController(String url, Player player) {

        this.url = url;
        this.player = player;

        // create a new JAX-RS 2.0 target pointing to the root of the web api
        WebTarget t = ClientBuilder.newClient().target(url);
        t.property(ClientProperties.CONNECT_TIMEOUT, 5000);
        t.property(ClientProperties.READ_TIMEOUT, 2000);

        // create a new client proxy for the BooksResource
        directionResource = WebResourceFactory.newResource(RemoteAiApi.class, t);

    }

    @Override
    public void execute(Game game) {
        try {
            player.setDirection(Direction.valueOf(directionResource.getDirection(new RemoteAiGameDto(game, player))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
