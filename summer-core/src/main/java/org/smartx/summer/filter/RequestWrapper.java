package org.smartx.summer.filter;

import org.apache.commons.io.input.TeeInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * <p>  </p>
 *
 * <b>Creation Time:</b> 2016年11月3日
 *
 * @author binglin
 * @since summer 0.1
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    private ByteArrayOutputStream bos = new ByteArrayOutputStream();

    public RequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            private TeeInputStream tee = new TeeInputStream(RequestWrapper.super.getInputStream(), bos, true);

            @Override
            public int read() throws IOException {
                return tee.read();
            }
        };
    }

    public byte[] toByteArray() {
        return bos.toByteArray();
    }
}
