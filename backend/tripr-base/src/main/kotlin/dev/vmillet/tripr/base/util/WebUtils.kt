package dev.vmillet.tripr.base.util

import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.servlet.LocaleResolver
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver




@Component
class WebUtils(
    messageSource: MessageSource,
    localeResolver: LocaleResolver,
    templateEngine: TemplateEngine,
    @Value("\${app.baseHost}")
    baseHost: String
) {

    init {
        WebUtils.messageSource = messageSource
        WebUtils.localeResolver = localeResolver
        WebUtils.templateEngine = templateEngine
        WebUtils.baseHost = baseHost
    }

    companion object {

        const val EMAIL_PATTERN =
                "([a-zA-Z0-9][\\-\\.\\+_]?)*[a-zA-Z0-9]+@([a-zA-Z0-9][\\-\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]+"

        lateinit var messageSource: MessageSource

        lateinit var localeResolver: LocaleResolver

        lateinit var templateEngine: TemplateEngine

        lateinit var baseHost: String

        @JvmStatic
        fun getRequest(): HttpServletRequest = (RequestContextHolder.getRequestAttributes() as
                ServletRequestAttributes).request

        @JvmStatic
        fun getMessage(code: String, vararg args: Any?): String? = messageSource.getMessage(code,
                args, code, localeResolver.resolveLocale(getRequest()))

        @JvmStatic
        fun renderTemplate(templateName: String, templateModel: Map<String, Any?>): String {

            val templateResolver = ClassLoaderTemplateResolver()
            templateResolver.suffix = ".html"
            templateResolver.templateMode = TemplateMode.HTML
            templateEngine.setTemplateResolver(templateResolver);

            val thymeleafContext = Context()
            thymeleafContext.setVariables(templateModel)
            thymeleafContext.setVariable("baseHost", baseHost)
            return templateEngine.process("templates$templateName", thymeleafContext)
        }

    }

}
