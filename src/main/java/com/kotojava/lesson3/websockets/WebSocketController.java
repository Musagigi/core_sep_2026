package com.kotojava.lesson3.websockets;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WebSocketController extends TextWebSocketHandler {

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    // Keep track of the scheduled task per session so we can cancel it on close
    private final Map<String, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            try {
                if (session.isOpen()) {
                    double value = Math.random(); // random double in [0, 1)
                    session.sendMessage(new TextMessage(String.valueOf(value)));
                } else {
                    cancelTask(session.getId());
                }
            } catch (IOException e) {
                cancelTask(session.getId());
            }
        }, 0, 50, TimeUnit.MILLISECONDS); // every 0.05s

        tasks.put(session.getId(), future);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        cancelTask(session.getId());
    }

    private void cancelTask(String sessionId) {
        ScheduledFuture<?> future = tasks.remove(sessionId);
        if (future != null) {
            future.cancel(true);
        }
    }
}