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

    fun validateUserEmail(emailAddress: String): EmailValResponse? {
        val uri = UriComponentsBuilder
            .fromUriString("https://api.apilayer.com/email_verification/check")
            .queryParam("email", emailAddress.trim())
            .toUriString()

        val headers = org.springframework.http.HttpHeaders().apply {
            set("apikey", mailAccesskey)  // ✅ new required header
        }

        val entity = org.springframework.http.HttpEntity<String>(headers)

        val response = restTemplate.exchange(
            uri,
            org.springframework.http.HttpMethod.GET,
            entity,
            EmailValResponse::class.java
        )

        return response.body
    }

    fun validateUserEmailByExpression(emailAddress: String): Boolean {
        val regex = Regex(
            "^(?=.{1,254}$)(?=.{1,64}@)[A-Za-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@" +
                    "(?:(?:[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?\\.)+[A-Za-z]{2,}|" +
                    "\\[(?:(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\])$"
        )
        return regex.matches(emailAddress)
    }

}
