module org.github.jhy.chat.common {
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires lombok;

    exports org.github.jhy.chat.common;
    exports org.github.jhy.chat.common.model;
}