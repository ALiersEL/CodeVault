package com.aliersel.codevaultbackend.constant.enums;

public enum CodeLanguage {
    PYTHON,
    CPP,
    CSHARP,
    GO,
    JAVA,
    JAVASCRIPT,
    TYPESCRIPT,
    RUST,
    SQL,
    SWIFT,
    KOTLIN,
    LUA,
    PERL,
    RUBY,
    SCALA,
    PHP,
    R,
    OBJECTIVE_C,
    ERLANG,
    ELIXIR,
    DART,
    APEX,
    MARKDOWN;

    public Integer getValue() {
        return ordinal();
    }
}
