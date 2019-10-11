package info.novatec.configuration

import io.micronaut.security.token.jwt.signature.rsa.RSASignatureConfiguration
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.inject.Named

@Named("customRSASignaturConfiguration")
class CustomRSASignatureConfiguration : RSASignatureConfiguration {

    private val key = """-----BEGIN PUBLIC KEY-----
    MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuQTabXCRBG62Pm12E+1y
    deRzDz3szcO1g2EZYSjYHAadMHPNAmEPohWM9OzEHMybJQtRuXUCfHKmgshN+e49
    4dk3atcNkiFG0eueZQdkTXA4kzVX6cFF3T1Vnvlvqk8wu5alfvDQV+spuZt2/KfW
    ze7edRv6cUeuWIqMfYFcEO2PRDpcQdMTvVXUu7R/QN8SM1Cm+X/IMtrDqx+0i3qa
    L55QiO2FZXQCsA3GaypHc+fvSCvCvtTkgiyAyB/5RuFhmq0LNhYyxP+MszxvBDun
    SCgW1ICV+ai9JAnGAsjrYBHVUimlJUZiTon2whs/aeN3Y5kyAwxd7gdUf/GBlenc
    bwIDAQAB
    -----END PUBLIC KEY-----""".trimIndent()

    override fun getPublicKey(): RSAPublicKey {
        val base64Key = Base64.getDecoder().decode(key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\n", "").replace(" ", ""))
        return KeyFactory.getInstance("RSA").generatePublic(X509EncodedKeySpec(base64Key))
                as RSAPublicKey
    }
}