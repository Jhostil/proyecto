package co.edu.uniquindio.proyecto.filtro;

import co.edu.uniquindio.proyecto.bean.SeguridadBean;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SeguridadFilter implements Filter {

    private final String PAGINAINICIO = "/index.xhtml";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {

        try {
            final HttpServletRequest request = (HttpServletRequest) servletRequest;
            final HttpServletResponse response = (HttpServletResponse) servletResponse;
            final String requestURI = request.getRequestURI();
//Aplicar el filtro a esta carpeta
            if (requestURI.startsWith("/usuario/") || requestURI.startsWith("/profesor/") ) {

//Obtenemos el objeto seguridadBean de la sesión actual
                SeguridadBean userManager = (SeguridadBean) request.getSession().getAttribute("seguridadBean");

                if (userManager != null) {
                    if (requestURI.startsWith("/profesor/"))
                    {
                        if (userManager.isProfesor()) {
//El usuario está logueado entonces si puede ver la página solicitada

                            filterChain.doFilter(servletRequest, servletResponse);
                        } else {
//El usuario no está logueado, entonces se redirecciona al inicio

                            response.sendRedirect(request.getContextPath() + PAGINAINICIO);
                        }
                    } else {
                        if (userManager.isAutenticado()) {
//El usuario está logueado entonces si puede ver la página solicitada

                            filterChain.doFilter(servletRequest, servletResponse);
                        } else {
//El usuario no está logueado, entonces se redirecciona al inicio

                            response.sendRedirect(request.getContextPath() + PAGINAINICIO);
                        }
                    }
                } else {
//El usuario no está logueado, entonces se redirecciona al inicio
                    response.sendRedirect(request.getContextPath() + PAGINAINICIO);
                }

            }else{
//La página solicitada no está en la carpeta /profesor entonces el filtro no aplica

                filterChain.doFilter(servletRequest, servletResponse);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}
