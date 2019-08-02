package info.novatec.configuration

import com.sun.org.apache.xml.internal.security.utils.Base64
import io.micronaut.security.token.jwt.signature.rsa.RSASignatureConfiguration
import sun.security.rsa.RSAPublicKeyImpl
import java.security.interfaces.RSAPublicKey

class CustomRSASignatureConfiguration : RSASignatureConfiguration {

    val key = """-----BEGIN PUBLIC KEY-----
    MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuQTabXCRBG62Pm12E+1y
    deRzDz3szcO1g2EZYSjYHAadMHPNAmEPohWM9OzEHMybJQtRuXUCfHKmgshN+e49
    4dk3atcNkiFG0eueZQdkTXA4kzVX6cFF3T1Vnvlvqk8wu5alfvDQV+spuZt2/KfW
    ze7edRv6cUeuWIqMfYFcEO2PRDpcQdMTvVXUu7R/QN8SM1Cm+X/IMtrDqx+0i3qa
    L55QiO2FZXQCsA3GaypHc+fvSCvCvtTkgiyAyB/5RuFhmq0LNhYyxP+MszxvBDun
    SCgW1ICV+ai9JAnGAsjrYBHVUimlJUZiTon2whs/aeN3Y5kyAwxd7gdUf/GBlenc
    bwIDAQAB
    -----END PUBLIC KEY-----""".trimIndent()

    override fun getPublicKey(): RSAPublicKey {
        return RSAPublicKeyImpl(Base64.decode(key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\n", "")))
    }
}