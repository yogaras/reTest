package com.estimulo.base.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class SessionController implements HttpSessionListener {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private int userCount;
    private ServletContext application;

    @Override
    public void sessionCreated(HttpSessionEvent SE) {
       if (logger.isDebugEnabled()) {
             logger.debug(" UserCounter : sessionCreated 시작 ");
       }
       HttpSession session = SE.getSession();
       application = session.getServletContext();

             if(application.getAttribute("userCount") == null) {
                application.setAttribute("userCount", 1);
             }else {
                userCount = (Integer)application.getAttribute("userCount");
                application.setAttribute("userCount", ++userCount);
             }
       
       System.out.println("접속자 수"+userCount);
       
       if (logger.isDebugEnabled()) {
             logger.debug(" UserCount : sessionCreated 종료 ");
         }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
       if (logger.isDebugEnabled()) {
             logger.debug(" UserCount : sessionDestroyed 시작 ");
       }
       
       if(se.getSession().getServletContext().getAttribute("userCount") != null) {
          userCount = (Integer)application.getAttribute("userCount");
          application.setAttribute("userCount", --userCount);
       }
       
       System.out.println("접속자 수"+userCount);
       
       if (logger.isDebugEnabled()) {
             logger.debug(" UserCount : sessionDestroyed 종료 ");
         }
    }
 }
