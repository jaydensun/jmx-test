package com.jayden;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author sunyongjun
 * @since 2019/9/10
 */
public class PrinterTest {

    @Test
    public void print() {
        assertEquals("printer", Printer.print());
    }
}