package com.jayden;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;

/**
 * @author sunyongjun
 * @since 2019/9/16
 */
public class JmxTest {


    private static final Logger logger = LoggerFactory.getLogger(JmxTest.class);
    public static final String JMX_URL_PATTERN = "service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi";

    public static String getServiceUrl(String ip, int port) {
        return String.format(JMX_URL_PATTERN, ip, port);
    }

    public static void main(String[] args) throws Exception {
        String jmxUrl = getServiceUrl("100.118.86.5", 12345);
        JMXConnector jmxConnector = null;
        try {
            JMXServiceURL url = new JMXServiceURL(jmxUrl);
            jmxConnector = JMXConnectorFactory.connect(url, null);
            MBeanServerConnection mbeanConnect = jmxConnector.getMBeanServerConnection();
            System.out.println("mbeanConnect.getMBeanCount() = " + mbeanConnect.getMBeanCount());

            for (ObjectInstance objectInstance : mbeanConnect.queryMBeans(new ObjectName("metrics:*"), null)) {
                System.out.println("objectInstance = " + objectInstance);
                MBeanInfo mBeanInfo = mbeanConnect.getMBeanInfo(objectInstance.getObjectName());
                for (MBeanAttributeInfo attribute : mBeanInfo.getAttributes()) {
                    System.out.println(mbeanConnect.getAttribute(objectInstance.getObjectName(), attribute.getName()));
                }
            }

            mbeanConnect.addNotificationListener(
                    new ObjectName("test:name=test1"),
                    (notification, handback) -> {
                        System.out.println("notification = " + notification);
                        System.out.println("handback = " + handback);
                    },
                    notification -> {
                        System.out.println("notification 2 = " + notification);
                        return true;
                    },
                    System.out);
            Thread.sleep(Long.MAX_VALUE);

        } finally {
            if (jmxConnector != null) {
                try {
                    jmxConnector.close();
                } catch (IOException e) {
                    logger.warn(null, e);
                }
            }
        }
    }

}
