module org.github.jhy.chat.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires jdk.jsobject;
    requires com.fasterxml.jackson.databind;
    requires io.netty.transport;
    requires lombok;
    requires org.github.jhy.chat.common;
    requires io.netty.buffer;
    requires io.netty.codec;
    requires io.netty.common;
    requires org.slf4j;
    requires ch.qos.logback.core;
    requires ch.qos.logback.classic;
    requires com.google.common;

    opens org.github.jhy.chat.client.gui.controller;
    exports org.github.jhy.chat.client.gui.controller;

    exports org.github.jhy.chat.client;
    exports org.github.jhy.chat.client.gui;
    opens org.github.jhy.chat.client.gui;
    exports org.github.jhy.chat.client.netty;
}