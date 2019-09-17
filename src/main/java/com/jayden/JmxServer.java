package com.jayden;

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
