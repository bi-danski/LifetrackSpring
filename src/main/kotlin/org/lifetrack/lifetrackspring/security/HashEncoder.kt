package org.lifetrack.lifetrackspring.security

import org.lifetrack.lifetrackspring.database.model.data.Hash
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class HashEncoder {
    private val bCryptPasswordEncoder = BCryptPasswordEncoder()

    fun hashPasswd(passwd: String): Hash {
        val hash = bCryptPasswordEncoder.encode(passwd)
        return Hash(passwordHash = hash.toString(), salt = null)
    }
    fun match(passwd: String, passwdHash: String): Boolean{
        return bCryptPasswordEncoder.matches(passwd, passwdHash)
    }
}