package info.novatec.configuration

import io.micronaut.context.annotation.BootstrapContextCompatible
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.ClientFilterChain
import io.micronaut.http.filter.HttpClientFilter
import io.reactivex.Flowable
import org.reactivestreams.Publisher

@Filter(serviceId = ["http://localhost:10000/like-service/local"])
@BootstrapContextCompatible
class ConfigServerBasicAuthFilter : HttpClientFilter {
    override fun doFilter(request: MutableHttpRequest<*>?, chain: ClientFilterChain?): Publisher<out HttpResponse<*>> {
        return Flowable.fromPublisher(chain?.proceed(request?.basicAuth("config", "secret")))
    }
}
