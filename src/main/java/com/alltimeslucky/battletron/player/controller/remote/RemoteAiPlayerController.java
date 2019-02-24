package com.alltimeslucky.battletron.player.controller.remote;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.player.controller.remote.api.RemoteAiApi;
import com.alltimeslucky.battletron.player.controller.remote.api.RemoteAiGameDto;
import com.alltimeslucky.battletron.player.model.Direction;
import com.alltimeslucky.battletron.player.model.Player;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

/**
 * Fetches a Direction for the Player by making a REST call.
 */
public class RemoteAiPlayerController implements PlayerController {

    private Player player;
    private RemoteAiApi directionResource;

    /**
     * Constructor.
     * @param url The URL of the remote server.
     * @param player The player which to control.
     */
    public RemoteAiPlayerController(String url, Player player) {

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
