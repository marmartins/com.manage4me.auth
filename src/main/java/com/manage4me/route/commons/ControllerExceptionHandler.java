package com.manage4me.route.commons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class ControllerExceptionHandler {

    private ResourceBundle resourceBundle;

    public ControllerExceptionHandler(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    private final Function<Errors, String> messageByError =
            (er) -> {
                try {
                    return APPException.getMessage(resourceBundle.getString(er.code()), er.params());
                } catch (Exception e) {
                    return er.code();
                }
            };

    private final Function<APPException, String> message =
            (ex) -> {
                try {
                    return messageByError.apply(ex.getError());
                } catch (Exception e) {
                    return ex.getError().code();
                }
            };

    private final Function<RuntimeException, String> responseByException = (ex) -> {
        if (ex instanceof APPException) {
            return "{\"code\":\"" + ((APPException) ex).getError().code()
                    + "\", \"message\":\"" + message.apply((APPException) ex) + "\"}";
        }
        return "{\"code\":\"500\", \"message\":\"Internal Server Error\"}";
    };

    private final Function<Errors, String> responseByError = (er) ->
            "{\"code\":\"" + er.code() + "\", \"message\":\"" + messageByError.apply(er) + "\"}";

    public ResponseStatusException throwException(Supplier<String> errorCode, String... params) {
        String message = responseByError.apply(new Errors(errorCode.get(), params));
        log.error("Errors code :: {} - Params :: {} - message :: {}", errorCode.get(), params, message);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }

}
