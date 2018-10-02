package id.my.rizki.onlinecompiler.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class MD5Test {

    @Test
    public void generate() {
        String result = MD5.generate("something");
        Assert.assertNotNull(result);
    }

    @Test
    public void generateDifferentTime() {
        String firstResult = MD5.generate("something");
        String secondResult = MD5.generate("something");
        Assert.assertNotSame(firstResult, secondResult);
    }
}