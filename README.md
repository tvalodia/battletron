# Battletron

[![Build Status](https://travis-ci.org/tvalodia/battletron.svg?branch=master)](https://travis-ci.org/tvalodia/battletron)

## Introduction

The purpose of this project is provide a way for humans and AIs to battle against one another in a version of the classic game "Tron Light Cycles".

Each player in the game can be controlled by keyboard events, pre-programmed AIs or HTTP responses from remote AI servers.

The challenge is to create a formidable AI that players and other AIs can battle.

##Server
The server is an embedded instance of Jetty with a REST API for managing games. The REST API expects JSON formatted payloads.
Each game is run in its own thread. Game state updates are sent to the Angular clients over web sockets.

##Client
The client is an Angular application hosted by the server. Game state updates are received over web sockets.

##Remote AIs
Games can be configured to fetch player inputs from a remote server. The URL of each remote AI is specified in the "host" field on the "New Game" screen.

At each turn, the server will make a REST request to the remote AI and wait for a response ("UP", "DOWN", "LEFT" or "RIGHT"). The response is used as input into the player's movements.

Games can be configured such that both opposing players are remote AIs.

An example Python implementation of a remote AI that moves the player randomly:

```python
from flask import Flask, request
import random

app = Flask(__name__)

directions = ['UP', 'DOWN', 'LEFT', 'RIGHT']


@app.route('/', methods=['PUT'])
def get_direction():
    print(request.data)
    return random.choice(directions)


if __name__ == '__main__':
    app.run(debug=True)

```