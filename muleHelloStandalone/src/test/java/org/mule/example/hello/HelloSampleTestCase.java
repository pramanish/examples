/*
 * $Id: HelloSampleTestCase.java 19191 2010-08-25 21:05:23Z tcarlson $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.example.hello;

import org.mule.tck.AbstractMuleTestCase;
import org.mule.transformer.types.DataTypeFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class HelloSampleTestCase extends AbstractMuleTestCase
{

    public void testGreeter()
    {
        NameString name = new NameString("Fred");
        assertNotNull(name.getName());
        assertNull(name.getGreeting());
        name.setName("Another Fred");

        Greeter greeter = new Greeter();
        greeter.greet(name);
        assertNotNull(name.getGreeting());
    }

    public void testChitChatter()
    {
        NameString name = new NameString("Barney");
        assertNotNull(name.getName());
        assertNull(name.getGreeting());

        ChatString chat = new ChatString();
        assertTrue(chat.getSize() == 0);
        ChitChatter chitChatter = new ChitChatter();
        chitChatter.chat(chat);
        assertTrue(chat.getSize() > 0);

        int size = chat.getSize();

        chat.append("Blah");
        chat.append(new StringBuffer("Blah"));
        assertTrue(chat.toString().endsWith("BlahBlah"));
        chat.insert(0, "Blah".toCharArray(), 0, 2);
        chat.insert(2, "Blah".toCharArray());
        assertTrue(chat.toString().startsWith("BlBlah"));
        assertEquals(size + 4 + 4 + 2 + 4, chat.getSize());

    }

    public void testStringToNameTransformer() throws Exception
    {
        String temp = "Wilma";
        StringToNameString trans = new StringToNameString();
        Object result = trans.transform(temp);

        assertNotNull(result);
        assertTrue(result instanceof NameString);

        NameString name = (NameString)result;

        assertNotNull(name.getName());
        assertNull(name.getGreeting());

        result = trans.transform("Another Wilma");

        assertNotNull(result);
        assertTrue(result instanceof NameString);

        name = (NameString)result;

        assertNotNull(name.getName());
        assertEquals("Another Wilma", name.getName());
    }

    public void testHttpRequestToNameTransformer() throws Exception
    {
        String temp = "whateverUrl?name=Wilma";
        HttpRequestToNameString trans = new HttpRequestToNameString();
        Object result = trans.transform(temp);

        assertNotNull(result);
        assertTrue(result instanceof NameString);

        NameString name = (NameString)result;

        assertNotNull(name.getName());
        assertNull(name.getGreeting());

        result = trans.transform("whateverUrl?street=Sonnenstrasse&name=Another%20Wilma");

        assertNotNull(result);
        assertTrue(result instanceof NameString);

        name = (NameString)result;

        assertNotNull(name.getName());
        assertEquals("Another Wilma", name.getName());
    }
    
    public void testHttpRequestToNameStreamingTransformer() throws Exception
    {
        InputStream in = new ByteArrayInputStream("whateverUrl?name=Wilma".getBytes());
        HttpRequestToNameString transformer = new HttpRequestToNameString();
        Object result = transformer.transform(in);
        
        assertNotNull(result);
        assertTrue(result instanceof NameString);
        
        NameString name = (NameString)result;
        assertNotNull(name.getName());
        assertNull(name.getGreeting());
    }
    
    public void testStdinToNameTransformer() throws Exception
    {
        String temp = "Wilma";
        StdinToNameString trans = new StdinToNameString();
        Object result = trans.transform(temp);

        assertNotNull(result);
        assertTrue(result instanceof NameString);

        NameString name = (NameString)result;

        assertNotNull(name.getName());
        assertNull(name.getGreeting());

        result = trans.transform("Another Wilma\r\n");

        assertNotNull(result);
        assertTrue(result instanceof NameString);

        name = (NameString)result;

        assertNotNull(name.getName());
        assertEquals("Another Wilma", name.getName());
    }    
    
    public void testNameToChatTransformer() throws Exception
    {
        NameString temp = new NameString("the other one");
        NameStringToChatString trans = new NameStringToChatString();
        trans.setReturnDataType(DataTypeFactory.create(ChatString.class));

        Object result = trans.transform(temp);
        assertNotNull(result);
        assertTrue(result instanceof ChatString);

        ChatString chat = (ChatString)result;

        assertTrue(chat.getSize() > 0);
    }

}
