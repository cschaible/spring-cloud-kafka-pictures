package info.novatec.configuration

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.security.token.jwt.signature.SignatureConfiguration
import io.micronaut.security.token.jwt.signature.rsa.RSASignature

@Factory
class CustomRSASignatureFactory {

    @Bean
    fun signatureConfiguration(): SignatureConfiguration {
        return RSASignature(CustomRSASignatureConfiguration())
    }

}