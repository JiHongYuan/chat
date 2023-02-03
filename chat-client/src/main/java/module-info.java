module org.github.jhy.chat.client {
    requires jdk.jsobject;

    requires javafx.controls;
    requires javafx.web;

    requires lombok;

    requires io.netty.buffer;
    requires io.netty.codec;
    requires io.netty.common;
    requires io.netty.transport;

    requires org.slf4j;

    requires ch.qos.logback.core;
    requires ch.qos.logback.classic;

    requires com.google.common;
    requires com.fasterxml.jackson.databind;

    requires org.github.jhy.chat.common;

    opens org.github.jhy.chat.client.gui.controller;
    exports org.github.jhy.chat.client.gui.controller;

    exports org.github.jhy.chat.client;
    exports org.github.jhy.chat.client.gui;
    opens org.github.jhy.chat.client.gui;
    exports org.github.jhy.chat.client.netty;
}