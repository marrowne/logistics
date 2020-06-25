package it.logistics.tracking.application;

import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;


public class CorsFilterTest {

    @Test
    public void testCorsFilter() throws IOException, ServletException {
        // given
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        CorsFilter corsFilter = new CorsFilter();
        when(httpServletRequest.getRequestURI()).thenReturn("/otherurl.jsp");

        // when
        corsFilter.doFilter(httpServletRequest, httpServletResponse,
                filterChain);

        // then
        verify(httpServletResponse, atLeastOnce()).setHeader(
                "Access-Control-Allow-Methods",
                "POST, PUT, GET, OPTIONS, DELETE, PATCH");
    }
}
