package com.jayden;

import com.sun.jdmk.comm.HtmlAdaptorServer;

import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * @author sunyongjun
 * @since 2019/9/17
 */
public class JmxServer {

    public static void main(String[] args) throws Exception {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        mBeanServer.registerMBean(new Hello(), new ObjectName("test:name=test1"));

        ObjectName adapterName = new ObjectName("HtmlAgent:name=htmladapter,port=8082");
        HtmlAdaptorServer adapter = new HtmlAdaptorServer();//需要引入jmxtools.jar
        mBeanServer.registerMBean(adapter, adapterName);
        adapter.start();

        System.out.println("server started");
        Thread.sleep(Long.MAX_VALUE);
    }

    private static class Hello extends NotificationBroadcasterSupport implements HelloMBean {
        private int seq;

        @Override
        public String getName() {
            return "hello test";
        }

        @Override
        public void hello() {
            Notification notification = new Notification("type.hello", this, ++seq);
            sendNotification(notification);
        }
    }

    public interface HelloMBean {
        String getName();

        void hello();
    }
}
