package com.alltimeslucky.battletron.player.controller.remote;

import com.alltimeslucky.battletron.exception.BattletronException;
import com.alltimeslucky.battletron.exception.ExceptionCode;

class RemoteAiConnectionException extends BattletronException {

    RemoteAiConnectionException(Throwable cause) {
        super(ExceptionCode.REMOTE_AI_CONNECTION_ERROR, cause);
    }
}
