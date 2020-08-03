package com.example.demo.user.api.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.PathProvider
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Value("\${swagger.path:/}")
    lateinit var path: String

    @Bean
    fun api(): Docket? {
        return Docket(DocumentationType.SWAGGER_2)
            .pathProvider(KubePathProvidor())
            .groupName("demo-api")
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController::class.java))
            .paths(PathSelectors.any())
            .build()
    }

    private fun apiInfo(): ApiInfo? {
        return ApiInfoBuilder().title("Demo User API")
            .description("")
            .version("1.0")
            .build()
    }

    inner class KubePathProvidor : PathProvider {
        override fun getApplicationBasePath(): String {
            return path
        }

        override fun getOperationPath(operationPath: String): String {
            return operationPath
        }

        override fun getResourceListingPath(groupName: String?, apiDeclaration: String?): String? {
            return null
        }
    }
}
