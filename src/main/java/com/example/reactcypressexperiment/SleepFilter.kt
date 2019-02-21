package com.example.reactcypressexperiment

import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import java.lang.Thread.sleep
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

@Component
class SleepFilter : GenericFilterBean() {
    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        sleep(1000)
        chain.doFilter(req, res)
    }
}
