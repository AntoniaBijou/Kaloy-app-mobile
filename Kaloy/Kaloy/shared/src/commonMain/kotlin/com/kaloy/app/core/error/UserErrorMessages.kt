package com.kaloy.app.core.error

object UserErrorMessages {
    fun fromThrowable(throwable: Throwable?): String {
        return fromRawMessage(throwable?.message)
    }

    fun fromRawMessage(rawMessage: String?): String {
        val message = (rawMessage ?: "").trim()
        if (message.isBlank()) {
            return "Une erreur est survenue. Veuillez réessayer."
        }

        val normalized = message.lowercase()

        return when {
            normalized.contains("constraint violation") ||
                normalized.contains("duplicate key") ||
                normalized.contains("violates unique constraint") ||
                normalized.contains("already exists") ||
                normalized.contains("already associated") ||
                normalized.contains("déjà associé") ||
                normalized.contains("déjà exist") -> {
                when {
                    normalized.contains("phone") || normalized.contains("telephone") || normalized.contains("numéro") ->
                        "Ce numéro de téléphone est déjà associé à un compte."
                    normalized.contains("email") || normalized.contains("mail") ->
                        "Cet email est déjà associé à un compte."
                    else -> "Cette information est déjà utilisée."
                }
            }

            normalized.contains("bad credentials") ||
                normalized.contains("incorrect") ||
                normalized.contains("invalid credentials") ||
                normalized.contains("mauvais") ||
                normalized.contains("email ou mot de passe incorrect") ->
                "Email ou mot de passe incorrect."

            normalized.contains("otp") ||
                normalized.contains("code") && (normalized.contains("invalid") || normalized.contains("expired") || normalized.contains("incorrect")) ->
                "Le code de vérification est invalide ou expiré."

            normalized.contains("email") && (normalized.contains("not found") || normalized.contains("introuvable")) ->
                "Aucun compte ne correspond à cet email."

            normalized.contains("phone") && (normalized.contains("not found") || normalized.contains("introuvable")) ->
                "Aucun compte ne correspond à ce numéro de téléphone."

            normalized.contains("network") || normalized.contains("timeout") || normalized.contains("unreachable") ->
                "Le serveur est indisponible pour le moment. Veuillez réessayer."

            normalized.contains("unauthorized") || normalized.contains("forbidden") ->
                "Vous n'êtes pas autorisé à effectuer cette action."

            normalized.contains("internal error") || normalized.contains("server error") || normalized.contains("500") ->
                "Une erreur serveur est survenue. Veuillez réessayer plus tard."

            else -> {
                message
                    .replace(Regex("\\s+"), " ")
                    .replace(Regex("(?i)constraint violation"), "")
                    .replace(Regex("(?i)duplicate key"), "")
                    .replace(Regex("(?i)SQL"), "")
                    .trim()
                    .ifBlank { "Une erreur est survenue. Veuillez réessayer." }
                    .let { sanitized ->
                        if (sanitized.length > 120) "Une erreur est survenue. Veuillez réessayer." else sanitized
                    }
            }
        }
    }
}
