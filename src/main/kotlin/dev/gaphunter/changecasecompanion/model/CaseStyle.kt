package dev.gaphunter.changecasecompanion.model

enum class CaseStyle(val displayName: String) {
    CAMEL("camelCase"),
    PASCAL("PascalCase"),
    SNAKE("snake_case"),
    KEBAB("kebab-case"),
    CONSTANT("CONSTANT_CASE"),
}
