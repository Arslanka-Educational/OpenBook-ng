package model

enum class UserType(val typeName: String) {
    CLIENT("client"),
    LIBRARY("library"),
    MODERATOR("moderator")
}