/*
 * Copyright (c) 2020 Jon Chambers
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.eatthepath.pushy.apns.server;

import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

public class ServerChannelClassUtilTest {

    @Test
    public void testGetCoreSocketChannelClass() {
        final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(1);

        try {
            assertEquals(NioServerSocketChannel.class, ServerChannelClassUtil.getServerSocketChannelClass(nioEventLoopGroup));
        } finally {
            nioEventLoopGroup.shutdownGracefully();
        }
    }

    @Test
    public void testGetKqueueSocketChannelClass() {
        final String unavailabilityMessage =
                KQueue.unavailabilityCause() != null ? KQueue.unavailabilityCause().getMessage() : null;

        assumeTrue("KQueue not available: " + unavailabilityMessage, KQueue.isAvailable());

        final KQueueEventLoopGroup kQueueEventLoopGroup = new KQueueEventLoopGroup(1);

        try {
            assertEquals(KQueueServerSocketChannel.class, ServerChannelClassUtil.getServerSocketChannelClass(kQueueEventLoopGroup));
        } finally {
            kQueueEventLoopGroup.shutdownGracefully();
        }
    }

    @Test
    public void testGetEpollSocketChannelClass() {
        final String unavailabilityMessage =
                Epoll.unavailabilityCause() != null ? Epoll.unavailabilityCause().getMessage() : null;

        assumeTrue("Epoll not available: " + unavailabilityMessage, Epoll.isAvailable());

        final EpollEventLoopGroup epollEventLoopGroup = new EpollEventLoopGroup(1);

        try {
            assertEquals(EpollServerSocketChannel.class, ServerChannelClassUtil.getServerSocketChannelClass(epollEventLoopGroup));
        } finally {
            epollEventLoopGroup.shutdownGracefully();
        }
    }
}
