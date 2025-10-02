package org.lifetrack.lifetrackspring.utils

import org.lifetrack.lifetrackspring.database.model.dto.EmailValResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class EmailValidation(
    @param:Value("\${mailplayer.access}") private val mailAccesskey: String
) {
    private val restTemplate = RestTemplate()

    fun validateUserEmail(emailAddress: String): EmailValResponse?{
        val uri = UriComponentsBuilder
            .fromUriString("https://https://apilayer.net/api/check")
            .queryParam("access_key", mailAccesskey)
            .queryParam("email", emailAddress.trim())
            .queryParam("smtp", 1)
            .queryParam("format", 1)
            .toUriString()
        return restTemplate.getForObject(uri, EmailValResponse::class.java)
    }
}